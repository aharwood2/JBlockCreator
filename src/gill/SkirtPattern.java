package gill;

import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class SkirtPattern
    extends Pattern
{
    /* Measurement file name */
    String inputFileName;

    /* Pattern-specific Measurements */
    private double a_FrWaistArc             = 36.2;
    private double b_BkWaistArc             = 34.9;
    private double c_FrAbdomenArc           = 41.4;
    private double d_BkAbdomenArc           = 45.0;
    private double e_BkSeatArc              = 52.7;
    private double f_FrHipArc               = 46.4;
    private double g_BkHipArc               = 51.4;
    private double h_WaistToAbdomen         = 10.7;
    private double i_WaistToSeat            = 20.6;
    private double j_WaistToHip             = 25.6;
    private double k_WaistToKnee            = 65.0;

    /* Arbitrary Measurements */

    //adjustment for setting the hem level x coordinate - same for everyone
    private double Arb_HemLevelX;

    //adjustment for setting the hem level y coordinate - average of many methods
    //will change when more concrete theory is established
    private double Arb_HemLevelY;

    // Arb for construction lines
    private double Arb_Con;

    // Arb for darts
    private double Arb_FrontDartPlacement;
    private double Arb_BackDartPlacement;
    private double Arb_FrontDartLength;
    private double Arb_BackDartLength;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public SkirtPattern(Measurements dataStore)
    {
        readMeasurements(dataStore);
        addEasement();

        // Populate arbitrary measurements
        Arb_HemLevelX = 6.5;
        Arb_HemLevelY = 5.0;
        Arb_Con = 2.0;
        Arb_FrontDartPlacement = 2.0/ 3.0;
        Arb_BackDartPlacement = 1.0/ 2.0;
        Arb_FrontDartLength = h_WaistToAbdomen - 1.5;
        Arb_BackDartLength = i_WaistToSeat - 1.5;

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
        a_FrWaistArc = dataStore.getId(26).value;
        b_BkWaistArc = dataStore.getId(27).value;
        c_FrAbdomenArc = dataStore.getId(28).value;
        d_BkAbdomenArc = dataStore.getId(29).value;
        e_BkSeatArc = dataStore.getId(30).value;
        f_FrHipArc = dataStore.getId(31).value;
        g_BkHipArc = dataStore.getId(32).value;
        h_WaistToAbdomen = dataStore.getId(33).value;
        i_WaistToSeat = dataStore.getId(34).value;
        j_WaistToHip = dataStore.getId(15).value;
        k_WaistToKnee = dataStore.getId(14).value;

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
        blocks.add(new Block(inputFileName + "_Gill_Skirt_Block"));
        Block fullBlock = blocks.get(0);

        // Add all the fixed points to the block that coincide with the basic rectangle. These points do not move
        // throughout the drafting process.
        // Points are the origin, 1, 2, 3, 4, 5, 6, 7, 8, 9  of the block rectangle forming three sides.
        // Bottom edge, right edge, top edge
        fullBlock.addKeypoint(new Vector2D(0.0, 0.0));
        fullBlock.addKeypoint(new Vector2D(h_WaistToAbdomen, 0.0));
        fullBlock.addKeypoint(new Vector2D(i_WaistToSeat, 0.0));
        fullBlock.addKeypoint(new Vector2D(j_WaistToHip, 0.0));
        fullBlock.addKeypoint(new Vector2D(k_WaistToKnee + Arb_HemLevelX, 0.0));
        fullBlock.addKeypoint(new Vector2D(k_WaistToKnee + Arb_HemLevelX, g_BkHipArc / 2 + Arb_HemLevelY / 4));
        fullBlock.addKeypoint(new Vector2D(k_WaistToKnee + Arb_HemLevelX, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4)));
        fullBlock.addKeypoint(new Vector2D(j_WaistToHip, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4)));
        fullBlock.addKeypoint(new Vector2D(h_WaistToAbdomen, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4)));
        fullBlock.addKeypoint(new Vector2D(0.0, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4)));

        // Construction lines for front/back block separation
        fullBlock.addConstructionPoint(new Vector2D(-1.25 - Arb_Con, g_BkHipArc / 2 + Arb_HemLevelY / 4), new Vector2D(k_WaistToKnee + Arb_HemLevelX + Arb_Con, g_BkHipArc / 2 + Arb_HemLevelY / 4), "");

        // Points 10, 11, and 12 on the waistline level
        fullBlock.addKeypoint(new Vector2D(-1.25, ((g_BkHipArc / 2 + Arb_HemLevelY / 4.0) + (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) / 4.0))));
        fullBlock.addKeypoint(new Vector2D(j_WaistToHip, g_BkHipArc / 2 + Arb_HemLevelY / 4.0));
        fullBlock.addKeypoint(new Vector2D(-1.25, ((g_BkHipArc / 2 + Arb_HemLevelY / 4.0) - (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) / 4.0))));

        // Curve between point 10 and point 11
        fullBlock.addCircularCurve(new Vector2D(-1.25, ((g_BkHipArc / 2 + Arb_HemLevelY / 4.0) + (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) / 4.0))),
                new Vector2D(j_WaistToHip, g_BkHipArc / 2 + Arb_HemLevelY / 4.0), 1.0, true);

        // Curve between point 11 and point 12
        fullBlock.addCircularCurve(new Vector2D(j_WaistToHip, g_BkHipArc / 2 + Arb_HemLevelY / 4.0),
                new Vector2D(-1.25, ((g_BkHipArc / 2 + Arb_HemLevelY / 4.0) - (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) / 4.0))), 1.0, true);

        // Add front dart.
        ArrayList<Vector2D> dartEdges = fullBlock.addDart(new Vector2D(0.0, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4)),
                new Vector2D(-1.25, ((g_BkHipArc / 2 + Arb_HemLevelY / 4.0) + (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) / 4.0))),
                Arb_FrontDartPlacement,
                (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) * 0.18),
                Arb_FrontDartLength, true, false);

        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        fullBlock.addRightAngleCurve(new Vector2D(0.0, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4)), dartEdges.get(0));

        fullBlock.addRightAngleCurve(dartEdges.get(2), new Vector2D(-1.25, ((g_BkHipArc / 2 + Arb_HemLevelY / 4.0) + (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) / 4.0))));

        // Add back dart
        dartEdges = fullBlock.addDart(new Vector2D(-1.25, ((g_BkHipArc / 2 + Arb_HemLevelY / 4.0) - (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) / 4.0))),
                new Vector2D(0.0, 0.0),
                Arb_BackDartPlacement,
                (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) * 0.32),
                Arb_BackDartLength, true, false);

        // Add curves
        fullBlock.addRightAngleCurve(new Vector2D(-1.25, ((g_BkHipArc / 2 + Arb_HemLevelY / 4.0) - (((g_BkHipArc + f_FrHipArc) - (b_BkWaistArc + a_FrWaistArc)) / 4.0))), dartEdges.get(0));

        fullBlock.addRightAngleCurve(dartEdges.get(2), new Vector2D(0.0, 0.0));

        // Add construction keypoints for Abdomen Level
        fullBlock.addConstructionPoint(new Vector2D(h_WaistToAbdomen, 0.0 - Arb_Con), new Vector2D(h_WaistToAbdomen, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4) + Arb_Con), "Abdomen");

        // Add construction keypoints for Hip Level
        fullBlock.addConstructionPoint(new Vector2D(i_WaistToSeat, 0.0 - Arb_Con), new Vector2D(i_WaistToSeat, g_BkHipArc / 2 + Arb_HemLevelY / 4 + 3.0 * Arb_Con), "Seat");

        // Add construction keypoints for Abdomen Level
        fullBlock.addConstructionPoint(new Vector2D(j_WaistToHip, 0.0 - Arb_Con), new Vector2D(j_WaistToHip, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4) + Arb_Con), "Hip");
    }
}