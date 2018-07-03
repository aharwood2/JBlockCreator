package jblockmain;

import dxfwriter.DxfFile;

import java.util.ArrayList;

/** Interface to be implemented by every pattern added to the module. */
public abstract class Pattern implements IPlottable
{
    // Constructor to initialise variables
    public Pattern()
    {
        blocks = new ArrayList<Block>();
    }

    // Blocks that comprise the pattern
    protected ArrayList<Block> blocks;

    // Obtain measurements from the body scan required by the pattern
    protected abstract void readMeasurements(Measurements dataStore);

    // Modify any measurements read from the scan by adding ease
    protected abstract void addEasement();

    // Start the creation of the blocks for the pattern
    protected abstract void createBlocks();

    /* Interface implementation */
    @Override
    public ArrayList<Double> getXPoints(int blockNumber) throws IndexOutOfBoundsException
    {
        if (blockNumber > blocks.size()) throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
        return blocks.get(blockNumber).getPlottableKeypointsX();
    }

    @Override
    public ArrayList<Double> getYPoints(int blockNumber) throws IndexOutOfBoundsException
    {
        if (blockNumber > blocks.size()) throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
        return blocks.get(blockNumber).getPlottableKeypointsY();
    }

    @Override
    public ArrayList<Double> getXCtPoints(int blockNumber) throws IndexOutOfBoundsException
    {
        if (blockNumber > blocks.size()) throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
        return blocks.get(blockNumber).getConstructionX();
    }

    @Override
    public ArrayList<Double> getYCtPoints(int blockNumber) throws IndexOutOfBoundsException
    {
        if (blockNumber > blocks.size()) throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
        return blocks.get(blockNumber).getConstructionY();
    }

    public ArrayList<String> getNames(int blockNumber) throws IndexOutOfBoundsException
    {
        if (blockNumber > blocks.size()) throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
        return blocks.get(blockNumber).getConstructionNames();
    }

    @Override
    public int getNumberOfBlocksToPlot()
    {
        return blocks.size();
    }

    @Override
    public void writeToDXF(String path)
    {
        for (int i = 0; i < getNumberOfBlocksToPlot(); i++)
        {
            DxfFile file = new DxfFile(path + blocks.get(i).getName());
            try
            {
                file.addLines(getXPoints(i), getYPoints(i));
                file.addConstructionPoints(getXCtPoints(i), getYCtPoints(i), getNames(i));
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
            file.writeFile(blocks.get(i).getName());
        }

    }

}
