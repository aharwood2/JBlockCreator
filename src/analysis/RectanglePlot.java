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
    private int xAxisID;
    private int yAxisID;
    private String userID;
    private boolean isLayered;
    private int numUsers;

    // Constructor
    public RectanglePlot()
    {
        rectangles = new ArrayList<>();
    }

    // Method to add a new Rectangle to the plot
    public void addNewRectangle(Measurements measurements, int measurementIdX, int measurementIdY, boolean isLayeredPlot)
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
        xAxisID = measurementIdX;
        yAxisID = measurementIdY;
        userID = measurements.getName();
        isLayered = isLayeredPlot;
        numUsers = measurements.getNames().size();
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
        for (int i = 0; i < rectangles.size(); i++)
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

            // Create new DXF file
            String filename = userID + "_" + xAxisID + "_" + yAxisID + "_Rectangle_Plot";
            DxfFile file = new DxfFile(path.toString() + "/" + filename);
            try
            {
                file.addLines(getXPoints(i), getYPoints(i));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            file.writeFile(filename, dxfLayerChooser);
        }

        if (isLayered && rectangles.size() == numUsers)
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
