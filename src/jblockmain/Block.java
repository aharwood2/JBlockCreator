package jblockmain;

import mathcontainers.Matrix2D;
import mathcontainers.PolyCoeffs;
import mathcontainers.Vector2D;
import mathcontainers.VectorND;

import java.util.ArrayList;

import static jblockmain.Main.tol;

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
            if (Math.abs(keypointsX.get(i) - xy.getX()) < tol &&
                    Math.abs(keypointsY.get(i) - xy.getY()) < tol)
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
     * Method to adjust the end points of the dart if applied to a straight line to ensure the edge is correct when the
     * dart closes.
     * @param points        The base start, apex and base end points of the dart before correction.
     * @param lineStart     The position of the start of the edge on which to add the dart.
     * @param lineEnd       The position of the end of the edge on which to add the dart.
     * @return              A new array with corrected values.
     */
    private ArrayList<Vector2D> adjustDartPointsForStraightEdge(ArrayList<Vector2D> points,
                                                                Vector2D lineStart,
                                                                Vector2D lineEnd)
    {
        // Extract and name points from points vector
        Vector2D baseStart = points.get(0);
        Vector2D apex = points.get(1);
        Vector2D baseEnd = points.get(2);

        // Translation of dart points to the origin wrt apex

        Vector2D baseStartd = Vector2D(baseStart.subtract(apex));
        Vector2D baseEndd = Vector2D(baseEnd.subtract(apex));
        Vector2D apexd = Vector2D(apex.subtract(apex));
        Vector2D lineStartd = Vector2D(lineStart.subtract(apex));
        Vector2D lineEndd = Vector2D(lineEnd.subtract(apex));

        // Calculation to find the angle between baseStartd and the y-axis

        new double theta = Math.atan(baseStartd.getY().divide(baseStartd.getX()));

        // Generation of a rotation vector and an inverse for later

        Vector2D RV = Math.cos(theta).subtract(Math.sin(theta)), Math.sin(theta).add(Math.cos(theta));
        Vector2D IRV = Math.cos(-theta).subtract(Math.sin(-theta)), Math.sin(-theta).add(Math.cos(-theta));

        // Rotation of dart points so that the start edge of the dart is on the y-axis

        Vector2D baseStartdd = Vector2D(baseStartd.multiply(RV));
        Vector2D baseEnddd = Vector2D(baseEndd.multiply(RV));
        Vector2D apexdd = Vector2D(apexd.multiply(RV));
        Vector2D lineStartdd = Vector2D(lineStartd.multiply(RV));
        Vector2D lineEnddd = Vector2D(lineEndd.multiply(RV));

        // Calculation to find the angle between the start and end dart edges, minus for the right direction

        new double phi = -Math.atan(baseEnddd.getY().divide(baseEnddd.getX()));

        // Generation of a rotation vector and inverse for later

        Vector2D RVD = Math.cos(phi).subtract(Math.sin(phi)), Math.sin(phi).add(Math.cos(phi));
        Vector2D IRVD = Math.cos(-phi).subtract(Math.sin(-phi)), Math.sin(-phi).add(Math.cos(-phi));

        // Rotation of lineEnddd and baseEnddd to simulate dart closure

        Vector2D baseEndddd = Vector2D(baseEnddd.multiply(phi));
        Vector2D lineEndddd = Vector2D(lineEnddd.multiply(phi));

        // Direction vector representing the closed dart final edge

        Vector2D directionddd = Vector2D(lineStartdd.subtract(lineEndddd));

        // Calculation to find where the the vector crosses the y-axis

        Vector2D zero = Vector2D(0.subtract(lineEndddd.getX());
        Vector2D ratio = Vector2D(zero.divide(directionddd));
        Vector2D distance = Vector2D(ratio.multiply(directionddd));
        Vector2D intercept = Vector2D(lineEndddd.add(distance)));

        // Reversing the second rotation

        Vector2D interceptd = Vector2D(intercept.multiply(IRVD));
        Vector2D lineEnddddd = Vector2D(lineEnddddd.multiply(IRVD));

        // Reversing the first rotation

        Vector2D interceptdd = Vector2D(intercept.multiply(RV));
        Vector2D interceptddd = Vector2D(interceptd.multiply(RV));
        Vector2D apexddd = Vector2D(apexdd.multiply(RV));
        Vector2D lineStartddd = Vector2D(lineStartdd.multiply(RV));
        Vector2D lineEnddddd = Vector2D(lineEndddd.multiply(RV));

        // Reversing the translation

        Vector2D interceptdddd = Vector2D(interceptdd.subtract(apex));
        Vector2D interceptddddd = Vector2D(interceptddd.subtract(apex));
        Vector2D apexdddd = Vector2D(apexdddd.subtract(apex));
        Vector2D lineStartdddd = Vector2D(lineStartdddd.subtract(apex));
        Vector2D lineEndddddd = Vector2D(lineEnddddd.subtract(apex));

        // Add corrected dart points
        ArrayList<Vector2D> correctedDartsPoints;

        return correctedDartsPoints;

    }

    /**
     * Method to add a dart given the end points of the line segment on which to add the dart, position along the
     * segment and the dimensions of the dart. Start and end points must be specified in the strict anti-clockwise order
     * for connectivity to be correct for plotting.
     * @param lineStart     position of start of segment
     * @param lineEnd       position of end of segment
     * @param position      position of dart centre in dimensionless units along edge
     * @param width         width of dart at base
     * @param length        depth of dart assuming it is symmetrical
     * @param dirNorm       flag indicating the direction of the dart (left or right)
     * @param straightSide  is side to which dart is to be added going to remain straight
     * @return              list of points of the dart edges
     */
    public ArrayList<Vector2D> addDart(Vector2D lineStart, Vector2D lineEnd, double position,
                                     double width, double length, boolean dirNorm, boolean straightSide)
    {
        // Find the equation of the line to find normal
        Vector2D direction = new Vector2D(lineEnd.subtract(lineStart));
        Vector2D normal = new Vector2D(direction.getY(), -direction.getX());
        if (dirNorm) normal.multiplyBy(-1.0);

        // Normalise the direction vectors
        direction.divideBy(direction.norm());
        normal.divideBy(normal.norm());

        // Find dart point
        double side_length = lineEnd.subtract(lineStart).norm();
        Vector2D point = new Vector2D(lineStart.add(direction.multiply(position * side_length)));

        // Package points
        ArrayList<Vector2D> pointsOfDart = new ArrayList<>();
        pointsOfDart.add(new Vector2D(point.subtract(direction.multiply(width / 2.0))));
        pointsOfDart.add(new Vector2D(point.add(normal.multiply(length))));
        pointsOfDart.add(new Vector2D(point.add(direction.multiply(width / 2.0))));

        // Correct points if necessary
        if (straightSide) pointsOfDart = adjustDartPointsForStraightEdge(pointsOfDart, lineStart, lineEnd);

        // Add keypoints
        addDartKeypoints(lineStart, pointsOfDart, EPosition.AFTER);

        // Return the corrected points
        return pointsOfDart;
    }

    /**
     * Method to add a dart given the two points at the base of the dart, its apex point and a point before or which to
     * insert it. Dart points must be in the strict anti-clockwise order.
     * @param baseStart     position of start of segment
     * @param baseEnd       position of end of segment
     * @param apex          position of the dart apex
     * @param neighbourPt   keypoint next to which the dart should be added
     * @param adjacency     whether to add dart before or after specified keypoint
     * @return              list of points of the dart edges
     */
    public ArrayList<Vector2D> addDart(Vector2D baseStart, Vector2D baseEnd, Vector2D apex,
                                       Vector2D neighbourPt, EPosition adjacency)
    {
        // Package and pass on
        ArrayList<Vector2D> pointsOfDart = new ArrayList<>();
        pointsOfDart.add(baseStart);
        pointsOfDart.add(apex);
        pointsOfDart.add(baseEnd);
        addDartKeypoints(neighbourPt, pointsOfDart, adjacency);
        return pointsOfDart;
    }

    /**
     * Method to add a dart given the end points of the line segment on which to add the dart, position along the
     * segment and the apex of the dart. Start and end points must be specified in the strict anti-clockwise order
     * for connectivity to be correct for plotting.
     * @param lineStart     position of start of segment
     * @param lineEnd       position of end of segment
     * @param position      position of dart centre in dimensionless units along edge
     * @param width         width of dart at base.
     * @param apex          position of the dart apex
     * @param straightSide  is side to which dart is to be added going to remain straight
     * @return              list of points of the dart edges
     */
    public ArrayList<Vector2D> addDart(Vector2D lineStart, Vector2D lineEnd, double position,
                                       double width, Vector2D apex, boolean straightSide)
    {
        // TODO: Really need to generalise this and the version above as they share al lot of the same code

        // Find the equation of the line
        Vector2D direction = new Vector2D(lineEnd.subtract(lineStart));
        Vector2D normal = new Vector2D(-direction.getY(), direction.getX());

        // Normalise the direction vectors
        direction.divideBy(direction.norm());
        normal.divideBy(normal.norm());

        // Find dart point
        double side_length = lineEnd.subtract(lineStart).norm();
        Vector2D point = new Vector2D(lineStart.add(direction.multiply(position * side_length)));

        // Package points
        ArrayList<Vector2D> pointsOfDart = new ArrayList<>();
        pointsOfDart.add(new Vector2D(point.subtract(direction.multiply(width / 2.0))));
        pointsOfDart.add(apex);
        pointsOfDart.add(new Vector2D(point.add(direction.multiply(width / 2.0))));

        // Correct points if necessary
        if (straightSide) pointsOfDart = adjustDartPointsForStraightEdge(pointsOfDart, lineStart, lineEnd);

        // Add keypoints
        addDartKeypoints(lineStart, pointsOfDart, EPosition.AFTER);

        // Return the corrected points
        return pointsOfDart;
    }

    /**
     * Adds given keypoints representing a dart to the keypoints list of the block.
     * @param adjPoint      point next to which the dart should be added.
     * @param points        the points to add (base, apex, base -- anticlockwise order)
     * @param adjacency     whether dart is added before or after the adjacent point
     */
    public void addDartKeypoints(Vector2D adjPoint,
                                 ArrayList<Vector2D> points,
                                 EPosition adjacency)
    {
        // Insert the keypoints
        if (adjacency == EPosition.AFTER)
        {
            addKeypointNextTo(points.get(0), adjPoint, EPosition.AFTER);
        }
        else
        {
            addKeypointNextTo(points.get(0), adjPoint, EPosition.BEFORE);
        }
        addKeypointNextTo(points.get(1), points.get(0), EPosition.AFTER);
        addKeypointNextTo(points.get(2), points.get(1), EPosition.AFTER);
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
     * Method to add a curve which has specified start and end point positions and gradients as well as an apex at which
     * the curve must have a stationary point. Fully qualified version requires user to specify the current directions
     * at the keypoints, otherwise the software will attempt to estimate these from neighbouring keypoints.
     * @param startPoint            start point of the curve
     * @param endPoint              end point of the curve
     * @param dirStart              direction of the start bounding line
     * @param dirEnd                direction of the end bounding line
     * @param tangentCorner         squared out corner of the apex of the curve
     * @param tangentPointOffset    offset from the apex corner to the curve itself
     * @param anglesAtEnds          angles required at the start and end points of the curve
     * @param offsetDirection       unit vector indicating quadrant of offset
     * @return                      final point on the curve
     */
    public Vector2D addDirectedCurveWithApexTangent(Vector2D startPoint, Vector2D endPoint,
                                                    Vector2D dirStart, Vector2D dirEnd,
                                                    Vector2D tangentCorner, double tangentPointOffset,
                                                    double[] anglesAtEnds, int[] offsetDirection)
    {
        // Specify the tangent point using corner and offset
        Vector2D tangentPoint = new Vector2D(
                tangentCorner.subtract(
                        new Vector2D(
                                tangentPointOffset * Math.cos(Math.PI / 4.0) * -offsetDirection[0],
                                tangentPointOffset * Math.sin(Math.PI / 4.0) * -offsetDirection[1]
                        )
                )
        );

        // Add guide point as a keypoint
        addKeypointNextTo(tangentPoint,
                          startPoint,
                          EPosition.AFTER);

        // Get direction of the bisect line (which will be normal to the curve)
        Vector2D apexToCorner = new Vector2D(tangentCorner.subtract(tangentPoint));

        // Get normal to this (which will be tangent to curve)
        Vector2D tangentDirection = new Vector2D(apexToCorner.getY(), -apexToCorner.getX());

        // Now we can construct the first part of the curve
        addDirectedCurve(startPoint,
                         tangentPoint,
                         dirStart,
                         tangentDirection, new double[] {anglesAtEnds[0], 0.0}
        );

        // Construct the second part of the curve (intersect at end is 90 degrees)
        return addDirectedCurve(tangentPoint,
                                endPoint,
                                tangentDirection,
                                dirEnd,
                                new double[] {0.0, anglesAtEnds[1]}
        );
    }

    /**
     * Method to add a curve which has specified start and end point gradients as well as an apex at which the curve
     * must have a stationary point. Directions are approximated from adjacent keypoints.
     * @param startPoint            start point of the curve
     * @param endPoint              end point of the curve
     * @param tangentCorner         squared out corner of the apex of the curve
     * @param tangentPointOffset    offset from the apex corner to the curve itself
     * @param anglesAtEnds          angles required at the start and end points of the curve
     * @param offsetDirection       unit vector indicating quadrant of offset
     * @return                      final point on the curve
     */
    public Vector2D addDirectedCurveWithApexTangent(Vector2D startPoint, Vector2D endPoint,
                                                Vector2D tangentCorner, double tangentPointOffset,
                                                double[] anglesAtEnds, int[] offsetDirection)
    {
        // Get directions
        Vector2D dirStart = getDirectionAtKeypoint(startPoint, EPosition.BEFORE);
        Vector2D dirEnd = getDirectionAtKeypoint(endPoint, EPosition.AFTER);

        // Pass to fully qualified version
        return addDirectedCurveWithApexTangent(startPoint, endPoint,
                                               dirStart, dirEnd,
                                               tangentCorner, tangentPointOffset,
                                               anglesAtEnds, offsetDirection);
    }

    /**
     * Add a curve between the start and end points which meets each bounding line at the specified angles in degrees.
     * Direction of both bounding lines are derived based on adjacent keypoints.
     * @param startPoint    start position of curve
     * @param endPoint      end position of curve
     * @param angleAtEnds   desired angle at each end of the curve
     * @return              final point on the curve
     */
    public Vector2D addDirectedCurve(Vector2D startPoint, Vector2D endPoint, double[] angleAtEnds)
    {
        // Get directions
        Vector2D dirStart = getDirectionAtKeypoint(startPoint, EPosition.BEFORE);
        Vector2D dirEnd = getDirectionAtKeypoint(endPoint, EPosition.AFTER);

        // Pass on arguments
        return addDirectedCurve(startPoint, endPoint, dirStart, dirEnd, angleAtEnds);
    }

    /**
     * Add a curve between the start and end points which meets each bounding line at the specified angles in degrees.
     * Direction of both bounding lines must be given. Note that the start and end points are assumed to be already in
     * the keypoints list and are not added by this method.
     * @param startPoint    start position of curve
     * @param endPoint      end position of curve
     * @param dirStart      direction of the start bounding line
     * @param dirEnd        direction of the end bounding line
     * @param angleAtEnds   desired angle at each end of the curve
     * @return              final point on the curve
     */
    public Vector2D addDirectedCurve(Vector2D startPoint, Vector2D endPoint,
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

        // Check we have the correct rotation -- dirStart should map to X axis if correct i.e. Y = 0
        double testY = Math.sin(rotang) * dirStart.getX() + Math.cos(rotang) * dirStart.getY();
        if (Math.abs(testY) > tol) rotang = -rotang;

        // Could test again and then error if cannot distinguish?

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
        VectorND coVec = new VectorND(inverse.postMultiply(constants));
        PolyCoeffs coeffs = new PolyCoeffs(coVec);

        // Check accuracy of solution
        VectorND test = new VectorND(mat.postMultiply(coVec));
        test.subtractThis(constants);
        test.subtractThis(constants);
        for (int i = 0; i < test.size(); i++)
        {
            if (Math.abs(test.get(i)) > tol)
            {
                System.out.println("Cubic spline solver is potentially inaccurate!");
            }
        }

        // Discretise by specified amount
        int numPts = (int)Math.ceil(refEnd.subtract(refStart).norm() * Main.res);

        // Find points on the curve by seeding x
        // might not always be robust -- should use a local curvilinear coordinate system really.
        double spacing = (refEnd.getX() - refStart.getX()) / (numPts - 1);
        Vector2D tmp = new Vector2D(startPoint);
        Vector2D tmp2 = new Vector2D(tmp);
        for (int i = 1; i < numPts - 1; i++)
        {
            double x = refStart.getX() + spacing * i;
            double y = coeffs.a * x * x * x + coeffs.b * x * x + coeffs.c * x + coeffs.d;

            // Add point reversing rotation and shift (exc. first and last)
            tmp = new Vector2D(Ri.postMultiply(new Vector2D(x, y)).add(startPoint));
            addKeypointNextTo(tmp, tmp2, EPosition.AFTER);
            tmp2 = new Vector2D(tmp);
        }

        // Return last point added
        return tmp;
    }

    /**
     * Method to construct a curve which meets a specified line at the end points at 90 degrees.
     * @param startPoint    start position of curve
     * @param endPoint      end position of curve
     * @return              final point on the curve
     */
    public Vector2D addRightAngleCurve(Vector2D startPoint, Vector2D endPoint)

    {
        double[] angles = new double[] {90.0, 90.0};
        return addDirectedCurve(startPoint, endPoint, angles);
    }

    /**
     * Method which constructs a curve which meets a specified line at the end points at 0 degrees (i.e. aligned)
     * @param startPoint    position of the start of the curve
     * @param endPoint      position of the end of the curve
     * @return              final point on the curve
     */
    public Vector2D addBlendedCurve(Vector2D startPoint, Vector2D endPoint)
    {
        double[] angles = new double[] {0.0, 0.0};
        return addDirectedCurve(startPoint, endPoint, angles);
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
    public static double triangleGetHypotenuseFromSide(double side1, double side2)
    {
        return Math.sqrt(side1 * side1 + side2 * side2);
    }

    /**
     * Get the adjacent side of a right-angled triangle given the hypotenuse and the other side
     * @param side1 adjacent side
     * @param hypotenuse hypotenuse
     * @return other adjacent side
     */
    public static double triangleGetAdjacentFromSide(double side1, double hypotenuse)
    {
        return Math.sqrt(hypotenuse * hypotenuse - side1 * side1);
    }

    /**
     * Get the opposite side of a right-angled triangle given the angle
     * @param hypotenuse hypotenuse side
     * @param angle internal angle
     * @return opposite
     */
    public static double triangleGetOppositeFromAngle(double hypotenuse, double angle)
    {
        return hypotenuse * Math.sin(Math.PI * angle / 180.0);
    }

    /**
     * Get the adjacent side of a right-angled triangle given the angle
     * @param hypotenuse hypotenuse side
     * @param angle internal angle
     * @return adjacent
     */
    public static double triangleGetAdjacentFromAngle(double hypotenuse, double angle)
    {
        return hypotenuse * Math.cos(Math.PI * angle / 180.0);
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
            int adjMultiplier = 1;
            if (adjacency == EPosition.BEFORE) j--;
            else
            {
                j++;
                adjMultiplier = -1;
            }

            // Periodic connection
            if (j < 0) j  = keypointsX.size() - 1;
            else if (j == keypointsX.size()) j = 0;
            Vector2D directionVector = new Vector2D(keypointsX.get(i) - keypointsX.get(j), keypointsY.get(i) - keypointsY.get(j));
            directionVector.multiplyBy(adjMultiplier);

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
