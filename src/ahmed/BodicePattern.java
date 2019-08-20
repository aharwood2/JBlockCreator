package ahmed;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockenums.EPosition;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

public class BodicePattern extends Pattern {
    private double halfBackCentreTapeMeasure;
    private double sideNeckToBustLengthR;
    private double sideNeckToBustToWaistR;
    private double acrossBackTapeMeasurement;
    private double acrossChestArmToArmLength;
    private double shoulderLengthRight;
    private double bustWidth;
    private double frontWaistArc;
    private double backWaistArc;
    private double frontBustArc;
    private double backBustArc;
    private double scyeDepth;
    private double shoulderSlope;
    private double sideSeamDepth;
    private double acrossShoulderBackandFront;
    private double waistToArmpitDepth;
    private double midShoulderToShoulderBlades;
    private double neckWidthFrontandBack;
    private double frontNeckDepth;
    private double backNeckDepth;
    private double shoulderToWaistDepth;

    private double armholeDepthEase;
    private double acrossBackEase;
    private double shoulderSlopeEase;
    private double frontBustArcEase;
    private double backBustArcEase;
    private double waistEase;
    private double shoulderRightX;

    @Override
    protected EMethod assignMethod() {
        return EMethod.AHMED;
    }

    @Override
    protected EGarment assignGarment() {
        return EGarment.BODICE;
    }

    @Override
    protected void addEasement() throws IndexOutOfBoundsException {
        // Refer to the populateEaseMeasurements static method for the order
        armholeDepthEase = easeMeasurements.get(0).getValue();
        acrossBackEase = easeMeasurements.get(1).getValue();
        shoulderSlopeEase = easeMeasurements.get(2).getValue();
        frontBustArcEase = easeMeasurements.get(3).getValue();
        backBustArcEase = easeMeasurements.get(4).getValue();
        waistEase = easeMeasurements.get(5).getValue();
    }

    public BodicePattern(Measurements dataStore) {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        createBlocks();
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore) {
        /*
        try
        {
            halfBackCentreTapeMeasure = dataStore.getMeasurement("A04").value;
            sideNeckToBustLengthR = dataStore.getMeasurement("A07").value;
            sideNeckToBustToWaistR = dataStore.getMeasurement("A08").value;
            acrossBackTapeMeasurement = dataStore.getMeasurement("A09").value;
            acrossChestArmToArmLength = dataStore.getMeasurement("A10").value;
            shoulderLengthRight = dataStore.getMeasurement("A11").value;
            bustWidth = dataStore.getMeasurement("A12").value;
            frontWaistArc = dataStore.getMeasurement("A26").value;
            backWaistArc = dataStore.getMeasurement("A27").value;
            frontBustArc = dataStore.getMeasurement("A56").value;
            backBustArc = dataStore.getMeasurement("A57").value;
            scyeDepth = dataStore.getMeasurement("B01").value;
            shoulderSlope = dataStore.getMeasurement("B04").value;
            sideSeamDepth = dataStore.getMeasurement("B05").value;
            acrossShoulderBackandFront = dataStore.getMeasurement("B06").value;
            waistToArmpitDepth = dataStore.getMeasurement("B07").value;
            midShoulderToShoulderBlades = dataStore.getMeasurement("B08").value;
            neckWidthFrontandBack = dataStore.getMeasurement("B09").value;
            frontNeckDepth = dataStore.getMeasurement("B10").value;
            backNeckDepth = dataStore.getMeasurement("B11").value;
            shoulderToWaistDepth = dataStore.getMeasurement("B16").value;
            shoulderRightX = dataStore.getMeasurement("B17").value;

            userName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            addMissingMeasurement(dataStore.getName(), e.getMeasurementId());
            return false;
        }

         */
        halfBackCentreTapeMeasure = 46.05;
        sideNeckToBustLengthR = 29.15;
        sideNeckToBustToWaistR = 47.11;
        acrossBackTapeMeasurement = 33.12;
        acrossChestArmToArmLength = 30.42;
        shoulderLengthRight = 12.89;
        bustWidth = 20.4;
        frontWaistArc = 41.48;
        backWaistArc = 39.33;
        frontBustArc = 51.85;
        backBustArc = 39.89;
        scyeDepth = 21.39;
        shoulderSlope = 3.59;
        sideSeamDepth = 22.54;
        acrossShoulderBackandFront = 35.94;
        waistToArmpitDepth = 22.54;
        midShoulderToShoulderBlades = 10.01;
        neckWidthFrontandBack = 13.02;
        frontNeckDepth = 4.91;
        backNeckDepth = 1.56;
        shoulderToWaistDepth = 41;
        shoulderRightX = 21.13;
        userName = dataStore.getName();
        return true;
    }

