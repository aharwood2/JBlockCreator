package analysis;

import dxfwriter.DxfFile;
import dxfwriter.DxfFileConfiguration;
import jblockenums.EAnalysis;
import jblockenums.EPlotType;
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
    private final EPlotType plotType;
    private final InputFileData fileData;
    // List of Rectangles in this plot
    private final ArrayList<Rectangle> rectangles = new ArrayList<>();

    // Constructor
    public RectanglePlot(InputFileData inputFileData,
                         String measurementIdX,
                         String measurementIdY,
                         EPlotType plotType)
    {
        fileData = inputFileData;
        xAxisID = measurementIdX;
        yAxisID = measurementIdY;
        this.plotType = plotType;
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
    public void writeToDXF(File fileOutput, DxfFileConfiguration config)
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

        if (plotType == EPlotType.RECTANGLE)
        {
            ArrayList<String> names = fileData.getUserNames();
            for (int i = 0; i < rectangles.size(); i++)
            {
                // Create new DXF file for each rectangle
                String filename = names.get(i) + "_" + xAxisID + "_" + yAxisID;
                if (config.getTimeStamp() != null) filename += "_" + config.getTimeStamp();;
                DxfFile file = new DxfFile(path.toString() + "/" + filename);

                try
                {
                    file.addLines(getXPoints(i), getYPoints(i));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                file.writeFile(filename, config);
            }
        }

        else
        {
            // Create new DXF file with all rectangles on one plot
            String filename = "Layered_" + xAxisID + "_" + yAxisID;
            if (config.getTimeStamp() != null) filename += "_" + config.getTimeStamp();

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
            file.writeFile(filename, config);
        }
    }
}
