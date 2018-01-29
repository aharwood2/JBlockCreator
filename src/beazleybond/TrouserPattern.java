package beazleybond;

import jblockmain.Measurements;
import jblockmain.Pattern;

/** Class to construct a trouser pattern using the Beazley and Bond drafting method. */
public class TrouserPattern
    extends Pattern
{

    /* Pattern-specific Measurements */
    // In future will be simply extracted from the Measurements object.
    private double a_Waist              = 70.0;
    private double a_WaistBand          = 70.0;
    private double b_UpperHip           = 90.0;
    private double c_Hip                = 96.0;
    private double d_Thigh              = 57.0;
    private double e_KneeStraight       = 37.0;
    private double e_KneeSlim           = 37.0;
    private double f_Ankle              = 25.0;
    private double g_UpperHip           = 10.0;
    private double h_Hip                = 20.0;
    private double i_Crutch             = 28.0;
    private double j_Knee               = 60.0;
    private double k_OutsideLegToAnkle  = 100.0;
    private double l_InsideLegToAnkle   = 72.0;

    /* Arbitrary Measurements */



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public TrouserPattern(Measurements dataStore)
    {
        readMeasurements(dataStore);
        addEasement();
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected void addEasement()
    {
        // Size 12 for now
        a_Waist += 4.0;
        a_WaistBand += 2.0;
        b_UpperHip += 4.0;
        c_Hip += 4.0;
        d_Thigh += 10.0;
        e_KneeStraight += 15.0;
        e_KneeSlim += 9.0;
        f_Ankle += 9.0;
        i_Crutch += 1.0;
        l_InsideLegToAnkle -= 1.0;
    }

    @Override
    protected void readMeasurements(Measurements dataStore)
    {
        // TODO: Implement when we couple to scan data
    }

    /**
     * The actual block creation process following the drafting method of Beazley and Bond.
     */
    @Override
    protected void createBlocks()
    {
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity
        // for plotting. The bottom left corner of the space to be the origin.

        // TODO: Redo
    }



}