    @Override
    protected void createBlocks() {
        Block fullBlock = new Block(userName + "_Ahmed_Bodice_Block");
        blocks.add(fullBlock);

        double overallDiffTotalPatternWidthToWaistWithEase = ((backBustArc / 2.0) + 3.0 + (frontBustArc / 2.0) + 1.0) -
                ((frontWaistArc / 2.0) + 1.5 + (backWaistArc / 2.0) + 1.5);
        double[][] circleP;
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
        // As an intermediate which is point 6 but really slightly adjusted
        Vector2D point6_2 = new Vector2D(point3.getX() + (backBustArc / 2.0) + 3.0 - 0.01, point3.getY());


        // Calculation of point 5 as the intersect between point 4 and point 6 given their length
        circleP = Block.circleIntersect(point4.getX(), point6.getX(),
                point4.getY(), point6.getY(),
                (backWaistArc / 2.0) + 1.5, sideSeamDepth - armholeDepthEase);

        // Pick the one with smaller Y for now
        if (circleP[0][1] < circleP[1][1]) {
            point5 = new Vector2D(circleP[0][0], circleP[0][1]);
        } else {
            point5 = new Vector2D(circleP[1][0], circleP[1][1]);
        }

        Vector2D point7;
        Vector2D point8 = new Vector2D(point6.getX() + (frontBustArc / 2.0) + 1.0, point4.getY());

        // Calculation of point 7 as the intersect between 2 circles of point 6 and point 8
        circleP = Block.circleIntersect(point6.getX(), point8.getX(),
                point6.getY(), point8.getY(),
                sideSeamDepth - armholeDepthEase, (frontWaistArc / 2.0) + 1.5);

        // Pick the one with a smaller y
        if (circleP[0][1] < circleP[1][1]) {
            point7 = new Vector2D(circleP[0][0], circleP[0][1]);
        } else {
            point7 = new Vector2D(circleP[1][0], circleP[1][1]);
        }

        // A bunch of vector key points calculated
        Vector2D point10 = new Vector2D(point8.getX(), point3.getY());
        Vector2D point11 = new Vector2D(point8.getX(), point8.getY() + ((sideNeckToBustToWaistR - frontNeckDepth) * 0.75));
        Vector2D point12 = new Vector2D(point8.getX(), point8.getY() + (sideNeckToBustToWaistR - frontNeckDepth));
        Vector2D point13 = new Vector2D(point12.getX() - (neckWidthFrontandBack / 2.0), point12.getY() + frontNeckDepth);

        // Y9 temporarily stores a height which is used in calculating the actual y position of point 9 vector-ially
        double y9 = Math.sqrt(Math.pow(sideNeckToBustLengthR, 2.0) - Math.pow((bustWidth / 2.0) - (neckWidthFrontandBack / 2.0), 2.0));
        Vector2D point9 = new Vector2D(point8.getX(), point13.getY() - y9);
        Vector2D bustPoint = new Vector2D(point9.getX() - (bustWidth) / 2.0, point9.getY());

        // Calculation of point14
        double x14 = Math.sqrt((shoulderLengthRight * shoulderLengthRight) - (shoulderSlope * shoulderSlope));
        Vector2D point14 = new Vector2D(point13.getX() - ((frontBustArc + backBustArc) * 0.08) - x14, point13.getY() - shoulderSlope);

        // Calculation of point 16,18 and using the intersect between them to calculate point 17
        Vector2D point16 = new Vector2D(point2.getX() + (acrossBackTapeMeasurement / 2.0) + acrossBackEase, point2.getY());
        Vector2D point17;
        Vector2D point18 = new Vector2D(point1.getX() + (neckWidthFrontandBack / 2.0), 0.0);

        // First calculate the intermediate point between point 18 and 17

        Vector2D point17_18 = new Vector2D(point18.getX() + x14, point18.getY() - shoulderSlope);

        // Use point 17_18 to direct point 18 to calculate location of point 17
        dx = point17_18.getX() - point18.getX();
        dy = point17_18.getY() - point18.getY();
        lamda = (shoulderLengthRight + 1.0) / Math.sqrt(((dx * dx) + (dy * dy)));
        point17 = new Vector2D(point18.add(point17_18.subtract(point18).multiply(lamda)));

        // Addition of a bunch of key points
        fullBlock.addKeypoint(point6);
        fullBlock.addKeypoint(point7);
        fullBlock.addKeypoint(point8);
        fullBlock.addKeypoint(point9);
        fullBlock.addKeypoint(point10);
        fullBlock.addKeypoint(point11);
        fullBlock.addKeypoint(point12);

        // Also bustdart base start 1 calculation
        fullBlock.addKeypoint(point13);
        fullBlock.addKeypoint(bustPoint);
        Vector2D bustBase2 = new Vector2D(point13.getX() - (frontBustArc + backBustArc) * 0.08, point13.getY());
        fullBlock.addKeypoint(bustBase2);
        fullBlock.addKeypoint(point14);

        // Calculation of point15 -> need to add the length of the dart at this y position to push point 15
        // So that when the dart closes, the distance between point 11 and 15 is exactly a10/2
        Vector2D D = new Vector2D(bustPoint.subtract(point13));
        lamda = (point11.getY() - point13.getY()) / (D.getY());
        double x15temp1 = new Vector2D(point13.add(D.multiply(lamda))).getX();
        D = new Vector2D(bustPoint.subtract(bustBase2));
        lamda = (point11.getY() - bustBase2.getY()) / (D.getY());
        double x15temp2 = new Vector2D(bustBase2.add(D.multiply(lamda))).getX();
        Vector2D point15 = new Vector2D(point11.getX() - ((acrossChestArmToArmLength / 2) + (x15temp1 - x15temp2))
                , point11.getY());

        // Apex points used in directing the curves of the armhole
        Vector2D apex1 = new Vector2D(point15.getX() - 1.414, point6.getY() + 1.414);
        Vector2D apex2 = new Vector2D(point16.getX() + 1.7678, point6.getY() + 1.7678);

        // Addition of the rest of the keypoints
        fullBlock.addKeypoint(apex1);
        fullBlock.addKeypoint(point6);
        fullBlock.addKeypoint(point6_2);
        fullBlock.addKeypoint(apex2);
        fullBlock.addKeypoint(point17);
        fullBlock.addKeypoint(point18);
        fullBlock.addKeypoint(point1);
        fullBlock.addKeypoint(point2);
        fullBlock.addKeypoint(point3);
        fullBlock.addKeypoint(point4);
        fullBlock.addKeypoint(point5);
        // Point 6_2 because cannot have 4 points going into 1 / issues with using just point 6
        fullBlock.addKeypoint(point6_2);

        // Calculation and addition of the dart between point 7 and 8
        D = new Vector2D(point8.subtract(point7));
        lamda = (bustPoint.getX() - point7.getX()) / (D.getX());
        ArrayList<Vector2D> Dart7_8 = fullBlock.addDart(point7, point8, lamda,
                overallDiffTotalPatternWidthToWaistWithEase * 0.37, new Vector2D(bustPoint.getX(),
                        bustPoint.getY() - 2.5), false);

        // Aadding the curves to keep the angles at 90 degrees between the darts and the adjacent lines
        fullBlock.addRightAngleCurve(point7, Dart7_8.get(0));
        fullBlock.addRightAngleCurve(Dart7_8.get(2),point8);

        //
        //fullBlock.addDirectedCurveWithApexTangent(point12, point13, new Vector2D(point12.subtract(point11)), new Vector2D(-1, 0), new Vector2D(point13.getX(), point12.getY()), 2, new double[]{90, 90}, new int[]{1, 1});
        fullBlock.addQuadraticBezierCurve(point12, new Vector2D(point13.getX(), point12.getY()), point13);

        // All these curves make up the armhole curves
        fullBlock.addDirectedCurve(apex1, point6, new Vector2D(-1.0, -1.0), new Vector2D(-1.0, 0.0), new double[]{0.0, 0.0});
        fullBlock.addDirectedCurve(point14, apex1, point15, 90.0);
        fullBlock.addDirectedCurve(point6_2, apex2, new Vector2D(-1.0, 0.0), new Vector2D(-1.0, 1.0), new double[]{0.0, 0.0});
        fullBlock.addDirectedCurve(apex2, point17, point16, 0.0);

        // Dart between 17 and 18
        ArrayList<Vector2D> Dart17_18 = fullBlock.addDart(point17, point18,
                0.5, 1.0, midShoulderToShoulderBlades - 2.5, true, true);

        fullBlock.addRightAngleCurve(point17, Dart17_18.get(0));

        fullBlock.addQuadraticBezierCurve(point18, new Vector2D(point18.getX(), point1.getY()), point1);

        fullBlock.addRightAngleCurve(Dart17_18.get(2), point18);

        ArrayList<Vector2D> Dart4_5 = fullBlock.addDart(point4, point5,
                (backWaistArc / 4.0) / (new Vector2D(point5.subtract(point4)).norm()),
                overallDiffTotalPatternWidthToWaistWithEase * 0.37, waistToArmpitDepth - 2.5, true, false);

        fullBlock.addRightAngleCurve(point4, Dart4_5.get(0));
        fullBlock.addRightAngleCurve(Dart4_5.get(2), point5);

    }

    protected static ArrayList<easeMeasurement> easeMeasurements = new ArrayList<>();

    public static void populateEaseMeasurements()
    {
        // Check to see it hasn't already been populated / it is empty
        if (easeMeasurements.size() > 0) {return;}
        easeMeasurements.add(new easeMeasurement("Armhole Depth", 1.7));
        easeMeasurements.add(new easeMeasurement("Across Back Ease", 0.5));
        easeMeasurements.add(new easeMeasurement("Shoulder Slop Ease", 0.3));
        easeMeasurements.add(new easeMeasurement("Front Bust Arc Ease", 1.0));
        easeMeasurements.add(new easeMeasurement("Back Bust Arc Ease", 3.0));
        easeMeasurements.add(new easeMeasurement("Waist Ease", 1.5));
    }

    public static ArrayList<easeMeasurement> getEaseMeasurement()
    {
        return easeMeasurements;
    }
}
