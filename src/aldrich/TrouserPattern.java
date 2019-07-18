package aldrich;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class TrouserPattern
        extends Pattern
{
    /* Pattern-specific Measurements */

    // Measurements listed in the Aldrich book
    private double a_Waist;
    private double b_Hips;                      // Actually used
    private double c_WaistToHip;                // Actually used
    private double d_BodyRise;                  // Actually used
    private double e_WaistToFloor;
    private double f_TrouserBottomWidth;

    // Custom measurement used for block creation
    private double g_HipCHeight;                // Actually used
    private double h_CrotchHeight;              // Actually used

    /* Arbitrary Measurements */

    // Arb measurement for hem width
    private double Arb_HemWidth;

    // Arb measurements for front dart
    private double Arb_FrontDartWidth;
    private double Arb_FrontDartLength;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public TrouserPattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Populate arbitrary measurements
        Arb_HemWidth = 22.0;

        // Create the blocks
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected EMethod assignMethod()
    {
        return EMethod.ALDRICH;
    }

    @Override
    protected EGarment assignGarment()
    {
        return EGarment.TROUSER;
    }

    @Override
    protected void addEasement()
    {
        // No easement needed
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {
            // Based on measurements for this pattern we can read the following from the scan
            b_Hips = dataStore.getMeasurement("A31").value + dataStore.getMeasurement("A32").value;
            c_WaistToHip = dataStore.getMeasurement("A15").value;
            d_BodyRise = dataStore.getMeasurement("A38").value;
            g_HipCHeight = dataStore.getMeasurement("A44").value;
            h_CrotchHeight = dataStore.getMeasurement("A43").value;

            // Get name
            userName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            addMissingMeasurement(dataStore.getName(), e.getMeasurementId());
            return false;
        }
    }

    /**
     * The actual block creation process following the drafting method of Aldrich.
     * NOTE: This is using a step-by-step guide created for easier coding
     */
    @Override
    protected void createBlocks() {
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Front Half Block */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Create component representing half back of trouser folded in half.
        blocks.add(new Block(userName + "_Aldrich_Front_Block"));
        Block frontblock = blocks.get(0);

        //first calculate

        // Step 9
        frontblock.addKeypoint(new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))));

        // Step 15
        frontblock.addKeypoint(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), -((Arb_HemWidth / 2.0) - 0.5 + 1.3)));

        // Step 14
        frontblock.addKeypoint(new Vector2D((c_WaistToHip + g_HipCHeight), -((Arb_HemWidth / 2.0) - 0.5)));

        // Step 12
        frontblock.addKeypoint(new Vector2D((c_WaistToHip + g_HipCHeight), ((Arb_HemWidth / 2.0) - 0.5)));

        // Step 13
        frontblock.addKeypoint(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), ((Arb_HemWidth / 2.0) - 0.5 + 1.3)));

        // Step 8
        frontblock.addKeypoint(new Vector2D(c_WaistToHip, ((b_Hips / 4.0) + 0.5) - ((b_Hips / 12) + 2)));

        // Step 11
        frontblock.addKeypoint(new Vector2D(0.0, ((b_Hips / 4.0) + 1.0) - ((b_Hips / 12.0) + 2)));

        // Step 10
        frontblock.addKeypoint(new Vector2D(0.0, (-((b_Hips / 12.0) + 2.0) + 1.0)));

        // Step 6
        frontblock.addKeypoint(new Vector2D((c_WaistToHip), -((b_Hips / 12.0) + 2.0)));

        // Step 9 (back to beginning)
        frontblock.addKeypoint(new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))));

        // Add front dart between points 11 and 10
        Vector2D startSegment = new Vector2D(0.0, ((b_Hips / 4.0) + 1.0) - ((b_Hips / 12.0) + 2));
        Vector2D endSegment = new Vector2D(0.0, (-((b_Hips / 12.0) + 2.0) - 1.0));
        double positionTopDart = 0.5;
        Arb_FrontDartWidth = 2.0;
        Arb_FrontDartLength = 10.0;
        ArrayList<Vector2D> dartPoints = frontblock.addDart(startSegment,
                endSegment,
                positionTopDart,
                Arb_FrontDartWidth,
                Arb_FrontDartLength,
                true,
                false
        );

        // Adding curve from Step 9 --> 15
        frontblock.addCircularCurve(new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))),
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), -((Arb_HemWidth / 2.0) - 0.5 + 1.3)),
                1.5,
                false
        );

        // Adding curve from Step 13 --> 8
        frontblock.addCircularCurve(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), ((Arb_HemWidth / 2.0) - 0.5 + 1.3)),
                new Vector2D(c_WaistToHip, ((b_Hips / 4.0) + 0.5) - ((b_Hips / 12) + 2)),
                0.5,
                true
        );

        // Adding curve from Step 8 --> 11
        frontblock.addCircularCurve(new Vector2D(c_WaistToHip, ((b_Hips / 4.0) + 0.5) - ((b_Hips / 12) + 2)),
                new Vector2D(0.0, ((b_Hips / 4.0) + 1.0) - ((b_Hips / 12.0) + 2)),
                0.5,
                true
                );

        // Adding curve from Step 6 --> 9
        frontblock.addCircularCurve(new Vector2D(c_WaistToHip, -((b_Hips / 12.0) + 2.0)),
                new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))),
                1.5,
                false
                );

        // Add construction keypoints for Centre Fold
        frontblock.addConstructionPoint(new Vector2D(0.0 - Arb_Con, 0.0),
                new Vector2D((c_WaistToHip + g_HipCHeight) + Arb_Con, 0.0),
                "Centre Fold");

        // Add construction keypoints for Crutch Depth
        frontblock.addConstructionPoint(new Vector2D(d_BodyRise, ((b_Hips / 4.0) + 0.5) - ((b_Hips / 12) + 2) + Arb_Con),
                new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0)) - Arb_Con),
                "Crutch Depth");

        // Add construction keypoints for Knee Line
        frontblock.addConstructionPoint(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), ((Arb_HemWidth / 2.0) - 0.5 + 1.3) + Arb_Con),
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), -((Arb_HemWidth / 2.0) - 0.5 + 1.3) - Arb_Con),
                "Knee Line");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Back Half Block */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Create component representing half back of trouser folded in half.
        blocks.add(new Block(userName + "_Aldrich_Back_Block"));
        Block backblock = blocks.get(1);

        // Adding Step 24
        backblock.addKeypoint(new Vector2D((d_BodyRise + 0.5), -((b_Hips/12)+2+(b_Hips/16)+1+0.8+(((b_Hips/16)+1)/2))));

        // Adding Step 29
        backblock.addKeypoint(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), -((Arb_HemWidth / 2.0) - 0.5 + 1.3) - 1.0));

        // Adding Step 28
        backblock.addKeypoint(new Vector2D((c_WaistToHip + g_HipCHeight), -((Arb_HemWidth / 2.0) - 0.5) - 1.0));

        // Adding Step 28.1
        backblock.addKeypoint(new Vector2D(((c_WaistToHip + g_HipCHeight) + 1.0), 0.0));

        // Adding Step 26
        backblock.addKeypoint(new Vector2D((c_WaistToHip + g_HipCHeight),((Arb_HemWidth / 2.0) - 0.5) + 1.0));

        // Adding Step 27
        backblock.addKeypoint(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),((Arb_HemWidth / 2.0) - 0.5 + 1.3) + 1.0));

        // Adding Step 25
        backblock.addKeypoint(new Vector2D(c_WaistToHip, (((b_Hips / 4.0) + 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))));

        // Adding Step 22
        backblock.addKeypoint(new Vector2D(0.0, ((b_Hips / 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))));

        // Adding Step 21
        backblock.addKeypoint(new Vector2D(-2.0, (-(((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0) - 2))));

        // Adding Step 19
        backblock.addKeypoint(new Vector2D((d_BodyRise / 2.0), (-(((b_Hips / 12.0) + 2) - ((((b_Hips / 12.0)) + 2.0) / 4.0)))));

        // Adding Step 24
        backblock.addKeypoint(new Vector2D((d_BodyRise + 0.5), -((b_Hips/12)+2+(b_Hips/16)+1+0.8+(((b_Hips/16)+1)/2))));

        // Add first back dart between points 22 and 21
        Vector2D startSegment2 = new Vector2D(0.0, ((b_Hips / 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0)));
        Vector2D endSegment2 = new Vector2D(-2.0, (-(((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0) - 2)));
        double positionTopDart2 = 1.0 / 3.0;
        Arb_FrontDartWidth = 2.0;
        Arb_FrontDartLength = 10.0;
        ArrayList<Vector2D> dartPoints2 = backblock.addDart(startSegment2,
                endSegment2,
                positionTopDart2,
                Arb_FrontDartWidth,
                Arb_FrontDartLength,
                true,
                false
        );

        // Add second back dart between points 22 and 21
        Vector2D startSegment3 = dartPoints2.get(2);
        Vector2D endSegment3 = new Vector2D(-2.0, (-(((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0) - 2)));
        double positionTopDart3 = 1.0 / 2.0;
        Arb_FrontDartWidth = 2.0;
        Arb_FrontDartLength = 12.0;
        ArrayList<Vector2D> dartPoints3 = backblock.addDart(startSegment3,
                endSegment3,
                positionTopDart3,
                Arb_FrontDartWidth,
                Arb_FrontDartLength,
                true,
                false
        );

        // Adding curve from Step 24 --> 29
        backblock.addCircularCurve(new Vector2D((d_BodyRise + 0.5), -((b_Hips/12)+2+(b_Hips/16)+1+0.8+(((b_Hips/16)+1)/2))),
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), -((Arb_HemWidth / 2.0) - 0.5 + 1.3) - 1.0),
                0.5,
                false
        );

        // Adding curve from Step 28 --> 28.1
        backblock.addCircularCurve(new Vector2D((c_WaistToHip + g_HipCHeight), -((Arb_HemWidth / 2.0) - 0.5) - 1.0),
                new Vector2D(((c_WaistToHip + g_HipCHeight) + 1.0), 0.0),
                0.25,
                true
        );

        // Adding curve from Step 28.1 --> 26
        backblock.addCircularCurve(new Vector2D(((c_WaistToHip + g_HipCHeight) + 1.0), 0.0),
                new Vector2D((c_WaistToHip + g_HipCHeight),((Arb_HemWidth / 2.0) - 0.5) + 1.0),
                0.25,
                true
        );

        // Adding curve from Step 27 --> 25
        backblock.addCircularCurve(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),((Arb_HemWidth / 2.0) - 0.5 + 1.3) + 1.0),
                new Vector2D(c_WaistToHip, (((b_Hips / 4.0) + 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))),
                0.5,
                true
        );

        // Adding curve from Step 25 --> 22
        backblock.addCircularCurve(new Vector2D(c_WaistToHip, (((b_Hips / 4.0) + 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))),
                new Vector2D(0.0, ((b_Hips / 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0))),
                0.5,
                true
        );

        // Adding curve from Step 19 --> 24
        backblock.addCircularCurve(new Vector2D((d_BodyRise / 2.0), (-(((b_Hips / 12.0) + 2) - ((((b_Hips / 12.0)) + 2.0) / 4.0)))),
                new Vector2D((d_BodyRise + 0.5), -((b_Hips/12)+2+(b_Hips/16)+1+0.8+(((b_Hips/16)+1)/2))),
                1.0,
                false
        );

        // Add construction keypoints for Centre Fold
        backblock.addConstructionPoint(new Vector2D(0.0 - ( 6.0 * Arb_Con), 0.0),
                new Vector2D(((c_WaistToHip + g_HipCHeight) + 1.0) + Arb_Con, 0.0),
                "Centre Fold");

        // Add construction keypoints for Crutch Depth
        backblock.addConstructionPoint(new Vector2D((d_BodyRise + 0.5), (((b_Hips / 4.0) + 4.0) - ((b_Hips / 12.0) + 2.0) - (((b_Hips / 12.0) + 2.0) / 4.0)) + Arb_Con),
                new Vector2D((d_BodyRise + 0.5), (-((((b_Hips / 12) + 2.0) + ((b_Hips / 16.0) + 1.0) + ((b_Hips / 16.0) + 1.0)) / 2.0 + 0.8)) - Arb_Con),
                "Crutch Depth");

        // Add construction keypoints for Knee Line
        backblock.addConstructionPoint(new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)),((Arb_HemWidth / 2.0) - 0.5 + 1.3) + 1.0 + Arb_Con),
                new Vector2D((d_BodyRise + ((h_CrotchHeight / 2.0) - 5.0)), -((Arb_HemWidth / 2.0) - 0.5 + 1.3) - 1.0 - Arb_Con),
                "Knee Line");
    }
}
