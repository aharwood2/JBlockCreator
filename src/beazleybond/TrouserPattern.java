package beazleybond;

import jblockmain.Block;
import jblockmain.EPosition;
import jblockmain.Measurements;
import jblockmain.Pattern;
import mathcontainers.Vector2D;


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
    // Some initialised after ease has been applied

    // Crutch shaping
    private double Arb_CrutchCentreFrontOffset = 0.5;
    private double Arb_CrutchCurveBisect = 2.5;

    // Waist Shaping
    private double Arb_DartSuppression = 4.0;
    private double Arb_DartLength = 10.0;
    private double Arb_DartWidth = 2.0;


    // Width of starting rectangle (quarter hip - 1cm + front crutch fork)
    private double Arb_FrontCrutchFork;
    private double Arb_WidthOfBlock;

    // Centre front line
    private double Arb_CentreFrontFromInsideLeg;

    // Trouser crease line
    private double Arb_CreaseLineFromInsideLeg;

    // Knee
    private double Arb_HalfKneeWidth;




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public TrouserPattern(Measurements dataStore)
    {
        readMeasurements(dataStore);
        addEasement();

        // Initialise dependent quantities
        Arb_FrontCrutchFork = (c_Hip / 20.0) + 0.5;
        Arb_WidthOfBlock = (c_Hip / 4.0) - 1.0 + Arb_FrontCrutchFork;
        Arb_CentreFrontFromInsideLeg = Arb_WidthOfBlock - ((c_Hip / 4.0) - 1.0);
        Arb_CreaseLineFromInsideLeg = Arb_CentreFrontFromInsideLeg + (c_Hip / 10.0);
        Arb_HalfKneeWidth = (e_KneeStraight / 4.0) - 1.0;

        // Create the blocks
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

        // Create front block first
        blocks.add(new Block("Trouser_Front_Block"));
        Block frontBlock = blocks.get(blocks.size() - 1);

        // Start keypoint placement from bottom left
        frontBlock.addKeypoint(new Vector2D(0.0,Arb_CentreFrontFromInsideLeg + Arb_CrutchCentreFrontOffset));

        // Next keypoint is hip level
        frontBlock.addKeypoint(new Vector2D(h_Hip, Arb_CentreFrontFromInsideLeg));

        // Add keypoint at inside leg and crutch
        frontBlock.addKeypoint(new Vector2D(i_Crutch, 0.0));

        // Add keypoint at inside leg and knee
        frontBlock.addKeypoint(new Vector2D(j_Knee, Arb_CreaseLineFromInsideLeg - Arb_HalfKneeWidth));

        // Add keypoint at inside leg and ankle intersection
        frontBlock.addKeypoint(new Vector2D(k_OutsideLegToAnkle, Arb_CreaseLineFromInsideLeg - Arb_HalfKneeWidth));

        // Add keypoint at outside leg and ankle intersection
        frontBlock.addKeypoint(new Vector2D(k_OutsideLegToAnkle, Arb_CreaseLineFromInsideLeg + Arb_HalfKneeWidth));

        // Add keypoint at outside leg and knee
        frontBlock.addKeypoint(new Vector2D(j_Knee, Arb_CreaseLineFromInsideLeg + Arb_HalfKneeWidth));

        // Add keypoint at outside leg at the crutch level
        frontBlock.addKeypoint(new Vector2D(i_Crutch, Arb_WidthOfBlock));

        // Add keypoint at outside leg at the hip level
        frontBlock.addKeypoint(new Vector2D(h_Hip, Arb_WidthOfBlock));

        // Add keypoint at outside leg and upper hip level
        frontBlock.addKeypoint(new Vector2D(g_UpperHip, Arb_CentreFrontFromInsideLeg + (b_UpperHip / 4.0)));

        // Add keypoint at outside leg and waist level
        frontBlock.addKeypoint(new Vector2D(0.0, Arb_CentreFrontFromInsideLeg + (a_Waist / 4.0) + Arb_DartSuppression));

        // Insert the inside leg curve
        // TODO: Add this bit...

        // Insert crutch curve
        frontBlock.addDirectedCurveWithApexTangent(
                new Vector2D(h_Hip, Arb_CentreFrontFromInsideLeg),
                new Vector2D(i_Crutch, 0.0),
                new Vector2D(i_Crutch, Arb_CentreFrontFromInsideLeg),
                Arb_CrutchCurveBisect,
                new double[] {0.0, 90.0}
        );


        // Insert darts
        // TODO: Add this bit...


        // Back block...
        // TODO: Add this bit...

    }



}
