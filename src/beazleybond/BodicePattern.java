package beazleybond;

import jblockmain.*;
import mathcontainers.Vector2D;

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
    private double Arb_FrontShoulderDartWidth = 4.5;
    private double Arb_FrontShoulderLine = k_Shoulder + Arb_FrontShoulderDartWidth;
    private double Arb_BackShoulderDartWidth = 1.5;
    private double Arb_BackShoulderLine = k_Shoulder + Arb_BackShoulderDartWidth;
    private double Arb_BackShoulderLevel = 4.0;
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

        // Create a block
        Block mainBlock = new Block("Bodice_Main_Block");
        blocks.add(mainBlock);

        // Add basic rectangle points for reference
        Vector2D refBottomLeft = new Vector2D(0.0, 0.0);             // Bottom left (nape and CB)
        Vector2D refBottomRight = new Vector2D(e_NapeToWaist, 0.0);     // Bottom right (waist and CB)
        Vector2D refTopRight = new Vector2D(e_NapeToWaist,
                                            (a_Bust / 2.0) + Arb_BackWaistDartSuppression);     // Top right (waist and CF)
        Vector2D refTopLeft = new Vector2D(0.0,
                                           (a_Bust / 2.0) + Arb_BackWaistDartSuppression);      // Top left (nape and CF)

        // Add keypoints for start and end of the front neck curve
        mainBlock.addKeypoint(new Vector2D(Arb_HalfFrontNeckWidth, refTopLeft.getY()));
        mainBlock.addKeypoint(new Vector2D(0.0, refTopLeft.getY() - Arb_FrontNeckDepth));

        // Add the front shoulder line end point
        double frontShoulderLineX = Arb_FrontShoulderLine * Math.sin(Math.PI * Arb_ShoulderSlant / 180.0);
        double frontShoulderLineY = Arb_FrontShoulderLine * Math.cos(Math.PI * Arb_ShoulderSlant / 180.0);
        mainBlock.addKeypoint(new Vector2D(frontShoulderLineX,
                                           refTopLeft.getY() - Arb_FrontNeckDepth - frontShoulderLineY)
        );

        // Add keypoint for the back should line start point
        double backShoulderLineY = Block.triangleGetAdjacentSide(Arb_BackShoulderLevel, Arb_BackShoulderLine);
        mainBlock.addKeypoint(new Vector2D(Arb_BackShoulderLevel, Arb_HalfBackNeckWidth + backShoulderLineY));

        // Add keypoints for back neck rise
        mainBlock.addKeypoint(new Vector2D(-Arb_BackNeckRise, Arb_HalfBackNeckWidth));
        mainBlock.addKeypoint(refBottomLeft);

        // Compute the bust point (BP)
        double BPTriangleY = (l_WidthBustProm / 2.0) - Arb_FrontNeckDepth;
        double BPTriangleX = Block.triangleGetAdjacentSide(BPTriangleY, g_FrNeckToBust);
        Vector2D refBustPoint = new Vector2D(BPTriangleX, refTopLeft.getY() - (l_WidthBustProm / 2.0));

        // Add the bottom right point (waist and CB)
        mainBlock.addKeypoint(refBottomRight);

        // Compute the new top left point
        mainBlock.addKeypoint(new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust), refTopRight.getY()));



    }



}
