package jblockmain;

import dxfwriter.DxfFile;
import jblockenums.EAnalysis;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/** Interface to be implemented by every analysis technique added to the module. */
public abstract class Analysis implements IPlottableAnalysis
{
    // User associated with the technique output (only for single not layered)
    protected static String userName;

    // Analysis technique associated
    protected final EAnalysis analysis;

    // Abstract method to assign final analysis technique type
    protected abstract EAnalysis assignAnalysis();

    // Arraylist for missing measurements
    private static ArrayList<String> missingMeasurements = new ArrayList<String>();

    // Blocks that comprise the analysis technique
    protected static ArrayList<Block> blocks = new ArrayList<Block>();

    public Analysis()
    {
        analysis = assignAnalysis();
    }

    // Method for storing data of patterns that could not be created
    protected static void addMissingMeasurement(String userid, String technique)
    {
        missingMeasurements.add(userid + "/" + technique);
    }

    // Obtain measurements from the body scan required by the pattern
    protected abstract boolean readMeasurements(Measurements dataStore);

    // Modify any measurements read from the scan by adding ease
    protected abstract void addEasement();

    // Start the creation of the blocks for the pattern
    protected abstract void isLayeredNo();

    /* Interface implementaiton */
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

    private ArrayList<String> getNames(int blockNumber) throws IndexOutOfBoundsException
    {
        rangeCheck(blockNumber);
        return blocks.get(blockNumber).getConstructionNames();
    }

    @Override
    public int getNumberOfBlocksToPlot()
    {
        return blocks.size();
    }

    public void writeToDXFAnalysis(File fileOutput, boolean[] dxfLayersAnalysis)
    {
        for (int i = 0; i < getNumberOfBlocksToPlot(); i++)
        {
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
            file.writeFile(blocks.get(i).getName(), dxfLayersAnalysis);
        }
    }
}