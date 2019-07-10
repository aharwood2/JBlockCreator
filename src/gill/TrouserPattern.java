package gill;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockenums.EPosition;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;
import java.util.Vector;

public class TrouserPattern extends Pattern
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

    public TrouserPattern(Measurements dataStore)
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
    protected void createBlocks()
    {
        Block frontBlock = new Block(userName + "_Gill_FrontBlock");
        blocks.add(frontBlock);

        double centreXpoint = 0;
        double centreYpoint = 0;

        double kneeXPosition = centreXpoint+crotchHeight-kneeCircumHeightR; //A43 - A42
        double ankleXPosition = centreXpoint+crotchHeight-ankleCircumHeightR; //A43 - A41
        double hipXPosition = centreXpoint-(bodyRise-waistToHipLength); //A38 - A15
        double seatXPosition = centreXpoint - (bodyRise-waistToSeat); //A38 - A34
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
        //A40 - reduction to hip depth
        double frontCrotchExtension = (hipDepth-6)*0.38;
        //cell G32
        double halfFrontCreaselineFromHipPoint = (((frontHipArc+(double)seatEase/2)/2)+frontCrotchExtension)/2;

        //point 1
        Vector2D point1 = new Vector2D(centreXpoint,-halfFrontCreaselineFromHipPoint);
        frontBlock.addKeypoint(point1);

        //point 2
        Vector2D point2 = new Vector2D(kneeXPosition,-(((kneeCircumR+kneeEase)/4)-0.75));
        frontBlock.addQuadraticBezierCurve(point1,new Vector2D(point1.getX(),point2.getY()),point2);

        frontBlock.addKeypoint(point2);

        /*
        Vector2D tangCorn12 = new Vector2D(point1.getX(),point2.getY());
        frontBlock.addDirectedCurveWithApexTangent(point1,point2,tangCorn12,5,
                new double[]{-30,0},new int []{1,-1});
         */

        //point 3
        frontBlock.addKeypoint(new Vector2D(ankleXPosition,-(((ankleCircumR+ankleEase)/4)-0.75)));

        //point 4
        frontBlock.addKeypoint(new Vector2D(ankleXPosition,((ankleCircumR+ankleEase)/4)-0.75));

        //point 5
        Vector2D point5 = new Vector2D(kneeXPosition,((kneeCircumR+kneeEase)/4)-0.75);
        frontBlock.addKeypoint(point5);

        //point 6
        Vector2D point6 = new Vector2D(centreXpoint,((frontCrotchExtension+(frontHipArc/2)+((double)hipEase/4))-halfFrontCreaselineFromHipPoint));
        frontBlock.addKeypoint(point6);

        //point 7
        Vector2D point7 = new Vector2D(hipXPosition, (frontHipArc/2+(double)hipEase/4+frontCrotchExtension)-halfFrontCreaselineFromHipPoint);
        frontBlock.addKeypoint(point7);

        //point 8
        //first calculate front seat extension
        double frontSeatExtension = (seatDepth-6)*0.38;
        Vector2D point8 = new Vector2D(seatXPosition,(frontSeatArc/2+(double)seatEase/4+frontSeatExtension)-halfFrontCreaselineFromHipPoint);


        //need to calculate front sideseam shaping first
        double interimFrontSideSeamShaping = (hipWidth-waistWidth)/2; // 5
        double interimFrontDartWidth = (frontAbdomenArc - frontWaistArc)/2; // 2.5
        double interimCBShaping = 0;
        if (frontAbdomenZ>frontWaistZ) {interimCBShaping =  frontAbdomenZ-frontWaistZ;} //0.18
        double sumInterims = interimFrontDartWidth+interimFrontSideSeamShaping+interimCBShaping; //7.7
        double halfOverallDiffFrHipToFrWaistWithEase = (((frontHipArc+((double)seatEase/2))-(frontWaistArc+((double)waistEase/2)))/2);

        //point 9
        Vector2D point9 = new Vector2D(waistXPosition-sideStreamUpliftR,halfFrontCreaselineFromHipPoint-3.3);
        frontBlock.addKeypoint(point9);

        //point 10
        frontBlock.addKeypoint(new Vector2D(waistXPosition,-(halfFrontCreaselineFromHipPoint-(frontSeatExtension+halfOverallDiffFrHipToFrWaistWithEase*interimCBShaping/sumInterims))));

        //point 11
        frontBlock.addKeypoint(new Vector2D(seatXPosition,-(halfFrontCreaselineFromHipPoint-frontSeatExtension)));

        //point 12
        frontBlock.addKeypoint(new Vector2D(hipXPosition,-(halfFrontCreaselineFromHipPoint-frontCrotchExtension)));

        //blended curve between point 5 and 6

        /*
        Block backBlock = new Block(userName + "_Gill_BackBlock");
        blocks.add(backBlock);

        //start from point 13 going anti-clockwise
        //need to calculate halfCreaselineFromHipPoint
        double backCrotchExtension = (hipDepth-6)*0.62;
        double halfBackCreaselineFromHipPoint = (((backHipArc+(double)seatEase/2)/2)+backCrotchExtension)/2;

        Vector2D point13 = new Vector2D(centreXpoint+1,halfBackCreaselineFromHipPoint);
        backBlock.addKeypoint(point13);

        Vector2D point14 = new Vector2D(hipXPosition,halfBackCreaselineFromHipPoint-backCrotchExtension);
        backBlock.addKeypoint(point14);

        Vector2D point15 = new Vector2D(seatXPosition,halfBackCreaselineFromHipPoint-(seatDepth-6)*0.62);
        backBlock.addKeypoint(point15);

        Vector2D point16 = new Vector2D(0,0); //TODO needs to be redone incorporating length of the whole crotch -> calculate length of cubic splinesssss

        Vector2D point17 = new Vector2D(waistXPosition-sideStreamUpliftR,)
        */


    }

}

