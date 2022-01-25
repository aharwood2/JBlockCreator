package jblockmain;

import dxfwriter.DxfFile;
import dxfwriter.DxfFileConfiguration;
import jblockenums.EPattern;
import jblockexceptions.MeasurementNotFoundException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Interface to be implemented by every pattern added to the module.
 */
public abstract class Pattern
        implements IPlottable
{
    /**
     * Measurement store based on required measurements for the pattern
     */
    protected MeasurementSet measurements = new MeasurementSet();

    /**
     * The type of pattern
     */
    protected final EPattern patternType;

    /**
     * Offset used for drawing of construction lines
     */
    protected double Arb_Con = 2.0;

    /**
     * User associated with this pattern object
     */
    protected final String userName;

    /**
     * Blocks that comprise the pattern
     */
    protected ArrayList<Block> blocks = new ArrayList<Block>();;

    /**
     * Constructor
     */
    public Pattern(String userName, InputFileData dataStore, MeasurementSet template)
    {
        this.userName = userName;
        patternType = assignPattern();

        // Initialises the measurement set
        try
        {
            defineRequiredMeasurements();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Pass the data store and templates through for mapping
        readMeasurements(dataStore, template);
    }

    public Pattern(String userName)
    {
        this(userName, null, null);
    }

    /**
     * Abstract method to assign final pattern type.
     *
     * @return method type to assign to the method field.
     */
    protected abstract EPattern assignPattern();

    /**
     * Method which defines the required measurements for this pattern by defining a measurement set.
     */
    protected abstract void defineRequiredMeasurements() throws Exception;

    /**
     * Read measurements into the measurement set from the input file.
     *
     * @param inputData the object holding all acquired input data from the file.
     */
    protected final void readMeasurements(InputFileData inputData, MeasurementSet template)
    {
        try
        {
            // Map template values into the measurement set
            if (template != null) measurements.mapFromTemplate(template);

            // Map the values from the input data into the measurement set
            if (inputData != null) measurements.mapFromInputData(userName, inputData);
        }
        catch (MeasurementNotFoundException e)
        {
            e.printStackTrace();
        }
    };

    /**
     * Create the blocks for this pattern.
     */
    public abstract void createBlocks();

    /**
     * Method to get a measurement from the measurement set
     * @param name name of the measurement to get
     * @return the value of the measurement
     */
    protected final double get(String name)
    {
        return measurements.getMeasurement(name).getValue();
    }

    /**
     * Method to check that a block number index is within the range of blocks stored in the pattern.
     *
     * @param blockNumber number to check.
     */
    private final void rangeCheck(int blockNumber)
    {
        if (blockNumber > blocks.size())
            throw new IndexOutOfBoundsException("Accessing out of range of number of blocks!");
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
    public void writeToDXF(File fileOutput, DxfFileConfiguration config)
    {
        for (int i = 0; i < getNumberOfBlocksToPlot(); i++)
        {
            // Construct output path
            Path path = Paths.get(fileOutput.toString() + "/" + patternType + "/");

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
            DxfFile file = null;
            if (config.getTimeStamp() == null)
                file = new DxfFile(path.toString() + "/" + blocks.get(i).getName());
            else
                file = new DxfFile(path.toString() + "/" + blocks.get(i).getName() + "_" + config.getTimeStamp());

            try
            {
                file.addLines(getXPoints(i), getYPoints(i));
                file.addConstructionPoints(getXCtPoints(i), getYCtPoints(i), getNames(i));
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
            file.writeFile(blocks.get(i).getName(), config);
        }
    }

    public MeasurementSet getMeasurementSet()
    {
        return measurements;
    }
}
