package beazleybond;

import jblockenums.EPattern;
import jblockenums.EPosition;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.Block;
import jblockmain.Measurements;
import jblockmain.Pattern;
import jblockmain.easeMeasurement;
import mathcontainers.Vector2D;

import java.util.ArrayList;

/**
 * Class to construct a skirt using the Beazley and Bond drafting method.
 */
public class SkirtPattern
        extends Pattern
{
    protected static ArrayList<easeMeasurement> easeMeasurements = new ArrayList<>();
    /* Pattern-specific Measurements */
    private double a_Waist = 70.0;
    private double b_UpperHip = 90.0;
    private double c_Hip = 96.0;
    private double d_CentreBack = 60.0;
    private double e_SideSeam = 61.0;

    /* Arbitrary Measurements */
    // Some of the following can be inferred from body scan information but for now assume that these follow the
    // empirically driven values.
    private double f_CentreFront = 60.0;
    // Ensures the waistline drops by 1cm to allow it to curve round the body. This can be informed from the body scan.
    private double Arb_WaistLevel;
    // Generic assumption that can in future be informed from the body scan.
    private double Arb_UpperHipLevel;
    // Generic assumption that can in future be informed from the body scan.
    private double Arb_HipLevel;
    // Waist suppression process required calculation of a front and back dart by dividing up the circumference of the
    // waist. For now we assume a fixed percentage is assigned to each although this could be adjusted in future.
    private double Arb_BackDartPercent;
    private double Arb_FrontDartPercent;
    private double Arb_SideSeamPercent;
    // Dart length is arbitrary but can be inferred from body scan data.
    private double Arb_BackDartLength;
    private double Arb_FrontDartLength;
    // Dart placement is also arbitrary and is specified as a percentage of quarter waist as measured from the start
    // point of the waist (using strict connectivity order)
    private double Arb_BackDartPlacement;
    private double Arb_FrontDartPlacement;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public SkirtPattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Populate arbitrary measurements
        Arb_WaistLevel = 1.0;
        Arb_UpperHipLevel = 10.0;
        Arb_HipLevel = 20.0;
        Arb_BackDartPercent = 0.35;
        Arb_FrontDartPercent = 0.20;
        Arb_SideSeamPercent = 0.45;
        Arb_BackDartLength = 14.0;
        Arb_FrontDartLength = 8.0;
        Arb_BackDartPlacement = 0.5;
        Arb_FrontDartPlacement = 1.0 / 3.0;

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
        easeMeasurements.add(new easeMeasurement("Waist Ease", 2.0));
        easeMeasurements.add(new easeMeasurement("Upper Hip Ease", 4.0));
        easeMeasurements.add(new easeMeasurement("Hip Ease", 4.0));
        easeMeasurements.add(new easeMeasurement("SideSeam Ease", 6.8));
    }

    public static ArrayList<easeMeasurement> getEaseMeasurement()
    {
        return easeMeasurements;
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.BEAZLEYBOND_SKIRT;
    }

    @Override
    protected void addEasement() throws IndexOutOfBoundsException
    {
        try
        {
            a_Waist += easeMeasurements.get(0).getValue();
            b_UpperHip += easeMeasurements.get(1).getValue();
            c_Hip += easeMeasurements.get(2).getValue();
            e_SideSeam += easeMeasurements.get(3).getValue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Ease array out of bounds");
        }
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {
            // Based on measurements for this pattern we can read the following from the scan:
            a_Waist = dataStore.getMeasurement("A02").value;
            b_UpperHip = dataStore.getMeasurement("A13").value;
            c_Hip = dataStore.getMeasurement("A03").value;
            d_CentreBack = dataStore.getMeasurement("A16").value;
            e_SideSeam = dataStore.getMeasurement("A16").value;
            f_CentreFront = dataStore.getMeasurement("A16").value;

            // Others
            Arb_HipLevel = dataStore.getMeasurement("A15").value;

            // Get name of user
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
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(userName + "_BB_Skirt_Back_Block"));
        Block backBlock = blocks.get(0);

        // Add all the fixed points to the block that coincide with the basic rectangle. These points do not move
        // throughout the drafting process.
        backBlock.addKeypoint(new Vector2D(d_CentreBack, 0.0));
        backBlock.addKeypoint(new Vector2D(d_CentreBack, c_Hip / 4.0));
        backBlock.addKeypoint(new Vector2D(Arb_HipLevel, c_Hip / 4.0));

        // Compute the waistline suppression by finding the difference between the waist measurement and half the hip
        // measurement and then divide by 4 for a quarter distance.
        double Int_WaistSupp = (c_Hip - a_Waist) / 4.0;

        // Add point for waist line drop.
        backBlock.addKeypointNextTo(new Vector2D(Arb_WaistLevel, 0.0),
                                    new Vector2D(d_CentreBack, 0), EPosition.BEFORE);

        // Add point for suppressed side seam at waist.
        // Can be computed using side seam percentage of total suppression required.
        double Int_SuppressedSS = (c_Hip / 4.0) - Arb_SideSeamPercent * Int_WaistSupp;
        backBlock.addKeypointNextTo(new Vector2D(0.0, Int_SuppressedSS),
                                    new Vector2D(Arb_WaistLevel, 0), EPosition.BEFORE);

        // Compute the suppressed Upper Hip Level point.
        // Can be computed from the difference between Hip and Upper Hip
        double Int_SuppressedUpHip = (c_Hip / 4.0) - (c_Hip - b_UpperHip) / 4.0;

        // Add curve between waist point and hip point (rather than upper-hip as stipulated in BB).
        // Assume for now, in the absence of vary form curve that this is a curve defined by a circle.
        backBlock.addCircularCurve(new Vector2D(Arb_HipLevel, c_Hip / 4.0),
                                   new Vector2D(0.0, Int_SuppressedSS), 0.5, true);


        // Add construction keypoints for Upper Hip Level
        backBlock.addConstructionPoint(new Vector2D((Arb_UpperHipLevel), 0.0 - Arb_Con),
                                       new Vector2D((Arb_UpperHipLevel), c_Hip / 4 + Arb_Con),
                                       "Upper Hip");

        // Add construction keypoints for Hip Level
        backBlock.addConstructionPoint(new Vector2D((Arb_HipLevel), 0.0 - Arb_Con),
                                       new Vector2D((Arb_HipLevel), c_Hip / 4 + Arb_Con),
                                       "Hip");

        // Trace off block
        blocks.add(new Block(backBlock, userName + "_BB_Skirt_Front_Block"));
        Block frontBlock = blocks.get(blocks.size() - 1);

        // Add back dart.
        ArrayList<Vector2D> dartEdges = backBlock.addDart(new Vector2D(0.0, Int_SuppressedSS),
                                                          new Vector2D(Arb_WaistLevel, 0.0),
                                                          Arb_BackDartPlacement,
                                                          Arb_BackDartPercent * Int_WaistSupp,
                                                          Arb_BackDartLength, true, false);

        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        backBlock.addRightAngleCurve(new Vector2D(0.0, Int_SuppressedSS), dartEdges.get(0));

        backBlock.addRightAngleCurve(dartEdges.get(2), new Vector2D(Arb_WaistLevel, 0.0));

        // Add front dart
        dartEdges = frontBlock.addDart(new Vector2D(0.0, Int_SuppressedSS),
                                       new Vector2D(Arb_WaistLevel, 0.0),
                                       Arb_FrontDartPlacement,
                                       Arb_FrontDartPercent * Int_WaistSupp,
                                       Arb_FrontDartLength, true, false);

        // Add curves
        frontBlock.addRightAngleCurve(new Vector2D(0.0, Int_SuppressedSS), dartEdges.get(0));

        frontBlock.addRightAngleCurve(dartEdges.get(2), new Vector2D(Arb_WaistLevel, 0.0));
    }
}
