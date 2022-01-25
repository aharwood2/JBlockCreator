package ahmed;

import jblockenums.EPattern;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class BodicePattern
        extends Pattern
{
    public BodicePattern(String username, InputFileData dataStore, MeasurementSet template)
    {
        super(username, dataStore, template);
    }

    @Override
    protected EPattern assignPattern()
    {
        return EPattern.AHMED_BODICE;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("halfBackCentreTapeMeasure", "A04"));
        measurements.addMeasurement(new Measurement("sideNeckToBustLengthR", "A07"));
        measurements.addMeasurement(new Measurement("sideNeckToBustToWaistR", "A08"));
        measurements.addMeasurement(new Measurement("acrossBackTapeMeasurement", "A09"));
        measurements.addMeasurement(new Measurement("acrossChestArmToArmLength", "A10"));
        measurements.addMeasurement(new Measurement("shoulderLengthRight", "A11"));
        measurements.addMeasurement(new Measurement("bustWidth", "A12"));
        measurements.addMeasurement(new Measurement("frontWaistArc", "A26"));
        measurements.addMeasurement(new Measurement("backWaistArc", "A27"));
        measurements.addMeasurement(new Measurement("frontBustArc", "A56"));
        measurements.addMeasurement(new Measurement("backBustArc", "A57"));
        measurements.addMeasurement(new Measurement("scyeDepth", "B01"));
        measurements.addMeasurement(new Measurement("shoulderSlope", "B04"));
        measurements.addMeasurement(new Measurement("sideSeamDepth", "B05"));
        measurements.addMeasurement(new Measurement("acrossShoulderBackandFront", "B06"));
        measurements.addMeasurement(new Measurement("waistToArmpitDepth", "B07"));
        measurements.addMeasurement(new Measurement("midShoulderToShoulderBlades", "B08"));
        measurements.addMeasurement(new Measurement("neckWidthFrontandBack", "B09"));
        measurements.addMeasurement(new Measurement("frontNeckDepth", "B10"));
        measurements.addMeasurement(new Measurement("backNeckDepth", "B11"));
        measurements.addMeasurement(new Measurement("shoulderToWaistDepth", "B16"));
        measurements.addMeasurement(new Measurement("shoulderRightX", "B19"));

        // Add ease as measurements with default values
        measurements.addMeasurement(new Measurement("armholeDepthEase", 1.7));
        measurements.addMeasurement(new Measurement("acrossBackEase", 0.5));
        measurements.addMeasurement(new Measurement("shoulderSlopeEase", 0.3));
        measurements.addMeasurement(new Measurement("frontBustArcEase", 1.0));
        measurements.addMeasurement(new Measurement("backBustArcEase", 3.0));
        measurements.addMeasurement(new Measurement("waistEase", 1.5));
    }

    @Override
    public void createBlocks()
    {
        // Pull from store
        var halfBackCentreTapeMeasure = get("halfBackCentreTapeMeasure");
        var sideNeckToBustLengthR= get("sideNeckToBustLengthR");
        var sideNeckToBustToWaistR= get("sideNeckToBustToWaistR");
        var acrossBackTapeMeasurement= get("acrossBackTapeMeasurement");
        var acrossChestArmToArmLength= get("acrossChestArmToArmLength");
        var shoulderLengthRight= get("shoulderLengthRight");
        var bustWidth= get("bustWidth");
        var frontWaistArc= get("frontWaistArc");
        var backWaistArc= get("backWaistArc");
        var frontBustArc= get("frontBustArc");
        var backBustArc= get("backBustArc");
        var scyeDepth= get("scyeDepth");
        var shoulderSlope= get("shoulderSlope");
        var sideSeamDepth= get("sideSeamDepth");
        var acrossShoulderBackandFront= get("acrossShoulderBackandFront");
        var waistToArmpitDepth= get("waistToArmpitDepth");
        var midShoulderToShoulderBlades= get("midShoulderToShoulderBlades");
        var neckWidthFrontandBack= get("neckWidthFrontandBack");
        var frontNeckDepth= get("frontNeckDepth");
        var backNeckDepth= get("backNeckDepth");
        var shoulderToWaistDepth= get("shoulderToWaistDepth");
        var armholeDepthEase= get("armholeDepthEase");
        var acrossBackEase= get("acrossBackEase");
        var shoulderSlopeEase= get("shoulderSlopeEase");
        var shoulderRightX= get("shoulderRightX");

        Block fullBlock = new Block(userName + "_Ahmed_Bodice_Block");
        blocks.add(fullBlock);

        // Calculation of the length between the bottom corner and armhole opening
        double overallDiffTotalPatternWidthToWaistWithEase = ((backBustArc / 2.0) + 3.0 + (frontBustArc / 2.0) + 1.0) -
                ((frontWaistArc / 2.0) + 1.5 + (backWaistArc / 2.0) + 1.5);
        double backWaistDartWidth = overallDiffTotalPatternWidthToWaistWithEase * 0.30;
        double frontWaistDartWidth = overallDiffTotalPatternWidthToWaistWithEase * 0.37;

        // Initialization of the double that will store the solution for circle intersections to calculate certain points
        double[][] circleP;
        // Initialization of temporary variables used in calculation further down
        double lamda, dx, dy;
        double rightShoulderToFrontWaistLength =
                Math.sqrt((shoulderRightX * shoulderRightX) + (shoulderToWaistDepth * shoulderToWaistDepth))
                        + shoulderSlopeEase;

        // Some vector keypoints added
        Vector2D point1 = new Vector2D(0.0, -backNeckDepth);
        Vector2D point2 = new Vector2D(0.0, point1.getY() - ((scyeDepth + armholeDepthEase) / 2.0));
        Vector2D point3 = new Vector2D(0.0, point1.getY() - (scyeDepth + armholeDepthEase));
        Vector2D point4 = new Vector2D(0.0, point1.getY() - halfBackCentreTapeMeasure);
        Vector2D point5;
        Vector2D point6 = new Vector2D(point3.getX() + (backBustArc / 2.0) + 3.0, point3.getY());

        // Can't go to point 6 twice when drafting bodice, instead this point is used
        // As an intermediate which is point 6 but really slightly adjusted by -0.01
        Vector2D point6_2 = new Vector2D(point3.getX() + (backBustArc / 2.0) + 3.0 - 0.01, point3.getY());

        // Calculation of point 5 as the intersect between two circles of point 4 and point 6 given their lengths
        circleP = Block.circleIntersect(point4.getX(), point6.getX(),
                                        point4.getY(), point6.getY(),
                                        (backWaistArc / 2.0) + 1.5 + backWaistDartWidth,
                                        sideSeamDepth - armholeDepthEase);

        // Pick the one with smaller Y for now as point5
        if (circleP[0][1] < circleP[1][1])
        {
            point5 = new Vector2D(circleP[0][0], circleP[0][1]);
        }
        else
        {
            point5 = new Vector2D(circleP[1][0], circleP[1][1]);
        }

        Vector2D point7;
        Vector2D point8 = new Vector2D(point6.getX() + (frontBustArc / 2.0) + 1.0, point4.getY());

        // Calculation of point 7 as the intersect between 2 circles of point 6 and point 8 with their lengths
        circleP = Block.circleIntersect(point6.getX(), point8.getX(),
                                        point6.getY(), point8.getY(),
                                        sideSeamDepth - armholeDepthEase,
                                        (frontWaistArc / 2.0) + 1.5 + frontWaistDartWidth);

        // Pick the one with a smaller y for point7 based on inspection of where the 2 points of intersection are
        // and which one looks more correct
        if (circleP[0][1] < circleP[1][1])
        {
            point7 = new Vector2D(circleP[0][0], circleP[0][1]);
        }
        else
        {
            point7 = new Vector2D(circleP[1][0], circleP[1][1]);
        }

        // A bunch of vector key points calculated
        Vector2D point10 = new Vector2D(point8.getX(), point3.getY());
        Vector2D point11 = new Vector2D(point8.getX(),
                                        point8.getY() + ((sideNeckToBustToWaistR - frontNeckDepth) * 0.75));
        Vector2D point12 = new Vector2D(point8.getX(), point8.getY() + (sideNeckToBustToWaistR - frontNeckDepth));
        Vector2D point13 = new Vector2D(point12.getX() - (neckWidthFrontandBack / 2.0),
                                        point12.getY() + frontNeckDepth);

        // Y9 temporarily stores a height which is used in calculating the actual y position of point 9 vector-ially
        double y9 = Math.sqrt(
                Math.pow(sideNeckToBustLengthR, 2.0) - Math.pow((bustWidth / 2.0) - (neckWidthFrontandBack / 2.0),
                                                                2.0));
        Vector2D point9 = new Vector2D(point8.getX(), point13.getY() - y9);
        Vector2D bustPoint = new Vector2D(point9.getX() - (bustWidth) / 2.0, point9.getY());

        // Calculation of point14 and x14 is also used in calculation of point17 as the shoulder length and shoulder
        // Drop is the same on both sides of the bodice
        double x14 = Math.sqrt((shoulderLengthRight * shoulderLengthRight) - (shoulderSlope * shoulderSlope));

        // Point14x: starting from point13, subtract bust dart width, then x14 to get x position
        // Point14y: from point13, go down by shoulder slope value
        Vector2D point14 = new Vector2D(point13.getX() - ((frontBustArc + backBustArc) * 0.08) - x14,
                                        point13.getY() - shoulderSlope);

        // Calculation of point 16,18
        Vector2D point16 = new Vector2D(point2.getX() + (acrossBackTapeMeasurement / 2.0) + acrossBackEase,
                                        point2.getY());
        Vector2D point17;
        Vector2D point18 = new Vector2D(point1.getX() + (neckWidthFrontandBack / 2.0), 0.0);

        // First calculate the intermediate point between point 18 and 17
        Vector2D point17_18 = new Vector2D(point1.getX() + acrossShoulderBackandFront / 2.0,
                                           point18.getY() - shoulderSlope);

        // Use point 17_18 to direct point 18 to calculate location of point 17 where 1.0 is the dart width
        dx = point17_18.getX() - point18.getX();
        dy = point17_18.getY() - point18.getY();
        lamda = (shoulderLengthRight + 1.0) / Math.sqrt(((dx * dx) + (dy * dy)));
        point17 = new Vector2D(point18.add(point17_18.subtract(point18).multiply(lamda)));

        // Addition of a bunch of key points starting from point6
        fullBlock.addKeypoint(point6);
        fullBlock.addKeypoint(point7);
        fullBlock.addKeypoint(point8);
        fullBlock.addKeypoint(point9);
        fullBlock.addKeypoint(point10);
        fullBlock.addKeypoint(point11);
        fullBlock.addKeypoint(point12);
        fullBlock.addKeypoint(point13);
        fullBlock.addKeypoint(bustPoint);
        // Calculation of the bust dart base 2
        Vector2D bustBase2 = new Vector2D(point13.getX() - (frontBustArc + backBustArc) * 0.08, point13.getY());
        fullBlock.addKeypoint(bustBase2);
        fullBlock.addKeypoint(point14);


        // Calculation of point15 -> need to add the length of the dart at this y position to push point 15
        // So that when the dart closes, the distance between point 11 and 15 is exactly a10/2
        // First calculation direction vector from point 13 to bust
        Vector2D D = new Vector2D(bustPoint.subtract(point13));

        // Solve Vector line equation for Y = point11 Y as point15 has this Y value
        lamda = (point11.getY() - point13.getY()) / (D.getY());

        // Calculate X value of the dart at this point
        double x15temp1 = new Vector2D(point13.add(D.multiply(lamda))).getX();

        // Same as above for the other side of the dart
        D = new Vector2D(bustPoint.subtract(bustBase2));
        lamda = (point11.getY() - bustBase2.getY()) / (D.getY());
        double x15temp2 = new Vector2D(bustBase2.add(D.multiply(lamda))).getX();

        // x15temp1-x15temp2 gives the width of the dart at Y = point15 which we then add since when the dart is closed,
        // It will be subtracted
        Vector2D point15 = new Vector2D(point11.getX() -
                                                ((acrossChestArmToArmLength / 2) + (x15temp1 - x15temp2)),
                                        point11.getY());

        // Apex points used in directing the curves of the armhole
        // Not used anymore in the new calculation of armhole curves
        // Values obtained by pythagoras
        Vector2D apex1 = new Vector2D(point15.getX() - 1.414, point6.getY() + 1.414);
        Vector2D apex2 = new Vector2D(point16.getX() + 1.7678, point6.getY() + 1.7678);

        Vector2D controlPoint1 = new Vector2D(point15.getX() + 1.414, point6.getY());
        Vector2D controlPoint2 = new Vector2D(point16.getX() - 1.7678, point6_2.getY());

        // Addition of the rest of the keypoints
        fullBlock.addKeypoint(point15);
        fullBlock.addKeypoint(point6);
        fullBlock.addKeypoint(point6_2);
        fullBlock.addKeypoint(point16);
        fullBlock.addKeypoint(point17);
        fullBlock.addKeypoint(point18);
        fullBlock.addKeypoint(point1);
        fullBlock.addKeypoint(point2);
        fullBlock.addKeypoint(point3);
        fullBlock.addKeypoint(point4);
        fullBlock.addKeypoint(point5);
        // Point 6_2 because cannot have 4 points going into 1 / issues with using just point 6
        fullBlock.addKeypoint(point6_2);

        ArrayList<Vector2D> Dart4_5 = fullBlock.addDart(point4, point5,
                                                        (backWaistArc / 4.0) / (new Vector2D(
                                                                point5.subtract(point4)).norm()),
                                                        backWaistDartWidth, waistToArmpitDepth - 2.5, true, false);

        /* Get a directional vector from midpoint of 17->18 to the apex of the back waist dart
         This is required as the dart between 17 and 18 needs to be linked to the dart between 4 and 5
         In terms of their apex points
         */
        Vector2D mid17_18 = new Vector2D(point18.add(point17.subtract(point18).divide(2.0)));
        D = new Vector2D(Dart4_5.get(1).subtract(mid17_18));
        Vector2D backShoulderApex = new Vector2D(mid17_18.add(D.divide(D.norm()).multiply(
                (midShoulderToShoulderBlades - 2.5))));
        ArrayList<Vector2D> Dart17_18 = fullBlock.addDart(point17, point18, 0.5, 1.0, backShoulderApex, true);

        fullBlock.addQuadraticBezierCurve(point12, new Vector2D(point13.getX(), point12.getY()), point13);

        // All these curves make up the armhole curves
        //fullBlock.addDirectedCurve(apex1, point6, new Vector2D(-1.0, -1.0), new Vector2D(-1.0, 0.0), new double[]{0.0, 0.0});
        //fullBlock.addDirectedCurve(point14, apex1, point15, 90.0);
        //fullBlock.addDirectedCurve(point6_2, apex2, new Vector2D(-1.0, 0.0), new Vector2D(-1.0, 1.0), new double[]{0.0, 0.0});
        //fullBlock.addDirectedCurve(apex2, point17, point16, 0.0);

        // Alternative method of making the armhole curve using bezier curves, less strict but more appealing to the eye
        fullBlock.addQuadraticBezierCurve(point15, controlPoint1, point6);
        fullBlock.addDirectedCurve(point14, point15, new double[]{90.0, 0.0});
        fullBlock.addQuadraticBezierCurve(point6_2, controlPoint2, point16);
        fullBlock.addDirectedCurve(point16, point17, new double[]{0.0, 90.0});

        fullBlock.addQuadraticBezierCurve(point18, new Vector2D(point18.getX(), point1.getY()), point1);

        // Calculation and addition of the dart between point 7 and 8 as it is a ratio of the total length
        D = new Vector2D(point8.subtract(point7));
        lamda = (bustPoint.getX() - point7.getX()) / (D.getX());

        ArrayList<Vector2D> Dart7_8 = fullBlock.addDart(point7, point8, lamda,
                                                        frontWaistDartWidth, new Vector2D(bustPoint.getX(),
                                                                                          bustPoint.getY() - 2.5),
                                                        false);
    }
}
