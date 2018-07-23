package jblockmain;

import dxfwriter.DxfFile;
import jblockenums.EGarment;
import jblockenums.EMethod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/** Interface to be implemented by every pattern added to the module. */
public abstract class Pattern implements IPlottable
{
    // Offset used for drawing of construction lines
    protected double Arb_Con = 2.0;

    // User associated with the pattern
    protected String userName;

    // Method associated with pattern
    protected final EMethod method;
    protected final EGarment garment;

    // Abstract method to assign final method type
    protected abstract EMethod assignMethod();
    protected abstract EGarment assignGarment();

    // Constructor to initialise variables
    public Pattern()
    {
        blocks = new ArrayList<Block>();
        method = assignMethod();
        garment = assignGarment();
    }

    // Blocks that comprise the pattern
    protected ArrayList<Block> blocks;

    // Obtain measurements from the body scan required by the pattern
    protected abstract boolean readMeasurements(Measurements dataStore);

    // Modify any measurements read from the scan by adding ease
    protected abstract void addEasement();

    // Start the creation of the blocks for the pattern
    protected abstract void createBlocks();

    /* Interface implementation */
    private void rangeCheck(int blockNumber)
    {
        if (blockNumber > blocks.size()) throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
    }

    @Override
    public ArrayList<Double> getXPoints(int blockNumber) throws IndexOutOfBoundsException
    {
        rangeCheck(blockNumber);
        return blocks.get(blockNumber).getPlottableKeypointsX();
    }

    @Override
    public ArrayList<Double> getYPoints(int blockNumber) throws IndexOutOfBoundsException
    {
        rangeCheck(blockNumber);
        return blocks.get(blockNumber).getPlottableKeypointsY();
    }

    @Override
    public ArrayList<Double> getXCtPoints(int blockNumber) throws IndexOutOfBoundsException
    {
        rangeCheck(blockNumber);
        return blocks.get(blockNumber).getConstructionX();
    }

    @Override
    public ArrayList<Double> getYCtPoints(int blockNumber) throws IndexOutOfBoundsException
    {
        rangeCheck(blockNumber);
        return blocks.get(blockNumber).getConstructionY();
    }

    public ArrayList<String> getNames(int blockNumber) throws IndexOutOfBoundsException
    {
        rangeCheck(blockNumber);
        return blocks.get(blockNumber).getConstructionNames();
    }

    @Override
    public int getNumberOfBlocksToPlot()
    {
        return blocks.size();
    }

    public void writeToDXF(File fileOutput, boolean[] dxfLayerChooser)
    {
        for (int i = 0; i < getNumberOfBlocksToPlot(); i++)
        {
            // Construct output path
            Path path = Paths.get(fileOutput.toString() + "/" + method + "/" + garment + "/");
    
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
            DxfFile file = new DxfFile(path.toString() + "/" + blocks.get(i).getName());
            try
            {
                file.addLines(getXPoints(i), getYPoints(i));
                file.addConstructionPoints(getXCtPoints(i), getYCtPoints(i), getNames(i));
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
            file.writeFile(blocks.get(i).getName(), dxfLayerChooser);
        }
    }
}
