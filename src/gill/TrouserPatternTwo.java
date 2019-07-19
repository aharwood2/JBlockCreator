package gill;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;
//todo: need to give it its own UI
public class TrouserPatternTwo extends Pattern
{

    private  double optSmallofBackWaist;        // Measurement [A02]
    private  double hipCircum;                  // Measurement [A03]
    private  double waistToHipLength;           // Measurement [A15]
    private  double kneeCircumR;                // Measurement [A18]
    private  double ankleCircumR;               // Measurement [A19]
    private  double frontWaistArc;              // Measurement [A26]
    private  double backWaistArc;               // Measurement [A27]
    private  double frontAbdomenArc;            // Measurement [A28]
    private  double backSeatArc;                // Measurement [A30]
    private  double frontHipArc;               // Measurement [A31]
    private  double backHipArc;                 // Measurement [A32]
    private  double waistToAbdomen;             // Measurement [A33]
    private  double waistToSeat;                // Measurement [A34]
    private  double sideStreamUpliftR;          // Measurement [A37]
    private  double bodyRise;                   // Measurement [A38]
    private  double seatDepth;                  // Measurement [A39]
    private  double hipDepth;                   // Measurement [A40]
    private  double ankleCircumHeightR;         // Measurement [A41]
    private  double kneeCircumHeightR;          // Measurement [A42]
    private  double crotchHeight;               // Measurement [A43]
    private  double frontSeatArc;               // Measurement [A46]
    private  double seatBKZ;                    // Measurement [A51]
    private  double frontWaistZ;                // Measurement [A52]
    private  double backWaistZ;                 // Measurement [A53]
    private  double waistWidth;                 // Measurement [A59]
    private  double hipWidth;                   // Measurement [A60]
    private  double frontAbdomenZ;              // Measurement [A61]
    private  double fullCrotchLength;           // Measurement [A62]



    /* Implement abstract methods from super class */
    @Override
    protected EMethod assignMethod()
    {
        return EMethod.GILL;
    }

    @Override
    protected EGarment assignGarment()
    {
        return EGarment.TROUSER;
    }

    @Override
    protected void addEasement()
    {

    }

    public TrouserPatternTwo(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Create the blocks
        createBlocks();
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {
            // Based on measurements for this pattern we can read the following from the scan
            optSmallofBackWaist = dataStore.getMeasurement("A02").value;
            hipCircum = dataStore.getMeasurement("A03").value;
            waistToHipLength = dataStore.getMeasurement("A15").value;
            kneeCircumR = dataStore.getMeasurement("A18").value;
            ankleCircumR = dataStore.getMeasurement("A19").value;
            frontWaistArc = dataStore.getMeasurement("A26").value;
            backWaistArc = dataStore.getMeasurement("A27").value;
            frontAbdomenArc = dataStore.getMeasurement("A28").value;
            backSeatArc = dataStore.getMeasurement("A30").value;
            frontHipArc = dataStore.getMeasurement("A31").value;
            backHipArc = dataStore.getMeasurement("A32").value;
            waistToAbdomen = dataStore.getMeasurement("A33").value;
            waistToSeat = dataStore.getMeasurement("A34").value;
            sideStreamUpliftR = dataStore.getMeasurement("A37").value;
            bodyRise = dataStore.getMeasurement("A38").value;
            seatDepth = dataStore.getMeasurement("A39").value;
            hipDepth = dataStore.getMeasurement("A40").value;
            ankleCircumHeightR = dataStore.getMeasurement("A41").value;
            kneeCircumHeightR = dataStore.getMeasurement("A42").value;
            crotchHeight = dataStore.getMeasurement("A43").value;
            frontSeatArc = dataStore.getMeasurement("A46").value;
            seatBKZ = dataStore.getMeasurement("A51").value;
            frontWaistZ = dataStore.getMeasurement("A52").value;
            backWaistZ = dataStore.getMeasurement("A53").value;
            waistWidth = dataStore.getMeasurement("A59").value;
            hipWidth = dataStore.getMeasurement("A60").value;
            frontAbdomenZ = dataStore.getMeasurement("A61").value;
            fullCrotchLength = dataStore.getMeasurement("A62").value;
            // Get name
            userName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            addMissingMeasurement(dataStore.getName(), e.getMeasurementId());
            return false;
        }
    }


