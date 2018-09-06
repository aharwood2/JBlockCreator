package jblockmain;

import dxfwriter.DxfFile;
import jblockenums.EGarment;
import jblockenums.EMethod;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.FileWriter;


/** Interface to be implemented by every pattern added to the module. */
public abstract class Pattern implements IPlottable
{
    /**
     * Offset used for drawing of construction lines
     */
    protected double Arb_Con = 2.0;

    /**
     * User associated with the pattern
     */
    protected String userName;

    /**
     * Method associated with pattern
     */
    protected final EMethod method;

    /**
     * Type of garment pattern represents
     */
    protected final EGarment garment;

    /**
     * Abstract method to assign final method type.
     * @return method type to assign to the method field.
     */
    protected abstract EMethod assignMethod();

    /**
     * Abstract method to assign final garment type.
     * @return method type to assign to the garment field.
     */
    protected abstract EGarment assignGarment();

    /**
     * Common store of missing measurements.
     */
    protected static ArrayList<String> missingMeasurements = new ArrayList<String>();

    /**
     * Constructor
     */
    public Pattern()
    {
        blocks = new ArrayList<Block>();
        method = assignMethod();
        garment = assignGarment();
    }

    /**
     * Method to add information about a failed pattern creation due to missing measurements.
     * @param userid    name of the user concerned.
     * @param id        ID of the measurement which caused the failure.
     */
    protected void addMissingMeasurement(String userid, String id)
    {
        missingMeasurements.add(userid + "/" + method + "/" + garment + " : Measurement ID = " + id);
    }

    /**
     * Method to print the missing measurements record to a file.
     * @param fileoutput    path to file.
     */
    protected static void printMissingMeasurements(File fileoutput)
    {
        if (missingMeasurements.size() > 0)
        {
            try
            {
                FileWriter writer = new FileWriter(fileoutput + "/" + JBlock.failedOutputsFilename);
                BufferedWriter writer2 = new BufferedWriter(writer);
                for (String str : missingMeasurements)
                {
                    writer2.append(str);
                    writer2.newLine();
                }
                writer2.close();
                writer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Blocks that comprise the pattern
     */
    protected ArrayList<Block> blocks;

    /**
     * Obtain measurements from the measurements hashmap as required by the pattern.
     * @param dataStore the object holding all acquired measurement data.
     * @return  inidication as to whether reading was successful.
     */
    protected abstract boolean readMeasurements(Measurements dataStore);

    /**
     * Modify any measurements by adding easement.
     */
    protected abstract void addEasement();

    /**
     * Create the blocks for this pattern.
     */
    protected abstract void createBlocks();

    /**
     * Method to check that a block number index is within the range of blocks stored in the pattern.
     * @param blockNumber   number to check.
     */
    private void rangeCheck(int blockNumber)
    {
        if (blockNumber > blocks.size()) throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
    }

    /* Interface implementation */
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

    @Override
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
