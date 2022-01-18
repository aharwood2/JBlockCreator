package beazleybond;

import jblockenums.EPattern;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;


/**
 * Class to construct a trouser pattern using the Beazley and Bond drafting method.
 */
public class TrouserPattern
        extends Pattern
{
    public TrouserPattern(String userName, InputFileData dataStore)
    {
        super(userName, dataStore);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.BEAZLEYBOND_TROUSER;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("a_Waist", "A02"));
        measurements.addMeasurement(new Measurement("b_UpperHip", "A13"));
        measurements.addMeasurement(new Measurement("c_Hip", "A03"));
        measurements.addMeasurement(new Measurement("d_Thigh", "A17"));
        measurements.addMeasurement(new Measurement("e_KneeStraight", "A18"));
        measurements.addMeasurement(new Measurement("f_Ankle", "A19"));
        measurements.addMeasurement(new Measurement("h_Hip", "A15"));
        measurements.addMeasurement(new Measurement("i_Crutch", "A20"));
        measurements.addMeasurement(new Measurement("j_Knee", "A14"));
        measurements.addMeasurement(new Measurement("k_OutsideLegToAnkle", "A21"));
        measurements.addMeasurement(new Measurement("l_InsideLegToAnkle", "A22"));

        // Arbitrary
        measurements.addMeasurement(new Measurement("Arb_UpperHipLevel", 10.0));
        measurements.addMeasurement(new Measurement("Arb_UpperHip", 10.0));
        measurements.addMeasurement(new Measurement("Arb_CrutchCentreFrontOffset", 0.5));
        measurements.addMeasurement(new Measurement("Arb_FrontCrutchCurveBisect", 2.5));
        measurements.addMeasurement(new Measurement("Arb_BackCrutchCurveBisect", 3.0));
        measurements.addMeasurement(new Measurement("Arb_FrontDartSuppression", 4.0));
        measurements.addMeasurement(new Measurement("Arb_FrontDartLength", 10.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartSuppression", 5.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartLengthShort", 13.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartLengthLong", 15.0));

        // Ease
        measurements.addMeasurement(new Measurement("WaistEase", 4.0));
        measurements.addMeasurement(new Measurement("HipEase", 4.0));
        measurements.addMeasurement(new Measurement("StraightKneeEase", 15.0));
        measurements.addMeasurement(new Measurement("CrutchEase", 1.0));
    }

    /**
     * The actual block creation process following the drafting method of Beazley and Bond.
     */
    @Override
    protected void createBlocks()
    {
        // Pull from store
        var a_Waist = get("a_Waist") + get("WaistEase");
        var b_UpperHip = get("b_UpperHip");
        var c_Hip = get("c_Hip") + get("HipEase");
        var d_Thigh = get("d_Thigh");
        var e_KneeStraight = get("e_KneeStraight") + get("StraightKneeEase");
        var f_Ankle = get("f_Ankle");
        var h_Hip = get("h_Hip");
        var i_Crutch = get("i_Crutch") + get("CrutchEase");
        var j_Knee = get("j_Knee");
        var k_OutsideLegToAnkle = get("k_OutsideLegToAnkle");
        var l_InsideLegToAnkle = get("l_InsideLegToAnkle");

        // Initialise driven arbitrary values
        var Arb_FrontCrutchFork = (c_Hip / 20.0) + 0.5;
        var Arb_BackCrutchFork = (c_Hip / 10.0) + 1.5;
        var Arb_FrontWidthOfBlock = (c_Hip / 4.0) - 1.0 + Arb_FrontCrutchFork;
        var Arb_BackWidthOfBlock = (c_Hip / 4.0) + 1.0 + Arb_BackCrutchFork;
        var Arb_CentreFrontFromInsideLeg = Arb_FrontWidthOfBlock - ((c_Hip / 4.0) - 1.0);
        var Arb_CentreBackFromInsideLeg = Arb_BackWidthOfBlock - ((c_Hip / 4.0) + 1.0);
        var Arb_FrontCreaseLineFromInsideLeg = Arb_CentreFrontFromInsideLeg + (c_Hip / 10.0);
        var Arb_BackCreaseLineFromInsideLeg = Arb_CentreBackFromInsideLeg + (c_Hip / 10.0) - 1.0;
        var Arb_FrontHalfKneeWidth = (e_KneeStraight / 4.0) - 1.0;
        var Arb_BackHalfKneeWidth = (e_KneeStraight / 4.0) + 1.0;
        var Arb_UpperHipLevel = get("Arb_UpperHipLevel");
        var Arb_UpperHip = get("Arb_UpperHip");
        var Arb_CrutchCentreFrontOffset = get("Arb_CrutchCentreFrontOffset");
        var Arb_FrontCrutchCurveBisect = get("Arb_FrontCrutchCurveBisect");
        var Arb_BackCrutchCurveBisect = get("Arb_BackCrutchCurveBisect");
        var Arb_FrontDartSuppression = get("Arb_FrontDartSuppression");
        var Arb_FrontDartLength = get("Arb_FrontDartLength");
        var Arb_BackDartSuppression = get("Arb_BackDartSuppression");
        var Arb_BackDartLengthShort = get("Arb_BackDartLengthShort");
        var Arb_BackDartLengthLong = get("Arb_BackDartLengthLong");
        var Arb_FrontDartWidth = Arb_FrontDartSuppression / 2.0;
        var Arb_BackDartWidth = Arb_BackDartSuppression / 2.0;

        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity
        // for plotting. The bottom left corner of the space to be the origin.

        // Create front block first
        blocks.add(new Block(userName + "_BB_Trousers_Front_Block"));
        Block frontBlock = blocks.get(blocks.size() - 1);

        // Start keypoint placement from bottom left
        frontBlock.addKeypoint(new Vector2D(0.0, Arb_CentreFrontFromInsideLeg + Arb_CrutchCentreFrontOffset));

        // Next keypoint is hip level
        frontBlock.addKeypoint(new Vector2D(h_Hip, Arb_CentreFrontFromInsideLeg));

        // Add keypoint at inside leg and crutch
        frontBlock.addKeypoint(new Vector2D(i_Crutch, 0.0));

        // Add keypoint at inside leg and knee
        frontBlock.addKeypoint(new Vector2D(j_Knee, Arb_FrontCreaseLineFromInsideLeg - Arb_FrontHalfKneeWidth));

        // Add keypoint at inside leg and ankle intersection
        frontBlock.addKeypoint(
                new Vector2D(k_OutsideLegToAnkle, Arb_FrontCreaseLineFromInsideLeg - Arb_FrontHalfKneeWidth));

        // Add keypoint at outside leg and ankle intersection
        frontBlock.addKeypoint(
                new Vector2D(k_OutsideLegToAnkle, Arb_FrontCreaseLineFromInsideLeg + Arb_FrontHalfKneeWidth));

        // Add keypoint at outside leg and knee
        frontBlock.addKeypoint(new Vector2D(j_Knee, Arb_FrontCreaseLineFromInsideLeg + Arb_FrontHalfKneeWidth));

        // Add keypoint at outside leg at the crutch level
        frontBlock.addKeypoint(new Vector2D(i_Crutch, Arb_FrontWidthOfBlock));

        // Add keypoint at outside leg at the hip level
        frontBlock.addKeypoint(new Vector2D(h_Hip, Arb_FrontWidthOfBlock));

        // Add keypoint at outside leg and upper hip level -- changed this feature (see below)
        //frontBlock.addKeypoint(new Vector2D(g_UpperHip, Arb_CentreFrontFromInsideLeg + (b_UpperHip / 4.0)));

        // Add keypoint at outside leg and waist level
        frontBlock.addKeypoint(
                new Vector2D(0.0, Arb_CentreFrontFromInsideLeg + (a_Waist / 4.0) + Arb_FrontDartSuppression));

        // Insert the inside leg curve -- circular curve will do the job rather than something more complicated
        frontBlock.addCircularCurve(new Vector2D(i_Crutch, 0.0),
                                    new Vector2D(j_Knee, Arb_FrontCreaseLineFromInsideLeg - Arb_FrontHalfKneeWidth),
                                    0.5,
                                    false);

        // Insert crutch curve
        frontBlock.addDirectedCurveWithApexTangent(
                new Vector2D(h_Hip, Arb_CentreFrontFromInsideLeg),
                new Vector2D(i_Crutch, 0.0),
                new Vector2D(i_Crutch, Arb_CentreFrontFromInsideLeg),
                Arb_FrontCrutchCurveBisect,
                new double[]{0.0, 90.0},
                new int[]{-1, -1}
        );


        // Insert darts in anti-clockwise order
        Vector2D startSegment = new Vector2D(0.0,
                                             Arb_CentreFrontFromInsideLeg + (a_Waist / 4.0) + Arb_FrontDartSuppression);
        Vector2D endSegment = new Vector2D(0.0, Arb_CentreFrontFromInsideLeg + Arb_CrutchCentreFrontOffset);
        double positionTopDart = (0.5 * (startSegment.getY() - Arb_FrontCreaseLineFromInsideLeg)) /
                (startSegment.getY() - endSegment.getY());
        ArrayList<Vector2D> dartPoints = frontBlock.addDart(startSegment,
                                                            endSegment,
                                                            positionTopDart,
                                                            Arb_FrontDartWidth,
                                                            Arb_FrontDartLength,
                                                            true, false);

        startSegment = dartPoints.get(2);
        double positionBottomDart = (startSegment.getY() - Arb_FrontCreaseLineFromInsideLeg) /
                (startSegment.getY() - endSegment.getY());
        frontBlock.addDart(startSegment,
                           endSegment,
                           positionBottomDart,
                           Arb_FrontDartWidth,
                           Arb_FrontDartLength,
                           true, false);

        // Add a curve instead of of straight line for the hip to waist line -- using a point at the upper hip level
        // positioned midway between the point at the hip and the point at the waist as an intermediate point
        Vector2D frontHipWaistStart = new Vector2D(h_Hip, Arb_FrontWidthOfBlock);
        Vector2D frontHipWaistEnd = new Vector2D(0.0,
                                                 Arb_CentreFrontFromInsideLeg + (a_Waist / 4.0) + Arb_FrontDartSuppression);
        double frontHipWaistY = (frontHipWaistStart.getY() - frontHipWaistEnd.getY()) / 2.0;
        Vector2D frontHipWaistInt = new Vector2D(Arb_UpperHipLevel, frontHipWaistEnd.getY() + frontHipWaistY);
        frontBlock.addDirectedCurve(frontHipWaistStart, frontHipWaistEnd, frontHipWaistInt, 0.0);


        // Add construction keypoints for Upper Hip Level
        frontBlock.addConstructionPoint(new Vector2D((Arb_UpperHip), 0.0 - Arb_Con),
                                        new Vector2D((Arb_UpperHip), c_Hip / 4 - 1.0 + Arb_FrontCrutchFork + Arb_Con),
                                        "Upper Hip");

        // Add construction keypoints for Hip Level
        frontBlock.addConstructionPoint(new Vector2D((h_Hip), 0.0 - Arb_Con),
                                        new Vector2D((h_Hip), c_Hip / 4 - 1.0 + Arb_FrontCrutchFork + Arb_Con),
                                        "Hip");

        // Add construction keypoints for Crutch Level
        frontBlock.addConstructionPoint(new Vector2D((i_Crutch), 0.0 - Arb_Con),
                                        new Vector2D((i_Crutch), c_Hip / 4 - 1.0 + Arb_FrontCrutchFork + Arb_Con),
                                        "Crutch");

        // Add construction keypoints for Knee Level
        frontBlock.addConstructionPoint(new Vector2D((j_Knee), 0.0 - Arb_Con),
                                        new Vector2D((j_Knee), c_Hip / 4 - 1.0 + Arb_FrontCrutchFork + Arb_Con),
                                        "Knee");

        // Back block //

        // Create block
        blocks.add(new Block(userName + "_BB_Trousers_Back_Block"));
        Block backBlock = blocks.get(blocks.size() - 1);

        // Add first keypoint (bottom left)
        backBlock.addKeypoint(new Vector2D(-1.0, Arb_CentreBackFromInsideLeg + 2.0));

        // Add keypoint at inside leg and hip level
        backBlock.addKeypoint(new Vector2D(h_Hip, Arb_CentreBackFromInsideLeg));

        // Add keypoint at inside leg and crutch
        backBlock.addKeypoint(new Vector2D(i_Crutch + 1.0, 0.0));

        // Add keypoint at inside leg and knee level
        backBlock.addKeypoint(new Vector2D(j_Knee, Arb_BackCreaseLineFromInsideLeg - Arb_BackHalfKneeWidth));

        // Add keypoint at inside leg and ankle
        backBlock.addKeypoint(
                new Vector2D(k_OutsideLegToAnkle, Arb_BackCreaseLineFromInsideLeg - Arb_BackHalfKneeWidth));

        // Add keypoint at outside leg and ankle
        backBlock.addKeypoint(
                new Vector2D(k_OutsideLegToAnkle, Arb_BackCreaseLineFromInsideLeg + Arb_BackHalfKneeWidth));

        // Add keypoint at outside leg and knee
        backBlock.addKeypoint(new Vector2D(j_Knee, Arb_BackCreaseLineFromInsideLeg + Arb_BackHalfKneeWidth));

        // Add keypoint at outside leg and crutch
        backBlock.addKeypoint(new Vector2D(i_Crutch, Arb_BackWidthOfBlock));

        // Add keypoint at outside leg and hip
        backBlock.addKeypoint(new Vector2D(h_Hip, Arb_BackWidthOfBlock));

        // Add keypoint at outside leg and waist
        double xOffset = 1.0;
        double yOffset = Block.triangleGetAdjacentFromSide(xOffset, (a_Waist / 4.0) + Arb_BackDartSuppression);
        backBlock.addKeypoint(new Vector2D(0.0, Arb_CentreBackFromInsideLeg + 2.0 + yOffset));

        // Add waist darts
        startSegment = new Vector2D(0.0, Arb_CentreBackFromInsideLeg + 2.0 + yOffset);
        endSegment = new Vector2D(-1.0, Arb_CentreBackFromInsideLeg + 2.0);

        // Assumes the apex of the dart is reference for its position not the base.
        // Need to therefore compute where the base of the dart would be given the angle of the waistline.
        double angleOfWaist = Math.atan(
                (endSegment.getX() - startSegment.getX()) / (endSegment.getY() - startSegment.getY()));
        double apexYPosition = 0.5 * (startSegment.getY() + Arb_BackCreaseLineFromInsideLeg);
        double baseYShiftFromApex = Arb_BackDartLengthShort * Math.sin(angleOfWaist);
        double baseYShiftFromStartSeg = startSegment.getY() - (apexYPosition + baseYShiftFromApex);
        positionTopDart = (baseYShiftFromStartSeg / Math.cos(angleOfWaist)) / (startSegment.subtract(
                endSegment).norm());
        dartPoints.clear();
        dartPoints = backBlock.addDart(startSegment,
                                       endSegment,
                                       positionTopDart,
                                       Arb_BackDartWidth,
                                       Arb_BackDartLengthShort,
                                       true, false);

        // Lower dart -- use same method to get position as previously
        startSegment = dartPoints.get(2);
        apexYPosition = Arb_BackCreaseLineFromInsideLeg;
        baseYShiftFromApex = Arb_BackDartLengthLong * Math.sin(angleOfWaist);
        baseYShiftFromStartSeg = startSegment.getY() - (apexYPosition + baseYShiftFromApex);
        positionBottomDart = (baseYShiftFromStartSeg / Math.cos(angleOfWaist)) / (startSegment.subtract(
                endSegment).norm());
        backBlock.addDart(startSegment,
                          endSegment,
                          positionBottomDart,
                          Arb_BackDartWidth,
                          Arb_BackDartLengthLong,
                          true, false);

        // Add inside leg curve -- again as we don't really have much information try a circular curve for now
        backBlock.addCircularCurve(new Vector2D(i_Crutch + 1.0, 0.0),
                                   new Vector2D(j_Knee, Arb_BackCreaseLineFromInsideLeg - Arb_BackHalfKneeWidth),
                                   1.5,
                                   false);


        // Add crutch curve -- approximate angle between inside leg curve as it looks less than 90 degrees.
        backBlock.addDirectedCurveWithApexTangent(
                new Vector2D(h_Hip, Arb_CentreBackFromInsideLeg),
                new Vector2D(i_Crutch + 1.0, 0.0),
                new Vector2D(i_Crutch, Arb_CentreBackFromInsideLeg),
                Arb_BackCrutchCurveBisect,
                new double[]{0.0, 75.0},
                new int[]{-1, -1}
        );

        // Add a curve instead of of straight line for the hip to waist line
        Vector2D backHipWaistStart = new Vector2D(h_Hip, Arb_BackWidthOfBlock);
        Vector2D backHipWaistEnd = new Vector2D(0.0, Arb_CentreBackFromInsideLeg + 2.0 + yOffset);
        double backHipWaistY = (backHipWaistStart.getY() - backHipWaistEnd.getY()) / 2.0;
        Vector2D backHipWaistInt = new Vector2D(Arb_UpperHipLevel, backHipWaistEnd.getY() + backHipWaistY);
        backBlock.addDirectedCurve(backHipWaistStart, backHipWaistEnd, backHipWaistInt, 0.0);

        // Add construction keypoints for Upper Hip Level
        backBlock.addConstructionPoint(new Vector2D((Arb_UpperHip), 0.0 - Arb_Con),
                                       new Vector2D((Arb_UpperHip), c_Hip / 4 + 1.0 + Arb_BackCrutchFork + Arb_Con),
                                       "Upper Hip");

        // Add construction keypoints for Hip Level
        backBlock.addConstructionPoint(new Vector2D((h_Hip), 0.0 - Arb_Con),
                                       new Vector2D((h_Hip), c_Hip / 4 + 1.0 + Arb_BackCrutchFork + Arb_Con),
                                       "Hip");

        // Add construction keypoints for Crutch Level
        backBlock.addConstructionPoint(new Vector2D((i_Crutch), 0.0 - Arb_Con),
                                       new Vector2D((i_Crutch), c_Hip / 4 + 1.0 + Arb_BackCrutchFork + Arb_Con),
                                       "Crutch");

        // Add construction keypoints for Knee Level
        backBlock.addConstructionPoint(new Vector2D((j_Knee), 0.0 - Arb_Con),
                                       new Vector2D((j_Knee), c_Hip / 4 + 1.0 + Arb_BackCrutchFork + Arb_Con),
                                       "Knee");
    }


}
