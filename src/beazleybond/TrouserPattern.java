package beazleybond;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.Block;
import jblockmain.Measurements;
import jblockmain.Pattern;
import jblockmain.easeMeasurement;
import mathcontainers.Vector2D;

import java.util.ArrayList;


/**
 * Class to construct a trouser pattern using the Beazley and Bond drafting method.
 */
public class TrouserPattern
        extends Pattern
{
    protected static ArrayList<easeMeasurement> easeMeasurements = new ArrayList<>();
    /* Pattern-specific Measurements */
    // In future will be simply extracted from the Measurements object.
    private double a_Waist = 70.0;
    private double a_WaistBand = 70.0;
    private double b_UpperHip = 90.0;
    private double c_Hip = 96.0;
    private double d_Thigh = 57.0;
    private double e_KneeStraight = 37.0;
    private double e_KneeSlim = 37.0;
    private double f_Ankle = 25.0;
    private double g_UpperHip = 10.0;
    private double h_Hip = 20.0;
    private double i_Crutch = 28.0;
    private double j_Knee = 60.0;
    private double k_OutsideLegToAnkle = 100.0;

    /* Arbitrary Measurements */
    // Some initialised after ease has been applied
    private double l_InsideLegToAnkle = 72.0;
    // Crutch shaping
    private double Arb_CrutchCentreFrontOffset = 0.5;
    private double Arb_FrontCrutchCurveBisect = 2.5;
    private double Arb_BackCrutchCurveBisect = 3.0;
    // Waist Shaping
    private double Arb_FrontDartSuppression = 4.0;
    private double Arb_FrontDartLength = 10.0;
    private double Arb_FrontDartWidth = Arb_FrontDartSuppression / 2.0;
    private double Arb_BackDartSuppression = 5.0;
    private double Arb_BackDartWidth = Arb_BackDartSuppression / 2.0;
    private double Arb_BackDartLengthShort = 13.0;
    private double Arb_BackDartLengthLong = 15.0;
    // Width of starting rectangle
    private double Arb_FrontCrutchFork;
    private double Arb_BackCrutchFork;
    private double Arb_FrontWidthOfBlock;
    private double Arb_BackWidthOfBlock;
    // Centre front and back lines
    private double Arb_CentreFrontFromInsideLeg;
    private double Arb_CentreBackFromInsideLeg;
    // Trouser crease line
    private double Arb_FrontCreaseLineFromInsideLeg;
    private double Arb_BackCreaseLineFromInsideLeg;
    // Knee
    private double Arb_FrontHalfKneeWidth;
    private double Arb_BackHalfKneeWidth;
    // Arb Measurement for construction lines
    private double Arb_UpperHipLevel;
    private double Arb_HipLevel;
    private double Arb_CrutchLevel;
    private double Arb_Knee;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public TrouserPattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Initialise dependent quantities (arbitrary measurements)
        Arb_FrontCrutchFork = (c_Hip / 20.0) + 0.5;
        Arb_BackCrutchFork = (c_Hip / 10.0) + 1.5;
        Arb_FrontWidthOfBlock = (c_Hip / 4.0) - 1.0 + Arb_FrontCrutchFork;
        Arb_BackWidthOfBlock = (c_Hip / 4.0) + 1.0 + Arb_BackCrutchFork;
        Arb_CentreFrontFromInsideLeg = Arb_FrontWidthOfBlock - ((c_Hip / 4.0) - 1.0);
        Arb_CentreBackFromInsideLeg = Arb_BackWidthOfBlock - ((c_Hip / 4.0) + 1.0);
        Arb_FrontCreaseLineFromInsideLeg = Arb_CentreFrontFromInsideLeg + (c_Hip / 10.0);
        Arb_BackCreaseLineFromInsideLeg = Arb_CentreBackFromInsideLeg + (c_Hip / 10.0) - 1.0;
        Arb_FrontHalfKneeWidth = (e_KneeStraight / 4.0) - 1.0;
        Arb_BackHalfKneeWidth = (e_KneeStraight / 4.0) + 1.0;

        // Arb Measurement for construction lines
        Arb_UpperHipLevel = 10.0;
        Arb_HipLevel = 20.0;
        Arb_CrutchLevel = 29.0;
        Arb_Knee = 60.0;

        // Create the blocks
        createBlocks();
    }

    public static void populateEaseMeasurements()
    {
        // Check to see it hasn't already been populated / it is empty
        if (easeMeasurements.size() > 0)
        {
            return;
        }
        easeMeasurements.add(new easeMeasurement("Waist Ease", 4.0));
        easeMeasurements.add(new easeMeasurement("Waist Band Ease", 2.0));
        easeMeasurements.add(new easeMeasurement("Upper Hip Ease", 4.0));
        easeMeasurements.add(new easeMeasurement("Hip Ease", 4.0));
        easeMeasurements.add(new easeMeasurement("Thigh Ease", 10.0));
        easeMeasurements.add(new easeMeasurement("Straight Knee Ease", 15.0));
        easeMeasurements.add(new easeMeasurement("Slim Knee Ease", 9.0));
        easeMeasurements.add(new easeMeasurement("Ankle Ease", 9.0));
        easeMeasurements.add(new easeMeasurement("Crutch Ease", 1.0));
        easeMeasurements.add(new easeMeasurement("Inside Leg To Ankle Ease", 1.0));
    }

    public static ArrayList<easeMeasurement> getEaseMeasurement()
    {
        return easeMeasurements;
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
        return EGarment.TROUSER;
    }

    @Override
    protected void addEasement() throws IndexOutOfBoundsException
    {
        // Size 12 for now
        a_Waist += easeMeasurements.get(0).getValue();
        a_WaistBand += easeMeasurements.get(1).getValue();
        b_UpperHip += easeMeasurements.get(2).getValue();
        c_Hip += easeMeasurements.get(3).getValue();
        d_Thigh += easeMeasurements.get(4).getValue();
        e_KneeStraight += easeMeasurements.get(5).getValue();
        e_KneeSlim += easeMeasurements.get(6).getValue();
        f_Ankle += easeMeasurements.get(7).getValue();
        i_Crutch += easeMeasurements.get(8).getValue();
        l_InsideLegToAnkle -= easeMeasurements.get(8).getValue();
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {
            // Get measurements from the scan data store
            a_Waist = dataStore.getMeasurement("A02").value;
            b_UpperHip = dataStore.getMeasurement("A13").value;
            c_Hip = dataStore.getMeasurement("A03").value;
            d_Thigh = dataStore.getMeasurement("A17").value;
            e_KneeStraight = dataStore.getMeasurement("A18").value;
            f_Ankle = dataStore.getMeasurement("A19").value;
            h_Hip = dataStore.getMeasurement("A15").value;
            i_Crutch = dataStore.getMeasurement("A20").value;
            j_Knee = dataStore.getMeasurement("A14").value;
            k_OutsideLegToAnkle = dataStore.getMeasurement("A21").value;
            l_InsideLegToAnkle = dataStore.getMeasurement("A22").value;

            // Get name
            userName = dataStore.getName();

            return true;
        }
        catch (MeasurementNotFoundException e)
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
        frontBlock.addConstructionPoint(new Vector2D((g_UpperHip), 0.0 - Arb_Con),
                                        new Vector2D((g_UpperHip), c_Hip / 4 - 1.0 + Arb_FrontCrutchFork + Arb_Con),
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
        backBlock.addConstructionPoint(new Vector2D((g_UpperHip), 0.0 - Arb_Con),
                                       new Vector2D((g_UpperHip), c_Hip / 4 + 1.0 + Arb_BackCrutchFork + Arb_Con),
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
