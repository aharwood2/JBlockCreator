package analysis;

import jblockexceptions.MeasurementNotFoundException;
import jblockmain.Analysis;
import jblockmain.Block;
import jblockmain.JBlock;
import jblockmain.Measurements;
import jblockenums.EAnalysis;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class RectanglePlot
    extends Analysis
{
    /* Technique Specific Variables */
    private static double x_Axis;
    private static int xID;
    private static double y_Axis;
    private static int yID;
    private static ArrayList<Double> x_Values = new ArrayList<Double>();
    private static ArrayList<Double> y_Values = new ArrayList<Double>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public RectanglePlot(Measurements dataStore, int xAxisID, int yAxisID, boolean isLayered, int loopNum)
        {
        xID = xAxisID;
        yID = yAxisID;

        if (!readMeasurements(dataStore)) return;
        addEasement();

        // Create the blocks
        createBlocks();

        if (isLayered)
        {
            isLayeredYes();
        }
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
        try
        {
            x_Axis = dataStore.getId(xID).value;
            y_Axis = dataStore.getId(yID).value;

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

        // Create rectangle plot
        blocks.add(new Block(userName + "_" + String.valueOf(xID) + "_" + String.valueOf(yID) + "_Rectangle_Plot"));
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

    // Method to create a layered rectangle plot output
    private static void isLayeredYes()
    {
        x_Values.add(x_Axis);
        y_Values.add(y_Axis);

//        if (x_Values.size() == 1 && y_Values.size() == 1)
//        {
//            // Create rectangle plot
//            blocks.add(new Block("layered_" + String.valueOf(xID) + "_" + String.valueOf(yID) + "_Rectangle_Plot"));
//            Block fullBlock = blocks.get(1);
//
//            // Adding the origin, constant for all plots
//            fullBlock.addKeypoint(new Vector2D(0.0, 0.0));
//
//            // Adding the bottom right point of the rectangle
//            fullBlock.addKeypoint(new Vector2D(x_Axis, 0.0));
//
//            // Adding the top right point of the rectangle
//            fullBlock.addKeypoint(new Vector2D(x_Axis, y_Axis));
//
//            // Adding the top left point of the rectangle
//            fullBlock.addKeypoint(new Vector2D(0.0, y_Axis));
//        }
//        else
//        {
//            // TODO get layered block from above as input and add the below rectangle plot on top of it
//            Block layers = blocks.get(1);
//
//            // Adding the origin, constant for all plots
//            layers.addKeypoint(new Vector2D(0.0, 0.0));
//
//            // Adding the bottom right point of the rectangle
//            layers.addKeypoint(new Vector2D(x_Axis, 0.0));
//
//            // Adding the top right point of the rectangle
//            layers.addKeypoint(new Vector2D(x_Axis, y_Axis));
//
//            // Adding the top left point of the rectangle
//            layers.addKeypoint(new Vector2D(0.0, y_Axis));
//        }
    }
}
