package ahmed;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockenums.EPosition;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;
import java.util.Vector;

public class BodicePattern extends Pattern
{
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
    protected EMethod assignMethod()
    {
        return EMethod.AHMED;
    }

    @Override
    protected EGarment assignGarment()
    {
        return EGarment.BODICE;
    }

    @Override
    protected void addEasement()
    {
          armholeDepthEase = 1.7;
          acrossBackEase = 0.5;
          shoulderSlopeEase = 0.3;
          frontBustArcEase = 1;
          backBustArcEase = 3;
          waistEase = 1.5;
    }


    public BodicePattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        createBlocks();
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
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
        shoulderSlope = 7.59;
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

    /**
     *
     * @param x1 x position of the center of circle 1
     * @param x2 x position of the center of circle 2
     * @param y1 y position of the center of circle 1
     * @param y2 y position of the center of circle 2
     * @param r1 length between point 1 and point of intersections
     * @param r2 length between point 2 and point of intersections
     * @return return a 2d array representing the 2 points of intersection [0][0] = x0, [0][1] = y0, [1][1] = x1, [1][1] = y1
     */
    private double[][] circleIntersect (double x1, double x2, double y1, double y2, double r1, double r2)
    {
        double [][] xy = new double[2][2];
        double A = ((r2*r2)-(r1*r1)-(x2*x2)+(x1*x1)-(y2*y2)+(y1*y1))/((-2*x2)+(2*x1));
        double D = ((-2*y2)+(2*y1))/((-2*x2)+(2*x1));
        double a = (D*D)+1;
        double b = ((-2*A*D)+(2*D*x1)+(-2*y1));
        double c = (A*A)+(x1*x1)+(-2*A*x1)+(y1*y1)-(r1*r1);
        double discriminant = (b*b)-(4*a*c);
        if (discriminant < 0) {return xy;}
        //todo circles do not intersect exception
        xy[0][1] = ((-1*b)+(Math.sqrt(discriminant)))/(2*a);
        xy[1][1] = ((-1*b)-(Math.sqrt(discriminant)))/(2*a);
        xy[0][0] = A - (D*xy[0][1]);
        xy[1][0] = A - (D*xy[1][1]);
        return xy;
    }

