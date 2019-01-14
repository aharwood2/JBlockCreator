package beazleybond;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockenums.EPosition;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

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
    
    /* Ease */
    private final double a_Bust_Ease = 6.0;
    private final double m_WidthArmhole_Ease = 1.5;

    // Width of armhole ease


    /* Arbitrary Measurements */
    
    // Use default (size 12) values of m_WidthArmhole and a_Bust to compute a ratio
    private final double Arb_ArmholeRatio = (m_WidthArmhole + m_WidthArmhole_Ease) / (a_Bust + a_Bust_Ease);

    // This relates to the height of the basic rectangle which includes this amount for suppression of back waist dart
    // and the side seam.
    private double Arb_BackWaistDartSuppression;

    // Level corresponding to the across back measurement is chosen as halfway between the armhole level and the neck
    private double Arb_AcrossBackLevel;

    // Setting of side seam position from the centre back (CB) at base of rectangle plus arbitrary 1.5cm
    private double Arb_SideSeamFromCentreBack;

    // Neck width and depth derived from the neck measurement
    private double Arb_HalfFrontNeckWidth;
    private double Arb_FrontNeckDepth;
    private double Arb_HalfBackNeckWidth;
    private double Arb_BackNeckRise;

    // Shoulder Level parameters. Shoulder slant measured in degrees.
    private double Arb_ShoulderSlant;
    private double Arb_FrontShoulderDartWidth;
    private double Arb_FrontShoulderLine;
    private double Arb_BackShoulderDartWidth;
    private double Arb_BackShoulderLine;
    private double Arb_BackShoulderLevel;
    private double Arb_BackShoulderDartPositionOnArmholeLevel;
    private double Arb_BackShoulderDartLength;

    // Waist suppression
    private double Arb_CBtoCF;
    private double Arb_BackFrontWaistDartWidth;
    private double Arb_SideSeamWaistDartWidth;
    private double Arb_FrontWaistDartApexFromBP;
    private double Arb_SideWaistLevel;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BodicePattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();
        
        // Rule for armhole width (applied after ease)
        if (a_Bust * Arb_ArmholeRatio > m_WidthArmhole) m_WidthArmhole = a_Bust * Arb_ArmholeRatio;

        // Populate arbitrary measurements
        /* Arbitrary Measurements */

        Arb_BackWaistDartSuppression = 1.5;
        Arb_AcrossBackLevel = f_ArmholeDepth / 2.0;
        Arb_SideSeamFromCentreBack = ((a_Bust - a_Bust_Ease) / 4.0) + 1.5;  // Deducted ease from bust measurement in this case
        Arb_HalfFrontNeckWidth = (c_Neck / 5.0) - 1.5;
        Arb_FrontNeckDepth = c_Neck / 5.0;
        Arb_HalfBackNeckWidth = (c_Neck / 5.0) - 0.5;
        Arb_BackNeckRise = 2.0;
        Arb_ShoulderSlant = 22.0;
        Arb_FrontShoulderDartWidth = 4.5;
        Arb_FrontShoulderLine = k_Shoulder + Arb_FrontShoulderDartWidth;
        Arb_BackShoulderDartWidth = 1.5;
        Arb_BackShoulderLine = k_Shoulder + Arb_BackShoulderDartWidth;
        Arb_BackShoulderLevel = 4.0;
        Arb_BackShoulderDartPositionOnArmholeLevel = 9.25;
        Arb_BackShoulderDartLength = 8.0;
        Arb_CBtoCF = (a_Bust / 2.0) + Arb_BackWaistDartSuppression;
        Arb_BackFrontWaistDartWidth = 4.0;
        Arb_SideSeamWaistDartWidth = 3.5;
        Arb_FrontWaistDartApexFromBP = 3.0;
        Arb_SideWaistLevel = 0.5;

        // Create blocks
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected EMethod assignMethod()
    {
        return EMethod.BEAZLEYBOND;
    }

    @Override
    protected EGarment assignGarment()
    {
        return EGarment.BODICE;
    }

    @Override
    protected void addEasement()
    {
        // Size 12 for now
        a_Bust += a_Bust_Ease;
        b_Waist += 4.0;
        c_Neck += 2.0;
        f_ArmholeDepth += 3.0;
        i_AcrossBack += 2.0;
        j_AcrossFront += 1.0;
        m_WidthArmhole += m_WidthArmhole_Ease;
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {        
            // Get measurements from the scan data store
            a_Bust = dataStore.getMeasurement("A01").value;
            b_Waist = dataStore.getMeasurement("A02").value;
            c_Neck = dataStore.getMeasurement("A05").value;
            e_NapeToWaist = dataStore.getMeasurement("A04").value;
            f_ArmholeDepth = dataStore.getMeasurement("A06").value;
            g_FrNeckToBust = dataStore.getMeasurement("A07").value;
            h_FrNeckToWaist = dataStore.getMeasurement("A08").value;
            i_AcrossBack = dataStore.getMeasurement("A09").value;
            j_AcrossFront = dataStore.getMeasurement("A10").value;
            k_Shoulder = dataStore.getMeasurement("A11").value;
            l_WidthBustProm = dataStore.getMeasurement("A12").value;

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
     * The actual block creation process following the drafting method of Beazley and Bond.
     */
    @Override
    protected void createBlocks()
    {
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity
        // for plotting. The bottom left corner of the space to be the origin.

        // Create a block
        Block mainBlock = new Block(userName + "_BB_Bodice_Main_Block");
        blocks.add(mainBlock);

        // Add basic rectangle points for reference
        Vector2D refBottomLeft = new Vector2D(0.0, 0.0);             // Bottom left (nape and CB)
        Vector2D refBottomRight = new Vector2D(e_NapeToWaist, 0.0);    // Bottom right (waist and CB)
        Vector2D refTopRight = new Vector2D(e_NapeToWaist, Arb_CBtoCF);   // Top right (waist and CF)
        Vector2D refTopLeft = new Vector2D(0.0, Arb_CBtoCF);           // Top left (nape and CF)

        // 1. Add keypoints for start and end of the front neck curve
        mainBlock.addKeypoint(new Vector2D(Arb_HalfFrontNeckWidth, refTopLeft.getY()));
        mainBlock.addKeypoint(new Vector2D(0.0, refTopLeft.getY() - Arb_HalfFrontNeckWidth));

        // 2. Add the front shoulder line end point
        double frontShoulderLineX = Block.triangleGetOppositeFromAngle(Arb_FrontShoulderLine, Arb_ShoulderSlant);
        double frontShoulderLineY = Block.triangleGetAdjacentFromAngle(Arb_FrontShoulderLine, Arb_ShoulderSlant);
        mainBlock.addKeypoint(
                new Vector2D(frontShoulderLineX,
                             refTopLeft.getY() - Arb_HalfFrontNeckWidth - frontShoulderLineY)
        );

        // 3. Add keypoint for the back shoulder line start point
        double backShoulderLineY = Block.triangleGetAdjacentFromSide(Arb_BackShoulderLevel + Arb_BackNeckRise, Arb_BackShoulderLine);
        mainBlock.addKeypoint(new Vector2D(Arb_BackShoulderLevel, Arb_HalfBackNeckWidth + backShoulderLineY));

        // 4. Add keypoints for back neck rise
        mainBlock.addKeypoint(new Vector2D(-Arb_BackNeckRise, Arb_HalfBackNeckWidth));
        mainBlock.addKeypoint(refBottomLeft);

        // Compute the bust point (BP)
        double BPTriangleY = (l_WidthBustProm / 2.0) - Arb_HalfFrontNeckWidth;
        double BPTriangleX = Block.triangleGetAdjacentFromSide(BPTriangleY, g_FrNeckToBust);
        Vector2D refBustPoint = new Vector2D(BPTriangleX, refTopLeft.getY() - (l_WidthBustProm / 2.0));

        // 5. Add the bottom right point (waist and CB)
        mainBlock.addKeypoint(refBottomRight);

        // 6. Compute the new top left point (knowing that measurement h is made up of two parts)
        mainBlock.addKeypoint(new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust), refTopRight.getY()));

        // 7. Add front neck curve
        mainBlock.addDirectedCurveWithApexTangent(new Vector2D(Arb_HalfFrontNeckWidth, refTopLeft.getY()),
                                                  new Vector2D(0.0, refTopLeft.getY() - Arb_HalfFrontNeckWidth),
                                                  new Vector2D(Arb_HalfFrontNeckWidth, refTopLeft.getY() - Arb_HalfFrontNeckWidth),
                                                  2.0,
                                                  new double[] {90.0, 90.0},
                                                  new int[] {-1, 1});

        // 8. Add back neck curve
        mainBlock.addDirectedCurveWithApexTangent(new Vector2D(-Arb_BackNeckRise, Arb_HalfBackNeckWidth),
                                                  refBottomLeft,
                                                  new Vector2D(0.0, Arb_HalfBackNeckWidth),
                                                  1.75,
                                                  new double[] {90.0, 90.0},
                                                  new int[] {-1, -1});

        // 9. Add front shoulder dart
        mainBlock.addDart(new Vector2D(0.0, refTopLeft.getY() - Arb_HalfFrontNeckWidth),
                          new Vector2D(frontShoulderLineX, refTopLeft.getY() - Arb_HalfFrontNeckWidth - frontShoulderLineY),
                          0.5,
                          Arb_FrontShoulderDartWidth,
                          refBustPoint,
                          true);

        // 10. Add back shoulder dart computing the apex point manually
        // Use similar triangles with the backShoulderTriangle constructed earlier
        Vector2D midCBArmHolePoint = new Vector2D(f_ArmholeDepth, Arb_BackShoulderDartPositionOnArmholeLevel);
        double midBackShoulderLineTriangleY = backShoulderLineY / 2.0;
        double midBackShoulderLineTriangleX = (Arb_BackShoulderLevel + Arb_BackNeckRise) / 2.0;
        Vector2D midBackShoulderLine =
                new Vector2D(midBackShoulderLineTriangleX - Arb_BackNeckRise,
                             midBackShoulderLineTriangleY + Arb_HalfBackNeckWidth);
        Vector2D dirDart = new Vector2D(midCBArmHolePoint.subtract(midBackShoulderLine));
        Vector2D backShoulderDartApex = new Vector2D(
                midBackShoulderLine.add(
                        dirDart.multiply(Arb_BackShoulderDartLength / dirDart.norm())
                )
        );
        mainBlock.addDart(new Vector2D(Arb_BackShoulderLevel, Arb_HalfBackNeckWidth + backShoulderLineY),
                          new Vector2D(-Arb_BackNeckRise, Arb_HalfBackNeckWidth),
                          0.5,
                          Arb_BackShoulderDartWidth,
                          backShoulderDartApex,
                          true);

        // Construction lines for armhole
        mainBlock.addConstructionPoint(new Vector2D(0.0 - 3.0 * Arb_Con, i_AcrossBack / 2.0),
                                       new Vector2D(f_ArmholeDepth + Arb_Con, i_AcrossBack / 2.0),
                                       "Bk_Arm" );
        mainBlock.addConstructionPoint(new Vector2D(0.0 - 3.0 * Arb_Con, i_AcrossBack / 2.0 + m_WidthArmhole),
                                       new Vector2D(f_ArmholeDepth + Arb_Con, i_AcrossBack / 2.0 + m_WidthArmhole),
                                       "Ft_Arm" );
        mainBlock.addConstructionPoint(new Vector2D(f_ArmholeDepth, 0.0 - Arb_Con),
                                       new Vector2D(f_ArmholeDepth, Arb_CBtoCF + Arb_Con),
                                       "Armhole" );


        // 11. Add armhole as four separate curves

        // Get touching point at back (use across back measurement)
        Vector2D touchBack = new Vector2D(Arb_AcrossBackLevel, i_AcrossBack / 2.0);

        // Get touching point at front first from touchBack
        Vector2D touchFront = new Vector2D(touchBack.add(new Vector2D(0.0, m_WidthArmhole)));

        // Get start point for first curve
        Vector2D startPt = new Vector2D(frontShoulderLineX,
                                        refTopLeft.getY() - Arb_HalfFrontNeckWidth - frontShoulderLineY);
        Vector2D preStartPt = new Vector2D(0.0, refTopLeft.getY() - Arb_HalfFrontNeckWidth);

        // Compute end point for first curve
        Vector2D endPt = new Vector2D((2.0 / 3.0) * (f_ArmholeDepth - frontShoulderLineX),
                                      touchFront.getY());

        // a. Add first curve plus its end point as a keypoint
        Vector2D adjPoint = mainBlock.addDirectedCurve(startPt,
                                                       endPt,
                                                       new Vector2D(startPt.subtract(preStartPt)),
                                                       new Vector2D(1.0, 0.0),
                                                       new double[] {90.0, 0.0});

        mainBlock.addKeypointNextTo(endPt, adjPoint, EPosition.AFTER);

        // Set start and end points for second part of curve
        startPt = endPt;
        endPt = new Vector2D(f_ArmholeDepth, Arb_SideSeamFromCentreBack);

        // Need to specify directions
        Vector2D dirStart = new Vector2D(1.0, 0.0);
        Vector2D dirEnd = new Vector2D(0.0, -1.0);

        // b. Add second part of curve and point after
        adjPoint = mainBlock.addDirectedCurveWithApexTangent(startPt, endPt,
                                                             dirStart, dirEnd,
                                                             new Vector2D(f_ArmholeDepth, touchFront.getY()),
                                                             2.5,
                                                             new double[] {0.0, 0.0},
                                                             new int[] {-1, -1});

        mainBlock.addKeypointNextTo(endPt, adjPoint, EPosition.AFTER);

        // Update start and end points again
        startPt = endPt;
        endPt = touchBack;

        // Update directions
        dirStart = dirEnd;
        dirEnd = new Vector2D(-1.0, 0.0);

        // c. Add third part of curve
        adjPoint = mainBlock.addDirectedCurveWithApexTangent(startPt, endPt,
                                                             dirStart, dirEnd,
                                                             new Vector2D(f_ArmholeDepth, touchBack.getY()),
                                                             3.0,
                                                             new double[] {0.0, 0.0},
                                                             new int[] {-1, 1});

        mainBlock.addKeypointNextTo(endPt, adjPoint, EPosition.AFTER);


        // Update start and end points
        startPt = endPt;
        endPt = new Vector2D(Arb_BackShoulderLevel, Arb_HalfBackNeckWidth + backShoulderLineY);

        // d. Add final part of armhole
        mainBlock.addDirectedCurve(startPt, endPt,
                                   new Vector2D(-1.0, 0.0),
                                   new Vector2D(-Arb_BackNeckRise - endPt.getX(), Arb_HalfBackNeckWidth - endPt.getY()),
                                   new double[] {0.0, 90.0});

        // Compute waist suppression
        double intWaistSuppression = Arb_CBtoCF - (b_Waist / 2.0);  // Can be used in future to compute dart widths

        // Get start, end and apex points of dart
        Vector2D apex = new Vector2D(refBustPoint.getX() + Arb_FrontWaistDartApexFromBP, refBustPoint.getY());
        startPt = new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust), apex.getY() - (Arb_BackFrontWaistDartWidth / 2.0));
        endPt = new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust), apex.getY() + (Arb_BackFrontWaistDartWidth / 2.0));
        Vector2D refPt = new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust), refTopRight.getY());

        // 12. Add front waist dart
        ArrayList<Vector2D> dartPts1 = mainBlock.addDart(startPt, endPt, apex, refPt, EPosition.BEFORE);

        // Update values to add the side seam dart
        startPt = new Vector2D(refBottomRight.getX() + Arb_SideWaistLevel,
                               Arb_SideSeamFromCentreBack - (Arb_SideSeamWaistDartWidth / 2.0));
        endPt = new Vector2D(refBottomRight.getX() + Arb_SideWaistLevel,
                             Arb_SideSeamFromCentreBack + (Arb_SideSeamWaistDartWidth / 2.0));

        // Need to shift so point doesn't coincide with an existing keypoint
        apex = new Vector2D(f_ArmholeDepth + JBlockCreator.tol, Arb_SideSeamFromCentreBack);

        // 13. Add side seam dart
        ArrayList<Vector2D> dartPts2 = mainBlock.addDart(startPt, endPt, apex, dartPts1.get(0),EPosition.BEFORE);

        // Update values for back waist dart
        startPt = new Vector2D(refBottomRight.getX() + Arb_SideWaistLevel,
                               midCBArmHolePoint.getY() - (Arb_SideSeamWaistDartWidth / 2.0));
        endPt = new Vector2D(refBottomRight.getX() + Arb_SideWaistLevel,
                             midCBArmHolePoint.getY() + (Arb_SideSeamWaistDartWidth / 2.0));
        apex = midCBArmHolePoint;

        // 14. Add back waist dart
        ArrayList<Vector2D> dartPts3 = mainBlock.addDart(startPt, endPt, apex, dartPts2.get(0),EPosition.BEFORE);


        // Compute the touch point (arbitrarily chosen as the mid point as we cannot enforce curve length yet)
        Vector2D waistTouch = new Vector2D(refBottomRight.getX(), 0.5 * (dartPts3.get(2).getY() + dartPts2.get(0).getY()));
        mainBlock.addKeypointNextTo(waistTouch, dartPts3.get(2), EPosition.AFTER);

        // 15. Add waist line curve back to side seam
        mainBlock.addDirectedCurve(dartPts3.get(2), waistTouch, new double[] {90.0, 0.0});
        mainBlock.addDirectedCurve(waistTouch, dartPts2.get(0), new double[] {0.0, 90.0});

        // 16. Add waist line curve side seam to front
        mainBlock.addDirectedCurve(dartPts2.get(2), dartPts1.get(0), new double[] {90.0, 90.0});

    }



}
