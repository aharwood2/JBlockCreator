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

    public String getScanDataFileName() {
        return scanDataFileName;
    }

    // Getter for name of file
    public String getName()
    {
        return userNames.get(mapNumber);
    }

    // Getter for current map number
    public int getMapNumber()
    {
        return mapNumber;
    }

    // Getter for measurements
    public Store getId(int id) throws MeasurementNotFoundException
    {
        Store store = storeMaps.get(mapNumber).get(id);
        if (store == null)
        {
            throw new MeasurementNotFoundException("No measurement with ID " + id + " found in the data store. " +
                    "Is it missing from the scan data input file?");
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

    public Measurements(String scanDataFileName)
    {
        // Call default constructor to create storage
        this();

        // Add a new HashMap for initial individual
        storeMaps.add(new HashMap<>());
        mapNumber = 0;

        try
        {
            // Open file and get an input stream
            FileReader file = new FileReader(scanDataFileName);
            BufferedReader fileStream = new BufferedReader(file);

            // Call utility method to assign measurements to stores
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
        // Store lines in a string
        String line;

        // Flag identifying if file is batched
        boolean isBatched = true;

        // Determine whether file is batched or not from number of lines of measurements it contains
        try
        {
            // If more than one line with [] in it, assume it is not batched
            int lineCount = 0;
            while ((line = fileStream.readLine()) != null && lineCount < 2)
            {
                // Check to see if line contains measurement(s)
                if (line.length() > 0 && line.contains("[") && line.contains("]"))
                {
                    // Increment counter
                    lineCount++;
                }
            }

            // If more than 1 line with [] in it then must be a non-batch file
            if (lineCount == 2)
            {
                isBatched = false;
                userNames.add(
                        scanDataFileName.substring(
                                scanDataFileName.lastIndexOf("\\", scanDataFileName.length() - 4)
                        )
                );
            }

            if (!isBatched)
            {
                while ((line = fileStream.readLine()) != null)
                {
                    // Only process lines that contain a []
                    if (line.length() > 0 && line.contains("[") && line.contains("]"))
                    {
                        // Split the line into the id, name and the value
                        int splitPoint = line.indexOf(":");
                        int splitPointIdEnd = line.indexOf("]");
                        int splitPointIdStart = line.indexOf("[");
                        String id = line.substring(splitPointIdStart + 1, splitPointIdEnd);
                        String name = line.substring(splitPointIdEnd + 2, splitPoint);
                        String val = line.substring(splitPoint + 2);
                        storeMaps.get(mapNumber).put(Integer.valueOf(id),
                                                     new Store(Integer.valueOf(id), name, Double.valueOf(val)));
                    }
                }
            }
            else
            {
                // Disregard line one as unneeded and store the second line of the input file
                // containing measurement id's and names
                fileStream.readLine();
                String allMeasurements = fileStream.readLine();

                // Splits the list into a string array dividing on every tab
                String[] dividedChunks = allMeasurements.split("\t");

                // Creates a variable corresponding to array length (total number of measurements)
                int arrayLength = dividedChunks.length;

                int expectedNumCustomMeasurements = 0;

                for (int i = 0; i < dividedChunks.length; i++)
                {
                    if (dividedChunks[i].length() > 0)
                    {
                        if (dividedChunks[i].charAt(0) == '[' && dividedChunks[i].charAt(1) == '0' && dividedChunks[i].charAt(4) == ']') {
                            expectedNumCustomMeasurements += 1;
                        }
                    }
                }


                // Creates a variable corresponding to final array length
                final int numCustomMeasurements = neededChunks.length;

                // Creates two new arrays, one for the measurement ID numbers and one for the measurement names
                String[] idNumber = new String[numCustomMeasurements];
                String[] idName = new String[numCustomMeasurements];

                // For loop separating the ID numbers
                for (int i = 0; i < numCustomMeasurements; ++i)
                {
                    // Takes the ID number part of the array and stores it in the IDNumber array
                    idNumber[i] = neededChunks[i].substring(1, 4);
                }

                // For loop separating the ID names
                for (int i = 0; i < numCustomMeasurements; ++i)
                {
                    // Takes the ID name part of the array and stores it in the IDName array
                    idName[i] = neededChunks[i].substring(6, neededChunks[i].length());
                }

                // Create a null string
                String measurementValues = null;

                // Start a counter for the number of users
                int numUsers = 0;

                // While loop for batch inputs
                while ((measurementValues = fileStream.readLine()) != null)
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
                    String[] numbers = measurementValues.split("\t");

                    // Creates a variable corresponding to the array length
                    int valueArrayLength = numbers.length;

                    // Gets the first entry in the numbers array, containing user ID data
                    String userData = numbers[0];

                    // Splits the user data into a string array dividing on every space
                    String[] userInfo = userData.split(" ");

                    // Gets the first entry in the array corresponding to the unique User ID and puts it in the userNames array
                    String userID = userInfo[0];
                    userNames.add(userID);

                    // Removes all values in the data array except the ones needed for pattern drafting
                    String[] idValues = Arrays.copyOfRange(numbers, valueArrayLength - expectedNumCustomMeasurements,
                                                           valueArrayLength);

                    for (int i = 0; i < numCustomMeasurements; ++i)
                    {
                        if (idValues[i].equals("Unavailable") || idValues[i].equals("null"))
                        {
                            System.out.println(
                                    "Measurement " + idName[i] + " is not available for user " + userNames.get(
                                            numUsers - 1));
                            idValues[i] = "0.0";
                        }
                        storeMaps.get(mapNumber).put(
                                Integer.valueOf(idNumber[i]),
                                new Store(Integer.valueOf(idNumber[i]), idName[i], Double.valueOf(idValues[i]))
                        );
                    }
                }

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