    @Override
    protected void createBlocks() {
        Block frontBlock = new Block(userName + "_Gill_FrontBlock");
        blocks.add(frontBlock);

        double centreXpoint = 0;
        double centreYpoint = 0;

        double kneeXPosition = centreXpoint + crotchHeight - kneeCircumHeightR; //A43 - A42
        double ankleXPosition = centreXpoint + crotchHeight - ankleCircumHeightR; //A43 - A41
        double hipXPosition = centreXpoint - (bodyRise - waistToHipLength); //A38 - A15
        double seatXPosition = centreXpoint - (bodyRise - waistToSeat); //A38 - A34
        double waistXPosition = centreXpoint - bodyRise; //A38

        //ease values
        int waistEase = 2;
        int seatEase = 6;
        int hipEase = 6;
        int kneeEase = 15;
        int ankleEase = 22;


        //starting from origin, add internal points as vectors not key points because nothing is actually attached
        //to them

        //starting from the centre point, directly south of crotch point, going in an anti-clockwise manner
        //calculation for front crotch extension
        double frontCrotchExtension = (hipDepth - 6) * 0.38;
        //cell G32
        double halfFrontCreaselineFromHipPoint = (((frontHipArc + (double) seatEase / 2) / 2) + frontCrotchExtension) / 2;

        double frontSeatExtension = (seatDepth - 6) * 0.38;

        //calculations for shaping
        double interimFrontSideSeamShaping = (hipWidth - waistWidth) / 2; // 5
        double interimFrontDartWidth = (frontAbdomenArc - frontWaistArc) / 2; // 2.5
        double interimCBShaping = 0;
        if (frontAbdomenZ > frontWaistZ) {
            interimCBShaping = frontAbdomenZ - frontWaistZ;
        } //0.18
        double sumInterims = interimFrontDartWidth + interimFrontSideSeamShaping + interimCBShaping; //7.7
        double halfOverallDiffFrHipToFrWaistWithEase = (((frontHipArc + ((double) seatEase / 2)) - (frontWaistArc + ((double) waistEase / 2))) / 2);

        //all the main keypoints added as vectors
        Vector2D point1 = new Vector2D(centreXpoint, -halfFrontCreaselineFromHipPoint+centreYpoint);
        Vector2D point2 = new Vector2D(kneeXPosition, -(((kneeCircumR + kneeEase) / 4) - 0.75)+centreYpoint);
        Vector2D point3 = new Vector2D(ankleXPosition, -(((ankleCircumR + ankleEase) / 4) - 0.75)+centreYpoint);
        Vector2D point4 = new Vector2D(ankleXPosition, ((ankleCircumR + ankleEase) / 4) - 0.75+centreYpoint);
        Vector2D point5 = new Vector2D(kneeXPosition, ((kneeCircumR + kneeEase) / 4) - 0.75+centreYpoint);
        Vector2D point6 = new Vector2D(centreXpoint, ((frontCrotchExtension + (frontHipArc / 2) + ((double) hipEase / 4)) - halfFrontCreaselineFromHipPoint)+centreYpoint);
        Vector2D point7 = new Vector2D(hipXPosition, ((frontHipArc / 2 + (double) hipEase / 4 + frontCrotchExtension) - halfFrontCreaselineFromHipPoint)+centreYpoint);
        Vector2D point8 = new Vector2D(seatXPosition, (frontSeatArc / 2 + (double) seatEase / 4 + frontSeatExtension) - halfFrontCreaselineFromHipPoint+centreYpoint);
        Vector2D point9 = new Vector2D(waistXPosition - sideStreamUpliftR, halfFrontCreaselineFromHipPoint - (halfOverallDiffFrHipToFrWaistWithEase * (interimFrontSideSeamShaping / sumInterims))+centreYpoint);
        Vector2D point10 = new Vector2D(waistXPosition, -(halfFrontCreaselineFromHipPoint -
                (frontSeatExtension + halfOverallDiffFrHipToFrWaistWithEase * interimCBShaping / sumInterims))+centreYpoint);
        Vector2D point11 = new Vector2D(seatXPosition, -(halfFrontCreaselineFromHipPoint - frontSeatExtension));
        Vector2D point12 = new Vector2D(hipXPosition, -(halfFrontCreaselineFromHipPoint - frontCrotchExtension));

        //point 1 and 2
        frontBlock.addKeypoint(point1);
        frontBlock.addKeypoint(point2);

        //calculation of a point for the curve to go through which is 1/3rd the width 1/4 the heigh between them
        Vector2D point1and2 = new Vector2D((point1.getX() - point2.getX()) / 3, point2.getY() - ((point2.getY() - point1.getY()) * 0.66)+centreYpoint);
        frontBlock.addDirectedCurve(point1,point2, new Vector2D(point1.subtract(point2)),new Vector2D(point2.subtract(point1and2)),new double[]{0,0});

        //point 3,4,5,6,7
        frontBlock.addKeypoint(point3);
        frontBlock.addKeypoint(point4);
        frontBlock.addKeypoint(point5);
        frontBlock.addKeypoint(point6);
        frontBlock.addKeypoint(point7);

        //curve between point 5 and 6 keeping angles at start and end 0 deg with respect to preceding and proceeding curves
        frontBlock.addDirectedCurve(point5, point6, new Vector2D(point5.subtract(point4)), new Vector2D(point7.subtract(point6)), new double[]{0, 0});

        //point 8 and 9
        frontBlock.addKeypoint(point8);
        frontBlock.addKeypoint(point9);

        //circular curve between 8 and 9 making sure apex is lower than height of point 8
        frontBlock.addCircularCurve(point8, point9, (point9.getY() - (new Vector2D(point8.add(point9.subtract(point8).divide(2)))).getY()) / 2, false);

        //curve that connect point 7 and 8 together as a circular curve
        frontBlock.addCircularCurve(point7, point8, (point8.getY() - (new Vector2D(point7.add(point8.subtract(point7).divide(2)))).getY()) / 2, true);

        //point 10
        frontBlock.addKeypoint(point10);

        //dart between 9 and 10
        //solved a vector line of equation for lambda when y = 0 to calculate position
        Vector2D frontDartApexPos = new Vector2D((new Vector2D(point9.add(point10.subtract(point9).divide(2)))).getX() + waistToAbdomen - 1.5, centreYpoint);
        ArrayList<Vector2D> frontDarts = frontBlock.addDart(point9, point10, -point9.getY() / new Vector2D(point10.subtract(point9)).getY(), (interimFrontDartWidth / sumInterims) * halfOverallDiffFrHipToFrWaistWithEase, frontDartApexPos, false);

        Vector2D frontDartStart = frontDarts.get(0);
        Vector2D frontDartEnd = frontDarts.get(2);

        //adding curves between points and dart to make sure angles are at 90 deg
        frontBlock.addRightAngleCurve(point9, frontDartStart);
        frontBlock.addRightAngleCurve(frontDartEnd, point10);

        //point 11
        frontBlock.addKeypoint(point11);

        //final steep curve that connects to crotch point point 12 not added as a keypoint but through curve function
        frontBlock.addDirectedCurve(point11, point1, point12, 0);


        Block backBlock = new Block(userName + "_Gill_BackBlock");
        blocks.add(backBlock);

        //start from point 13 going anti-clockwise
        //need to calculate halfCreaselineFromHipPoint
        double backCrotchExtension = (hipDepth - 6) * 0.62;
        //Height from origin to point 13
        //(A32 + Ease/2)/2+ crotch extension
        double halfBackCreaselineFromHipPoint = (((backHipArc + (double) seatEase / 2) / 2) + backCrotchExtension) / 2;

        //back shaping
        double halfOverallDiffBkHipToBkWaistInclEase = (backHipArc + (double) hipEase / 2) - (backWaistArc + (double) waistEase / 2);
        //interim values
        double interimBackSideSeamShaping = (hipWidth - waistWidth) / 2;
        double interimBackDartWidth = (backSeatArc - backWaistArc) / 2;
        double interimBackCBShaping = Math.abs(seatBKZ - backWaistZ);
        double backSumInterims = interimBackCBShaping + interimBackDartWidth + interimBackSideSeamShaping;
        double backSeatExtension = (seatDepth - 6) * 0.62;//calculation of CELL G25

        //all keypoints except point 16 added as vectors
        Vector2D point13 = new Vector2D(centreXpoint + 1, halfBackCreaselineFromHipPoint);
        Vector2D point14 = new Vector2D(hipXPosition, halfBackCreaselineFromHipPoint - backCrotchExtension);
        Vector2D point15 = new Vector2D(seatXPosition, halfBackCreaselineFromHipPoint - backSeatExtension);
        double y16 = halfBackCreaselineFromHipPoint - (backSeatExtension + (halfOverallDiffBkHipToBkWaistInclEase * interimBackCBShaping / backSumInterims));
        Vector2D point17 = new Vector2D(waistXPosition - sideStreamUpliftR, -(halfBackCreaselineFromHipPoint - (halfOverallDiffBkHipToBkWaistInclEase / 2 * (interimBackSideSeamShaping / backSumInterims))));
        Vector2D point18 = new Vector2D(seatXPosition, -((backSeatArc / 2 + (double) seatEase / 4) - (halfBackCreaselineFromHipPoint - backSeatExtension))); //y:A30/2 + SeatEase/4 ) - (crotch height - CELL g25)
        Vector2D point19 = new Vector2D(hipXPosition, -(((backHipArc / 2) + (double) hipEase / 4) - (halfBackCreaselineFromHipPoint - backCrotchExtension)));
        Vector2D point20 = new Vector2D(centreXpoint, -(((backHipArc / 2) + (double) hipEase / 4) - (halfBackCreaselineFromHipPoint - backCrotchExtension))); //same as point 19
        Vector2D point21 = new Vector2D(kneeXPosition, -(((kneeCircumR + kneeEase) / 4) + 0.75));
        Vector2D point22 = new Vector2D(ankleXPosition, -(((ankleCircumR + kneeEase) / 4) + 0.75));
        Vector2D point24 = new Vector2D(ankleXPosition, (((ankleCircumR + kneeEase) / 4) + 0.75));
        Vector2D point25 = new Vector2D(kneeXPosition, (((kneeCircumR + kneeEase) / 4) + 0.75));

        //addition of keypoints 17 and 18
        backBlock.addKeypoint(point17);
        backBlock.addKeypoint(point18);

        //curve between 17 and 18
        backBlock.addCircularCurve(point17, point18, (point18.getY() - (new Vector2D(point17.add(point18.subtract(point17).divide(2)))).getY()) / 2, false);

        backBlock.addKeypoint(point19);
        backBlock.addKeypoint(point20);
        backBlock.addKeypoint(point21);

        backBlock.addDirectedCurve(point20, point21, new Vector2D(point20.subtract(point19)), new Vector2D(point22.subtract(point21)), new double[]{0, 0});

        backBlock.addKeypoint(point22);
        backBlock.addKeypoint(point24);

        //circular curve with height 1 to ensure keypoint 23 is added
        backBlock.addCircularCurve(point22, point24, 1, true);

        backBlock.addKeypoint(point25);
        backBlock.addKeypoint(point13);

        //added a new vector 1/4 height and 1/3 length for this curve to go through
        Vector2D point25and13 = new Vector2D((point25.getX() - point13.getX()) / 3, point13.getY() - ((point13.getY() - point25.getY()) * 0.66));
        backBlock.addDirectedCurve(point25,point13, new Vector2D(point25.subtract(point24)),new Vector2D(point13.subtract(point25and13)),new double[]{0,0});

        backBlock.addKeypoint(point14);
        backBlock.addKeypoint(point15);

        backBlock.addDirectedCurve(point13,point14,new double[] {90,0});

        //calculate length point 16 needs to be moved in the -x direction by to keep overall crotch length constant
        double crotchExtension = fullCrotchLength - (frontBlock.getLengthBetweenPoints(point10, point1) + backBlock.getLengthBetweenPoints(point13, point15));
        double x16 = Math.sqrt((crotchExtension * crotchExtension) - (y16 * y16));
        Vector2D point16 = new Vector2D(seatXPosition - x16, y16);

        backBlock.addKeypoint(point16);

        //calculation of dart width
        double dartWidth = (interimBackDartWidth / backSumInterims) * halfOverallDiffBkHipToBkWaistInclEase;

        Vector2D D16D17 = new Vector2D(point17.subtract(point16));

        //calculaton of apex point
        double lambda = (waistToSeat - 5) / Math.sqrt (Math.pow(((point15.getX() - point16.getX())), 2) + Math.pow(((point15.getY() - point16.getY())), 2));
        System.out.println(lambda);
        Vector2D Apex = new Vector2D(point16.add(D16D17.divide(2)).add(new Vector2D(point15.subtract(point16)).multiply(lambda)));

        ArrayList<Vector2D> backDarts = backBlock.addDart(point16,point17,0.5,dartWidth,Apex,true);
        backBlock.addRightAngleCurve(point16, backDarts.get(0));
        backBlock.addRightAngleCurve(backDarts.get(2), point17);

    }

}

