package aldrich;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

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

    /* Arbitrary Measurements */




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public TrouserPattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Populate arbitrary measurements


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
            b_Hips = dataStore.getId(31).value + dataStore.getId(32).value;
            c_WaistToHip = dataStore.getId(15).value;
            d_BodyRise = dataStore.getId(38).value;
            g_HipCHeight = dataStore.getId(44).value;


            // Get name
            userName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            Pattern.addMissingMeasurement(dataStore.getName(), method.toString(), garment.toString());
            return false;
        }
    }

    /**
     * The actual block creation process following the drafting method of Aldrich.
     */
    @Override
    protected void createBlocks()
    {
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity for
        // plotting. The bottom left corner of the space to be the origin.

        // Create component representing half back of skirt folded in half.
        blocks.add(new Block(userName + "_Aldrich_Front_Block"));
        Block frontblock = blocks.get(0);

        // Origin
        frontblock.addKeypoint(new Vector2D(0.0, 0.0));

        // Step 1
        frontblock.addKeypoint(new Vector2D(d_BodyRise, 0.0));

        // Step 9
        frontblock.addKeypoint(new Vector2D(d_BodyRise, -(((b_Hips / 12.0) + 2.0) + ((b_Hips / 16.0) + 1.0))));

        // Step 15
        frontblock.addKeypoint(new Vector2D(((c_WaistToHip + g_HipCHeight - d_BodyRise) / 2.0), -((22.0 / 2.0) - 0.5 + 1.3)));

        // Step 14
        frontblock.addKeypoint(new Vector2D((c_WaistToHip + g_HipCHeight), -((22.0 / 2.0) - 0.5)));
    }

}
