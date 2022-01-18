package analysis;

import dxfwriter.DxfFile;
import jblockenums.EAnalysis;
import jblockmain.IPlottable;
import jblockmain.InputFileData;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RectanglePlot
        implements IPlottable
{
    /* Technique Specific Variables */

    // Enum for analysis type
    private final EAnalysis analysis = EAnalysis.RECTANGLEPLOT;
    // Variables for x,y axis measurement ID
    private final String xAxisID;
    private final String yAxisID;
    private final boolean isLayered;
    private final boolean isRect;
    private final InputFileData fileData;
    // List of Rectangles in this plot
    private final ArrayList<Rectangle> rectangles = new ArrayList<>();

    // Constructor
    public RectanglePlot(InputFileData inputFileData, String measurementIdX, String measurementIdY,
                         boolean isLayeredPlot, boolean isRectangle)
    {
        fileData = inputFileData;
        xAxisID = measurementIdX;
        yAxisID = measurementIdY;
        isLayered = isLayeredPlot;
        isRect = isRectangle;
    }

    // Method to add a new Rectangle to the plot
    public void addNewRectangle(String userName)
    {
        // Add new rectangle
        rectangles.add(
                new Rectangle(
                        0.0,
                        0.0,
                        fileData.getInputValue(userName, xAxisID).value,
                        fileData.getInputValue(userName, yAxisID).value
                )
        );

    }

    /* Interface implementation */
    private void rangeCheck(int blockNumber)
    {
        if (blockNumber > rectangles.size())
            throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
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

    @Override
    public int getNumberOfBlocksToPlot()
    {
        return rectangles.size();
    }

    @Override
    public void writeToDXF(File fileOutput, boolean[] dxfLayerChooser, String timeStamp)
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
            ArrayList<String> names = fileData.getUserNames();
            for (int i = 0; i < rectangles.size(); i++)
            {

                // Create new DXF file
                String filename = names.get(i) + "_" + xAxisID + "_" + yAxisID + "_Rectangle_Plot" + timeStamp;
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
        }

        if (isLayered)
        {
            // Create new DXF file
            String filename;
            if (timeStamp == null)
                filename = "Layered_" + xAxisID + "_" + yAxisID + "_Rectangle_Plot";
            else
                filename = "Layered_" + xAxisID + "_" + yAxisID + "_Rectangle_Plot" + "_" + timeStamp;

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
