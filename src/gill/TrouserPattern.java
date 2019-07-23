package gill;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class TrouserPattern
        extends Pattern
{
    /* Pattern-specific Measurements */
    private double a_WaistToHip;            // Measurement [015]
    private double b_ThighCircR;            // Measurement [017]
    private double c_KneeCircR;             // Measurement [018]
    private double d_AnkleCircleR;          // Measurement [019]
    private double e_FrWaistArc;            // Measurement [026]
    private double f_BkWaistArc;            // Measurement [027]
    private double g_BkSeatArc;             // Measurement [030]
    private double h_FrHipArc;              // Measurement [031]
    private double i_BkHipArc;              // Measurement [032]
    private double j_WaistToSeat;           // Measurement [034]
    private double k_BodyRise;              // Measurement [038]
    private double l_AnkleCRHeight;         // Measurement [041]
    private double m_KneeCRHeight;          // Measurement [042]
    private double n_CrotchHeight;          // Measurement [043]
    private double o_HipCHeight;            // Measurement [044]
    private double p_SeatCHeight;           // Measurement [045]
    private double q_FrSeatArc;             // Measurement [046]
    private double r_CrotchZ;               // Measurement [047]
    private double s_HipFrZ;                // Measurement [048]
    private double t_HipBkZ;                // Measurement [049]
    private double u_SeatFrZ;               // Measurement [050]
    private double v_SeatBkZ;               // Measurement [051]
    private double w_WaistFrZ;              // Measurement [052]
    private double x_WaistBkZ;              // Measurement [053]
    private double y_FrCrotchLength;        // Measurement [054]
    private double z_BkCrotchLength;        // Measurement [055]

    /* Arbitrary Measurements */

    // Arb measurement for 2 inches in centimetres
    private double Arb_TwoInches;

    // Arb measurement for the crotch
    private double Arb_CrotchPointFive;

    // Arb measurement for back hem drop
    private double Arb_BkHemDrop;

    // Arb measurement for the crotch reduction
    private double Arb_CrotchReduction;

    // Arb measurements for the back dart
    private double Arb_BackDartWidth;
    private double Arb_BackDartLength;

    // Arb measurements for the back dart
    private double Arb_FrontDartWidth;
    private double Arb_FrontDartLength;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public TrouserPattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Populate arbitrary measurements
        Arb_TwoInches = 5.08;
        Arb_CrotchPointFive = 0.5;
        Arb_BkHemDrop = 1.0;
        Arb_CrotchReduction = 5.0;

        // Create the blocks
        createBlocks();
    }

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
        // No easement needed
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {
            // Based on measurements for this pattern we can read the following from the scan
            a_WaistToHip = dataStore.getMeasurement("A15").value;            // Measurement [015]
            b_ThighCircR = dataStore.getMeasurement("A17").value;            // Measurement [017]
            c_KneeCircR = dataStore.getMeasurement("A18").value;             // Measurement [018]
            d_AnkleCircleR = dataStore.getMeasurement("A19").value;          // Measurement [019]
            e_FrWaistArc = dataStore.getMeasurement("A26").value;            // Measurement [026]
            f_BkWaistArc = dataStore.getMeasurement("A27").value;            // Measurement [027]
            g_BkSeatArc = dataStore.getMeasurement("A30").value;             // Measurement [030]
            h_FrHipArc = dataStore.getMeasurement("A31").value;              // Measurement [031]
            i_BkHipArc = dataStore.getMeasurement("A32").value;              // Measurement [032]
            j_WaistToSeat = dataStore.getMeasurement("A34").value;           // Measurement [034]
            k_BodyRise = dataStore.getMeasurement("A38").value;              // Measurement [038]
            l_AnkleCRHeight = dataStore.getMeasurement("A41").value;         // Measurement [041]
            m_KneeCRHeight = dataStore.getMeasurement("A42").value;          // Measurement [042]
            n_CrotchHeight = dataStore.getMeasurement("A43").value;          // Measurement [043]
            o_HipCHeight = dataStore.getMeasurement("A44").value;            // Measurement [044]
            p_SeatCHeight = dataStore.getMeasurement("A45").value;           // Measurement [045]
            q_FrSeatArc = dataStore.getMeasurement("A46").value;             // Measurement [046]
            r_CrotchZ = dataStore.getMeasurement("A47").value;               // Measurement [047]
            s_HipFrZ = dataStore.getMeasurement("A48").value;                // Measurement [048]
            t_HipBkZ = dataStore.getMeasurement("A49").value;                // Measurement [049]
            u_SeatFrZ = dataStore.getMeasurement("A50").value;               // Measurement [050]
            v_SeatBkZ = dataStore.getMeasurement("A51").value;               // Measurement [051]
            w_WaistFrZ = dataStore.getMeasurement("A52").value;              // Measurement [052]
            x_WaistBkZ = dataStore.getMeasurement("A53").value;              // Measurement [053]
            y_FrCrotchLength = dataStore.getMeasurement("A54").value;        // Measurement [054]
            z_BkCrotchLength = dataStore.getMeasurement("A55").value;        // Measurement [055]

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

    /**
     * The actual block creation process following the drafting method of Gill.
     */
    @Override
    protected void createBlocks()
    {
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The crotch point is the origin

        // Predefining Step 9 Y value as it is used as the basis for multiple other steps
        // Has an if statement depending on the sign of measurement [047]
        if (r_CrotchZ>0)
        {
            s_HipFrZ -= r_CrotchZ;
            w_WaistFrZ -= r_CrotchZ;
            u_SeatFrZ -= r_CrotchZ;

            t_HipBkZ -= r_CrotchZ;
            x_WaistBkZ -= r_CrotchZ;
            v_SeatBkZ -= r_CrotchZ;
        }
        if (r_CrotchZ<0) {
            s_HipFrZ -= r_CrotchZ;
            w_WaistFrZ -= r_CrotchZ;
            u_SeatFrZ -= r_CrotchZ;

            t_HipBkZ -= r_CrotchZ;
            x_WaistBkZ -= r_CrotchZ;
            v_SeatBkZ -= r_CrotchZ;
        }

        // Create component representing half back of skirt folded in half.
        Block fullBlock = new Block(userName + "_Gill_Trouser_Block");
        blocks.add(fullBlock);

        double centreXPosition = 0;
        double centreYPosition = 0;

        double crotchXPosition = centreXPosition;
        double kneeXPosition = centreXPosition + n_CrotchHeight-m_KneeCRHeight;
        double ankleXPosition = n_CrotchHeight- l_AnkleCRHeight;
        double hipXPosition = -(o_HipCHeight-n_CrotchHeight);
        double seatXPosition = -(p_SeatCHeight-n_CrotchHeight);
        double waistXPosition = -k_BodyRise;

        double halfWaistSuppression = ((h_FrHipArc+i_BkHipArc)/2)-((e_FrWaistArc+f_BkWaistArc)/2);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Back half points, curves and dart */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //pre-calculation of point 7 y coordinate - used in a lot of calculations
        double y7 = (i_BkHipArc/2)-t_HipBkZ-2.5+2;

        Vector2D point1 = new Vector2D(crotchXPosition+0.5, centreYPosition);
        Vector2D point2 = new Vector2D(kneeXPosition, y7/2-(((c_KneeCircR/2)+7+2)/2));
        Vector2D point3 = new Vector2D(ankleXPosition,y7/2-(((d_AnkleCircleR/2)+10+2)/2));
        Vector2D point5 = new Vector2D(ankleXPosition,y7/2+(((d_AnkleCircleR/2)+10+2)/2));
        Vector2D point6 = new Vector2D(kneeXPosition, y7/2+(((c_KneeCircR/2)+7+2)/2));
        Vector2D point7 = new Vector2D(crotchXPosition,y7);
        Vector2D point8 = new Vector2D(hipXPosition,(i_BkHipArc/2)-t_HipBkZ-2.5+2);
        Vector2D point9 = new Vector2D(seatXPosition,((-v_SeatBkZ)-2.5)+(g_BkSeatArc/2)+2);
        Vector2D point10 = new Vector2D(waistXPosition,((-v_SeatBkZ)-2.5+(-x_WaistBkZ)-2.5)+(0.32*halfWaistSuppression)+(f_BkWaistArc/2)+0.35);
        //todo: need to move point 11 in -x direction based on back crotch length
        Vector2D point11 = new Vector2D(waistXPosition+4,(-v_SeatBkZ-2.5+-x_WaistBkZ-2.5));
        Vector2D point12 = new Vector2D(seatXPosition,-v_SeatBkZ-2.5);
        Vector2D point13 = new Vector2D(hipXPosition,(-t_HipBkZ-2.5));

        //frontBlock key vectors
        Vector2D point15 = new Vector2D(crotchXPosition,centreYPosition);
        Vector2D point16 = new Vector2D(hipXPosition,-(s_HipFrZ-2.5));
        Vector2D point17 = new Vector2D(seatXPosition,-(u_SeatFrZ-2.5));
        Vector2D point18 = new Vector2D(waistXPosition,-(w_WaistFrZ-2.5));
        Vector2D point19 = new Vector2D(waistXPosition,point18.getY()-((e_FrWaistArc/2)+0.35+(0.18*halfWaistSuppression)));
        Vector2D point20 = new Vector2D(seatXPosition,point17.getY()-((q_FrSeatArc/2)+1));
        Vector2D point21 = new Vector2D(hipXPosition,point16.getY()-((h_FrHipArc/2)+1));
        Vector2D point22 = new Vector2D(centreXPosition,-(s_HipFrZ-2.5+(h_FrHipArc/2)+1));
        Vector2D point23 = new Vector2D(kneeXPosition,(point22.getY()/2)-(((c_KneeCircR/2)+7-2)/2));
        Vector2D point24 = new Vector2D(ankleXPosition,(point22.getY()/2)-(((d_AnkleCircleR/2)+10-2)/2));
        Vector2D point25 = new Vector2D(ankleXPosition,(point22.getY()/2)+(((d_AnkleCircleR/2)+10-2)/2));
        Vector2D point26 = new Vector2D(kneeXPosition,(point22.getY()/2)+(((c_KneeCircR/2)+7-2)/2));

        fullBlock.addKeypoint(point1);
        fullBlock.addKeypoint(point2);
        fullBlock.addKeypoint(point3);
        fullBlock.addKeypoint(point5);
        fullBlock.addKeypoint(point6);
        fullBlock.addKeypoint(point7);
        fullBlock.addKeypoint(point8);
        fullBlock.addKeypoint(point9);
        fullBlock.addKeypoint(point10);
        fullBlock.addKeypoint(point11);
        fullBlock.addKeypoint(point12);
        fullBlock.addKeypoint(point13);

        fullBlock.addKeypoint(point15);
        fullBlock.addKeypoint(point16);
        fullBlock.addKeypoint(point17);
        fullBlock.addKeypoint(point18);
        fullBlock.addKeypoint(point19);
        fullBlock.addKeypoint(point20);
        fullBlock.addKeypoint(point21);
        fullBlock.addKeypoint(point22);
        fullBlock.addKeypoint(point23);
        fullBlock.addKeypoint(point24);
        fullBlock.addKeypoint(point25);
        fullBlock.addKeypoint(point26);

        Vector2D point1and2 = new Vector2D((point1.getX() + (point2.getX() - point1.getX())) / 3, point1.getY() + ((point2.getY() - point1.getY()) * 0.75));
        fullBlock.addDirectedCurve(point1,point2, new Vector2D(point1and2.subtract(point1)),new Vector2D(point3.subtract(point2)),new double[]{0,0});

        fullBlock.addCircularCurve(point3,point5,1,true);

        fullBlock.addDirectedCurve(point6,point7,new Vector2D(point6.subtract(point5)),new Vector2D(point8.subtract(point7)),new double[]{0,0});

        fullBlock.addCircularCurve(point9,point10,(point10.getY() - (new Vector2D(point9.add(point10.subtract(point9).divide(2)))).getY()) / 2,false);

        Arb_BackDartWidth = (0.32 * (((h_FrHipArc + i_BkHipArc) / 2.0) - ((e_FrWaistArc + f_BkWaistArc) / 2.0)));
        Arb_BackDartLength = j_WaistToSeat - 5.0;
        ArrayList<Vector2D> dartPoints = fullBlock.addDart(point10,
                point11,
                0.5,
                Arb_BackDartWidth,
                Arb_BackDartLength,
                true, false);

        fullBlock.addRightAngleCurve(point10,dartPoints.get(0));
        fullBlock.addRightAngleCurve(dartPoints.get(2),point11);

        fullBlock.addDirectedCurve(point13,point1,new Vector2D(point12.subtract(point11)),new Vector2D(point1and2.subtract(point1)),new double[]{0,90});

        fullBlock.addCircularCurve(point19,point20,(point20.getY() - (new Vector2D(point19.add(point20.subtract(point19).divide(2)))).getY()) / 2,false);

        fullBlock.addDirectedCurve(point22,point23,new Vector2D(1,0),new Vector2D(point24.subtract(point23)),new double[]{0,0});

        Vector2D point26and15 = new Vector2D((point15.getX() + (point26.getX() - point15.getX())) / 3, point15.getY() + ((point26.getY() - point15.getY()) * 0.75));
        fullBlock.addDirectedCurve(point26,point15, new Vector2D(point26.subtract(point25)),new Vector2D(point15.subtract(point26and15)),new double[]{0,0});

        fullBlock.addDirectedCurve(point15,point16,new Vector2D(point15.subtract(point26and15)),new Vector2D(point18.subtract(point16)),new double[]{90,0});

        Arb_FrontDartWidth = (0.18 * (((h_FrHipArc + i_BkHipArc) / 2.0) - ((e_FrWaistArc + f_BkWaistArc) / 2.0)));
        Arb_FrontDartLength = j_WaistToSeat - 1.5;
        ArrayList<Vector2D> dartPoints2 = fullBlock.addDart(point18,
                point19,
                0.3,
                Arb_BackDartWidth,
                Arb_BackDartLength,
                true, false);

        fullBlock.addRightAngleCurve(point18,dartPoints2.get(0));
        fullBlock.addRightAngleCurve(dartPoints2.get(2),point19);

    }
}