package gill;

import jblockenums.EPattern;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.Block;
import jblockmain.Measurements;
import jblockmain.Pattern;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class SkirtPattern
        extends Pattern
{
    /* Pattern-specific Measurements */
    private double a_FrWaistArc = 38.6;
    private double b_BkWaistArc = 44.4;
    private double c_FrAbdomenArc = 41.8;
    private double d_BkAbdomenArc = 44.4;
    private double e_BkSeatArc = 52.7;
    private double f_FrHipArc = 44.5;
    private double g_BkHipArc = 53.3;
    private double h_WaistToAbdomen = 10.6;
    private double i_WaistToSeat = 20.6;
    private double j_WaistToHip = 25.6;
    private double k_WaistToKnee = 65.3;
    private double l_SideseamUplift = 0.7;

    // Difference between waist and hip
    private double waisthipsuppression;

    /* Arbitrary Measurements */

    //adjustment for setting the hem level x coordinate - same for everyone
    private double Arb_HemLevelX;

    //adjustment for setting the hem level y coordinate - average of many methods
    //will change when more concrete theory is established
    private double Arb_HemLevelY;

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
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Populate arbitrary measurements
        Arb_HemLevelX = 6.5;
        Arb_HemLevelY = 5.0;
        Arb_FrontDartPlacement = 2.0 / 3.0;
        Arb_BackDartPlacement = 1.0 / 2.0;
        Arb_FrontDartLength = h_WaistToAbdomen - 1.5;
        Arb_BackDartLength = i_WaistToSeat - 1.5;

        // Create the blocks
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.GILL_SKIRT;
    }

    @Override
    protected void addEasement()
    {
        //no easement needed
    }


    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {
            // Based on measurements for this pattern we can read the following from the scan:
            a_FrWaistArc = dataStore.getMeasurement("A26").value;
            b_BkWaistArc = dataStore.getMeasurement("A27").value;
            c_FrAbdomenArc = dataStore.getMeasurement("A28").value;
            d_BkAbdomenArc = dataStore.getMeasurement("A29").value;
            e_BkSeatArc = dataStore.getMeasurement("A30").value;
            f_FrHipArc = dataStore.getMeasurement("A31").value;
            g_BkHipArc = dataStore.getMeasurement("A32").value;
            h_WaistToAbdomen = dataStore.getMeasurement("A33").value;
            i_WaistToSeat = dataStore.getMeasurement("A34").value;
            j_WaistToHip = dataStore.getMeasurement("A15").value;
            k_WaistToKnee = dataStore.getMeasurement("A14").value;
            l_SideseamUplift = dataStore.getMeasurement("A37").value;

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
     * The actual block creation process following the drafting method of Gill.
     */
    @Override
    protected void createBlocks()
    {
        // Setting waist hip suppression variable
        waisthipsuppression = ((f_FrHipArc + g_BkHipArc) - (a_FrWaistArc + b_BkWaistArc));

        if (waisthipsuppression < 0)
        {
            waisthipsuppression = -waisthipsuppression;
        }

        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(userName + "_Gill_Skirt_Block"));
        Block fullBlock = blocks.get(0);

        // Predefining the max Y value to make the code look tidier
        double maxY = ((g_BkHipArc / 2.0) + (Arb_HemLevelY / 4.0) + ((f_FrHipArc / 2.0) + (Arb_HemLevelY / 4.0)));

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
        fullBlock.addKeypoint(new Vector2D(k_WaistToKnee + Arb_HemLevelX, maxY));
        fullBlock.addKeypoint(new Vector2D(j_WaistToHip, maxY));
        fullBlock.addKeypoint(new Vector2D(h_WaistToAbdomen, maxY));
        fullBlock.addKeypoint(new Vector2D(0.0, maxY));

        // Construction lines for front/back block separation
        fullBlock.addConstructionPoint(new Vector2D(-1.25 - Arb_Con, g_BkHipArc / 2 + Arb_HemLevelY / 4),
                                       new Vector2D(k_WaistToKnee + Arb_HemLevelX + Arb_Con,
                                                    g_BkHipArc / 2 + Arb_HemLevelY / 4),
                                       "");

        // Point 10 has an if condition to allow for more accurate pattern creation
        double frontdartwidth = ((((g_BkHipArc + f_FrHipArc) / 2.0) - ((b_BkWaistArc + a_FrWaistArc)) / 2.0) * 0.18);
        double pointtenif;
        if (l_SideseamUplift >= 1.25)
        {

            fullBlock.addKeypoint(
                    new Vector2D(-l_SideseamUplift, (maxY - (a_FrWaistArc / 2.0) - 0.35 - frontdartwidth)));
            pointtenif = -l_SideseamUplift;
        }
        else
        {

            fullBlock.addKeypoint(new Vector2D(-1.25, (maxY - (a_FrWaistArc / 2.0) - 0.35 - frontdartwidth)));
            pointtenif = -1.25;
        }

        // Point 11
        fullBlock.addKeypoint(new Vector2D(j_WaistToHip, g_BkHipArc / 2 + Arb_HemLevelY / 4.0));

        // Point 12 has an if condition to allow for more accurate pattern creation
        double backdartwidth = ((((g_BkHipArc + f_FrHipArc) / 2.0) - ((b_BkWaistArc + a_FrWaistArc)) / 2.0) * 0.32);
        double pointtwelveif;
        if (l_SideseamUplift >= 1.25)
        {
            fullBlock.addKeypoint(new Vector2D(-l_SideseamUplift, ((b_BkWaistArc / 2.0) + 0.35 + backdartwidth)));
            pointtwelveif = -l_SideseamUplift;
        }
        else
        {
            fullBlock.addKeypoint(new Vector2D(-1.25, ((b_BkWaistArc / 2.0) + 0.35 + backdartwidth)));
            pointtwelveif = -1.25;
        }

        // Curve between point 10 and point 11
        fullBlock.addCircularCurve(new Vector2D(pointtenif, (maxY - (a_FrWaistArc / 2.0) - 0.35 - frontdartwidth)),
                                   new Vector2D(j_WaistToHip, g_BkHipArc / 2 + Arb_HemLevelY / 4.0),
                                   0.5,
                                   true);

        // Curve between point 11 and point 12
        fullBlock.addCircularCurve(new Vector2D(j_WaistToHip, g_BkHipArc / 2 + Arb_HemLevelY / 4.0),
                                   new Vector2D(pointtwelveif, ((b_BkWaistArc / 2.0) + 0.35 + backdartwidth)),
                                   0.5,
                                   true);

        // Add front dart.
        ArrayList<Vector2D> dartEdges = fullBlock.addDart(new Vector2D(0.0, maxY),
                                                          new Vector2D(pointtenif,
                                                                       (maxY - (a_FrWaistArc / 2.0) - 0.35 - frontdartwidth)),
                                                          Arb_FrontDartPlacement,
                                                          frontdartwidth,
                                                          Arb_FrontDartLength,
                                                          true,
                                                          false);

        // Add curves either side of dart ensuring the curve intersects the joining edges at a right angle.
        fullBlock.addRightAngleCurve(new Vector2D(0.0, maxY), dartEdges.get(0));

        fullBlock.addRightAngleCurve(dartEdges.get(2),
                                     new Vector2D(pointtenif, (maxY - (a_FrWaistArc / 2.0) - 0.35 - frontdartwidth)));

        // Add back dart
        dartEdges = fullBlock.addDart(new Vector2D(pointtwelveif, ((b_BkWaistArc / 2.0) + 0.35 + backdartwidth)),
                                      new Vector2D(0.0, 0.0),
                                      Arb_BackDartPlacement,
                                      backdartwidth,
                                      Arb_BackDartLength, true, false);

        // Add curves
        fullBlock.addRightAngleCurve(new Vector2D(pointtwelveif, ((b_BkWaistArc / 2.0) + 0.35 + backdartwidth)),
                                     dartEdges.get(0));

        fullBlock.addRightAngleCurve(dartEdges.get(2), new Vector2D(0.0, 0.0));

        // Add construction keypoints for Abdomen Level
        fullBlock.addConstructionPoint(new Vector2D(h_WaistToAbdomen, 0.0 - Arb_Con), new Vector2D(h_WaistToAbdomen,
                                                                                                   (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4) + Arb_Con),
                                       "Abdomen");

        // Add construction keypoints for Hip Level
        fullBlock.addConstructionPoint(new Vector2D(i_WaistToSeat, 0.0 - Arb_Con),
                                       new Vector2D(i_WaistToSeat, g_BkHipArc / 2 + Arb_HemLevelY / 4 + 3.0 * Arb_Con),
                                       "Seat");

        // Add construction keypoints for Abdomen Level
        fullBlock.addConstructionPoint(new Vector2D(j_WaistToHip, 0.0 - Arb_Con), new Vector2D(j_WaistToHip,
                                                                                               (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4) + Arb_Con),
                                       "Hip");
    }
}