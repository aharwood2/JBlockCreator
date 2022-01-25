package aldrich;

import jblockenums.EPattern;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

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
        return EPattern.ALDRICH_SKIRT;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("a_WaistFrontArc", "A26"));
        measurements.addMeasurement(new Measurement("a_WaistBackArc", "A27"));
        measurements.addMeasurement(new Measurement("b_HipsFrontArc", "A31"));
        measurements.addMeasurement(new Measurement("b_HipsBackArc", "A32"));
        measurements.addMeasurement(new Measurement("c_WaistToHip", "A15"));
        measurements.addMeasurement(new Measurement("d_SkirtLength", "A14"));

        // Arbitrary
        measurements.addMeasurement(new Measurement("Arb_HipAdjustment", 1.5));
        measurements.addMeasurement(new Measurement("Arb_WaistCurve", -1.25));
        measurements.addMeasurement(new Measurement("Arb_BackHipAdjustment", 1.5));
        measurements.addMeasurement(new Measurement("Arb_BackNaturalWaist", 4.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartOneDepth", 14.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartTwoDepth", 13.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartWidth", 2.0));
        measurements.addMeasurement(new Measurement("Arb_FrontNaturalWaist", 2.5));
        measurements.addMeasurement(new Measurement("Arb_FrontDartDepth", 12.0));
        measurements.addMeasurement(new Measurement("Arb_FrontDartWidth", 2.5));
        measurements.addMeasurement(new Measurement("Arb_FrontDartPlacement", 2.0 / 3.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartPlacement", 1.0 / 3.0));
    }

    /**
     * The actual block creation process following the drafting method of Gill.
     */
    @Override
    public void createBlocks()
    {
        // Pull from store
        var a_Waist = get("a_WaistFrontArc") + get("a_WaistBackArc");
        var b_Hips = get("b_HipsFrontArc") + get("b_HipsBackArc");
        var c_WaistToHip = get("c_WaistToHip");
        var d_SkirtLength = get("d_SkirtLength");
        var Arb_HipAdjustment = get("Arb_HipAdjustment");
        var Arb_WaistCurve = get("Arb_WaistCurve");
        var Arb_BackHipAdjustment = get("Arb_BackHipAdjustment");
        var Arb_BackNaturalWaist = get("Arb_BackNaturalWaist");
        var Arb_BackDartOneDepth = get("Arb_BackDartOneDepth");
        var Arb_BackDartTwoDepth = get("Arb_BackDartTwoDepth");
        var Arb_BackDartWidth = get("Arb_BackDartWidth");
        var Arb_FrontNaturalWaist = get("Arb_FrontNaturalWaist");
        var Arb_FrontDartDepth = get("Arb_FrontDartDepth");
        var Arb_FrontDartWidth = get("Arb_FrontDartWidth");
        var Arb_FrontDartPlacement = get("Arb_FrontDartPlacement");
        var Arb_BackDartPlacement = get("Arb_BackDartPlacement");
        
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(userName + "_Aldrich_Skirt_Block"));
        Block fullBlock = blocks.get(0);

        // Add all the fixed points to the block that coincide with the basic rectangle. These points do not move
        // throughout the drafting process.
        // Points are the origin, back hip line, back knee corner, knee side seam, front knee corner, front hip line, front waist line
        fullBlock.addKeypoint(new Vector2D(0.0, 0.0));
        fullBlock.addKeypoint(new Vector2D(c_WaistToHip, 0.0));
        fullBlock.addKeypoint(new Vector2D(d_SkirtLength, 0.0));
        fullBlock.addKeypoint(new Vector2D(d_SkirtLength, b_Hips / 4.0 + Arb_BackHipAdjustment));
        fullBlock.addKeypoint(new Vector2D(d_SkirtLength, b_Hips / 2.0 + Arb_BackHipAdjustment));
        fullBlock.addKeypoint(new Vector2D(c_WaistToHip, b_Hips / 2.0 + Arb_BackHipAdjustment));
        fullBlock.addKeypoint(new Vector2D(0.0, b_Hips / 2.0 + Arb_BackHipAdjustment));

        // Construction lines for front/back block separation
        fullBlock.addConstructionPoint(new Vector2D((Arb_WaistCurve - Arb_Con), (b_Hips / 4.0 + Arb_BackHipAdjustment)),
                                       new Vector2D((d_SkirtLength + Arb_Con), (b_Hips / 4.0 + Arb_BackHipAdjustment)),
                                       "");

        // Three major waistline points
        fullBlock.addKeypoint(new Vector2D(Arb_WaistCurve,
                                           (b_Hips / 2.0 + Arb_BackHipAdjustment) - (a_Waist / 4.0 + Arb_FrontNaturalWaist)));
        fullBlock.addKeypoint(new Vector2D(c_WaistToHip, b_Hips / 4.0 + Arb_BackHipAdjustment));
        fullBlock.addKeypoint(new Vector2D(Arb_WaistCurve, a_Waist / 4.0 + Arb_BackNaturalWaist));

        // Adding the two side seam curves
        fullBlock.addCircularCurve(new Vector2D(Arb_WaistCurve,
                                                (b_Hips / 2.0 + Arb_BackHipAdjustment) - (a_Waist / 4.0 + Arb_FrontNaturalWaist)),
                                   new Vector2D(c_WaistToHip, b_Hips / 4.0 + Arb_HipAdjustment),
                                   1.0,
                                   true);

        fullBlock.addCircularCurve(new Vector2D(c_WaistToHip, b_Hips / 4.0 + Arb_HipAdjustment),
                                   new Vector2D(Arb_WaistCurve, a_Waist / 4.0 + Arb_BackNaturalWaist),
                                   1.0,
                                   true);

        // Add construction line for Hip line
        fullBlock.addConstructionPoint(new Vector2D(c_WaistToHip, -Arb_Con),
                                       new Vector2D(c_WaistToHip, b_Hips / 2.0 + Arb_HipAdjustment + Arb_Con),
                                       "Hip");

        // Add front dart
        ArrayList<Vector2D> dartEdges = fullBlock.addDart(new Vector2D(0.0, b_Hips / 2.0 + Arb_BackHipAdjustment),
                                                          new Vector2D(Arb_WaistCurve,
                                                                       (b_Hips / 2.0 + Arb_BackHipAdjustment) - (a_Waist / 4.0 + Arb_FrontNaturalWaist)),
                                                          Arb_FrontDartPlacement, Arb_FrontDartWidth,
                                                          Arb_FrontDartDepth, true, true);

        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        fullBlock.addRightAngleCurve(new Vector2D(0.0, b_Hips / 2.0 + Arb_BackHipAdjustment), dartEdges.get(0));

        fullBlock.addRightAngleCurve(dartEdges.get(2), new Vector2D(Arb_WaistCurve,
                                                                    (b_Hips / 2.0 + Arb_BackHipAdjustment) - (a_Waist / 4.0 + Arb_FrontNaturalWaist)));

        // Add second back dart - second as anticlockwise
        ArrayList<Vector2D> dartEdges2 = fullBlock.addDart(
                new Vector2D(Arb_WaistCurve, a_Waist / 4.0 + Arb_BackNaturalWaist),
                new Vector2D(0.0, 0.0),
                Arb_BackDartPlacement, Arb_BackDartWidth, Arb_BackDartTwoDepth, true, false);

        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        fullBlock.addRightAngleCurve(new Vector2D(Arb_WaistCurve, a_Waist / 4.0 + Arb_BackNaturalWaist),
                                     dartEdges2.get(0));


        // Add first back dart - first as anticlockwise
        ArrayList<Vector2D> dartEdges3 = fullBlock.addDart(dartEdges2.get(2),
                                                           new Vector2D(0.0, 0.0),
                                                           1.0 / 2.0, Arb_BackDartWidth, Arb_BackDartOneDepth, true,
                                                           false);


        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        fullBlock.addRightAngleCurve(dartEdges2.get(2), dartEdges3.get(0));
        fullBlock.addRightAngleCurve(dartEdges3.get(2), new Vector2D(0.0, 0.0));
    }
}
