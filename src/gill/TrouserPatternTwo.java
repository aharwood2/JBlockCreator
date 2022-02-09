package gill;

import jblockenums.EPattern;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;
import java.util.Collections;

public class TrouserPatternTwo
        extends Pattern
{
    public TrouserPatternTwo(String userName, InputFileData dataStore, MeasurementSet template)
    {
        super(userName, dataStore, template);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.GILL_TROUSER2;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("waistToHipLength", "A15"));
        measurements.addMeasurement(new Measurement("kneeCircumR", "A18"));
        measurements.addMeasurement(new Measurement("ankleCircumR", "A19"));
        measurements.addMeasurement(new Measurement("frontWaistArc", "A26"));
        measurements.addMeasurement(new Measurement("backWaistArc", "A27"));
        measurements.addMeasurement(new Measurement("frontAbdomenArc", "A28"));
        measurements.addMeasurement(new Measurement("backSeatArc", "A30"));
        measurements.addMeasurement(new Measurement("frontHipArc", "A31"));
        measurements.addMeasurement(new Measurement("backHipArc", "A32"));
        measurements.addMeasurement(new Measurement("waistToAbdomen", "A33"));
        measurements.addMeasurement(new Measurement("waistToSeat", "A34"));
        measurements.addMeasurement(new Measurement("sideStreamUpliftR", "A37"));
        measurements.addMeasurement(new Measurement("bodyRise", "A38"));
        measurements.addMeasurement(new Measurement("seatDepth", "A39"));
        measurements.addMeasurement(new Measurement("hipDepth", "A40"));
        measurements.addMeasurement(new Measurement("ankleCircumHeightR", "A41"));
        measurements.addMeasurement(new Measurement("kneeCircumHeightR", "A42"));
        measurements.addMeasurement(new Measurement("crotchHeight", "A43"));
        measurements.addMeasurement(new Measurement("frontSeatArc", "A46"));
        measurements.addMeasurement(new Measurement("seatBKZ", "A51"));
        measurements.addMeasurement(new Measurement("frontWaistZ", "A52"));
        measurements.addMeasurement(new Measurement("backWaistZ", "A53"));
        measurements.addMeasurement(new Measurement("waistWidth", "A59"));
        measurements.addMeasurement(new Measurement("hipWidth", "A60"));
        measurements.addMeasurement(new Measurement("frontAbdomenZ", "A61"));
        measurements.addMeasurement(new Measurement("fullCrotchLength", "A62"));

        // Ease
        measurements.addMeasurement(new Measurement("waistEase", 2.0));
        measurements.addMeasurement(new Measurement("seatEase", 6.0));
        measurements.addMeasurement(new Measurement("hipEase", 6.0));
        measurements.addMeasurement(new Measurement("kneeEase", 15.0));
        measurements.addMeasurement(new Measurement("ankleEase", 22.0));
    }

    @Override
    public void createBlocks()
    {
        // Pull from store
        var waistToHipLength= get("waistToHipLength");
        var kneeCircumR= get("kneeCircumR");
        var ankleCircumR= get("ankleCircumR");
        var frontWaistArc= get("frontWaistArc");
        var backWaistArc= get("backWaistArc");
        var frontAbdomenArc= get("frontAbdomenArc");
        var backSeatArc= get("backSeatArc");
        var frontHipArc= get("frontHipArc");
        var backHipArc= get("backHipArc");
        var waistToAbdomen= get("waistToAbdomen");
        var waistToSeat= get("waistToSeat");
        var sideStreamUpliftR= get("sideStreamUpliftR");
        var bodyRise= get("bodyRise");
        var seatDepth= get("seatDepth");
        var hipDepth= get("hipDepth");
        var ankleCircumHeightR= get("ankleCircumHeightR");
        var kneeCircumHeightR= get("kneeCircumHeightR");
        var crotchHeight= get("crotchHeight");
        var frontSeatArc= get("frontSeatArc");
        var seatBKZ= get("seatBKZ");
        var frontWaistZ= get("frontWaistZ");
        var backWaistZ= get("backWaistZ");
        var waistWidth= get("waistWidth");
        var hipWidth= get("hipWidth");
        var frontAbdomenZ= get("frontAbdomenZ");
        var fullCrotchLength= get("fullCrotchLength");
        var waistEase= get("waistEase");
        var seatEase= get("seatEase");
        var hipEase= get("hipEase");
        var kneeEase= get("kneeEase");
        var ankleEase= get("ankleEase");
        
        Block frontBlock = new Block(userName + "_Gill_FrontBlock");
        blocks.add(frontBlock);

        double centreXpoint = 0.0;
        double centreYpoint = 0.0;

        double kneeXPosition = centreXpoint + crotchHeight - kneeCircumHeightR; //A43 - A42
        double ankleXPosition = centreXpoint + crotchHeight - ankleCircumHeightR; //A43 - A41
        double hipXPosition, seatXPosition;
        if (waistToHipLength > waistToSeat)
        {
            hipXPosition = centreXpoint - (bodyRise - waistToHipLength); //A38 - A15
            seatXPosition = centreXpoint - (bodyRise - waistToSeat); //A38 - A34
        }
        else
        {
            hipXPosition = centreXpoint - (bodyRise - waistToSeat); //A38 - A15
            seatXPosition = centreXpoint - (bodyRise - waistToHipLength); //A38 - A34
        }
        double waistXPosition = centreXpoint - bodyRise; //A38

        // Starting from the centre point, directly south of crotch point, going in an anti-clockwise manner
        // Calculation for front crotch extension
        double frontCrotchExtension = (hipDepth - 6.0) * 0.38;
        // Cell G32
        double halfFrontCreaselineFromHipPoint = (((frontHipArc + seatEase / 2.0) / 2.0) + frontCrotchExtension) / 2.0;

        double frontSeatExtension = (seatDepth - 6.0) * 0.38;

        // Calculations for shaping
        double interimFrontSideSeamShaping = (hipWidth - waistWidth) / 2.0;
        double interimFrontDartWidth = (frontAbdomenArc - frontWaistArc) / 2.0;
        double interimCBShaping = 0.0;
        if (frontAbdomenZ > frontWaistZ)
        {
            interimCBShaping = frontAbdomenZ - frontWaistZ;
        }
        double sumInterims = interimFrontDartWidth + interimFrontSideSeamShaping + interimCBShaping;
        double halfOverallDiffFrHipToFrWaistWithEase = (((frontHipArc + (seatEase / 2.0))
                - (frontWaistArc + (waistEase / 2.0))) / 2.0);

        // All the main keypoints added as vectors
        Vector2D point1 = new Vector2D(centreXpoint, -halfFrontCreaselineFromHipPoint + centreYpoint);
        Vector2D point2 = new Vector2D(kneeXPosition, -(((kneeCircumR + kneeEase) / 4.0) - 0.75) + centreYpoint);
        Vector2D point3 = new Vector2D(ankleXPosition, -(((ankleCircumR + ankleEase) / 4.0) - 0.75) + centreYpoint);
        Vector2D point4 = new Vector2D(ankleXPosition, ((ankleCircumR + ankleEase) / 4.0) - 0.75 + centreYpoint);
        Vector2D point5 = new Vector2D(kneeXPosition, ((kneeCircumR + kneeEase) / 4.0) - 0.75 + centreYpoint);

        Vector2D point6 = new Vector2D(centreXpoint, ((frontCrotchExtension + (frontHipArc / 2.0) + (hipEase / 4.0))
                - halfFrontCreaselineFromHipPoint) + centreYpoint);

        Vector2D point7 = new Vector2D(hipXPosition, ((frontHipArc / 2.0 + hipEase / 4.0 + frontCrotchExtension)
                - halfFrontCreaselineFromHipPoint) + centreYpoint);

        Vector2D point8 = new Vector2D(seatXPosition, (frontSeatArc / 2.0 + seatEase / 4.0 + frontSeatExtension)
                - halfFrontCreaselineFromHipPoint + centreYpoint);

        Vector2D point9 = new Vector2D(waistXPosition - sideStreamUpliftR, halfFrontCreaselineFromHipPoint
                - (halfOverallDiffFrHipToFrWaistWithEase * (interimFrontSideSeamShaping / sumInterims)) + centreYpoint);

        Vector2D point10 = new Vector2D(waistXPosition, -(halfFrontCreaselineFromHipPoint -
                (frontSeatExtension + halfOverallDiffFrHipToFrWaistWithEase * interimCBShaping / sumInterims))
                + centreYpoint);
        Vector2D point11 = new Vector2D(seatXPosition,
                                        -(halfFrontCreaselineFromHipPoint - frontSeatExtension + centreYpoint));
        Vector2D point12 = new Vector2D(hipXPosition,
                                        -(halfFrontCreaselineFromHipPoint - frontCrotchExtension + centreYpoint));

        // Point 1 and 2
        frontBlock.addKeypoint(point1);
        frontBlock.addKeypoint(point2);

        // Calculation of a point for the curve to go through which is 1/3rd the width 1/4 the heigh between them
        Vector2D point1and2 = new Vector2D((point1.getX() + ((point2.getX() - point1.getX())) / 3.0),
                                           point1.getY() + ((point2.getY() - point1.getY()) * 0.75) + centreYpoint);

        frontBlock.addDirectedCubicSpline(point1, point2, new Vector2D(point1and2.subtract(point1)),
                                    new Vector2D(point3.subtract(point2)), new double[]{0.0, 0.0});

        // Point 3,4,5,6,7
        frontBlock.addKeypoint(point3);
        frontBlock.addKeypoint(point4);
        frontBlock.addKeypoint(point5);
        frontBlock.addKeypoint(point6);
        frontBlock.addKeypoint(point7);

        // Curve between point 5 and 6 keeping angles at start and end 0 deg with respect to preceding and proceeding curves
        frontBlock.addDirectedCubicSpline(point5, point6, new Vector2D(point5.subtract(point4)),
                                    new Vector2D(point7.subtract(point6)), new double[]{0.0, 0.0});

        // Point 8 and 9
        frontBlock.addKeypoint(point8);
        frontBlock.addKeypoint(point9);

        // Circular curve between 8 and 9 making sure apex is lower than height of point 8
        frontBlock.addCircularArc(point8, point9, (point9.getY()
                - (new Vector2D(point8.add(point9.subtract(point8).divide(2.0)))).getY()) / 2.0, false);

        // Curve that connect point 7 and 8 together as a circular curve
        frontBlock.addCircularArc(point7, point8, (point8.getY()
                - (new Vector2D(point7.add(point8.subtract(point7).divide(2.0)))).getY()) / 2.0, true);

        // Point 10
        frontBlock.addKeypoint(point10);

        // Dart between 9 and 10
        // Solved a vector line of equation for lambda when y = 0 to calculate position
        Vector2D frontDartApexPos = new Vector2D((new Vector2D(point9.add(point10.subtract(point9).divide(2.0)))).getX()
                                                         + waistToAbdomen - 1.5, centreYpoint);

        ArrayList<Vector2D> frontDarts = frontBlock.addDart(point9, point10,
                                                            -point9.getY() / new Vector2D(
                                                                    point10.subtract(point9)).getY(),
                                                            (interimFrontDartWidth / sumInterims) * halfOverallDiffFrHipToFrWaistWithEase,
                                                            frontDartApexPos, false);

        Vector2D frontDartStart = frontDarts.get(0);
        Vector2D frontDartEnd = frontDarts.get(2);

        // Adding curves between points and dart to make sure angles are at 90 deg
        frontBlock.addRightAngleCurve(point9, frontDartStart);
        frontBlock.addRightAngleCurve(frontDartEnd, point10);

        // Point 11
        frontBlock.addKeypoint(point11);

        // Final steep curve that connects to crotch point point 12 not added as a keypoint but through curve function
        frontBlock.addDirectedCubicSpline(point11, point1, point12, 0.0);

        // Get the max and min y with a small additional buffer of 5cm for the construction lines
        double maxFrontY = Collections.max(frontBlock.getPlottableKeypointsY()) + 5.0;
        double minFrontY = Collections.min(frontBlock.getPlottableKeypointsY()) - 5.0;

        // All the key construction points
        frontBlock.addConstructionPoint(new Vector2D(waistXPosition, minFrontY),
                                        new Vector2D(waistXPosition, maxFrontY), "Waist");
        frontBlock.addConstructionPoint(new Vector2D(seatXPosition, minFrontY), new Vector2D(seatXPosition, maxFrontY),
                                        "Seat");
        frontBlock.addConstructionPoint(new Vector2D(hipXPosition, minFrontY), new Vector2D(hipXPosition, maxFrontY),
                                        "Hip");
        frontBlock.addConstructionPoint(new Vector2D(centreXpoint, minFrontY), new Vector2D(centreXpoint, maxFrontY),
                                        "Crotch");
        frontBlock.addConstructionPoint(new Vector2D(kneeXPosition, minFrontY), new Vector2D(kneeXPosition, maxFrontY),
                                        "Knee");
        frontBlock.addConstructionPoint(new Vector2D(ankleXPosition, minFrontY),
                                        new Vector2D(ankleXPosition, maxFrontY), "Ankle");

        // Construction point representing the centre fold
        frontBlock.addConstructionPoint(new Vector2D(Collections.min(frontBlock.getPlottableKeypointsX()) - 5.0,
                                                     centreYpoint),
                                        new Vector2D(Collections.max(frontBlock.getPlottableKeypointsX()) + 5.0,
                                                     centreYpoint), "CentreFold");


        Block backBlock = new Block(userName + "_Gill_BackBlock");
        blocks.add(backBlock);

        // Start from point 13 going anti-clockwise
        // Need to calculate halfCreaselineFromHipPoint
        double backCrotchExtension = (hipDepth - 6.0) * 0.62;
        // Height from origin to point 13
        // (A32 + Ease/2)/2+ crotch extension
        double halfBackCreaselineFromHipPoint = (((backHipArc + seatEase / 2.0) / 2.0) + backCrotchExtension) / 2.0;

        // Back shaping
        double halfOverallDiffBkHipToBkWaistInclEase = ((backHipArc + (hipEase / 2.0)) - (backWaistArc + (waistEase / 2.0))) / 2.0;

        // Interim values
        double interimBackSideSeamShaping = (hipWidth - waistWidth) / 2.0;
        double interimBackDartWidth = (backSeatArc - backWaistArc) / 2.0;
        double interimBackCBShaping = Math.abs(seatBKZ - backWaistZ);
        double backSumInterims = interimBackCBShaping + interimBackDartWidth + interimBackSideSeamShaping;
        double backSeatExtension = (seatDepth - 6.0) * 0.62;//calculation of CELL G25

        // All keypoints except point 16 added as vectors
        Vector2D point13 = new Vector2D(centreXpoint + 1.0, halfBackCreaselineFromHipPoint + centreYpoint);
        Vector2D point14 = new Vector2D(hipXPosition,
                                        halfBackCreaselineFromHipPoint - backCrotchExtension + centreYpoint);
        Vector2D point15 = new Vector2D(seatXPosition,
                                        halfBackCreaselineFromHipPoint - backSeatExtension + centreYpoint);
        double y16 = point15.getY() - (halfOverallDiffBkHipToBkWaistInclEase * interimBackCBShaping / backSumInterims);
        Vector2D point17 = new Vector2D(waistXPosition - sideStreamUpliftR, -(halfBackCreaselineFromHipPoint
                - (halfOverallDiffBkHipToBkWaistInclEase / 2.0 * (interimBackSideSeamShaping / backSumInterims)) + centreYpoint));

        Vector2D point16Temp = new Vector2D(point17.getX(), y16);
        Vector2D point18 = new Vector2D(seatXPosition, -((backSeatArc / 2.0 + seatEase / 4.0)
                - (halfBackCreaselineFromHipPoint - backSeatExtension) + centreYpoint));

        Vector2D point19 = new Vector2D(hipXPosition, -(((backHipArc / 2.0) + hipEase / 4.0)
                - (halfBackCreaselineFromHipPoint - backCrotchExtension) + centreYpoint));

        Vector2D point20 = new Vector2D(centreXpoint, -(((backHipArc / 2.0) + hipEase / 4.0)
                - (halfBackCreaselineFromHipPoint - backCrotchExtension) + centreYpoint)); //same as point 19
        Vector2D point21 = new Vector2D(kneeXPosition, -(((kneeCircumR + kneeEase) / 4.0) + 0.75 + centreYpoint));
        Vector2D point22 = new Vector2D(ankleXPosition, -(((ankleCircumR + kneeEase) / 4.0) + 0.75 + centreYpoint));
        Vector2D point24 = new Vector2D(ankleXPosition, (((ankleCircumR + kneeEase) / 4.0) + 0.75) + centreYpoint);
        Vector2D point25 = new Vector2D(kneeXPosition, (((kneeCircumR + kneeEase) / 4.0) + 0.75) + centreYpoint);

        // Addition of keypoints 17 and 18
        backBlock.addKeypoint(point17);
        backBlock.addKeypoint(point18);

        // Curve between 17 and 18
        backBlock.addCircularArc(point17, point18, (point18.getY()
                - (new Vector2D(point17.add(point18.subtract(point17).divide(2.0)))).getY()) / 2.0, false);

        backBlock.addKeypoint(point19);
        backBlock.addKeypoint(point20);
        backBlock.addKeypoint(point21);

        backBlock.addDirectedCubicSpline(point20, point21, new Vector2D(point20.subtract(point19)),
                                   new Vector2D(point22.subtract(point21)), new double[]{0.0, 0.0});

        backBlock.addKeypoint(point22);
        backBlock.addKeypoint(point24);

        // Circular curve with height 1 to ensure keypoint 23 is added
        backBlock.addCircularArc(point22, point24, 1.0, true);

        backBlock.addKeypoint(point25);
        backBlock.addKeypoint(point13);

        // Added a new vector 1/4 height and 1/3 length for this curve to go through
        Vector2D point25and13 = new Vector2D(point13.getX() + ((point25.getX() - point13.getX()) / 3.0),
                                             point13.getY() + ((point25.getY() - point13.getY()) * 0.75));

        backBlock.addDirectedCubicSpline(point25, point13, new Vector2D(point25.subtract(point24)),
                                   new Vector2D(point25and13.subtract(point13)), new double[]{0.0, 0.0});

        backBlock.addKeypoint(point14);
        backBlock.addKeypoint(point15);

        backBlock.addDirectedCubicSpline(point13, point14, new Vector2D(point13.subtract(point25and13)),
                                   new Vector2D(point16Temp.subtract(point15)), new double[]{90.0, 0.0});

        // Calculate length point 16 needs to be moved in the -x direction by to keep overall crotch length constant
        double crotchExtension = fullCrotchLength - (frontBlock.getLengthBetweenPoints(point10, point1)
                + backBlock.getLengthBetweenPoints(point13, point15));

        double x16 = Math.sqrt((crotchExtension * crotchExtension) - (y16 * y16));
        Vector2D point16 = new Vector2D(seatXPosition - x16, y16);

        backBlock.addKeypoint(point16);

        // Calculation of dart width
        double dartWidth = (interimBackDartWidth / backSumInterims) * halfOverallDiffBkHipToBkWaistInclEase;

        Vector2D D16D17 = new Vector2D(point17.subtract(point16));

        // Calculaton of apex point
        double lambda = (waistToSeat - 5.0) / Math.sqrt(Math.pow(((point15.getX()
                - point16.getX())), 2.0) + Math.pow(((point15.getY() - point16.getY())), 2.0));

        Vector2D Apex = new Vector2D(
                point16.add(D16D17.divide(2.0)).add(new Vector2D(point15.subtract(point16)).multiply(lambda)));

        ArrayList<Vector2D> backDarts = backBlock.addDart(point16, point17, 0.5, dartWidth, Apex, true);
        backBlock.addRightAngleCurve(point16, backDarts.get(0));
        backBlock.addRightAngleCurve(backDarts.get(2), point17);


        // Get the max and min y with a small additional buffer of 5cm for the construction lines
        double maxBackY = Collections.max(backBlock.getPlottableKeypointsY()) + 5.0;
        double minBackY = Collections.min(backBlock.getPlottableKeypointsY()) - 5;

        // All the key construction points
        backBlock.addConstructionPoint(new Vector2D(waistXPosition, minBackY), new Vector2D(waistXPosition, maxBackY),
                                       "Waist");
        backBlock.addConstructionPoint(new Vector2D(seatXPosition, minBackY), new Vector2D(seatXPosition, maxBackY),
                                       "Seat");
        backBlock.addConstructionPoint(new Vector2D(hipXPosition, minBackY), new Vector2D(hipXPosition, maxBackY),
                                       "Hip");
        backBlock.addConstructionPoint(new Vector2D(centreXpoint, minBackY), new Vector2D(centreXpoint, maxBackY),
                                       "Crotch");
        backBlock.addConstructionPoint(new Vector2D(kneeXPosition, minBackY), new Vector2D(kneeXPosition, maxBackY),
                                       "Knee");
        backBlock.addConstructionPoint(new Vector2D(ankleXPosition, minBackY), new Vector2D(ankleXPosition, maxBackY),
                                       "Ankle");

        // Construction point representing the centre fold
        backBlock.addConstructionPoint(new Vector2D(Collections.min(backBlock.getPlottableKeypointsX()) - 5.0,
                                                    centreYpoint),
                                       new Vector2D(Collections.max(backBlock.getPlottableKeypointsX()) + 5.0,
                                                    centreYpoint), "CentreFold");
    }

}