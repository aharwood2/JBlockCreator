package aldrich;

import jblockenums.EPattern;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class TrouserPattern
        extends Pattern
{
    public TrouserPattern(String userName, InputFileData dataStore, MeasurementSet template)
    {
        super(userName, dataStore, template);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.ALDRICH_TROUSER;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("b_HipsFrontArc", "A31"));
        measurements.addMeasurement(new Measurement("b_HipsBackArc", "A32"));
        measurements.addMeasurement(new Measurement("c_WaistToSeat", "A34"));
        measurements.addMeasurement(new Measurement("d_BodyRise", "A38"));
        measurements.addMeasurement(new Measurement("g_SeatCHeight", "A45"));
        measurements.addMeasurement(new Measurement("h_CrotchHeight", "A43"));

        // Arbitrary measurement
        measurements.addMeasurement(new Measurement("Arb_HemWidth", 22.0));
        measurements.addMeasurement(new Measurement("Arb_FrontDartWidth", 2.0));
        measurements.addMeasurement(new Measurement("Arb_FrontDartLength", 10.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartWidth", 2.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartLength1", 10.0));
        measurements.addMeasurement(new Measurement("Arb_BackDartLength2", 12.0));
    }

    /**
     * The actual block creation process following the drafting method of Aldrich.
     * NOTE: This is using a step-by-step guide created for easier coding
     */
    @Override
    public void createBlocks()
    {
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        // Pull from store
        var b_Hips = get("b_HipsFrontArc") + get("b_HipsBackArc");
        var c_WaistToSeat = get("c_WaistToSeat");
        var d_BodyRise = get("d_BodyRise");
        var g_SeatCHeight = get("g_SeatCHeight");
        var h_CrotchHeight = get("h_CrotchHeight");
        var Arb_HemWidth = get("Arb_HemWidth");
        var Arb_FrontDartWidth = get("Arb_FrontDartWidth");
        var Arb_FrontDartLength = get("Arb_FrontDartLength");
        var Arb_BackDartWidth = get("Arb_BackDartWidth");
        var Arb_BackDartLength1 = get("Arb_BackDartLength1");
        var Arb_BackDartLength2 = get("Arb_BackDartLength2");



        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Front Half Block */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Create component representing half back of trouser folded in half.
        blocks.add(new Block(userName + "_Aldrich_Front_Block"));
        Block frontblock = blocks.get(0);

        // Step 9
        frontblock.addKeypoint(new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))));

        // Step 15
        frontblock.addKeypoint(
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), -((Arb_HemWidth / 2.0) - 0.5 + 1.3)));

        // Step 14
        frontblock.addKeypoint(new Vector2D((c_WaistToSeat + g_SeatCHeight), -((Arb_HemWidth / 2.0) - 0.5)));

        // Step 12
        frontblock.addKeypoint(new Vector2D((c_WaistToSeat + g_SeatCHeight), ((Arb_HemWidth / 2.0) - 0.5)));

        // Step 13
        frontblock.addKeypoint(
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), ((Arb_HemWidth / 2.0) - 0.5 + 1.3)));

        // Step 8
        frontblock.addKeypoint(new Vector2D(c_WaistToSeat, ((b_Hips / 4.0) + 0.5) - ((b_Hips / 12) + 2)));

        // Step 11
        frontblock.addKeypoint(new Vector2D(0.0, ((b_Hips / 4.0) + 1.0) - ((b_Hips / 12.0) + 2)));

        // Step 10
        frontblock.addKeypoint(new Vector2D(0.0, (-((b_Hips / 12.0) + 2.0) + 1.0)));

        // Step 6
        frontblock.addKeypoint(new Vector2D((c_WaistToSeat), -((b_Hips / 12.0) + 2.0)));

        // Step 9 (back to beginning)
        frontblock.addKeypoint(new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))));

        // Add front dart between points 11 and 10
        Vector2D startSegment = new Vector2D(0.0, ((b_Hips / 4.0) + 1.0) - ((b_Hips / 12.0) + 2));
        Vector2D endSegment = new Vector2D(0.0, (-((b_Hips / 12.0) + 2.0) - 1.0));
        double positionTopDart = 0.5;
        ArrayList<Vector2D> dartPoints = frontblock.addDart(startSegment,
                                                            endSegment,
                                                            positionTopDart,
                                                            Arb_FrontDartWidth,
                                                            Arb_FrontDartLength,
                                                            true,
                                                            false
        );

        // Adding curve from Step 9 --> 15
        frontblock.addCircularArc(new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))),
                                    new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),
                                                 -((Arb_HemWidth / 2.0) - 0.5 + 1.3)),
                                    1.5,
                                    false
        );

        // Adding curve from Step 13 --> 8
        frontblock.addCircularArc(
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), ((Arb_HemWidth / 2.0) - 0.5 + 1.3)),
                new Vector2D(c_WaistToSeat, ((b_Hips / 4.0) + 0.5) - ((b_Hips / 12) + 2)),
                0.5,
                true
        );

        // Adding curve from Step 8 --> 11
        frontblock.addCircularArc(new Vector2D(c_WaistToSeat, ((b_Hips / 4.0) + 0.5) - ((b_Hips / 12) + 2)),
                                    new Vector2D(0.0, ((b_Hips / 4.0) + 1.0) - ((b_Hips / 12.0) + 2)),
                                    0.5,
                                    true
        );

        // Adding curve from Step 6 --> 9
        frontblock.addCircularArc(new Vector2D(c_WaistToSeat, -((b_Hips / 12.0) + 2.0)),
                                    new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))),
                                    1.5,
                                    false
        );

        // Add construction keypoints for Centre Fold
        frontblock.addConstructionPoint(new Vector2D(0.0 - Arb_Con, 0.0),
                                        new Vector2D((c_WaistToSeat + g_SeatCHeight) + Arb_Con, 0.0),
                                        "Centre Fold");

        // Add construction keypoints for Crutch Depth
        frontblock.addConstructionPoint(
                new Vector2D(d_BodyRise, ((b_Hips / 4.0) + 0.5) - ((b_Hips / 12) + 2) + Arb_Con),
                new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0)) - Arb_Con),
                "Crotch Depth");

        // Add construction keypoints for Knee Line
        frontblock.addConstructionPoint(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),
                                                     ((Arb_HemWidth / 2.0) - 0.5 + 1.3) + Arb_Con),
                                        new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),
                                                     -((Arb_HemWidth / 2.0) - 0.5 + 1.3) - Arb_Con),
                                        "Knee Line");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Back Half Block */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Create component representing half back of trouser folded in half.
        blocks.add(new Block(userName + "_Aldrich_Back_Block"));
        Block backblock = blocks.get(1);

        // Adding Step 24
        backblock.addKeypoint(new Vector2D((d_BodyRise + 0.5),
                                           -((b_Hips / 12.0) + 2.0 + (b_Hips / 16.0) + 1.0 + 0.8 + (((b_Hips / 16.0) + 1.0) / 2.0))));

        // Adding Step 29
        backblock.addKeypoint(
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), -((Arb_HemWidth / 2.0) - 0.5 + 1.3) - 1.0));

        // Adding Step 28
        backblock.addKeypoint(new Vector2D((c_WaistToSeat + g_SeatCHeight), -((Arb_HemWidth / 2.0) - 0.5) - 1.0));

        // Adding Step 28.1
        backblock.addKeypoint(new Vector2D(((c_WaistToSeat + g_SeatCHeight) + 1.0), 0.0));

        // Adding Step 26
        backblock.addKeypoint(new Vector2D((c_WaistToSeat + g_SeatCHeight), ((Arb_HemWidth / 2.0) - 0.5) + 1.0));

        // Adding Step 27
        backblock.addKeypoint(
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), ((Arb_HemWidth / 2.0) - 0.5 + 1.3) + 1.0));

        // Adding Step 25
        backblock.addKeypoint(new Vector2D(c_WaistToSeat,
                                           (((b_Hips / 4.0) + 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))));

        // Adding Step 22
        backblock.addKeypoint(
                new Vector2D(0.0, ((b_Hips / 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))));

        // Adding Step 21
        backblock.addKeypoint(new Vector2D(-2.0, (-(((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0) - 2))));

        // Adding Step 19
        backblock.addKeypoint(
                new Vector2D((d_BodyRise / 2.0), (-(((b_Hips / 12.0) + 2) - ((((b_Hips / 12.0)) + 2.0) / 4.0)))));

        // Adding Step 24
        backblock.addKeypoint(new Vector2D((d_BodyRise + 0.5),
                                           -((b_Hips / 12) + 2 + (b_Hips / 16) + 1 + 0.8 + (((b_Hips / 16) + 1) / 2))));

        // Add first back dart between points 22 and 21
        Vector2D startSegment2 = new Vector2D(0.0,
                                              ((b_Hips / 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0)));
        Vector2D endSegment2 = new Vector2D(-2.0, (-(((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0) - 2)));
        double positionTopDart2 = 1.0 / 3.0;
        ArrayList<Vector2D> dartPoints2 = backblock.addDart(startSegment2,
                                                            endSegment2,
                                                            positionTopDart2,
                                                            Arb_BackDartWidth,
                                                            Arb_BackDartLength1,
                                                            true,
                                                            false
        );

        // Add second back dart between points 22 and 21
        Vector2D startSegment3 = dartPoints2.get(2);
        Vector2D endSegment3 = new Vector2D(-2.0, (-(((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0) - 2)));
        double positionTopDart3 = 1.0 / 2.0;
        ArrayList<Vector2D> dartPoints3 = backblock.addDart(startSegment3,
                                                            endSegment3,
                                                            positionTopDart3,
                                                            Arb_BackDartWidth,
                                                            Arb_BackDartLength2,
                                                            true,
                                                            false
        );

        // Adding curve from Step 24 --> 29
        backblock.addCircularArc(new Vector2D((d_BodyRise + 0.5),
                                                -((b_Hips / 12) + 2 + (b_Hips / 16) + 1 + 0.8 + (((b_Hips / 16) + 1) / 2))),
                                   new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),
                                                -((Arb_HemWidth / 2.0) - 0.5 + 1.3) - 1.0),
                                   0.5,
                                   false
        );

        // Adding curve from Step 28 --> 28.1
        backblock.addCircularArc(new Vector2D((c_WaistToSeat + g_SeatCHeight), -((Arb_HemWidth / 2.0) - 0.5) - 1.0),
                                   new Vector2D(((c_WaistToSeat + g_SeatCHeight) + 1.0), 0.0),
                                   0.25,
                                   true
        );

        // Adding curve from Step 28.1 --> 26
        backblock.addCircularArc(new Vector2D(((c_WaistToSeat + g_SeatCHeight) + 1.0), 0.0),
                                   new Vector2D((c_WaistToSeat + g_SeatCHeight), ((Arb_HemWidth / 2.0) - 0.5) + 1.0),
                                   0.25,
                                   true
        );

        // Adding curve from Step 27 --> 25
        backblock.addCircularArc(
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), ((Arb_HemWidth / 2.0) - 0.5 + 1.3) + 1.0),
                new Vector2D(c_WaistToSeat,
                             (((b_Hips / 4.0) + 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))),
                0.5,
                true
        );

        // Adding curve from Step 25 --> 22
        backblock.addCircularArc(new Vector2D(c_WaistToSeat,
                                                (((b_Hips / 4.0) + 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))),
                                   new Vector2D(0.0,
                                                ((b_Hips / 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))),
                                   0.5,
                                   true
        );

        // Adding curve from Step 19 --> 24
        backblock.addCircularArc(
                new Vector2D((d_BodyRise / 2.0), (-(((b_Hips / 12.0) + 2) - ((((b_Hips / 12.0)) + 2.0) / 4.0)))),
                new Vector2D((d_BodyRise + 0.5),
                             -((b_Hips / 12) + 2 + (b_Hips / 16) + 1 + 0.8 + (((b_Hips / 16) + 1) / 2))),
                1.0,
                false
        );

        // Add construction keypoints for Centre Fold
        backblock.addConstructionPoint(new Vector2D(0.0 - (6.0 * Arb_Con), 0.0),
                                       new Vector2D(((c_WaistToSeat + g_SeatCHeight) + 1.0) + Arb_Con, 0.0),
                                       "Centre Fold");

        // Add construction keypoints for Crutch Depth
        backblock.addConstructionPoint(new Vector2D((d_BodyRise + 0.5),
                                                    (((b_Hips / 4.0) + 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0)) + Arb_Con),
                                       new Vector2D((d_BodyRise + 0.5),
                                                    (-((((b_Hips / 12) + 2.0) + ((b_Hips / 16.0) + 1.0) + ((b_Hips / 16.0) + 1.0)) / 2.0 + 0.8)) - Arb_Con),
                                       "Crutch Depth");

        // Add construction keypoints for Knee Line
        backblock.addConstructionPoint(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),
                                                    ((Arb_HemWidth / 2.0) - 0.5 + 1.3) + 1.0 + Arb_Con),
                                       new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),
                                                    -((Arb_HemWidth / 2.0) - 0.5 + 1.3) - 1.0 - Arb_Con),
                                       "Knee Line");
    }
}