package beazleybond;

import jblockmain.*;

/** Class to construct a fitted bodice using the Beazley and Bond drafting method. */
public class BodicePattern
    extends Pattern
{

    /* Pattern-specific Measurements */
    // In future will be simply extracted from the Measurements object.
    private double a_Bust           = 88.0;
    private double b_Waist          = 70.0;
    private double c_Neck           = 38.0;
    private double d_BackNeckRise   = 2.0;
    private double e_NapeToWaist    = 41.0;
    private double f_ArmholeDepth   = 21.0;
    private double g_FrNeckToBust   = 27.0;
    private double h_FrNeckToWaist  = 44.0;
    private double i_AcrossBack     = 35.0;
    private double j_AcrossFront    = 32.0;
    private double k_Shoulder       = 13.0;
    private double l_WidthBustProm  = 19.0;
    private double m_WidthArmhole   = 10.0;

    /* Arbitrary Measurements */

    // This relates to the height of the basic rectangle which includes this amount for suppression of back waist dart
    // and the side seam.
    private double Arb_BackWaistDartSuppression = 1.5;

    // Level corresponding to the across back measurement is chosen as halfway between the armhole level and the neck
    private double Arb_AcrossBackLevel = f_ArmholeDepth / 2.0;

    // Setting of side seam position from the centre back (CB) at base of rectangle plus arbitrary 1.5cm
    private double Arb_SideSeamFromCentreBack = (a_Bust / 4.0) + 1.5;

    // Neck width and depth derived from the neck measurement
    private double Arb_HalfFrontNeckWidth = (c_Neck / 5.0) - 1.5;
    private double Arb_FrontNeckDepth = c_Neck / 5.0;
    private double Arb_HalfBackNeckWidth = (c_Neck / 5.0) - 0.5;
    private double Arb_BackNeckRise = 2.0;

    // Shoulder Level parameters. Shoulder slant measured in degrees.
    private double Arb_ShoulderSlant = 22.0;
    private double Arb_ShoulderLevel = 6.0;
    private double Arb_ShoulderLine = 17.5;
    private double Arb_BackShoulderLevel = 4.0;
    private double Arb_FrontShoulderDartWidth = 4.5;
    private double Arb_BackShoulderDartWidth = 1.5;
    private double Arb_BackShoulderDartPositionOnArmholeLevel = 9.25;
    private double Arb_BackShoulderDartLength = 8.0;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BodicePattern(Measurements dataStore)
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
        a_Bust += 6.0;
        b_Waist += 4.0;
        c_Neck += 2.0;
        f_ArmholeDepth += 3.0;
        i_AcrossBack += 2.0;
        j_AcrossFront += 1.0;
        m_WidthArmhole += 1.5;
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
