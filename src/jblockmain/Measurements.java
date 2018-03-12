package jblockmain;

import jblockexceptions.MeasurementNotFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/** Class to hold the measurements used by the block packages.
 * For now this essentially encapsulates the sizestream scan data file. */
public class Measurements
{
    
    // Nested structure
    public class Store
    {
        private int id;
        private String scanDataName;
        public double value;
        
        public Store(int _id, String _scanDataName, double _value)
        {
            this.id = _id;
            this.scanDataName = _scanDataName;
            this.value = _value;

            // Update the hash map
            if (this.id > 0) storeMap.put(id, this);
        }
    }

    // Mapper to map measurement ID to value store
    private HashMap<Integer, Store> storeMap;

    // Getter for measurements
    public Store getId(int id) throws MeasurementNotFoundException
    {
        Store store =  storeMap.get(id);
        if (store == null)
        {
            throw new MeasurementNotFoundException("No measurement with ID " + id + " found in the data store. " +
                                                           "Is it missing from the scan data input file?" );
        }
        return store;
    }

    // Constructor
    public Measurements(String scanDataFileName)
    {
        // Initialise HashMap
        storeMap = new HashMap<>();

        try
        {
            // Open file and get an input stream
            FileReader file = new FileReader(scanDataFileName);
            BufferedReader fileStream = new BufferedReader(file);

            assignMeasurements(fileStream);

            fileStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Method to inspect each line and assign a value to the variable named
    private void assignMeasurements(BufferedReader fileStream)
    {
        try
        {
            // Read first line
            String line;

            while ((line = fileStream.readLine()) != null)
            {
                // Only process lines that start with a 1 and contain a []
                if (line.charAt(0) == '1' && line.contains("[") && line.contains("]"))
                {
                    // Split the line into the id, name and the value
                    int splitPoint = line.indexOf(":");
                    int splitPointIdEnd = line.indexOf("]");
                    int splitPointIdStart = line.indexOf("[");
                    String id = line.substring(splitPointIdStart + 1, splitPointIdEnd);
                    String name = line.substring(splitPointIdEnd + 2, splitPoint);
                    String val = line.substring(splitPoint + 2);
                    storeMap.put(Integer.valueOf(id), new Store(Integer.valueOf(id), name, Double.valueOf(val)));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}