    @Override
    protected void createBlocks()
    {

        Block fullBlock = new Block(userName + "_Ahmed_Bodice_Block");
        blocks.add(fullBlock);
        Vector2D point1 = new Vector2D(0,-backNeckDepth);
        Vector2D point2 = new Vector2D(0,point1.getY()-((scyeDepth+armholeDepthEase)/2));
        Vector2D point3 = new Vector2D(0,point1.getY()-(scyeDepth+armholeDepthEase));
        Vector2D point4 = new Vector2D(0,point1.getY()-halfBackCentreTapeMeasure);
        Vector2D point5;
        Vector2D point6 = new Vector2D(point3.getX()+(backBustArc/2)+3,point3.getY());
        double [][] circleP;
        circleP = circleIntersect(point4.getX(),point6.getX(),point4.getY(),point6.getY(),sideSeamDepth-armholeDepthEase,(backWaistArc/2)+1.5);
        //pick the one with smaller Y for now
        if (circleP[0][1] < circleP[1][1])
        {
            point5 = new Vector2D(circleP[0][0],circleP[0][1]);
        } else
        {
            point5 = new Vector2D(circleP[1][0],circleP[1][1]);
        }

        Vector2D point7;
        Vector2D point8 = new Vector2D(point6.getX()+(frontBustArc/2)+1,point4.getY());

        circleP = circleIntersect(point6.getX(),point8.getX(),point6.getY(),point8.getY(),(sideSeamDepth-armholeDepthEase),(frontWaistArc/2)+1.5);
        if (circleP[0][1] < circleP[1][1])
        {
            point7 = new Vector2D(circleP[0][0],circleP[0][1]);
        } else
        {
            point7 = new Vector2D(circleP[1][0],circleP[1][1]);
        }

        Vector2D point10 = new Vector2D(point8.getX(),point3.getY());
        Vector2D point11 = new Vector2D(point8.getX(),point8.getY()+((sideNeckToBustToWaistR-frontNeckDepth)*0.75));
        Vector2D point12 = new Vector2D(point8.getX(),point8.getY()+(sideNeckToBustToWaistR-frontNeckDepth));
        Vector2D point13 = new Vector2D(point12.getX()-(neckWidthFrontandBack/2),point12.getY()+frontNeckDepth);
        Vector2D point9 = new Vector2D(point8.getX(),point13.getY()-Math.sqrt(Math.pow(sideNeckToBustLengthR,2)-Math.pow((bustWidth/2)-(neckWidthFrontandBack/2),2)));
        Vector2D bustPoint = new Vector2D(point9.getX()-(bustWidth)/2,point9.getY());

        Vector2D point13_14;
        double hyp = Math.sqrt((shoulderRightX*shoulderRightX)+(shoulderToWaistDepth*shoulderToWaistDepth))+shoulderSlopeEase;
        circleP = circleIntersect(point8.getX(),point13.getX(),point8.getY(),point13.getY(),hyp,shoulderLengthRight );
        point13_14 = new Vector2D(circleP[1][0],circleP[1][1]);
        if (circleP[0][0] < circleP [1][0]) {point13_14 = new Vector2D(circleP[0][0],circleP[0][1]);}

        double dx = point13_14.getX()-point13.getX();
        double dy = point13_14.getY()-point13.getY();
        double lamda = (shoulderLengthRight/2)/Math.sqrt(((dx*dx)+(dy*dy)));
        Vector2D dartPT1Bust = new Vector2D(point13.add(point13_14.subtract(point13).multiply(lamda)));

        fullBlock.addDart()

        Vector2D point16 = new Vector2D(point2.getX()+(acrossBackTapeMeasurement/2)+acrossBackEase,point2.getY());
        Vector2D point17;
        Vector2D point18 = new Vector2D(point1.getX()+(neckWidthFrontandBack/2),0);
        circleP = circleIntersect(point4.getX(),point18.getX(),point4.getY(),point18.getY(),hyp,shoulderLengthRight );
        Vector2D point17_18;
        point17_18 = new Vector2D(circleP[1][0],circleP[1][1]);
        if (circleP[0][0] > circleP [1][0]) {point17_18 = new Vector2D(circleP[0][0],circleP[0][1]);}
        dx = point17_18.getX()-point18.getX();
        dy = point17_18.getY()-point18.getY();
        lamda = (shoulderLengthRight+1)/Math.sqrt(((dx*dx)+(dy*dy)));
        point17 = new Vector2D(point18.add(point17_18.subtract(point18).multiply(lamda)));

        fullBlock.addKeypoint(point16);
        fullBlock.addKeypoint(point17);
        fullBlock.addKeypoint(point18);
        fullBlock.addKeypoint(point1);
        fullBlock.addKeypoint(point2);
        fullBlock.addKeypoint(point3);
        fullBlock.addKeypoint(point4);
        fullBlock.addKeypoint(point5);
        fullBlock.addKeypoint(point6);
        fullBlock.addKeypoint(point7);
        fullBlock.addKeypoint(point8);
        fullBlock.addKeypoint(point9);

        fullBlock.addKeypoint(point10);
        fullBlock.addKeypoint(point11);
        fullBlock.addKeypoint(point12);
        fullBlock.addKeypoint(point13);
        fullBlock.addKeypoint(dartPT1Bust);
        fullBlock.addKeypoint(point13_14);




        /*
        System.out.println(point4.getX() + "," + point4.getY());
        System.out.println(point6.getX() + "," + point6.getY());
        System.out.println(x4y6[0][0] + "," + x4y6[0][1]);
        System.out.println(x4y6[1][0] + "," + x4y6[1][1]);
         */


        /*
        attempt 1
        double point13_14y = point13.getY() - Math.sqrt(Math.pow(shoulderLengthRight,2)-Math.pow((acrossShoulderBackandFront/2)-(neckWidthFrontandBack/2),2));

        double hyp = Math.sqrt((shoulderRightX*shoulderRightX)+(shoulderToWaistDepth*shoulderToWaistDepth))+shoulderSlopeEase;

        Vector2D point13_14 = new Vector2D(point12.getX()-(acrossShoulderBackandFront/2),point13_14y);
        System.out.println(new Vector2D(point8.subtract(point13_14)).norm());
        System.out.println(hyp);
         */
    }

}
