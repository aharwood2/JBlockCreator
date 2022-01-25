package beazleybond;

import jblockenums.EPattern;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class StraightSleevePattern
        extends Pattern
{
    public StraightSleevePattern(String userName, InputFileData dataStore, MeasurementSet template)
    {
        super(userName, dataStore, template);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.BEAZLEYBOND_STRAIGHTSLEEVE;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("a_UpperArmGirth", "A24"));
        measurements.addMeasurement(new Measurement("b_FullLength", "A23"));
        measurements.addMeasurement(new Measurement("c_DepthOfSleeveHead", "A35"));
        measurements.addMeasurement(new Measurement("d_SleeveHeadToElbow", "A25"));

        // Arbitrary
        measurements.addMeasurement(new Measurement("Arb_ForeArmShortening", 1.0));
        measurements.addMeasurement(new Measurement("Arb_UnderarmShortening", 0.5));

        // Ease
        measurements.addMeasurement(new Measurement("upperArmGirthEase", 6.0));
    }

    /**
     * The actual block creation process following the drafting method of Beazley Bond.
     */
    @Override
    public void createBlocks()
    {
        // Pull from store
        var a_UpperArmGirth = get("a_UpperArmGirth") + get("upperArmGirthEase");
        var b_FullLength = get("b_FullLength");
        var c_DepthOfSleeveHead = get("c_DepthOfSleeveHead");
        var d_SleeveHeadToElbow = get("d_SleeveHeadToElbow");
        var Arb_ForeArmShortening = get("Arb_ForeArmShortening");
        var Arb_UnderarmShortening = get("Arb_UnderarmShortening");

        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(userName + "_BB_Sleeve_Block"));
        Block fullBlock = blocks.get(0);

        // Upper arm line point on back underarm
        fullBlock.addKeypoint(new Vector2D(c_DepthOfSleeveHead, 0.0));

        // Wrist point on back underarm including shaping
        fullBlock.addKeypoint(new Vector2D(b_FullLength - Arb_UnderarmShortening, 0.0));

        // Back arm line point on wrist line
        fullBlock.addKeypoint(new Vector2D(b_FullLength, a_UpperArmGirth / 4.0));

        // Forearm line point including shaping
        fullBlock.addKeypoint(new Vector2D(b_FullLength - Arb_ForeArmShortening, (3.0 * a_UpperArmGirth) / 4.0));

        // Underarm point on wrist line
        fullBlock.addKeypoint(new Vector2D((b_FullLength - Arb_UnderarmShortening), a_UpperArmGirth));

        // Upper arm line point on underarm
        fullBlock.addKeypoint(new Vector2D(c_DepthOfSleeveHead, a_UpperArmGirth));

        // Forearm line point for armhole shaping
        fullBlock.addKeypoint(new Vector2D(c_DepthOfSleeveHead / 2.0, (3.0 * a_UpperArmGirth) / 4.0));

        // Centre line point for armhole shaping
        fullBlock.addKeypoint(new Vector2D(0.0, a_UpperArmGirth / 2.0));

        // Back arm line point for armhole shaping
        fullBlock.addKeypoint(new Vector2D(c_DepthOfSleeveHead / 2.0, a_UpperArmGirth / 4.0));

        // Back arm construction line
        fullBlock.addConstructionPoint(new Vector2D(0.0 - Arb_Con, a_UpperArmGirth / 4.0),
                                       new Vector2D(b_FullLength + Arb_Con, a_UpperArmGirth / 4.0),
                                       "Back Arm");

        // Centre construction line
        fullBlock.addConstructionPoint(new Vector2D(0.0 - 2.0 * Arb_Con, a_UpperArmGirth / 2.0),
                                       new Vector2D(b_FullLength + Arb_Con, a_UpperArmGirth / 2.0),
                                       "Centre");

        // Forearm construction line
        fullBlock.addConstructionPoint(new Vector2D(0.0 - Arb_Con, 3.0 * (a_UpperArmGirth / 4.0)),
                                       new Vector2D(b_FullLength + Arb_Con, 3.0 * (a_UpperArmGirth / 4.0)),
                                       "Forearm");

        // Upper arm construction line
        fullBlock.addConstructionPoint(new Vector2D(c_DepthOfSleeveHead, 0.0 - Arb_Con),
                                       new Vector2D(c_DepthOfSleeveHead, a_UpperArmGirth + Arb_Con),
                                       "Upper arm");

        // Elbow construction line
        fullBlock.addConstructionPoint(new Vector2D(d_SleeveHeadToElbow, 0.0 - Arb_Con),
                                       new Vector2D(d_SleeveHeadToElbow, a_UpperArmGirth + Arb_Con),
                                       "Elbow");

        // Diagonal Elbow construction line
        fullBlock.addConstructionPoint(new Vector2D(0.0, a_UpperArmGirth / 2.0),
                                       new Vector2D(d_SleeveHeadToElbow, a_UpperArmGirth / 4.0),
                                       "");

        // Underarm to forearm arm hole curve
        fullBlock.addCircularCurve(new Vector2D(c_DepthOfSleeveHead, a_UpperArmGirth),
                                   new Vector2D(c_DepthOfSleeveHead / 2.0, 3.0 * (a_UpperArmGirth / 4.0)),
                                   1.75,
                                   false);

        // Forearm to centre arm hole curve
        fullBlock.addCircularCurve(new Vector2D(c_DepthOfSleeveHead / 2.0, 3.0 * (a_UpperArmGirth / 4.0)),
                                   new Vector2D(0.0, a_UpperArmGirth / 2.0),
                                   1.75,
                                   true);

        // Centre to back arm arm hole curve
        fullBlock.addCircularCurve(new Vector2D(0.0, a_UpperArmGirth / 2.0),
                                   new Vector2D(c_DepthOfSleeveHead / 2.0, a_UpperArmGirth / 4.0),
                                   1.25,
                                   true);

        // Back arm to underarm arm hole curve
        fullBlock.addCircularCurve(new Vector2D(c_DepthOfSleeveHead / 2.0, a_UpperArmGirth / 4.0),
                                   new Vector2D(c_DepthOfSleeveHead, 0.0),
                                   1.25,
                                   false);
    }
}
