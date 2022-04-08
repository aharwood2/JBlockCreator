package gill;

import jblockenums.EPattern;
import jblockenums.EUnitType;
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
        return EPattern.GILL_SKIRT;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("a_FrWaistArc", "A26"));
        measurements.addMeasurement(new Measurement("b_BkWaistArc", "A27"));
        measurements.addMeasurement(new Measurement("f_FrHipArc", "A31"));
        measurements.addMeasurement(new Measurement("g_BkHipArc", "A32"));
        measurements.addMeasurement(new Measurement("h_WaistToAbdomen", "A33"));
        measurements.addMeasurement(new Measurement("i_WaistToSeat", "A34"));
        measurements.addMeasurement(new Measurement("j_WaistToHip", "A15"));
        measurements.addMeasurement(new Measurement("k_WaistToKnee", "A14"));
        measurements.addMeasurement(new Measurement("l_SideseamUplift", "A37"));

        // Arbitrary
        measurements.addMeasurement(new Measurement("Arb_HemLevelX", 6.5));
        measurements.addMeasurement(new Measurement("Arb_HemLevelY", 5.0));
        measurements.addMeasurement(new Measurement("Arb_FrontDartPlacement", 66, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_BackDartPlacement", 33, EUnitType.PERCENTAGE));
    }

    /**
     * The actual block creation process following the drafting method of Gill.
     */
    @Override
    public void createBlocks()
    {
        // Pull from store
        var a_FrWaistArc = get("a_FrWaistArc");
        var b_BkWaistArc = get("b_BkWaistArc");
        var f_FrHipArc = get("f_FrHipArc");
        var g_BkHipArc = get("g_BkHipArc");
        var h_WaistToAbdomen = get("h_WaistToAbdomen");
        var i_WaistToSeat = get("i_WaistToSeat");
        var j_WaistToHip = get("j_WaistToHip");
        var k_WaistToKnee = get("k_WaistToKnee");
        var l_SideseamUplift = get("l_SideseamUplift");
        var Arb_HemLevelX = get("Arb_HemLevelX");
        var Arb_HemLevelY = get("Arb_HemLevelY");
        var Arb_FrontDartPlacement = get("Arb_FrontDartPlacement") / 100.0;
        var Arb_BackDartPlacement = get("Arb_BackDartPlacement") / 100.0;
        var Arb_FrontDartLength = get("h_WaistToAbdomen") - 1.5;
        var Arb_BackDartLength = get("i_WaistToSeat") - 1.5;

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
        fullBlock.addCircularArc(new Vector2D(pointtenif, (maxY - (a_FrWaistArc / 2.0) - 0.35 - frontdartwidth)),
                                   new Vector2D(j_WaistToHip, g_BkHipArc / 2 + Arb_HemLevelY / 4.0),
                                   0.5,
                                   true);

        // Curve between point 11 and point 12
        fullBlock.addCircularArc(new Vector2D(j_WaistToHip, g_BkHipArc / 2 + Arb_HemLevelY / 4.0),
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
        fullBlock.addConstructionPoint(new Vector2D(h_WaistToAbdomen, 0.0 - Arb_Con),
                new Vector2D(h_WaistToAbdomen, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4) + Arb_Con),
                                       "Abdomen");

        // Add construction keypoints for Hip Level
        fullBlock.addConstructionPoint(new Vector2D(i_WaistToSeat, 0.0 - Arb_Con),
                                       new Vector2D(i_WaistToSeat, g_BkHipArc / 2 + Arb_HemLevelY / 4 + 3.0 * Arb_Con),
                                       "Seat");

        // Add construction keypoints for Abdomen Level
        fullBlock.addConstructionPoint(new Vector2D(j_WaistToHip, 0.0 - Arb_Con),
                new Vector2D(j_WaistToHip, (g_BkHipArc / 2 + Arb_HemLevelY / 4) + (f_FrHipArc / 2 + Arb_HemLevelY / 4) + Arb_Con),
                                       "Hip");
    }
}