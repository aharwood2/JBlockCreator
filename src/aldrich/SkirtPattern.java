package aldrich;

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public SkirtPattern(Measurements dataStore)
    {
        readMeasurements(dataStore);
        addEasement();

        // Populate arbitrary measurements
        Arb_HipAdjustment = 1.5;
        Arb_WaistCurve = 1.25;
        Arb_BackHipAdjustment = 1.5;
        Arb_BackNaturalWaist = 4.0;
        Arb_BackDartOneDepth = 14.0;
        Arb_BackDartTwoDepth = 13.0;
        Arb_BackDartWidth = 2.0;
        Arb_FrontNaturalWaist = 2.5;
        Arb_FrontDartDepth = 12.0;
        Arb_FrontDartWidth = 2.5;

        // Create the blocks
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected void addEasement()
    {
        //no easement needed
    }

    @Override
    protected void readMeasurements(Measurements dataStore)
    {
        // Based on measurements for this pattern we can read the following from the scan:
        a_Waist = dataStore.getId(26).value + dataStore.getId(27).value;
        b_Hips = dataStore.getId(31).value + dataStore.getId(32).value;
        c_WaistToHip = dataStore.getId(15).value;
        d_SkirtLength = dataStore.getId(14).value;

        // Get name
        inputFileName = dataStore.getName();
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
        fullBlock.addKeypoint(new Vector2D(d_SkirtLength, b_Hips / 4.0 + Arb_HipAdjustment));
        fullBlock.addKeypoint(new Vector2D(d_SkirtLength, b_Hips / 2.0 + Arb_HipAdjustment));
        fullBlock.addKeypoint(new Vector2D(c_WaistToHip,b_Hips / 2.0 + Arb_HipAdjustment));
        fullBlock.addKeypoint(new Vector2D(0.0, b_Hips / 2.0 + Arb_HipAdjustment));


    }

}
