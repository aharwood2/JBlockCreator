package jblockmain;

import jblockexceptions.MeasurementNotFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// TODO: Complete javadoc

/** Class to hold the measurements used by the block packages.
 * For now this essentially encapsulates the sizestream scan data file. */
public class Measurements
{
    // Name of the input file
    private String scanDataFileName;

    public ArrayList<String> getNames()
    {
        return userNames;
    }

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
            if (this.id > 0) storeMaps.get(mapNumber).put(id, this);
        }
    }

    // Mapper to map measurement ID to value store
    private ArrayList<HashMap<Integer, Store>> storeMaps;

    // Internal ID of current storeMap
    private int mapNumber;

    // Mapper to map measurement ID to value store
    private ArrayList<String> userNames;

    // Setter for the current map to use
    public void setMapNumber(int num)
    {
        try
        {
            mapNumber = num;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // Getter for name of file
    public String getName()
    {
        return userNames.get(mapNumber);
    }

    // Getter for measurements
    public Store getId(int id) throws MeasurementNotFoundException
    {
        Store store =  storeMaps.get(mapNumber).get(id);
        if (store == null)
        {
            throw new MeasurementNotFoundException("No measurement with ID " + id + " found in the data store. " +
                                                           "Is it missing from the scan data input file?" );
        }
        return store;
    }

    // Constructor
    public Measurements()
    {
        // Initialise HashMap
        storeMaps = new ArrayList<>();

        // Initialise names list
        userNames = new ArrayList<>();
    }

    public Measurements(String scanDataFileName, boolean isBatch)
    {
        this();

        // Assign name
        this.scanDataFileName = scanDataFileName.substring(0, scanDataFileName.length() - 4);

        // Add a new HashMap
        storeMaps.add(new HashMap<>());
        mapNumber = 0;

        try
        {
            // Open file and get an input stream
            FileReader file = new FileReader("./input/" + scanDataFileName);
            BufferedReader fileStream = new BufferedReader(file);

            assignMeasurements(fileStream, isBatch);

            fileStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Method to inspect each line and assign a value to the variable named
    private void assignMeasurements(BufferedReader fileStream, boolean isTabbed)
    {
        if(!isTabbed)
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
                        storeMaps.get(mapNumber).put(Integer.valueOf(id), new Store(Integer.valueOf(id), name, Double.valueOf(val)));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {
            try
            {
                // Disregard line one as unneeded and store the second line of the input file
                // containing measurement id's and names
                fileStream.readLine();
                String CustomMeasurements = fileStream.readLine();

                // Splits the list into a string array dividing on every tab
                String[] DividedChunks = CustomMeasurements.split("\t");

                // Creates a variable corresponding to array length
                int ArrayLength = DividedChunks.length;

                // Adds all needed chunks to a new array
                // TODO possibly make this bit automatic not manual?
                String[] NeededChunks = Arrays.copyOfRange(DividedChunks, ArrayLength - 33, ArrayLength);

                // Creates a variable corresponding to final array length
                final int FinalArrayLength = NeededChunks.length;

                // Creates two new arrays, one for the ID numbers and one for the ID names
                String[] IDNumber = new String[FinalArrayLength];
                String[] IDName = new String[FinalArrayLength];

                // For loop separating the ID numbers
                for (int i = 0; i < NeededChunks.length; ++i)
                {
                    // Takes the ID number part of the array and stores it in the IDNumber array
                    IDNumber[i] = NeededChunks[i].substring(1, 4);
                }

                // For loop separating the ID names
                for (int i = 0; i < NeededChunks.length; ++i)
                {
                    // Takes the ID name part of the array and stores it in the IDName array
                    IDName[i] = NeededChunks[i].substring(6, NeededChunks[i].length());
                }

                // Create a null string
                String MeasurementValues = null;

                // Start a counter for the number of users
                int numUsers = 0;

                // While loop for batch inputs
                while((MeasurementValues = fileStream.readLine()) != null)
                {
                    // Increment user counter
                    numUsers++;

                    // Constructor creates first instance
                    if (numUsers != 1)
                    {
                        // Prepare map
                        mapNumber++;
                        storeMaps.add(new HashMap<>());
                    }

                    // Splits the list into a string array dividing on every tab
                    String[] Numbers = MeasurementValues.split("\t");

                    // Creates a variable corresponding to the array length
                    int DataArrayLength = Numbers.length;

                    // Gets the first entry in the numbers array, containing user ID data
                    String UserData = Numbers[0];

                    // Splits the user data into a string array dividing on every space
                    String[] UserInfo = UserData.split(" ");

                    // Gets the first entry in the array corresponding to the unique User ID and puts it in the userNames array
                    String UserID = UserInfo[0];
                    userNames.add(UserID);

                    // Removes all values in the data array except the ones needed for pattern drafting
                    String[] IDValues = Arrays.copyOfRange(Numbers, DataArrayLength - 33, DataArrayLength);

                    for (int i = 0; i < FinalArrayLength; ++i)
                    {
                        storeMaps.get(mapNumber).put(Integer.valueOf(IDNumber[i]), new Store(Integer.valueOf(IDNumber[i]), IDName[i], Double.valueOf(IDValues[i])));
                    }
                }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        }
    }
}