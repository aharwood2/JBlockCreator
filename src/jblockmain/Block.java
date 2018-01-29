package jblockmain;

import mathcontainers.Matrix2D;
import mathcontainers.PolyCoeffs;
import mathcontainers.Vector2D;
import mathcontainers.VectorND;

import java.util.ArrayList;

/**
 * Class that represents a block as a series of connected keypoints.
 * When plotted in order, these keypoints will trace out the block which can then be cut from fabric.
 * Every new pattern must have at least one block object.
 */
public class Block
{
    /**
     * Name of the block.
     */
    private String name;

    /**
     * X positions of the keypoints.
     */
    private ArrayList<Double> keypointsX;

    /**
     * Y positions of the keypoints.
     */
    private ArrayList<Double> keypointsY;

    /**
     * Constructor.
     * @param newName   name of new block.
     */
    public Block(String newName)
    {
        keypointsX = new ArrayList<Double>();
        keypointsY = new ArrayList<Double>();
        this.name = newName;
    }

    /**
     * Copy constructor.
     * @param otherBlock    block to be copied.
     * @param newName       name of new block.
     */
    public Block(Block otherBlock, String newName)
    {
        this(newName);
        this.keypointsX = new ArrayList<>(otherBlock.keypointsX);
        this.keypointsY = new ArrayList<>(otherBlock.keypointsY);
    }

    /**
     * Method to retrieve the number of a keypoint given its position.
     * (Within floating point tolerances)
     *
     * @param xy x position of point
     * @return keypoint number
     */
    public int getKeypointNumber(Vector2D xy) throws Exception
    {
        int i = 0;
        while (i < keypointsX.size())
        {
            if (Math.abs(keypointsX.get(i) - xy.getX()) < Main.tol &&
                    Math.abs(keypointsY.get(i) - xy.getY()) < Main.tol)
            {
                // Point found
                break;
            }

            // Increment counter
            i++;
        }

        // If point is outside, point not found so return -1;
        if (i > keypointsX.size()) throw new Exception("Cannot find the point: (" + xy.getX() + ", " + xy.getY() + ") in the keypoints list!");
        else return i;
    }

    /**
     * Method to add a keypoint to the end of the list.
     * @param xy    position of point
     * @return      keypoint number
     */
    public int addKeypoint(Vector2D xy)
    {
        // Add to end of list
        keypointsX.add(xy.getX());
        keypointsY.add(xy.getY());
        return keypointsX.size() - 1;
    }

