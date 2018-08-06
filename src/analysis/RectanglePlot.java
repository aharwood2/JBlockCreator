package analysis;

import jblockmain.IPlottable;
import jblockmain.Measurements;

import java.io.File;
import java.util.ArrayList;

public class RectanglePlot implements IPlottable
{
    /* Technique Specific Variables */

    // List of Rectangles in this plot
    private ArrayList<Rectangle> rectangles;

    // Constructor
    public RectanglePlot()
    {
        rectangles = new ArrayList<>();
    }

    // Method to add a new Rectangle to the plot
    public void addNewRectangle(Measurements measurements, int measurementIdX, int measurementIdY)
    {
        // Add new rectangle
        rectangles.add(
                new Rectangle(
                        0.0,
                        0.0,
                        measurements.getId(measurementIdX).value,
                        measurements.getId(measurementIdY).value
                )
        );
    }

    @Override
    public ArrayList<Double> getXPoints(int blockNumber) throws Exception
    {
        return rectangles.get(blockNumber).getX();
    }

    @Override
    public ArrayList<Double> getYPoints(int blockNumber) throws Exception
    {
        return rectangles.get(blockNumber).getY();
    }

    @Override
    public ArrayList<Double> getXCtPoints(int blockNumber) throws Exception
    {
        return null;
    }

    @Override
    public ArrayList<Double> getYCtPoints(int blockNumber) throws Exception
    {
        return null;
    }

    @Override
    public int getNumberOfBlocksToPlot()
    {
        return rectangles.size();
    }

    @Override
    public void writeToDXF(File fileOutput, boolean[] dxfLayerChooser)
    {
        // TODO
    }
}
