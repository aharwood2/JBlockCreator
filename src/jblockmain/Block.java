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
        try
        {
            int i = getKeypointNumber(adjacent);

            // Correct position if placing after
            if (position == EPosition.AFTER) i++;

            // Insert the point at the location
            keypointsX.add(i, xy.getX());
            keypointsY.add(i, xy.getY());

            return i;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Moves a keypoint from one location to another
     * @param existingPosition  existing position of the keypoint
     * @param newPosition       desired new position of the keypoint
     * @return  index of the keypoint in the list
     */
    public int moveKeypoint(Vector2D existingPosition, Vector2D newPosition)
    {
        // Get the point number of the adjacent point
        try
        {
            int i = getKeypointNumber(existingPosition);

            // Update position
            keypointsX.set(i, newPosition.getX());
            keypointsY.set(i, newPosition.getY());

            return i;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Method to remove a keypoint from the list.
     * @param location  location of the keypoint to be deleted
     */
    public void deleteKeypoint(Vector2D location)
    {
        // Get the point number of the adjacent point
        try
        {
            int i = getKeypointNumber(location);

            // Delete position
            keypointsX.remove(i);
            keypointsY.remove(i);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
     * @param dirNorm       flag indicating the direction of the dart (left or right)
     * @return              list of points of the dart edges
     */
    public ArrayList<Vector2D> addDart(Vector2D lineStart, Vector2D lineEnd, double position,
                                     double width, double length, boolean dirNorm)
    {
        // Find the equation of the line to find normal
        Vector2D direction = new Vector2D(VectorND.getDirectionVector(lineStart, lineEnd));
        Vector2D normal = new Vector2D(direction.getY(), -direction.getX());
        if (dirNorm) normal.multiplyBy(-1.0);

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
     * Method to add a dart given the end points of the line segment on which to add the dart, position along the
     * segment and the apex of the dart. Start and end points must be specified in the strict anti-clockwise order
     * for connectivity to be correct for plotting.
     * @param lineStart     position of start of segment
     * @param lineEnd       position of end of segment
     * @param position      position of dart centre
     * @param width         width of dart at base.
     * @param apex          position of the dart apex
     * @return              list of points of the dart edges
     */
    public ArrayList<Vector2D> addDart(Vector2D lineStart, Vector2D lineEnd, double position,
                                       double width, Vector2D apex)
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
        Vector2D pt3 = new Vector2D(point.add(direction.multiply(width / 2.0)));
        addKeypointNextTo(pt1, lineStart, EPosition.AFTER);
        addKeypointNextTo(apex, pt1, EPosition.AFTER);
        addKeypointNextTo(pt3, apex, EPosition.AFTER);

        ArrayList<Vector2D> dartEdges = new ArrayList<Vector2D>();
        dartEdges.add(pt1);
        dartEdges.add(apex);
        dartEdges.add(pt3);
        return dartEdges;
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
     * Method to add a curve which has specified start and end point gradients as well as an apex at which the curve
     * must have a stationary point. Directions are approximated from adjacent keypoints and hence are not specified.
     * @param startPoint            start point of the curve
     * @param endPoint              end point of the curve
     * @param tangentCorner         squared out corner of the apex of the curve
     * @param tangentPointOffset    offset from the apex corner to the curve itself
     * @param anglesAtEnds          angles required at the start and end points of the curve
     */
    public void addDirectedCurveWithApexTangent(Vector2D startPoint, Vector2D endPoint,
                                                Vector2D tangentCorner, double tangentPointOffset,
                                                double[] anglesAtEnds)
    {
        // Specify the tangent point using corner and offset
        Vector2D tangentPoint = new Vector2D(
                tangentCorner.subtract(
                        new Vector2D(
                                tangentPointOffset * Math.cos(Math.PI / 4.0),
                                tangentPointOffset * Math.sin(Math.PI / 4.0)
                        )
                )
        );

        // Add guide point as a keypoint
        addKeypointNextTo(tangentPoint,
                          startPoint,
                          EPosition.AFTER);

        // Get direction of the bisect line (which will be normal to the curve)
        Vector2D cornerToTangentLine = new Vector2D(tangentCorner.subtract(tangentPoint));

        // Get normal to this (which will be tangent to curve)
        Vector2D tangentDirection = new Vector2D(cornerToTangentLine.getY(), -cornerToTangentLine.getX());

        // Now we can construct the first part of the curve
        addDirectedCurve(startPoint,
                         tangentPoint,
                         getDirectionAtKeypoint(startPoint, EPosition.BEFORE),
                         tangentDirection, new double[] {anglesAtEnds[0], 0.0}
        );

        // Construct the second part of the curve (intersect at end is 90 degrees)
        addDirectedCurve(tangentPoint,
                         endPoint,
                         tangentDirection,
                         getDirectionAtKeypoint(endPoint, EPosition.AFTER),
                         new double[] {0.0, anglesAtEnds[1]}
        );
    }

    /**
     * Add a curve between the start and end points which meets each bounding line at the specified angles in degrees.
     * Direction of both bounding lines are derived based on adjacent keypoints.
     * @param startPoint    start position of curve
     * @param endPoint      end position of curve
     * @param angleAtEnds   desired angle at each end of the curve
     */
    public void addDirectedCurve(Vector2D startPoint, Vector2D endPoint, double[] angleAtEnds)
    {
        // Get directions
        Vector2D dirStart = getDirectionAtKeypoint(startPoint, EPosition.BEFORE);
        Vector2D dirEnd = getDirectionAtKeypoint(endPoint, EPosition.AFTER);

        // Pass on arguments
        addDirectedCurve(startPoint, endPoint, dirStart, dirEnd, angleAtEnds);
    }

    /**
     * Add a curve between the start and end points which meets each bounding line at the specified angles in degrees.
     * Direction of both bounding lines must be given.
     * @param startPoint    start position of curve
     * @param endPoint      end position of curve
     * @param dirStart      direction of the start bounding line
     * @param dirEnd        direction of the end bounding line
     * @param angleAtEnds   desired angle at each end of the curve
     */
    public void addDirectedCurve(Vector2D startPoint, Vector2D endPoint,
                                 Vector2D dirStart, Vector2D dirEnd,
                                 double[] angleAtEnds)
    {
        // To ensure the process is more robust we first map the two end points and their side directions onto a
        // reference X axis. This is done by first shifting the start point to the reference origin then rotating the
        // points and directions such that the start bounding line is coincident with the reference Y axis.

        // Shift the start and end points
        Vector2D refStart = new Vector2D(startPoint.subtract(startPoint));
        Vector2D refEnd = new Vector2D(endPoint.subtract(startPoint));

        // Find rotation angle such that curve will start parallel to X axis:
        double rotang = Math.acos(dirStart.getY() / dirStart.norm()) - (Math.PI * (90.0 - angleAtEnds[0]) / 180.0);

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

        // Rotate direction and position vectors
        Vector2D refDirStart = new Vector2D(R.postMultiply(dirStart));
        Vector2D refDirEnd = new Vector2D(R.postMultiply(dirEnd));
        refStart = new Vector2D(R.postMultiply(refStart));
        refEnd = new Vector2D(R.postMultiply(refEnd));

        // Compute curve start and end direction vectors and hence gradients in reference system
        Vector2D refCurveStart;
        Vector2D refCurveEnd;
        double refDxDyStart;
        double refDxDyEnd;

        // Rotate the directions given by the amount given
        double startAngle = angleAtEnds[0] * Math.PI / 180.0;
        Matrix2D RCurveStart = new Matrix2D(2, 2,
                                  new double[][]{
                                          {Math.cos(startAngle), -Math.sin(startAngle)},
                                          {Math.sin(startAngle), Math.cos(startAngle)}
                                  }
        );
        double endAngle = angleAtEnds[1] * Math.PI / 180.0;
        Matrix2D RCurveEnd = new Matrix2D(2, 2,
                                            new double[][]{
                                                    {Math.cos(endAngle), -Math.sin(endAngle)},
                                                    {Math.sin(endAngle), Math.cos(endAngle)}
                                            }
        );
        refCurveStart = new Vector2D(RCurveStart.postMultiply(refDirStart));
        refCurveEnd = new Vector2D(RCurveEnd.postMultiply(refDirEnd));

        // Compute the required gradients of the curve
        refDxDyStart = refCurveStart.getY() / refCurveStart.getX();
        refDxDyEnd = refCurveEnd.getY() / refCurveEnd.getX();



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
     * Method to construct a curve which meets a specified line at the end points at 90 degrees.
     * @param startPoint    start position of curve
     * @param endPoint      end position of curve
     */
    public void addRightAngleCurve(Vector2D startPoint, Vector2D endPoint)

    {
        double[] angles = new double[] {90.0, 90.0};
        addDirectedCurve(startPoint, endPoint, angles);
    }

    /**
     * Method which constructs a curve which meets a specified line at the end points at 0 degrees (i.e. aligned)
     * @param startPoint    position of the start of the curve
     * @param endPoint      position of the end of the curve
     */
    public void addBlendedCurve(Vector2D startPoint, Vector2D endPoint)
    {
        double[] angles = new double[] {0.0, 0.0};
        addDirectedCurve(startPoint, endPoint, angles);
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

    /**
     * Get the hypotenuse of a right-angled triangle given the other two sides
     * @param side1 adjacent side
     * @param side2 other adjacent side
     * @return hypotenuse
     */
    public static double triangleGetHypotenuse(double side1, double side2)
    {
        return Math.sqrt(side1 * side1 + side2 * side2);
    }

    /**
     * Get the adjacent side of a right-angled triangle given the hypotenuse and the other side
     * @param side1 adjacent side
     * @param hypotenuse hypotenuse
     * @return other adjacent side
     */
    public static double triangleGetAdjacentSide(double side1, double hypotenuse)
    {
        return Math.sqrt(hypotenuse * hypotenuse - side1 * side1);
    }

    /**
     * Method to get a direction vector at a given point on the curve based on an adjacent keypoint.
     * @param keypoint  point at which direction vector is required.
     * @param adjacency connecting point from which to infer direction.
     * @return  normalised direction vector.
     */
    public Vector2D getDirectionAtKeypoint(Vector2D keypoint, EPosition adjacency)
    {
        try
        {
            int i = getKeypointNumber(keypoint);

            // Approximate direction using linear connection to the adjacent point in the list
            int j = i;
            if (adjacency == EPosition.BEFORE) j--;
            else j++;

            // Periodic connection
            if (j < 0) j  = keypointsX.size() - 1;
            else if (j == keypointsX.size()) j = 0;
            Vector2D directionVector = new Vector2D(keypointsX.get(i) - keypointsX.get(j), keypointsY.get(i) - keypointsY.get(j));

            // Normalise and return
            directionVector.divideBy(directionVector.norm());
            return directionVector;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new Vector2D(-1.0, -1.0);
    }
}