    /**
     * Method to add a keypoint next to an existing keypoint.
     * @param xy            new keypoint position
     * @param adjacent      existing keypoint position
     * @param position      place point before or after existing point
     * @return              keypoint number
     */
    public int addKeypointNextTo(Vector2D xy, Vector2D adjacent, EPosition position)
    {
        // Get the point number of the adjacent point
        int i = 0;
        try
        {
            i = getKeypointNumber(adjacent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Correct position if placing after
        if (position == EPosition.AFTER) i++;

        // Insert the point at the location
        keypointsX.add(i, xy.getX());
        keypointsY.add(i, xy.getY());

        return i;

    }

    /**
     * Method to add a dart given the end points of the line segment on which to add the dart, position along the
     * segment and the dimensions of the dart. Start and end points must be specified in the strict anti-clockwise order
     * for connectivity to be correct for plotting.
     * @param lineStart     position of start of segment
     * @param lineEnd       position of end of segment
     * @param position      position of dart centre
     * @param width         width of dart at base.
     * @param length        depth of dart assuming it is symmetrical
     * @return              list of points of the dart edges
     */
    public ArrayList<Vector2D> addDart(Vector2D lineStart, Vector2D lineEnd, double position,
                                     double width, double length)
    {
        // Find the equation of the line
        Vector2D direction = new Vector2D(lineEnd.subtract(lineStart));
        Vector2D normal = new Vector2D(-direction.getY(), direction.getX());

        // Normalise the direction vectors
        direction.divideBy(direction.norm());
        normal.divideBy(normal.norm());

        // Find dart point
        double side_length = lineEnd.subtract(lineStart).norm();
        Vector2D point = new Vector2D(lineStart.add(direction.multiply(position * side_length)));

        // Add the keypoints associated with the darts
        Vector2D pt1 = new Vector2D(point.subtract(direction.multiply(width / 2.0)));
        Vector2D pt2 = new Vector2D(point.add(normal.multiply(length)));
        Vector2D pt3 = new Vector2D(point.add(direction.multiply(width / 2.0)));
        addKeypointNextTo(pt1, lineStart, EPosition.AFTER);
        addKeypointNextTo(pt2, pt1, EPosition.AFTER);
        addKeypointNextTo(pt3, pt2, EPosition.AFTER);

        ArrayList<Vector2D> dartEdges = new ArrayList<Vector2D>();
        dartEdges.add(pt1);
        dartEdges.add(pt2);
        dartEdges.add(pt3);
        return dartEdges;
    }

    /**
     * Add a curve between the start and end points which meets each bounding line at 90 degrees. Direction of both
     * bounding lines must be given as well as whether normal should be to the right or left for each bounding edge.
     * This is indicated as a true or false 'dirNorm' flag. Note: the normals need to have a magnitude that makes sense
     * in the context of the start and end point specification order as well as the world space as this is the system
     * used to specify the equation of the curve. More often than not, the normals will be pointing in the same
     * direction so the flags are usually set to the same value.
     * @param startPoint    start position of curve
     * @param endPoint      end position of curve
     * @param dirStart      direction of the start bounding line
     * @param dirEnd        direction of the end bounding line
     * @param dirNorm       direction of the normal of the bounding lines
     */
    public void addRightAngleCurve(Vector2D startPoint, Vector2D endPoint,
                                   Vector2D dirStart, Vector2D dirEnd, boolean[] dirNorm)
    {
        // To ensure the process is more robust we first map the two end points and their side directions onto a
        // reference X axis. This is done by first shifting the start point to the reference origin then rotating the
        // points and directions such that the start bounding line is coincident with the reference Y axis.

        // Shift the start and end points
        Vector2D refStart = new Vector2D(startPoint.subtract(startPoint));
        Vector2D refEnd = new Vector2D(endPoint.subtract(startPoint));

        // Find rotation angle
        double rotang = Math.acos(dirStart.getY() / dirStart.norm());

        // Construct rotation matrix
        Matrix2D R = new Matrix2D(2, 2,
                                  new double[][]{
                                          {Math.cos(rotang), -Math.sin(rotang)},
                                          {Math.sin(rotang), Math.cos(rotang)}
                                  }
        );

        // And reverse for later
        Matrix2D Ri = new Matrix2D(2, 2,
                                   new double[][]{
                                           {Math.cos(-rotang), -Math.sin(-rotang)},
                                           {Math.sin(-rotang), Math.cos(-rotang)}
                                   }
        );

        // Rotate direction and positions vectors
        Vector2D refDirStart = new Vector2D(R.postMultiply(dirStart));
        Vector2D refDirEnd = new Vector2D(R.postMultiply(dirEnd));
        refStart = new Vector2D(R.postMultiply(refStart));
        refEnd = new Vector2D(R.postMultiply(refEnd));

        // Compute normal vectors in reference system
        Vector2D refNormStart;
        Vector2D refNormEnd;
        double refDxDyStart;
        double refDxDyEnd;
        if (dirNorm[0])
        {
            // RH normal
            refNormStart = new Vector2D(refDirStart.getY(), -refDirStart.getX());
        }
        else
        {
            // LH normal
            refNormStart = new Vector2D(-refDirStart.getY(), refDirStart.getX());
        }

        if (dirNorm[1])
        {
            refNormEnd = new Vector2D(refDirEnd.getY(), -refDirEnd.getX());
        }
        else
        {
            refNormEnd = new Vector2D(-refDirEnd.getY(), refDirEnd.getX());
        }

        // Compute the required gradients of the curve
        refDxDyStart = refNormStart.getY() / refNormStart.getX();
        refDxDyEnd = refNormEnd.getY() / refNormEnd.getX();

        // Find coefficients for the cubic spline:
        // ax^3 + bx^2 + cx + d
        // By using two end point conditions and two gradient conditions to define a set of simultaneous equations.
        final VectorND constants =
                new VectorND(4, new double[] {refStart.getY(), refEnd.getY(), refDxDyStart, refDxDyEnd});
        final Matrix2D mat = new Matrix2D(4, 4,
                                          new double[][] {
                                                  {Math.pow(refStart.getX(),3), Math.pow(refStart.getX(),2), refStart.getX(), 1.0},
                                                  {Math.pow(refEnd.getX(),3), Math.pow(refEnd.getX(),2), refEnd.getX(), 1.0},
                                                  {3.0 * Math.pow(refStart.getX(),2), 2.0 * refStart.getX(), 1.0, 0.0},
                                                  {3.0 * Math.pow(refEnd.getX(),2), 2.0 * refEnd.getX(), 1.0, 0.0}
                                          }
        );

        // Solve to get coefficients
        final Matrix2D inverse = mat.invert();
        PolyCoeffs coeffs = new PolyCoeffs(inverse.postMultiply(constants));

        // Discretise by specified amount
        int numPts = (int)Math.ceil(refEnd.subtract(refStart).norm() * Main.res);

        // Find points on the curve by seeding x
        // might not always be robust -- should use a local curvilinear coordinate system really.
        double spacing = (refEnd.getX() - refStart.getX()) / (numPts - 1);
        Vector2D tmp;
        Vector2D tmp2 = new Vector2D(startPoint);
        for (int i = 1; i < numPts - 1; i++)
        {
            double x = refStart.getX() + spacing * i;
            double y = coeffs.a * x * x * x + coeffs.b * x * x + coeffs.c * x + coeffs.d;

            // Add point reversing rotation and shift (exc. first and last)
            tmp = new Vector2D(Ri.postMultiply(new Vector2D(x, y)).add(startPoint));
            addKeypointNextTo(tmp, tmp2, EPosition.AFTER);
            tmp2 = new Vector2D(tmp);
        }

    }

    /**
     * Add a curve given the height of curve above the centre of a line joining the two points. Assumes the curve is
     * cut from a circle and hence given points are on the circle circumference. Direction of normal is indicated by
     * boolean value -- true for right hand normal and false for left hand normal -- which tells the method which way
     * to curve. Start and end points must be specified in the strict anti-clockwise order of the keypoints list.
     * @param startPoint    start position of curve
     * @param endPoint      end position of curve
     * @param height        height of curve above a straight line joining start and end positions
     * @param dirNorm       direction of the centre of the circle
     */
    public void addCircularCurve(Vector2D startPoint, Vector2D endPoint, double height, boolean dirNorm)
    {
        // Get equation of line
        Vector2D direction = new Vector2D(endPoint.subtract(startPoint));
        Vector2D norm_dir;
        if (dirNorm)
        {
            norm_dir = new Vector2D(direction.getY(), -direction.getX());
        }
        else
        {
            norm_dir = new Vector2D(-direction.getY(), direction.getX());
        }
        norm_dir.divideBy(norm_dir.norm());

        // Use normalised direction to find midpoint
        Vector2D midpt = new Vector2D(startPoint.add(direction.multiply(0.5)));

        // Find the 3rd point required for the arc
        Vector2D crestpt = new Vector2D(midpt.add(norm_dir.multiply(height)));

        // Solve for coefficients
        double lam1 = -Math.pow(startPoint.getX(), 2) - Math.pow(startPoint.getY(), 2);
        double lam2 = -Math.pow(endPoint.getX(), 2) - Math.pow(endPoint.getY(), 2);
        double lam3 = -Math.pow(crestpt.getX(), 2) - Math.pow(crestpt.getY(), 2);
        double lam23 = lam2 - lam3;
        double lam13 = lam1 - lam3;
        double y23 = endPoint.getY() - crestpt.getY();
        double y13 = startPoint.getY() - crestpt.getY();
        double x23 = endPoint.getX() - crestpt.getX();
        double x13 = startPoint.getX() - crestpt.getX();
        double alp = (lam23 - (y23/y13) * lam13) / ((x23 * y13 - y23 * x13) / y13);
        double bet = (lam13 - x13 * alp) / y13;
        double gam = lam1 - startPoint.getY() * bet - startPoint.getX() * alp;

        // Convert to standard form of equation for circle
        double centrex = -alp / 2.0;
        double centrey = -bet / 2.0;
        double radius = Math.sqrt(centrex * centrex + centrey * centrey - gam);

        // Discretise equation of circle using specified resolution
        double th1 = Math.acos((startPoint.getX() - centrex) / radius);
        double th2 = Math.acos((endPoint.getX() - centrex) / radius);
        double dcircum = Math.abs(th2 - th1) * radius;
        int numPts = (int)Math.ceil(dcircum * Main.res);

        // Specify in polar coordinates then convert to Cartesian
        Vector2D tmp;
        Vector2D tmp2 = new Vector2D(startPoint);
        double spacing = (th2 - th1) / (numPts - 1);
        double th;
        for (int i = 1; i < numPts - 1; i++)
        {
            th = th1 + i * spacing;
            tmp = new Vector2D(radius * Math.cos(th) + centrex, radius * Math.sin(th) + centrey);
            addKeypointNextTo(tmp, tmp2, EPosition.AFTER);
            tmp2 = new Vector2D(tmp);
        }
    }

    /**
     * Method to get a plottable list of keypoints -- adds the wrap around necessary to close the shape.
     * @return  array list of keypoints for plotting
     */
    public ArrayList<Double> getPlottableKeypointsX()
    {
        ArrayList<Double> tmp = new ArrayList<>(keypointsX);
        tmp.add(keypointsX.get(0));
        return tmp;
    }

    /**
     * Method to get a plottable list of keypoints -- adds the wrap around necessary to close the shape.
     * @return  array list of keypoints for plotting
     */
    public ArrayList<Double> getPlottableKeypointsY()
    {
        ArrayList<Double> tmp = new ArrayList<>(keypointsY);
        tmp.add(keypointsY.get(0));
        return tmp;
    }

    /**
     * Getter for name of block
     * @return name of block
     */
    public String getName()
    {
        return name;
    }
}
