package aldrich;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class SkirtPattern
    extends Pattern
{
    /* Skirt pattern block is constructed to fit on the 'natural waistline' */

    /* Measurement file name */
    String inputFileName;

    /* Pattern-specific Measurements */
    private double a_Waist = 68.0;
    private double b_Hips = 94.0;
    private double c_WaistToHip = 20.6;
    private double d_SkirtLength = 71.5;
    // d_SkirtLength is affected by fashion so could realistically be anything
    // Set the same as the default for the Gill pattern

    /* Arbitrary Measurements */

    // Apparent adjustment for setting waistline
    private double Arb_HipAdjustment;

    // Waist curve adjustment
    private double Arb_WaistCurve;

    /* Back Arbs */

    // Back hip adjustment
    private double Arb_BackHipAdjustment;

    // 'Natural waist' adjustment
    private double Arb_BackNaturalWaist;

    // Back dart depths
    private double Arb_BackDartOneDepth;
    private double Arb_BackDartTwoDepth;

    // Back dart width
    private double Arb_BackDartWidth;

    /* Front Arbs */

    // 'Natural waist' adjustment
    private double Arb_FrontNaturalWaist;

    // Front dart depth
    private double Arb_FrontDartDepth;

    // Front dart width
    private double Arb_FrontDartWidth;

    // Dart placement
    private double Arb_FrontDartPlacement;
    private double Arb_BackDartPlacement;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public SkirtPattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Populate arbitrary measurements
        Arb_HipAdjustment = 1.5;
        Arb_WaistCurve = -1.25;
        Arb_BackHipAdjustment = 1.5;
        Arb_BackNaturalWaist = 4.0;
        Arb_BackDartOneDepth = 14.0;
        Arb_BackDartTwoDepth = 13.0;
        Arb_BackDartWidth = 2.0;
        Arb_FrontNaturalWaist = 2.5;
        Arb_FrontDartDepth = 12.0;
        Arb_FrontDartWidth = 2.5;
        Arb_FrontDartPlacement = 2.0 / 3.0;
        Arb_BackDartPlacement = 1.0 / 3.0;

        // Create the blocks
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected EMethod assignMethod()
    {
        return EMethod.ALRICH;
    }

    @Override
    protected EGarment assignGarment()
    {
        return EGarment.SKIRT;
    }

    @Override
    protected void addEasement()
    {
        //no easement needed
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try {
            // Based on measurements for this pattern we can read the following from the scan:
            a_Waist = dataStore.getId(26).value + dataStore.getId(27).value;
            b_Hips = dataStore.getId(31).value + dataStore.getId(32).value;
            c_WaistToHip = dataStore.getId(15).value;
            d_SkirtLength = dataStore.getId(14).value;

            // Get name
            inputFileName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            return false;
        }
    }

    /**
     * The actual block creation process following the drafting method of Gill.
     */
    @Override
    protected void createBlocks()
    {
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(inputFileName + "_Aldrich_Skirt_Block"));
        Block fullBlock = blocks.get(0);

        // Add all the fixed points to the block that coincide with the basic rectangle. These points do not move
        // throughout the drafting process.
        // Points are the origin, back hip line, back knee corner, knee side seam, front knee corner, front hip line, front waist line
        fullBlock.addKeypoint(new Vector2D(0.0, 0.0));
        fullBlock.addKeypoint(new Vector2D(c_WaistToHip, 0.0));
        fullBlock.addKeypoint(new Vector2D(d_SkirtLength, 0.0));
        fullBlock.addKeypoint(new Vector2D(d_SkirtLength, b_Hips / 4.0 + Arb_BackHipAdjustment));
        fullBlock.addKeypoint(new Vector2D(d_SkirtLength, b_Hips / 2.0 + Arb_BackHipAdjustment));
        fullBlock.addKeypoint(new Vector2D(c_WaistToHip,b_Hips / 2.0 + Arb_BackHipAdjustment));
        fullBlock.addKeypoint(new Vector2D(0.0, b_Hips / 2.0 + Arb_BackHipAdjustment));

        // Construction lines for front/back block separation
        fullBlock.addConstructionPoint(new Vector2D((Arb_WaistCurve - Arb_Con),(b_Hips / 4.0 + Arb_BackHipAdjustment)),
                                       new Vector2D((d_SkirtLength + Arb_Con),(b_Hips / 4.0 + Arb_BackHipAdjustment)),
                                       "");

        // Three major waistline points
        fullBlock.addKeypoint(new Vector2D(Arb_WaistCurve, (b_Hips / 2.0 + Arb_BackHipAdjustment) - (a_Waist / 4.0 + Arb_FrontNaturalWaist)));
        fullBlock.addKeypoint(new Vector2D(c_WaistToHip, b_Hips / 4.0 + Arb_BackHipAdjustment));
        fullBlock.addKeypoint(new Vector2D(Arb_WaistCurve, a_Waist / 4.0 + Arb_BackNaturalWaist));

        // Adding the two side seam curves
        fullBlock.addCircularCurve(new Vector2D(Arb_WaistCurve, (b_Hips / 2.0 + Arb_BackHipAdjustment) - (a_Waist / 4.0 + Arb_FrontNaturalWaist)),
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
                new Vector2D(Arb_WaistCurve, (b_Hips / 2.0 + Arb_BackHipAdjustment) - (a_Waist / 4.0 + Arb_FrontNaturalWaist)),
                Arb_FrontDartPlacement, Arb_FrontDartWidth, Arb_FrontDartDepth, true, true);

        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        fullBlock.addRightAngleCurve(new Vector2D(0.0, b_Hips / 2.0 + Arb_BackHipAdjustment), dartEdges.get(0));

        fullBlock.addRightAngleCurve(dartEdges.get(2), new Vector2D(Arb_WaistCurve, (b_Hips / 2.0 + Arb_BackHipAdjustment) - (a_Waist / 4.0 + Arb_FrontNaturalWaist)));

        // Add second back dart - second as anticlockwise
        ArrayList<Vector2D> dartEdges2 = fullBlock.addDart(new Vector2D(Arb_WaistCurve, a_Waist / 4.0 + Arb_BackNaturalWaist),
                new Vector2D(0.0, 0.0),
                Arb_BackDartPlacement, Arb_BackDartWidth, Arb_BackDartTwoDepth, true, false);

        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        fullBlock.addRightAngleCurve(new Vector2D(Arb_WaistCurve, a_Waist / 4.0 + Arb_BackNaturalWaist), dartEdges2.get(0));


        // Add first back dart - first as anticlockwise
        ArrayList<Vector2D> dartEdges3 = fullBlock.addDart(dartEdges2.get(2),
                new Vector2D(0.0, 0.0),
                1.0 / 2.0, Arb_BackDartWidth, Arb_BackDartOneDepth, true, false);


        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        fullBlock.addRightAngleCurve(dartEdges2.get(2),dartEdges3.get(0));
        fullBlock.addRightAngleCurve(dartEdges3.get(2),new Vector2D(0.0, 0.0));
    }

}
