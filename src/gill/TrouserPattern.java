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
        double stepNineY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepNineY = ((t_HipBkZ - r_CrotchZ) - 2.5 * (Arb_CrotchReduction) + ((i_BkHipArc / 2.0) + 2.0));
        }
        else if (r_CrotchZ > 0)
        {
            stepNineY = ((t_HipBkZ + r_CrotchZ) - 2.5 * (Arb_CrotchReduction) + ((i_BkHipArc / 2.0) + 2.0));
        }
        else if (r_CrotchZ == 0)
        {
            stepNineY = ((t_HipBkZ) - 2.5 * (Arb_CrotchReduction) + ((i_BkHipArc / 2.0) + 2.0));
        }

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(userName + "_Gill_Trouser_Block"));
        Block fullBlock = blocks.get(0);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Back half points, curves and dart */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Adding Step 1
        fullBlock.addKeypoint(new Vector2D(Arb_CrotchPointFive, 0.0));

        // Adding Step 2
        fullBlock.addKeypoint(new Vector2D(Arb_TwoInches, ((stepNineY / 2.0) - (((b_ThighCircR + 5.5) + 2.0) / 4.0))));

        // Adding Step 3
        fullBlock.addKeypoint(new Vector2D((n_CrotchHeight - m_KneeCRHeight), ((stepNineY / 2.0) - (((c_KneeCircR * 0.5 + 2.0) + 7.0) / 2.0))));

        // Adding Step 4
        fullBlock.addKeypoint(new Vector2D((n_CrotchHeight - l_AnkleCRHeight), ((stepNineY / 2.0) - (((d_AnkleCircleR * 0.5) + 10.0) / 2.0))));

        // Adding Step 5
        fullBlock.addKeypoint(new Vector2D(((n_CrotchHeight - l_AnkleCRHeight) + Arb_BkHemDrop), (stepNineY / 2.0)));

        // Adding Step 6
        fullBlock.addKeypoint(new Vector2D((n_CrotchHeight - l_AnkleCRHeight), ((stepNineY / 2.0) + (((0.5 * d_AnkleCircleR + 2.0) + 10.0) / 2.0))));

        // Adding Step 7
        fullBlock.addKeypoint(new Vector2D((n_CrotchHeight - m_KneeCRHeight), ((stepNineY / 2.0) + (((0.5 * c_KneeCircR + 2.0) + 7.0) / 2.0))));

        // Adding Step 8
        fullBlock.addKeypoint(new Vector2D(Arb_TwoInches, ((stepNineY / 2.0) + (((b_ThighCircR + 5.5) + 2.0) / 4.0))));

        // Adding Step 9
        fullBlock.addKeypoint(new Vector2D(0.0, stepNineY));

        // Adding Step 10
        // TODO: This step will need an additional easement to be calculate from analysis of patter outputs
        fullBlock.addKeypoint(new Vector2D((-(k_BodyRise - a_WaistToHip)), ((stepNineY / 2.0) + ((i_BkHipArc / 4.0) + 2.0))));

        // Adding Step 11
        // Has an if statement depending on the sign of measurement [047]
        double stepElevenY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepElevenY = ((v_SeatBkZ - r_CrotchZ) - 2.5 * Arb_CrotchReduction + ((g_BkSeatArc / 2.0) + 2.0));
        }
        else if (r_CrotchZ > 0)
        {
            stepElevenY = ((v_SeatBkZ + r_CrotchZ) - 2.5 * Arb_CrotchReduction + ((g_BkSeatArc / 2.0) + 2.0));
        }
        else if (r_CrotchZ == 0)
        {
            stepElevenY = ((v_SeatBkZ) - 2.5 * Arb_CrotchReduction + ((g_BkSeatArc / 2.0) + 2.0));
        }

        fullBlock.addKeypoint(new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepElevenY));

        // Adding Step 12
        // Has an if statement depending on the sign of measurement [047]
        double stepTwelveY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepTwelveY = ((x_WaistBkZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction) + ((v_SeatBkZ - x_WaistBkZ) * 2.0) + ((f_BkWaistArc  + 0.5) / 2.0));
        }
        else if (r_CrotchZ > 0)
        {
            stepTwelveY = ((x_WaistBkZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction) + ((v_SeatBkZ - x_WaistBkZ) * 2.0) + ((f_BkWaistArc  + 0.5) / 2.0));
        }
        else if (r_CrotchZ == 0)
        {
            stepTwelveY = ((x_WaistBkZ) - (2.5 * Arb_CrotchReduction) + ((v_SeatBkZ - x_WaistBkZ) * 2.0) + ((f_BkWaistArc  + 0.5) / 2.0));
        }

        fullBlock.addKeypoint(new Vector2D(-k_BodyRise, stepTwelveY));

        // Adding Step 13
        // TODO: Needs an offset value adding to the x value, this will be calculated from analysis of the outputs
        // Has an if statement depending on the sign of measurement [047]
        double stepThirteenY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepThirteenY = ((x_WaistBkZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction) + ((v_SeatBkZ - x_WaistBkZ) * 2.0));
        }
        else if (r_CrotchZ > 0)
        {
            stepThirteenY = ((x_WaistBkZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction) + ((v_SeatBkZ - x_WaistBkZ) * 2.0));
        }
        else if (r_CrotchZ == 0)
        {
            stepThirteenY = ((x_WaistBkZ) - (2.5 * Arb_CrotchReduction) + ((v_SeatBkZ - x_WaistBkZ) * 2.0));
        }

        fullBlock.addKeypoint(new Vector2D(-k_BodyRise, stepThirteenY));

        // Adding Step 14
        // Has an if statement depending on the sign of measurement [047]
        double stepFourteenY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepFourteenY = ((v_SeatBkZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction));
        }
        else if (r_CrotchZ > 0)
        {
            stepFourteenY = ((v_SeatBkZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction));
        }
        else if (r_CrotchZ == 0)
        {
            stepFourteenY = ((v_SeatBkZ) - (2.5 * Arb_CrotchReduction));
        }

        fullBlock.addKeypoint(new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepFourteenY));

        // Adding Step 15
        // Has an if statement depending on the sign of measurement [047]
        double stepFifteenY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepFifteenY = ((t_HipBkZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction));
        }
        else if (r_CrotchZ > 0)
        {
            stepFifteenY = ((t_HipBkZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction));
        }
        else if (r_CrotchZ == 0)
        {
            stepFifteenY = ((t_HipBkZ) - (2.5 * Arb_CrotchReduction));
        }

        fullBlock.addKeypoint(new Vector2D((-(k_BodyRise - a_WaistToHip)), stepFifteenY));

        // Adding Step 16
        fullBlock.addKeypoint(new Vector2D(Arb_CrotchPointFive, 0.0));

        // Add back dart
        Vector2D startSegment = new Vector2D(-k_BodyRise, stepTwelveY);
        Vector2D endSegment = new Vector2D(-k_BodyRise, stepThirteenY);
        double positionTopDart = 0.5;
        Arb_BackDartWidth = (0.32 * (((h_FrHipArc + i_BkHipArc) / 2.0) - ((e_FrWaistArc + f_BkWaistArc) / 2.0)));
        Arb_BackDartLength = j_WaistToSeat - 5.0;
        ArrayList<Vector2D> dartPoints = fullBlock.addDart(startSegment,
                endSegment,
                positionTopDart,
                Arb_BackDartWidth,
                Arb_BackDartLength,
                true, false);

        // Adding curve from 1 --> 2
        fullBlock.addCircularCurve(new Vector2D(Arb_CrotchPointFive, 0.0),
                new Vector2D(Arb_TwoInches, ((stepNineY / 2.0) - (((b_ThighCircR + 5.5) + 2.0) / 4.0))),
                0.5,
                false
        );

        // Adding curve from 2 --> 3
        fullBlock.addCircularCurve(new Vector2D(Arb_TwoInches, ((stepNineY / 2.0) - (((b_ThighCircR + 5.5) + 2.0) / 4.0))),
                new Vector2D((n_CrotchHeight - m_KneeCRHeight), ((stepNineY / 2.0) - (((c_KneeCircR * 0.5 + 2.0) + 7.0) / 2.0))),
                        0.5,
                        false
                );

        // Adding curve from 4 --> 5
        fullBlock.addCircularCurve(new Vector2D((n_CrotchHeight - l_AnkleCRHeight), ((stepNineY / 2.0) - (((d_AnkleCircleR * 0.5) + 10.0) / 2.0))),
                new Vector2D(((n_CrotchHeight - l_AnkleCRHeight) + Arb_BkHemDrop), (stepNineY / 2.0)),
                0.25,
                true
        );

        // Adding curve from 5 --> 6
        fullBlock.addCircularCurve(new Vector2D(((n_CrotchHeight - l_AnkleCRHeight) + Arb_BkHemDrop), (stepNineY / 2.0)),
                new Vector2D((n_CrotchHeight - l_AnkleCRHeight), ((stepNineY / 2.0) + (((0.5 * d_AnkleCircleR + 2.0) + 10.0) / 2.0))),
                0.25,
                true
        );

        // Adding curve from 7 --> 8
        fullBlock.addCircularCurve(new Vector2D((n_CrotchHeight - m_KneeCRHeight), ((stepNineY / 2.0) + (((0.5 * c_KneeCircR + 2.0) + 7.0) / 2.0))),
                new Vector2D(Arb_TwoInches, ((stepNineY / 2.0) + (((b_ThighCircR + 5.5) + 2.0) / 4.0))),
                0.5,
                false
        );

        // Adding curve from 8 --> 9
        fullBlock.addCircularCurve(new Vector2D(Arb_TwoInches, ((stepNineY / 2.0) + (((b_ThighCircR + 5.5) + 2.0) / 4.0))),
                new Vector2D(0.0, stepNineY),
                0.5,
                true
        );

        // Adding curve from 9 --> 10
        fullBlock.addCircularCurve(new Vector2D(0.0, stepNineY),
                new Vector2D((-(k_BodyRise - a_WaistToHip)), ((stepNineY / 2.0) + ((i_BkHipArc / 4.0) + 2.0))),
                0.5,
                true
        );

        // Adding curve from 10 --> 11
        fullBlock.addCircularCurve(new Vector2D((-(k_BodyRise - a_WaistToHip)), ((stepNineY / 2.0) + ((i_BkHipArc / 4.0) + 2.0))),
                new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepElevenY),
                0.5,
                true
                );

        // Adding curve from 11 --> 12
        fullBlock.addCircularCurve(new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepElevenY),
                new Vector2D(-k_BodyRise, stepTwelveY),
                0.5,
                true
                );

        // Adding curve from 12 --> dart start
        fullBlock.addCircularCurve(new Vector2D(-k_BodyRise, stepTwelveY),
                dartPoints.get(0),
                0.5,
                false
        );

        // Adding curve from dart end --> 13
        fullBlock.addCircularCurve(dartPoints.get(2),
                new Vector2D(-k_BodyRise, stepThirteenY),
                0.5,
                false
        );

        // Adding curve from 14 --> 15
        fullBlock.addCircularCurve(new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepFourteenY),
                new Vector2D((-(k_BodyRise - a_WaistToHip)), stepFifteenY),
                0.5,
                false
        );

        // Adding curve from 15 --> 16
        fullBlock.addCircularCurve(new Vector2D((-(k_BodyRise - a_WaistToHip)), stepFifteenY),
                new Vector2D(Arb_CrotchPointFive, 0.0),
                0.5,
                false
        );

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Front half points, curves and dart */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Adding Step 17
        fullBlock.addKeypoint(new Vector2D(0.0, 0.0));

        // Adding Step 18
        // Has an if statement depending on the sign of measurement [047]
        double stepEighteenY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepEighteenY = (-((s_HipFrZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction)));
        }
        else if (r_CrotchZ > 0)
        {
            stepEighteenY = (-((s_HipFrZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction)));
        }
        else if (r_CrotchZ == 0)
        {
            stepEighteenY = (-((s_HipFrZ) - (2.5 * Arb_CrotchReduction)));
        }

        fullBlock.addKeypoint(new Vector2D((-(k_BodyRise - a_WaistToHip)), stepEighteenY));

        // Adding Step 19
        // Has an if statement depending on the sign of measurement [047]
        double stepNineteenY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepNineteenY = (-((u_SeatFrZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction)));
        }
        else if (r_CrotchZ > 0)
        {
            stepNineteenY = (-((u_SeatFrZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction)));
        }
        else if (r_CrotchZ == 0)
        {
            stepNineteenY = (-((u_SeatFrZ) - (2.5 * Arb_CrotchReduction)));
        }

        fullBlock.addKeypoint(new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepNineteenY));

        // Adding Step 20
        // Has an if statement depending on the sign of measurement [047]
        double stepTwentyY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepTwentyY = (-((w_WaistFrZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction)));
        }
        else if (r_CrotchZ > 0)
        {
            stepTwentyY = (-((w_WaistFrZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction)));
        }
        else if (r_CrotchZ == 0)
        {
            stepTwentyY = (-((w_WaistFrZ) - (2.5 * Arb_CrotchReduction)));
        }

        fullBlock.addKeypoint(new Vector2D(-k_BodyRise, stepTwentyY));

        // Adding Step 21
        // Has an if statement depending on the sign of measurement [047]
        double stepTwentyOneY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepTwentyOneY = (-((w_WaistFrZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction) + (e_FrWaistArc + 0.25) / 2.0));
        }
        else if (r_CrotchZ > 0)
        {
            stepTwentyOneY = (-((w_WaistFrZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction) + (e_FrWaistArc + 0.25) / 2.0));
        }
        else if (r_CrotchZ == 0)
        {
            stepTwentyOneY = (-((w_WaistFrZ) - (2.5 * Arb_CrotchReduction) + (e_FrWaistArc + 0.25) / 2.0));
        }

        fullBlock.addKeypoint(new Vector2D(-k_BodyRise, stepTwentyOneY));

        // Adding Step 22
        // Has an if statement depending on the sign of measurement [047]
        double stepTwentyTwoY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepTwentyTwoY = (-((u_SeatFrZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction) + ((q_FrSeatArc / 2.0) + 1.0)));
        }
        else if (r_CrotchZ > 0)
        {
            stepTwentyTwoY = (-((u_SeatFrZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction) + ((q_FrSeatArc / 2.0) + 1.0)));
        }
        else if (r_CrotchZ == 0)
        {
            stepTwentyTwoY = (-((u_SeatFrZ) - (2.5 * Arb_CrotchReduction) + ((q_FrSeatArc / 2.0) + 1.0)));
        }

        fullBlock.addKeypoint(new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepTwentyTwoY));

        // Adding Step 23
        // Has an if statement depending on the sign of measurement [047]
        double stepTwentyThreeY = 0.0;
        if (r_CrotchZ < 0)
        {
            stepTwentyThreeY = (-((s_HipFrZ + r_CrotchZ) - (2.5 * Arb_CrotchReduction) + ((h_FrHipArc / 2.0) + 1.0)));
        }
        else if (r_CrotchZ > 0)
        {
            stepTwentyThreeY = (-((s_HipFrZ - r_CrotchZ) - (2.5 * Arb_CrotchReduction) + ((h_FrHipArc / 2.0) + 1.0)));
        }
        else if (r_CrotchZ == 0)
        {
            stepTwentyThreeY = (-((s_HipFrZ) - (2.5 * Arb_CrotchReduction) + ((h_FrHipArc / 2.0) + 1.0)));
        }

        fullBlock.addKeypoint(new Vector2D((-(k_BodyRise - a_WaistToHip)), stepTwentyThreeY));

        // Adding Step 24
        fullBlock.addKeypoint(new Vector2D(0.0, stepTwentyThreeY));

        // Adding Step 25
        fullBlock.addKeypoint(new Vector2D(Arb_TwoInches, ((stepTwentyThreeY / 2.0) - (((b_ThighCircR + 5.5) - 2) / 4.0))));

        // Adding Step 26
        fullBlock.addKeypoint(new Vector2D((n_CrotchHeight - m_KneeCRHeight), ((stepTwentyThreeY / 2.0) - (((c_KneeCircR + 5.5) - 2.0) / 2.0))));

        // Adding Step 27
        fullBlock.addKeypoint(new Vector2D((n_CrotchHeight - l_AnkleCRHeight), ((stepTwentyThreeY / 2.0) - (((d_AnkleCircleR + 5.5) - 2.0) / 2.0))));

        // Adding Step 28
        fullBlock.addKeypoint(new Vector2D((n_CrotchHeight - l_AnkleCRHeight), ((stepTwentyThreeY / 2.0) + (((d_AnkleCircleR + 5.5) - 2.0) / 4.0))));

        // Adding Step 29
        fullBlock.addKeypoint(new Vector2D((n_CrotchHeight - m_KneeCRHeight), ((stepTwentyThreeY / 2.0) + (((c_KneeCircR + 5.5) - 2.0) / 4.0))));

        // Adding Step 30
        fullBlock.addKeypoint(new Vector2D(Arb_TwoInches, ((stepTwentyThreeY / 2.0) + (((b_ThighCircR + 5.5) - 2) / 4.0))));

        // Adding Step 31
        fullBlock.addKeypoint(new Vector2D(0.0, 0.0));

        // Add front dart
        Vector2D startSegment2 = new Vector2D(-k_BodyRise, stepTwentyY);
        Vector2D endSegment2 = new Vector2D(-k_BodyRise, stepTwentyOneY);
        double positionTopDart2 = 1.0 / 3.0;
        Arb_FrontDartWidth = (0.18 * (((h_FrHipArc + i_BkHipArc) / 2.0) - ((e_FrWaistArc + f_BkWaistArc) / 2.0)));
        Arb_FrontDartLength = j_WaistToSeat - 1.5;
        ArrayList<Vector2D> dartPoints2 = fullBlock.addDart(startSegment2,
                endSegment2,
                positionTopDart2,
                Arb_BackDartWidth,
                Arb_BackDartLength,
                true, false);

        // Adding curve from 17 --> 18
        fullBlock.addCircularCurve(new Vector2D(0.0, 0.0),
                new Vector2D((-(k_BodyRise - a_WaistToHip)), stepEighteenY),
                0.5,
                false
        );

        // Adding curve from 18 --> 19
        fullBlock.addCircularCurve(new Vector2D((-(k_BodyRise - a_WaistToHip)), stepEighteenY),
                new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepNineteenY),
                0.5,
                false
        );

        // Adding curve from 21 --> 22
        fullBlock.addCircularCurve(new Vector2D(-k_BodyRise, stepTwentyOneY),
                new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepTwentyTwoY),
                0.5,
                true
        );

        // Adding curve from 22 --> 23
        fullBlock.addCircularCurve(new Vector2D((-(k_BodyRise - j_WaistToSeat)), stepTwentyTwoY),
                new Vector2D((-(k_BodyRise - a_WaistToHip)), stepTwentyThreeY),
                0.5,
                true
        );

        // Adding curve from 23 --> 24
        fullBlock.addCircularCurve(new Vector2D((-(k_BodyRise - a_WaistToHip)), stepTwentyThreeY),
                new Vector2D(0.0, stepTwentyThreeY),
                0.5,
                true
        );

        // Adding curve from 24 --> 25
        fullBlock.addCircularCurve(new Vector2D(0.0, stepTwentyThreeY),
                new Vector2D(Arb_TwoInches, ((stepTwentyThreeY / 2.0) - (((b_ThighCircR + 5.5) - 2) / 4.0))),
                0.5,
                true
        );

        // Adding curve from 25 --> 26
        fullBlock.addCircularCurve(new Vector2D(Arb_TwoInches, ((stepTwentyThreeY / 2.0) - (((b_ThighCircR + 5.5) - 2) / 4.0))),
                new Vector2D((n_CrotchHeight - m_KneeCRHeight), ((stepTwentyThreeY / 2.0) - (((c_KneeCircR + 5.5) - 2.0) / 2.0))),
                0.5,
                true
        );

        // Adding curve from 29 --> 30
        fullBlock.addCircularCurve(new Vector2D((n_CrotchHeight - m_KneeCRHeight), ((stepTwentyThreeY / 2.0) + (((c_KneeCircR + 5.5) - 2.0) / 4.0))),
                new Vector2D(Arb_TwoInches, ((stepTwentyThreeY / 2.0) + (((b_ThighCircR + 5.5) - 2) / 4.0))),
                0.5,
                false
        );

        // Adding curve from 30 --> 31
        fullBlock.addCircularCurve(new Vector2D(Arb_TwoInches, ((stepTwentyThreeY / 2.0) + (((b_ThighCircR + 5.5) - 2) / 4.0))),
                new Vector2D(0.0, 0.0),
                0.5,
                false
        );
    }
}