package beazleybond;

import jblockenums.EPattern;
import jblockenums.EPosition;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

/**
 * Class to construct a fitted bodice using the Beazley and Bond drafting method.
 */
public class BodicePattern
        extends Pattern
{
    public BodicePattern(String userName, InputFileData dataStore)
    {
        super(userName, dataStore);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.BEAZLEYBOND_BODICE;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("a_Bust", "A01"));
        measurements.addMeasurement(new Measurement("b_Waist", "A02"));
        measurements.addMeasurement(new Measurement("c_Neck", "A05"));
        measurements.addMeasurement(new Measurement("e_NapeToWaist", "A04"));
        measurements.addMeasurement(new Measurement("f_ArmholeDepth", "A06"));
        measurements.addMeasurement(new Measurement("g_FrNeckToBust", "A07"));
        measurements.addMeasurement(new Measurement("h_FrNeckToWaist", "A08"));
        measurements.addMeasurement(new Measurement("i_AcrossBack", "A09"));
        measurements.addMeasurement(new Measurement("j_AcrossFront", "A10"));
        measurements.addMeasurement(new Measurement("k_Shoulder", "A11"));
        measurements.addMeasurement(new Measurement("l_WidthBustProm", "A12"));

        // Arbitrary
        measurements.addMeasurement(new Measurement("Arb_WidthArmhole", 10.0));
        measurements.addMeasurement(new Measurement("Arb_BackWaistDartSuppression", 1.5));
        measurements.addMeasurement(new Measurement("Arb_BackNeckRise", 2.0));
        measurements.addMeasurement(new Measurement("Arb_ShoulderSlant", 22.0));
        measurements.addMeasurement(new Measurement("Arb_FrontShoulderDartWidth", 4.5));
        measurements.addMeasurement(new Measurement("Arb_BackShoulderDartWidth", 1.5));
        measurements.addMeasurement(new Measurement("Arb_BackShoulderLevel", 4.0));
        measurements.addMeasurement(new Measurement("Arb_BackShoulderDartPositionOnArmholeLevel", 9.25));
        measurements.addMeasurement(new Measurement("Arb_BackShoulderDartLength", 8.0));
        measurements.addMeasurement(new Measurement("Arb_BackFrontWaistDartWidth", 4.0));
        measurements.addMeasurement(new Measurement("Arb_SideSeamWaistDartWidth", 3.5));
        measurements.addMeasurement(new Measurement("Arb_FrontWaistDartApexFromBP", 3.0));
        measurements.addMeasurement(new Measurement("Arb_SideWaistLevel", 0.5));

        // Ease
        measurements.addMeasurement(new Measurement("bustEase", 6.0));
        measurements.addMeasurement(new Measurement("waistEase", 4.0));
        measurements.addMeasurement(new Measurement("neckEase", 2.0));
        measurements.addMeasurement(new Measurement("armholeDepthEase", 3.0));
        measurements.addMeasurement(new Measurement("acrossBackEase", 2.0));
        measurements.addMeasurement(new Measurement("acrossFrontEase", 1.0));
        measurements.addMeasurement(new Measurement("armholeWidthEase", 1.5));
    }

    /**
     * The actual block creation process following the drafting method of Beazley and Bond.
     */
    @Override
    protected void createBlocks()
    {
        // Pull from store
        var a_Bust = get("a_Bust") + get("bustEase");
        var b_Waist = get("b_Waist") + get("waistEase");
        var c_Neck = get("c_Neck") + get("neckEase");
        var e_NapeToWaist = get("e_NapeToWaist");
        var f_ArmholeDepth = get("f_ArmholeDepth") + get("armholeDepthEase");
        var g_FrNeckToBust = get("g_FrNeckToBust");
        var h_FrNeckToWaist = get("h_FrNeckToWaist");
        var i_AcrossBack = get("i_AcrossBack") + get("acrossBackEase");
        var j_AcrossFront = get("j_AcrossFront") + get("acrossFrontEase");
        var k_Shoulder = get("k_Shoulder");
        var l_WidthBustProm = get("l_WidthBustProm");

        var Arb_FrontShoulderDartWidth = get("Arb_FrontShoulderDartWidth");
        var Arb_BackShoulderDartWidth = get("Arb_BackShoulderDartWidth");
        var Arb_BackWaistDartSuppression = get("Arb_BackWaistDartSuppression");
        var Arb_ShoulderSlant = get("Arb_ShoulderSlant");
        var Arb_BackShoulderLevel = get("Arb_BackShoulderLevel");
        var Arb_BackNeckRise = get("Arb_BackNeckRise");
        var Arb_BackShoulderDartPositionOnArmholeLevel = get("Arb_BackShoulderDartPositionOnArmholeLevel");
        var Arb_BackShoulderDartLength = get("Arb_BackShoulderDartLength");
        var Arb_FrontWaistDartApexFromBP = get("Arb_FrontWaistDartApexFromBP");
        var Arb_BackFrontWaistDartWidth = get("Arb_BackFrontWaistDartWidth");
        var Arb_SideWaistLevel = get("Arb_SideWaistLevel");
        var Arb_SideSeamWaistDartWidth = get("Arb_SideSeamWaistDartWidth");
        var Arb_WidthArmhole = get("Arb_WidthArmhole") + get("armholeWidthEase");

        var Arb_AcrossBackLevel = f_ArmholeDepth / 2.0;
        var Arb_BackArmholeTouchX = Arb_AcrossBackLevel + 2.0;
        var Arb_SideSeamFromCentreBack = ((a_Bust - get("bustEase")) / 4.0) + 1.5;  // Deducted ease from bust measurement in this case
        var Arb_HalfFrontNeckWidth = (c_Neck / 5.0) - 1.5;
        var Arb_FrontNeckDepth = c_Neck / 5.0;
        var Arb_HalfBackNeckWidth = (c_Neck / 5.0) - 0.5;
        var Arb_FrontShoulderLine = k_Shoulder + Arb_FrontShoulderDartWidth;
        var Arb_BackShoulderLine = k_Shoulder + Arb_BackShoulderDartWidth;
        var Arb_CBtoCF = (a_Bust / 2.0) + Arb_BackWaistDartSuppression;
        var Arb_ArmholeRatio = (Arb_WidthArmhole + get("armholeWidthEase")) / (a_Bust + get("bustEase"));

        if (a_Bust * Arb_ArmholeRatio > Arb_WidthArmhole) Arb_WidthArmhole = a_Bust * Arb_ArmholeRatio;

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
        double backShoulderLineY = Block.triangleGetAdjacentFromSide(Arb_BackShoulderLevel + Arb_BackNeckRise,
                                                                     Arb_BackShoulderLine);
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
        mainBlock.addKeypoint(
                new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust), refTopRight.getY()));

        // 7. Add front neck curve
        mainBlock.addDirectedCurveWithApexTangent(new Vector2D(Arb_HalfFrontNeckWidth, refTopLeft.getY()),
                                                  new Vector2D(0.0, refTopLeft.getY() - Arb_HalfFrontNeckWidth),
                                                  new Vector2D(Arb_HalfFrontNeckWidth,
                                                               refTopLeft.getY() - Arb_HalfFrontNeckWidth),
                                                  2.0,
                                                  new double[]{90.0, 90.0},
                                                  new int[]{-1, 1});

        // 8. Add back neck curve
        mainBlock.addDirectedCurveWithApexTangent(new Vector2D(-Arb_BackNeckRise, Arb_HalfBackNeckWidth),
                                                  refBottomLeft,
                                                  new Vector2D(0.0, Arb_HalfBackNeckWidth),
                                                  1.75,
                                                  new double[]{90.0, 90.0},
                                                  new int[]{-1, -1});

        // 9. Add front shoulder dart
        ArrayList<Vector2D> frontDartPoints =
                mainBlock.addDart(new Vector2D(0.0, refTopLeft.getY() - Arb_HalfFrontNeckWidth),
                                  new Vector2D(frontShoulderLineX,
                                               refTopLeft.getY() - Arb_HalfFrontNeckWidth - frontShoulderLineY),
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
                                       "Bk_Arm");
        mainBlock.addConstructionPoint(new Vector2D(0.0 - 3.0 * Arb_Con, i_AcrossBack / 2.0 + Arb_WidthArmhole),
                                       new Vector2D(f_ArmholeDepth + Arb_Con, i_AcrossBack / 2.0 + Arb_WidthArmhole),
                                       "Ft_Arm");
        mainBlock.addConstructionPoint(new Vector2D(f_ArmholeDepth, 0.0 - Arb_Con),
                                       new Vector2D(f_ArmholeDepth, Arb_CBtoCF + Arb_Con),
                                       "Armhole");


        // 11. Add armhole as four separate curves

        // Get touching point at back (use across back measurement)
        Vector2D touchBack = new Vector2D(Arb_BackArmholeTouchX, i_AcrossBack / 2.0);

        // Get touching point at front
        Vector2D touchFront = new Vector2D(
                frontShoulderLineX + (2.0 / 3.0) * (f_ArmholeDepth - frontShoulderLineX),
                touchBack.getY() + Arb_WidthArmhole);

        // Get start point for first curve
        Vector2D startPt = new Vector2D(frontShoulderLineX,
                                        refTopLeft.getY() - Arb_HalfFrontNeckWidth - frontShoulderLineY);
        Vector2D preStartPt = frontDartPoints.get(2);

        // Compute end point for first curve
        Vector2D endPt = new Vector2D(touchFront);

        // a. Add first curve plus its end point as a keypoint
        Vector2D adjPoint = mainBlock.addDirectedCurve(startPt,
                                                       endPt,
                                                       new Vector2D(startPt.subtract(preStartPt)),
                                                       new Vector2D(1.0, 0.0),
                                                       new double[]{90.0, 0.0});

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
                                                             new double[]{0.0, 0.0},
                                                             new int[]{-1, -1});

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
                                                             new double[]{0.0, 0.0},
                                                             new int[]{-1, 1});

        mainBlock.addKeypointNextTo(endPt, adjPoint, EPosition.AFTER);


        // Update start and end points
        startPt = endPt;
        endPt = new Vector2D(Arb_BackShoulderLevel, Arb_HalfBackNeckWidth + backShoulderLineY);

        // d. Add final part of armhole
        mainBlock.addDirectedCurve(startPt, endPt,
                                   new Vector2D(-1.0, 0.0),
                                   new Vector2D(-Arb_BackNeckRise - endPt.getX(), Arb_HalfBackNeckWidth - endPt.getY()),
                                   new double[]{0.0, 90.0});

        // Compute waist suppression
        double intWaistSuppression = Arb_CBtoCF - (b_Waist / 2.0);  // Can be used in future to compute dart widths

        // Get start, end and apex points of dart
        Vector2D apex = new Vector2D(refBustPoint.getX() + Arb_FrontWaistDartApexFromBP, refBustPoint.getY());
        startPt = new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust),
                               apex.getY() - (Arb_BackFrontWaistDartWidth / 2.0));
        endPt = new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust),
                             apex.getY() + (Arb_BackFrontWaistDartWidth / 2.0));
        Vector2D refPt = new Vector2D(refBustPoint.getX() + (h_FrNeckToWaist - g_FrNeckToBust), refTopRight.getY());

        // 12. Add front waist dart
        ArrayList<Vector2D> dartPts1 = mainBlock.addDart(startPt, endPt, apex, refPt, EPosition.BEFORE);

        // Update values to add the side seam dart
        startPt = new Vector2D(refBottomRight.getX() + Arb_SideWaistLevel,
                               Arb_SideSeamFromCentreBack - (Arb_SideSeamWaistDartWidth / 2.0));
        endPt = new Vector2D(refBottomRight.getX() + Arb_SideWaistLevel,
                             Arb_SideSeamFromCentreBack + (Arb_SideSeamWaistDartWidth / 2.0));

        // Need to shift so point doesn't coincide with an existing keypoint
        apex = new Vector2D(f_ArmholeDepth + Block.tolerance, Arb_SideSeamFromCentreBack);

        // 13. Add side seam dart
        ArrayList<Vector2D> dartPts2 = mainBlock.addDart(startPt, endPt, apex, dartPts1.get(0), EPosition.BEFORE);

        // Update values for back waist dart
        startPt = new Vector2D(refBottomRight.getX() + Arb_SideWaistLevel,
                               midCBArmHolePoint.getY() - (Arb_SideSeamWaistDartWidth / 2.0));
        endPt = new Vector2D(refBottomRight.getX() + Arb_SideWaistLevel,
                             midCBArmHolePoint.getY() + (Arb_SideSeamWaistDartWidth / 2.0));
        apex = midCBArmHolePoint;

        // 14. Add back waist dart
        ArrayList<Vector2D> dartPts3 = mainBlock.addDart(startPt, endPt, apex, dartPts2.get(0), EPosition.BEFORE);


        // Compute the touch point (arbitrarily chosen as the mid point as we cannot enforce curve length yet)
        Vector2D waistTouch = new Vector2D(refBottomRight.getX(),
                                           0.5 * (dartPts3.get(2).getY() + dartPts2.get(0).getY()));
        mainBlock.addKeypointNextTo(waistTouch, dartPts3.get(2), EPosition.AFTER);

        // 15. Add waist line curve back to side seam
        mainBlock.addDirectedCurve(dartPts3.get(2), waistTouch, new double[]{90.0, 0.0});
        mainBlock.addDirectedCurve(waistTouch, dartPts2.get(0), new double[]{0.0, 90.0});

        // 16. Add waist line curve side seam to front
        mainBlock.addDirectedCurve(dartPts2.get(2), dartPts1.get(0), new double[]{90.0, 90.0});

    }

}
