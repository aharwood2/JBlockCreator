package analysis;

import jblockexceptions.MeasurementNotFoundException;
import jblockmain.Analysis;
import jblockmain.Block;
import jblockmain.Measurements;
import jblockenums.EAnalysis;
import mathcontainers.Vector2D;

// TODO make this retrieve the x and y choices from the GUI and use them throughout, as is you can only use 038 & 040

public class RectanglePlot
    extends Analysis
{
    /* Technique Specific Variables */
    private Double x_Axis;
    private Double y_Axis;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public RectanglePlot(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Create the blocks
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected EAnalysis assignAnalysis()
    {
        return EAnalysis.RECTANGLEPLOT;
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
            x_Axis = dataStore.getId(40).value;
            y_Axis = dataStore.getId(38).value;

            // Get name
            userName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            Analysis.addMissingMeasurement(dataStore.getName(), analysis.toString());
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
        blocks.add(new Block(userName + "_Rectangle_Plot"));
        Block fullBlock = blocks.get(0);

        // Adding the origin, constant for all plots
        fullBlock.addKeypoint(new Vector2D(0.0, 0.0));

        // Adding the bottom right point of the rectangle
        fullBlock.addKeypoint(new Vector2D(x_Axis, 0.0));

        // Adding the top right point of the rectangle
        fullBlock.addKeypoint(new Vector2D(x_Axis, y_Axis));

        // Adding the top left point of the rectangle
        fullBlock.addKeypoint(new Vector2D(0.0, y_Axis));
    }
}
