package gill;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

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
    private double k_AnkleCRHeight;         // Measurement [041]
    private double l_KneeCRHeight;          // Measurement [042]
    private double m_CrotchHeight;          // Measurement [043]
    private double n_HipCHeight;            // Measurement [044]
    private double o_SeatCHeight;           // Measurement [045]
    private double p_FrSeatArc;             // Measurement [046]
    private double q_CrotchZ;               // Measurement [047]
    private double r_HipFrZ;                // Measurement [048]
    private double s_HipBkZ;                // Measurement [049]
    private double t_SeatFrZ;               // Measurement [050]
    private double u_SeatBkZ;               // Measurement [051]
    private double v_WaistFrZ;              // Measurement [052]
    private double w_WaistBkZ;              // Measurement [053]
    private double x_FrCrotchLength;        // Measurement [054]
    private double y_BkCrotchLength;        // Measurement [055]

    /* Arbitrary Measurements */

    // Arb measurement for 2 inches in centimetres
    private double Arb_TwoInches;

    // Arb measurement for the crotch
    private double Arb_CrotchPointFive;

    // Arb measurement for back hem drop
    private double Arb_BkHemDrop;

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
            a_WaistToHip = dataStore.getId(15).value;            // Measurement [015]
            b_ThighCircR = dataStore.getId(17).value;            // Measurement [017]
            c_KneeCircR = dataStore.getId(18).value;             // Measurement [018]
            d_AnkleCircleR = dataStore.getId(19).value;          // Measurement [019]
            e_FrWaistArc = dataStore.getId(26).value;            // Measurement [026]
            f_BkWaistArc = dataStore.getId(27).value;            // Measurement [027]
            g_BkSeatArc = dataStore.getId(30).value;             // Measurement [030]
            h_FrHipArc = dataStore.getId(31).value;              // Measurement [031]
            i_BkHipArc = dataStore.getId(32).value;              // Measurement [032]
            j_WaistToSeat = dataStore.getId(34).value;           // Measurement [034]
            k_AnkleCRHeight = dataStore.getId(41).value;         // Measurement [041]
            l_KneeCRHeight = dataStore.getId(42).value;          // Measurement [042]
            m_CrotchHeight = dataStore.getId(43).value;          // Measurement [043]
            n_HipCHeight = dataStore.getId(44).value;            // Measurement [044]
            o_SeatCHeight = dataStore.getId(45).value;           // Measurement [045]
            p_FrSeatArc = dataStore.getId(46).value;             // Measurement [046]
            q_CrotchZ = dataStore.getId(47).value;               // Measurement [047]
            r_HipFrZ = dataStore.getId(48).value;                // Measurement [048]
            s_HipBkZ = dataStore.getId(49).value;                // Measurement [049]
            t_SeatFrZ = dataStore.getId(50).value;               // Measurement [050]
            u_SeatBkZ = dataStore.getId(51).value;               // Measurement [051]
            v_WaistFrZ = dataStore.getId(52).value;              // Measurement [052]
            w_WaistBkZ = dataStore.getId(53).value;              // Measurement [053]
            x_FrCrotchLength = dataStore.getId(54).value;        // Measurement [054]
            y_BkCrotchLength = dataStore.getId(55).value;        // Measurement [055]

            // Get name
            userName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            Pattern.addMissingMeasurement(dataStore.getName(), method.toString(), garment.toString());
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
        if (q_CrotchZ < 0)
        {
            double stepNineY = ((s_HipBkZ - q_CrotchZ) - 2.5 * (CROTCHREDUCTION) + ((i_BkHipArc / 2.0) + 2.0));
        }
        else if (q_CrotchZ > 0)
        {
            double stepNineY = ((s_HipBkZ + q_CrotchZ) - 2.5 * (CROTCHREDUCTION) + ((i_BkHipArc / 2.0) + 2.0));
        }
        else if (q_CrotchZ == 0)
        {
            double stepNineY = ((s_HipBkZ) - 2.5 * (CROTCHREDUCTION) + ((i_BkHipArc / 2.0) + 2.0));
        }

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(userName + "_Gill_Trouser_Block"));
        Block fullBlock = blocks.get(0);

        // Adding the origin (crotch point)
        fullBlock.addKeypoint(new Vector2D(0.0, 0.0));


    }
}
