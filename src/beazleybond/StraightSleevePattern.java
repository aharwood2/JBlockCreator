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

public class StraightSleevePattern
        extends Pattern
{
    /* Straight sleeve pattern */

    protected static ArrayList<easeMeasurement> easeMeasurements = new ArrayList<>();
    /* Pattern-specific Measurements */
    private double a_UpperArmGirth = 28.0;
    private double b_FullLength = 59.0;
    private double c_DepthOfSleeveHead = 15.0;

    /* Arbitrary Measurements */
    private double d_SleeveHeadToElbow = 35.0;
    // Sleeve head height shaping
    private double Arb_SleeveShaping;
    // Wrist shortening
    private double Arb_ForeArmShortening;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Underarm seam shortening
    private double Arb_UnderarmShortening;

    public StraightSleevePattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Populate arbitrary measurements
        Arb_SleeveShaping = 1.5;
        Arb_ForeArmShortening = 1.0;
        Arb_UnderarmShortening = 0.5;
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
        easeMeasurements.add(new easeMeasurement("Upper Arm Girth Ease", 6.0));
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
        return EGarment.STRAIGHTSLEEVE;
    }

    @Override
    protected void addEasement() throws IndexOutOfBoundsException
    {
        a_UpperArmGirth += easeMeasurements.get(0).getValue();
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {
            // Based on measurements for this pattern we can read the following from the scan:
            a_UpperArmGirth = dataStore.getMeasurement("A24").value;
            b_FullLength = dataStore.getMeasurement("A23").value;
            c_DepthOfSleeveHead = dataStore.getMeasurement("A35").value;
            d_SleeveHeadToElbow = dataStore.getMeasurement("A25").value;

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
     * The actual block creation process following the drafting method of Beazley Bond.
     */
    @Override
    protected void createBlocks()
    {
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
