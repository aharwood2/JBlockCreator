package gill;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockenums.EPosition;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.*;

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

    private void readjustMeasurement()
    {
        optSmallofBackWaist = 83.33;
        hipCircum = 93.89;
        waistToHipLength = 25.88;
        kneeCircumR = 35.36;
        ankleCircumR = 27.15;
        frontWaistArc = 34.38;
        backWaistArc = 32.36;
        frontAbdomenArc = 39.63;
        backSeatArc = 49.51;
        frontHipArc = 43.06;
        backHipArc = 50.78;
        waistToAbdomen = 9.9;
        waistToSeat = 21.91;
        sideStreamUpliftR = 0.58;
        bodyRise = 30.16;
        seatDepth = 22.05;
        hipDepth = 20.68;
        ankleCircumHeightR = 7.43;
        kneeCircumHeightR = 44.67;
        crotchHeight = 78.13;
        frontSeatArc = 43.14;
        seatBKZ = -17.91;
        frontWaistZ = 6.07;
        backWaistZ = -11.12;
        waistWidth = 24.43;
        hipWidth = 34.52;
        frontAbdomenZ = 6.25;
        fullCrotchLength = 75.67;
    }

    @Override
    protected void createBlocks()
    {
        readjustMeasurement();
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

        double frontSeatExtension = (seatDepth-6)*0.38;

        //point 1
        Vector2D point1 = new Vector2D(centreXpoint,-halfFrontCreaselineFromHipPoint);
        frontBlock.addKeypoint(point1);

        //point 2
        Vector2D point2 = new Vector2D(kneeXPosition,-(((kneeCircumR+kneeEase)/4)-0.75));
        frontBlock.addKeypoint(point2);

        frontBlock.addCircularCurve(point1,point2,(point2.getY()-(new Vector2D(point1.add(point2.subtract(point1).divide(2)))).getY())/2,false);

        //need to figure out why ~5 gives best curve
        /*
        frontBlock.addKeypoint(point2);
        frontBlock.addDirectedCurveWithApexTangent(point1,point2,new Vector2D(point1.getX(),point2.getY()),4.6,
                new double[]{-90,0},new int []{1,-1});

         */

        /*
        frontBlock.addQuadraticBezierCurve(point1,new Vector2D(point1.getX(),point2.getY()),point2);
        frontBlock.addKeypoint(point2);
        &/
         */



        //point 3
        Vector2D point3 = new Vector2D(new Vector2D(ankleXPosition,-(((ankleCircumR+ankleEase)/4)-0.75)));
        frontBlock.addKeypoint(point3);

        //point 4
        Vector2D point4 = new Vector2D(ankleXPosition,((ankleCircumR+ankleEase)/4)-0.75);
        frontBlock.addKeypoint(point4);

        //point 5
        Vector2D point5 = new Vector2D(kneeXPosition,((kneeCircumR+kneeEase)/4)-0.75);
        frontBlock.addKeypoint(point5);

        //point 6
        Vector2D point6 = new Vector2D(centreXpoint,((frontCrotchExtension+(frontHipArc/2)+((double)hipEase/4))-halfFrontCreaselineFromHipPoint));
        frontBlock.addKeypoint(point6);

        //point 7
        Vector2D point7 = new Vector2D(hipXPosition, ((frontHipArc/2+(double)hipEase/4+frontCrotchExtension)-halfFrontCreaselineFromHipPoint));
        frontBlock.addKeypoint(point7);

        frontBlock.addDirectedCurve(point5,point6,new Vector2D(point5.subtract(point4)),new Vector2D(point7.subtract(point6)),new double[]{0,0});

        //point 8
        //first calculate front seat extension


        Vector2D point8 = new Vector2D(seatXPosition,(frontSeatArc/2+(double)seatEase/4+frontSeatExtension)-halfFrontCreaselineFromHipPoint);
        frontBlock.addKeypoint(point8);
        //Curve that joins point 6 to point 8 keeping the angles the same at start




        //need to calculate front sideseam shaping first
        double interimFrontSideSeamShaping = (hipWidth-waistWidth)/2; // 5
        double interimFrontDartWidth = (frontAbdomenArc - frontWaistArc)/2; // 2.5
        double interimCBShaping = 0;
        if (frontAbdomenZ>frontWaistZ) {interimCBShaping =  frontAbdomenZ-frontWaistZ;} //0.18
        double sumInterims = interimFrontDartWidth+interimFrontSideSeamShaping+interimCBShaping; //7.7
        double halfOverallDiffFrHipToFrWaistWithEase = (((frontHipArc+((double)seatEase/2))-(frontWaistArc+((double)waistEase/2)))/2);

        //point 9
        Vector2D point9 = new Vector2D(waistXPosition-sideStreamUpliftR,halfFrontCreaselineFromHipPoint-(halfOverallDiffFrHipToFrWaistWithEase*(interimFrontSideSeamShaping/sumInterims)));
        frontBlock.addKeypoint(point9);

        frontBlock.addCircularCurve(point7,point8,(point8.getY()-(new Vector2D(point7.add(point8.subtract(point7).divide(2)))).getY())/2,true);



        //point 10
        Vector2D point10 = new Vector2D(new Vector2D(waistXPosition,-(halfFrontCreaselineFromHipPoint-
                (frontSeatExtension+halfOverallDiffFrHipToFrWaistWithEase*interimCBShaping/sumInterims))));
        frontBlock.addKeypoint(point10);

        //dart between 9 and 10
        //solved a vector line of equation for lambda when y = 0 to calculate position
        //DART TEST 1 apex point is not directly center needs a tiny bit more work
        //frontBlock.addDart(point9,point10,-point9.getY()/new Vector2D(point10.subtract(point9)).getY(),
          //      (interimFrontDartWidth/sumInterims)*halfOverallDiffFrHipToFrWaistWithEase,waistToAbdomen-1.5,true,false);
        //Dart test 2 gives a better apex position
        Vector2D frontDartApexPos = new Vector2D((new Vector2D(point9.add(point10.subtract(point9).divide(2)))).getX()+waistToAbdomen-1.5,centreYpoint);
        frontBlock.addDart(point9,point10,-point9.getY()/new Vector2D(point10.subtract(point9)).getY(),(interimFrontDartWidth/sumInterims)*halfOverallDiffFrHipToFrWaistWithEase,frontDartApexPos,false);

        //curve between 8 and 9 with attempt to keep angle between point 9 and incoming curve greater than 90
        //   frontBlock.addDirectedCurve(point8,point9,new Vector2D(point8.subtract(point7)),new Vector2D(point10.subtract(point9)),new double[]{0,100});
        //attempt with Circular curve -> may look better
        frontBlock.addCircularCurve(point8,point9,(point9.getY()-(new Vector2D(point8.add(point9.subtract(point8).divide(2)))).getY())/2,false);


        //point 11
        Vector2D point11 = new Vector2D(seatXPosition,-(halfFrontCreaselineFromHipPoint-frontSeatExtension));
        frontBlock.addKeypoint(point11);

        //point 12
        Vector2D point12 = new Vector2D(hipXPosition,-(halfFrontCreaselineFromHipPoint-frontCrotchExtension));

        //final steep curve that connects to crotch point
        PolyCoeffs crotchCurve = new PolyCoeffs(new VectorND(4,new double[]{0,0,0,0}));
        frontBlock.addDirectedCurve(point11,point1,point12,frontBlock.getDirectionAtKeypoint(point11, EPosition.BEFORE),0,crotchCurve);



        Block backBlock = new Block(userName + "_Gill_BackBlock");
        blocks.add(backBlock);

        //start from point 13 going anti-clockwise
        //need to calculate halfCreaselineFromHipPoint
        double backCrotchExtension = (hipDepth-6)*0.62;
        //Height from origin to point 13
        //(A32 + Ease/2)/2+ crotch extension
        double halfBackCreaselineFromHipPoint = (((backHipArc+(double)seatEase/2)/2)+backCrotchExtension)/2;

        //back shaping
        double halfOverallDiffBkHipToBkWaistInclEase = (backHipArc+(double)hipEase/2)-(backWaistArc+(double)waistEase/2);
        //interim values
        double interimBackSideSeamShaping = (hipWidth-waistWidth)/2;
        double interimBackDartWidth = (backSeatArc-backWaistArc)/2;
        double interimBackCBShaping = Math.abs(seatBKZ-backWaistZ);
        double backSumInterms = interimBackCBShaping+interimBackDartWidth+interimBackSideSeamShaping;
        //calculation for Y position of point 18
        //get height of position 15 and subtract it from width of points 18 and 15
        double backSeatExtension = (seatDepth - 6)*0.62;//calculation of CELL G25

        double lengthPoint10Point11 = (new Vector2D(point11.subtract(point10))).norm();



        Vector2D point13 = new Vector2D(centreXpoint+1,halfBackCreaselineFromHipPoint);
        Vector2D point14 = new Vector2D(hipXPosition,halfBackCreaselineFromHipPoint-backCrotchExtension);
        Vector2D point15 = new Vector2D(seatXPosition,halfBackCreaselineFromHipPoint-backSeatExtension);
        Vector2D point16 = new Vector2D(0,0); //TODO needs to be redone incorporating length of the whole crotch -> calculate length of cubic splines
        Vector2D point17 = new Vector2D(waistXPosition-sideStreamUpliftR,-(halfBackCreaselineFromHipPoint-(halfOverallDiffBkHipToBkWaistInclEase/2*(interimBackSideSeamShaping/backSumInterms))));
        Vector2D point18 = new Vector2D(seatXPosition,-((backSeatArc/2+(double)seatEase/4)-(halfBackCreaselineFromHipPoint-backSeatExtension))); //y:A30/2 + SeatEase/4 ) - (crotch height - CELL g25)
        Vector2D point19 = new Vector2D(hipXPosition,-(((backHipArc/2)+(double)hipEase/4)-(halfBackCreaselineFromHipPoint-backCrotchExtension)));
        Vector2D point20 = new Vector2D(centreXpoint,point19.getY());//for now same Y pos as point 10
        Vector2D point21 = new Vector2D(kneeXPosition,0);

        backBlock.addKeypoint(point13);


        backBlock.addKeypoint(point14);


        backBlock.addKeypoint(point15);



    }

}

