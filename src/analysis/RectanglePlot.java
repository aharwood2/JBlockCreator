package analysis;

import dxfwriter.DxfFile;
import jblockenums.EAnalysis;
import jblockmain.IPlottable;
import jblockmain.Measurements;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RectanglePlot implements IPlottable
{
    /* Technique Specific Variables */

    // List of Rectangles in this plot
    private ArrayList<Rectangle> rectangles;

    // Enum for analysis type
    private final EAnalysis analysis = EAnalysis.RECTANGLEPLOT;

    // Variables for x,y axis measurement ID
    private final String xAxisID;
    private final String yAxisID;
    private final String measurementFileName;
    private final boolean isLayered;
    private final boolean isRect;
    private final int numUsers;
    private final Measurements measurements;

    // Constructor
    public RectanglePlot(Measurements _measurements, String measurementIdX, String measurementIdY,
                         boolean isLayeredPlot, boolean isRectangle)
    {
        measurements = _measurements;
        rectangles = new ArrayList<>();
        xAxisID = measurementIdX;
        yAxisID = measurementIdY;
        measurementFileName = measurements.getScanDataFileName();
        isLayered = isLayeredPlot;
        isRect = isRectangle;
        numUsers = measurements.getNames().size();

    }

    // Method to add a new Rectangle to the plot
    public void addNewRectangle()
    {
        // Add new rectangle
        rectangles.add(
                new Rectangle(
                        0.0,
                        0.0,
                        measurements.getMeasurement(xAxisID).value,
                        measurements.getMeasurement(yAxisID).value
                )
        );

    }

    /* Interface implementation */
    private void rangeCheck(int blockNumber)
    {
        if (blockNumber > rectangles.size()) throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
    }

    @Override
    public ArrayList<Double> getXPoints(int blockNumber) throws Exception
    {
        rangeCheck(blockNumber);
        return rectangles.get(blockNumber).getX();
    }

    @Override
    public ArrayList<Double> getYPoints(int blockNumber) throws Exception
    {
        rangeCheck(blockNumber);
        return rectangles.get(blockNumber).getY();
    }

    @Override
    public ArrayList<Double> getXCtPoints(int blockNumber) throws Exception
    {
        rangeCheck(blockNumber);
        return null;
    }

    @Override
    public ArrayList<Double> getYCtPoints(int blockNumber) throws Exception
    {
        rangeCheck(blockNumber);
        return null;
    }

    private ArrayList<String> getNames(int blockNumber) throws IndexOutOfBoundsException
    {
        rangeCheck(blockNumber);
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
        // Construct output path
        Path path = Paths.get(fileOutput.toString() + "/" + analysis + "/");

        // Create directory structure if required
        try
        {
            Files.createDirectories(path);
        }
        catch (java.io.IOException e)
        {
            System.err.println("Cannot create directories - " + e);
        }

        if (isRect)
        {
            ArrayList<String> names = measurements.getNames();
            for (int i = 0; i < rectangles.size(); i++) {

                // Create new DXF file
                String filename = names.get(i) + "_" + xAxisID + "_" + yAxisID + "_Rectangle_Plot";
                DxfFile file = new DxfFile(path.toString() + "/" + filename);

                try {
                    file.addLines(getXPoints(i), getYPoints(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                file.writeFile(filename, dxfLayerChooser);
            }
        }

        if (isLayered)
        {
            // Create new DXF file
            String filename = "Layered_" + xAxisID + "_" + yAxisID + "_Rectangle_Plot";
            DxfFile file = new DxfFile(path.toString() + "/" + filename);
            try
            {
                for (int i = 0; i < rectangles.size(); i++)
                {
                    file.addLines(getXPoints(i), getYPoints(i));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            file.writeFile(filename, dxfLayerChooser);
        }
    }
}
