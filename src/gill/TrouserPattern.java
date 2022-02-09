package gill;

import jblockenums.EPattern;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class TrouserPattern
        extends Pattern
{
    public TrouserPattern(String userName, InputFileData dataStore, MeasurementSet template)
    {
        super(userName, dataStore, template);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.GILL_TROUSER;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("c_KneeCircR", "A18"));
        measurements.addMeasurement(new Measurement("d_AnkleCircleR", "A19"));
        measurements.addMeasurement(new Measurement("e_FrWaistArc", "A26"));
        measurements.addMeasurement(new Measurement("f_BkWaistArc", "A27"));
        measurements.addMeasurement(new Measurement("g_BkSeatArc", "A30"));
        measurements.addMeasurement(new Measurement("h_FrHipArc", "A31"));
        measurements.addMeasurement(new Measurement("i_BkHipArc", "A32"));
        measurements.addMeasurement(new Measurement("j_WaistToSeat", "A34"));
        measurements.addMeasurement(new Measurement("k_BodyRise", "A38"));
        measurements.addMeasurement(new Measurement("l_AnkleCRHeight", "A41"));
        measurements.addMeasurement(new Measurement("m_KneeCRHeight", "A42"));
        measurements.addMeasurement(new Measurement("n_CrotchHeight", "A43"));
        measurements.addMeasurement(new Measurement("o_HipCHeight", "A44"));
        measurements.addMeasurement(new Measurement("p_SeatCHeight", "A45"));
        measurements.addMeasurement(new Measurement("q_FrSeatArc", "A46"));
        measurements.addMeasurement(new Measurement("r_CrotchZ", "A47"));
        measurements.addMeasurement(new Measurement("s_HipFrZ", "A48"));
        measurements.addMeasurement(new Measurement("t_HipBkZ", "A49"));
        measurements.addMeasurement(new Measurement("u_SeatFrZ", "A50"));
        measurements.addMeasurement(new Measurement("v_SeatBkZ", "A51"));
        measurements.addMeasurement(new Measurement("w_WaistFrZ", "A52"));
        measurements.addMeasurement(new Measurement("x_WaistBkZ", "A53"));
    }

    /**
     * The actual block creation process following the drafting method of Gill.
     */
    @Override
    public void createBlocks()
    {
        // Pull from store
        var c_KneeCircR = get("c_KneeCircR");
        var d_AnkleCircleR = get("d_AnkleCircleR");
        var e_FrWaistArc = get("e_FrWaistArc");
        var f_BkWaistArc = get("f_BkWaistArc");
        var g_BkSeatArc = get("g_BkSeatArc");
        var h_FrHipArc = get("h_FrHipArc");
        var i_BkHipArc = get("i_BkHipArc");
        var j_WaistToSeat = get("j_WaistToSeat");
        var k_BodyRise = get("k_BodyRise");
        var l_AnkleCRHeight = get("l_AnkleCRHeight");
        var m_KneeCRHeight = get("m_KneeCRHeight");
        var n_CrotchHeight = get("n_CrotchHeight");
        var o_HipCHeight = get("o_HipCHeight");
        var p_SeatCHeight = get("p_SeatCHeight");
        var q_FrSeatArc = get("q_FrSeatArc");
        var r_CrotchZ = get("r_CrotchZ");
        var s_HipFrZ = get("s_HipFrZ");
        var t_HipBkZ = get("t_HipBkZ");
        var u_SeatFrZ = get("u_SeatFrZ");
        var v_SeatBkZ = get("v_SeatBkZ");
        var w_WaistFrZ = get("w_WaistFrZ");
        var x_WaistBkZ = get("x_WaistBkZ");

        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The crotch point is the origin

        //need to fix the fact that the crotch point is not always centered on the body scanner
        //hence need to shift the front/back by that amount
        if (r_CrotchZ > 0.0)
        {
            s_HipFrZ -= r_CrotchZ;
            w_WaistFrZ -= r_CrotchZ;
            u_SeatFrZ -= r_CrotchZ;

            t_HipBkZ -= r_CrotchZ;
            x_WaistBkZ -= r_CrotchZ;
            v_SeatBkZ -= r_CrotchZ;
        }
        if (r_CrotchZ < 0.0)
        {
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

        double centreXPosition = 0.0;
        double centreYPosition = 0.0;

        double crotchXPosition = centreXPosition;
        double kneeXPosition = centreXPosition + n_CrotchHeight - m_KneeCRHeight;
        double ankleXPosition = n_CrotchHeight - l_AnkleCRHeight;
        double hipXPosition = -(o_HipCHeight - n_CrotchHeight);
        double seatXPosition = -(p_SeatCHeight - n_CrotchHeight);
        double waistXPosition = -k_BodyRise;

        double halfWaistSuppression = ((h_FrHipArc + i_BkHipArc) / 2.0) - ((e_FrWaistArc + f_BkWaistArc) / 2.0);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Back half key vectors/points */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Pre-calculation of point 7 y coordinate - used in a lot of calculations
        double y7 = (i_BkHipArc / 2.0) - t_HipBkZ - 2.5 + 2.0;

        Vector2D point1 = new Vector2D(crotchXPosition + 0.5, centreYPosition);
        Vector2D point2 = new Vector2D(kneeXPosition, y7 / 2.0 - (((c_KneeCircR / 2.0) + 7.0 + 2.0) / 2.0));
        Vector2D point3 = new Vector2D(ankleXPosition, y7 / 2.0 - (((d_AnkleCircleR / 2.0) + 10.0 + 2.0) / 2.0));
        Vector2D point5 = new Vector2D(ankleXPosition, y7 / 2.0 + (((d_AnkleCircleR / 2.0) + 10.0 + 2.0) / 2.0));
        Vector2D point6 = new Vector2D(kneeXPosition, y7 / 2.0 + (((c_KneeCircR / 2.0) + 7.0 + 2.0) / 2.0));
        Vector2D point7 = new Vector2D(crotchXPosition, y7);
        Vector2D point8 = new Vector2D(hipXPosition, (i_BkHipArc / 2.0) - t_HipBkZ - 2.5 + 2.0);
        Vector2D point9 = new Vector2D(seatXPosition, ((-v_SeatBkZ) - 2.5) + (g_BkSeatArc / 2.0) + 2.0);
        Vector2D point10 = new Vector2D(waistXPosition, ((-v_SeatBkZ) - 2.5 + (-x_WaistBkZ) - 2.5)
                + (0.32 * halfWaistSuppression) + (f_BkWaistArc / 2.0) + 0.35);

        // TODO: Need to move point 11 in -x direction based on back crotch length

        Vector2D point11 = new Vector2D(waistXPosition + 4.0, (-v_SeatBkZ - 2.5 + -x_WaistBkZ - 2.5));
        Vector2D point12 = new Vector2D(seatXPosition, -v_SeatBkZ - 2.5);
        Vector2D point13 = new Vector2D(hipXPosition, (-t_HipBkZ - 2.5));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*  Front half key vectors/points */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Vector2D point15 = new Vector2D(crotchXPosition, centreYPosition);
        Vector2D point16 = new Vector2D(hipXPosition, -(s_HipFrZ - 2.5));
        Vector2D point17 = new Vector2D(seatXPosition, -(u_SeatFrZ - 2.5));
        Vector2D point18 = new Vector2D(waistXPosition, -(w_WaistFrZ - 2.5));
        Vector2D point19 = new Vector2D(waistXPosition, point18.getY() - ((e_FrWaistArc / 2.0) + 0.35
                + (0.18 * halfWaistSuppression)));
        Vector2D point20 = new Vector2D(seatXPosition, point17.getY() - ((q_FrSeatArc / 2.0) + 1.0));
        Vector2D point21 = new Vector2D(hipXPosition, point16.getY() - ((h_FrHipArc / 2.0) + 1.0));
        Vector2D point22 = new Vector2D(centreXPosition, -(s_HipFrZ - 2.5 + (h_FrHipArc / 2.0) + 1.0));
        Vector2D point23 = new Vector2D(kneeXPosition,
                                        (point22.getY() / 2.0) - (((c_KneeCircR / 2.0) + 7.0 - 2.0) / 2.0));

        Vector2D point24 = new Vector2D(ankleXPosition,
                                        (point22.getY() / 2.0) - (((d_AnkleCircleR / 2.0) + 10.0 - 2.0) / 2.0));

        Vector2D point25 = new Vector2D(ankleXPosition,
                                        (point22.getY() / 2.0) + (((d_AnkleCircleR / 2.0) + 10.0 - 2.0) / 2.0));

        Vector2D point26 = new Vector2D(kneeXPosition,
                                        (point22.getY() / 2.0) + (((c_KneeCircR / 2.0) + 7.0 - 2.0) / 2.0));

        if (seatXPosition > hipXPosition)
        {
            Vector2D temp;
            temp = point8;
            point8 = point9;
            point9 = temp;

            temp = point12;
            point12 = point13;
            point13 = temp;

            temp = point16;
            point16 = point17;
            point17 = temp;

            temp = point20;
            point20 = point21;
            point21 = temp;

        }

        // All the back half keypoints/vectors added
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
        fullBlock.addKeypoint(point1);

        // All the front half keypoints/vectors added
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

        // Used to help guide curve between crotch point and ankle by having an apex 1/3rd length and 1/4 height
        // From the length and height
        Vector2D point1and2 = new Vector2D((point1.getX() + (point2.getX() - point1.getX())) / 3.0,
                                           point1.getY() + ((point2.getY() - point1.getY()) * 0.75));

        // A bunch of curves
        fullBlock.addDirectedCubicSpline(
                point1, point2,
                new Vector2D(point1and2.subtract(point1)),
                new Vector2D(point3.subtract(point2)),
                new double[]{0.0, 0.0});

        fullBlock.addCircularArc(point3, point5, 1.0, true);

        fullBlock.addDirectedCubicSpline(
                point6, point7,
                new Vector2D(point6.subtract(point5)),
                new Vector2D(point8.subtract(point7)),
                new double[]{0.0, 0.0});

        fullBlock.addCircularArc(
                point9, point10,
                (point10.getY() - (new Vector2D(point9.add(point10.subtract(point9).divide(2.0)))).getY()) / 2.0,
                false);

        var Arb_BackDartWidth = (0.32 * (((h_FrHipArc + i_BkHipArc) / 2.0) - ((e_FrWaistArc + f_BkWaistArc) / 2.0)));
        var Arb_BackDartLength = j_WaistToSeat - 5.0;
        ArrayList<Vector2D> dartPoints = fullBlock.addDart(
                point10, point11,
                0.5,
                Arb_BackDartWidth,
                Arb_BackDartLength,
                true, false);

        // More curves added between the points
        fullBlock.addRightAngleCurve(point10, dartPoints.get(0));
        fullBlock.addRightAngleCurve(dartPoints.get(2), point11);

        fullBlock.addDirectedCubicSpline(
                point13, point1,
                new Vector2D(point12.subtract(point11)),
                new Vector2D(point1and2.subtract(point1)),
                new double[]{0.0, 90.0});

        fullBlock.addCircularArc(
                point19, point20,
                (point20.getY() - (new Vector2D(point19.add(point20.subtract(point19).divide(2.0)))).getY()) / 2.0,
                false);

        fullBlock.addDirectedCubicSpline(
                point22, point23,
                new Vector2D(1.0, 0.0),
                new Vector2D(point24.subtract(point23)), new double[]{0.0, 0.0});

        // Used to help guide curve between crotch point and ankle by having an apex 1/3rd length and 1/4 height
        // From the length and height
        Vector2D point26and15 = new Vector2D((point15.getX() + (point26.getX()
                - point15.getX())) / 3.0, point15.getY() + ((point26.getY() - point15.getY()) * 0.75));

        fullBlock.addDirectedCubicSpline(
                point26, point15,
                new Vector2D(point26.subtract(point25)),
                new Vector2D(point15.subtract(point26and15)), new double[]{0.0, 0.0});

        fullBlock.addDirectedCubicSpline(
                point15, point16,
                new Vector2D(point15.subtract(point26and15)),
                new Vector2D(point18.subtract(point16)), new double[]{90.0, 0.0});

        var Arb_FrontDartWidth = (0.18 * (((h_FrHipArc + i_BkHipArc) / 2.0) - ((e_FrWaistArc + f_BkWaistArc) / 2.0)));
        var Arb_FrontDartLength = j_WaistToSeat - 1.5;
        ArrayList<Vector2D> dartPoints2 = fullBlock.addDart(
                point18, point19,
                0.3,
                Arb_FrontDartWidth,
                Arb_FrontDartLength,
                true, false);

        fullBlock.addRightAngleCurve(point18, dartPoints2.get(0));
        fullBlock.addRightAngleCurve(dartPoints2.get(2), point19);

    }

}