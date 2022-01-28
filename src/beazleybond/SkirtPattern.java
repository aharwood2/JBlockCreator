package beazleybond;

import jblockenums.EPattern;
import jblockenums.EPosition;
import jblockenums.EUnitType;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

/**
 * Class to construct a skirt using the Beazley and Bond drafting method.
 */
public class SkirtPattern
        extends Pattern
{
    public SkirtPattern(String userName, InputFileData dataStore, MeasurementSet template)
    {
        super(userName, dataStore, template);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.BEAZLEYBOND_SKIRT;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("a_Waist", "A02"));
        measurements.addMeasurement(new Measurement("b_UpperHip", "A13"));
        measurements.addMeasurement(new Measurement("c_Hip", "A03"));
        measurements.addMeasurement(new Measurement("d_CentreBack", "A16"));
        measurements.addMeasurement(new Measurement("g_HipLevel", "A15"));

        // Arbitrary
        measurements.addMeasurement(new Measurement("Arb_WaistLevel", 1.0));
        measurements.addMeasurement(new Measurement("Arb_UpperHipLevel", 10.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartPercent", 35, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_FrontDartPercent", 20, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_SideSeamPercent", 45, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_BackDartLength", 14.0));
        measurements.addMeasurement(new Measurement("Arb_FrontDartLength", 8.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartPlacement", 50, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_FrontDartPlacement", 33, EUnitType.PERCENTAGE));

        // Ease
        measurements.addMeasurement(new Measurement("WaistEase", 2.0));
        measurements.addMeasurement(new Measurement("UpperHipEase", 4.0));
        measurements.addMeasurement(new Measurement("HipEase", 4.0));
    }

    /**
     * The actual block creation process following the drafting method of Beazley and Bond.
     */
    @Override
    public void createBlocks()
    {
        // Pull from store
        var a_Waist = get("a_Waist") + get("WaistEase");
        var b_UpperHip = get("b_UpperHip") + get("UpperHipEase");
        var c_Hip = get("c_Hip") + get("HipEase");
        var d_CentreBack = get("d_CentreBack");
        var g_HipLevel = get("g_HipLevel");
        var Arb_WaistLevel = get("Arb_WaistLevel");
        var Arb_UpperHipLevel = get("Arb_UpperHipLevel");
        var Arb_BackDartPercent = get("Arb_BackDartPercent") / 100.0;
        var Arb_FrontDartPercent = get("Arb_FrontDartPercent") / 100.0;
        var Arb_SideSeamPercent = get("Arb_SideSeamPercent") / 100.0;
        var Arb_BackDartLength = get("Arb_BackDartLength");
        var Arb_FrontDartLength = get("Arb_FrontDartLength");
        var Arb_BackDartPlacement = get("Arb_BackDartPlacement") / 100.0;
        var Arb_FrontDartPlacement = get("Arb_FrontDartPlacement") / 100.0;

        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(userName + "_BB_Skirt_Back_Block"));
        Block backBlock = blocks.get(0);

        // Add all the fixed points to the block that coincide with the basic rectangle. These points do not move
        // throughout the drafting process.
        backBlock.addKeypoint(new Vector2D(d_CentreBack, 0.0));
        backBlock.addKeypoint(new Vector2D(d_CentreBack, c_Hip / 4.0));
        backBlock.addKeypoint(new Vector2D(g_HipLevel, c_Hip / 4.0));

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
        backBlock.addCircularCurve(new Vector2D(g_HipLevel, c_Hip / 4.0),
                                   new Vector2D(0.0, Int_SuppressedSS), 0.5, true);


        // Add construction keypoints for Upper Hip Level
        backBlock.addConstructionPoint(new Vector2D((Arb_UpperHipLevel), 0.0 - Arb_Con),
                                       new Vector2D((Arb_UpperHipLevel), c_Hip / 4 + Arb_Con),
                                       "Upper Hip");

        // Add construction keypoints for Hip Level
        backBlock.addConstructionPoint(new Vector2D((g_HipLevel), 0.0 - Arb_Con),
                                       new Vector2D((g_HipLevel), c_Hip / 4 + Arb_Con),
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
