package jblockmain;

import jblockexceptions.MeasurementNotFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// TODO: Complete javadoc

/**
 * Class to hold the measurements used by the block packages.
 * For now this essentially encapsulates the input data files which follow the prescribed format.
 */
public class Measurements
{
    /**
     * Name of the input file
     */
    private String scanDataFileName;

    /**
     * Getter for the list of individuals in the measurements object.
     * @return list of names
     */
    public ArrayList<String> getNames()
    {
        return userNames;
    }

    /**
     * Nested class representing a measurement with an ID number, a human-readable name and a numerical value.
     */
    public class Measurement
    {
        private String id;
        private String humanReadableName;
        public double value;

        /**
         * Constructor.
         * @param _id                   ID of measurement using the format [X00].
         * @param _humanReadableName    Text description.
         * @param _value                Numerical value.
         */
        public Measurement(String _id, String _humanReadableName, double _value)
        {
            this.id = _id;
            this.humanReadableName = _humanReadableName;
            this.value = _value;

            // Update the hash map
            allMeasurements.get(currentUser).put(id, this);
        }
    }

    /**
     * List of hashmaps of measurements, one for each individual read in from the input files.
     */
    private ArrayList<HashMap<String, Measurement>> allMeasurements;

    /**
     * Internal ID of current storeMap
     */
    private int currentUser;

    /**
     * List of individuals read in from the input files.
     */
    private ArrayList<String> userNames;

    /**
     * Constructor to perform initialisation only.
     */
    private Measurements()
    {
        // Initialise HashMap list
        allMeasurements = new ArrayList<>();

        // Initialise names list
        userNames = new ArrayList<>();
    }

    /**
     * Constructor which takes an input file name.
     * @param scanDataFileName  name of input file to read from.
     */
    public Measurements(String scanDataFileName)
    {
        // Call default constructor to create storage
        this();

        // Add a new HashMap for initial individual
        allMeasurements.add(new HashMap<>());
        currentUser = 0;

        try
        {
            // Open file and get an input stream
            FileReader fileReader = new FileReader(scanDataFileName);
            BufferedReader fileBuffer = new BufferedReader(fileReader);

            // If more than one line with [] in it, assume it is not batched
            int lineCount = 0;
            String line;
            while ((line = fileBuffer.readLine()) != null && lineCount < 2)
            {
                // Check to see if line contains measurement(s)
                if (line.length() > 0 && line.contains("[") && line.contains("]"))
                {
                    // Increment counter
                    lineCount++;
                }
            }

            // If more than 1 line with [] in it then must be a non-batch file
            boolean isBatched = true;
            if (lineCount == 2)
            {
                isBatched = false;
                userNames.add(
                        scanDataFileName.substring(
                                scanDataFileName.lastIndexOf("\\", scanDataFileName.length() - 4)
                        )
                );
            }

            // Close file
            fileBuffer.close();
            fileReader.close();

            // Re-open the file
            fileReader = new FileReader(scanDataFileName);
            fileBuffer = new BufferedReader(fileReader);

            // Call utility method to assign measurements to stores
            assignMeasurements(fileReader, isBatched);

            // Close the file
            fileReader.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to check whether user has specified a measurement ID in the correct format. If not an exception is thrown.
     * @param text  ID as text.
     * @return      Indication whether format is correct.
     */
    public static boolean checkIdFormat(String text) throws Exception
    {
        // If it has leading and trailing brackets remove them first
        if (text.contains("[") && text.contains("]")) text = text.substring(text.indexOf('[') + 1, text.indexOf(']'));

        // Check that it is 3 characters long
        if (text.length() != 3) return false;

        // Check first character is capital letter
        if ((int)text.charAt(0) < 65 || (int)text.charAt(0) > 90) return false;

        // Check remaining characters are all numbers
        for (int ch = 1; ch < 3; ch++)
        {
            if ((int) text.charAt(ch) < 48 || (int) text.charAt(ch) > 57) return false;
        }

        // If all tests passed then should be OK
        return true;
    }

    /**
     * Means of changing the map targeted by the get measurement method.
     * @param num   user to target.
     */
    public void setCurrentUser(int num)
    {
        try
        {
            currentUser = num;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Getter for the input file name.
     * @return input file name.
     */
    public String getScanDataFileName()
    {
        return scanDataFileName;
    }

    /**
     * Getter for the current user.
     * @return  current user name.
     */
    public String getName()
    {
        return userNames.get(currentUser);
    }

    /**
     * Getter for current user id.
     * @return  current user id.
     */
    public int getCurrentUser()
    {
        return currentUser;
    }

    /**
     * Getter for a specific measurement for current user.
     * @param id    measurement ID in conforming format.
     * @return      value of the measurement.
     * @throws MeasurementNotFoundException when measurement with specified ID does not exist.
     */
    public Measurement getMeasurement(String id) throws MeasurementNotFoundException
    {
        Measurement measurement = allMeasurements.get(currentUser).get(id);
        if (measurement == null)
        {
            throw new MeasurementNotFoundException(id);
        }
        return measurement;
    }

    /**
     * Method to inspect each line of the input file and populate the measurements in the hashmaps.
     * @param fileReader    An open file.
     * @param isBatched     internal flag indicating whether the file uses the batch format or the serial format.
     */
    private void assignMeasurements(FileReader fileReader, boolean isBatched)
    {
        // Create a buffered reader
        BufferedReader fileStream = new BufferedReader(fileReader);

        // Store lines in a string
        String line;

        try
        {
            // If not batched
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
                        allMeasurements.get(currentUser).put(id, new Measurement(id, name, Double.valueOf(val)));
                    }
                }
            }
            else
            {
                // Read until first (or second if tabbed) character of line is "[" which indicates the header
                String row = fileStream.readLine();
                while (row != null && (row.charAt(0) != '[' && row.charAt(1) != '['))
                {
                    row = fileStream.readLine();
                }
                if (row == null) throw new IOException("List of measurements not found in batch input file!");

                // Splits the list into a string array dividing on every tab
                String[] dividedChunks = row.split("\t");

                // Creates two new arrays, one for the measurement ID numbers and one for the measurement names
                ArrayList<String> idNumber = new ArrayList<>();
                ArrayList<String> idName = new ArrayList<>();

                // Loop over chunks and identify valid measurement tags
                ArrayList<Integer> validMeasurementPositions = new ArrayList<>();
                for (int i = 0; i < dividedChunks.length; i++)
                {
                    if (
                            dividedChunks[i].length() > 0 &&
                            dividedChunks[i].charAt(0) == '[' &&
                            Measurements.checkIdFormat(dividedChunks[i]) &&
                            dividedChunks[i].charAt(4) == ']'
                        )
                    {
                        // Stash position in the list
                        validMeasurementPositions.add(i);

                        // Takes the ID number part of the array and stores it in the IDNumber array
                        idNumber.add(dividedChunks[i].substring(dividedChunks[i].indexOf('[') + 1, dividedChunks[i].indexOf(']')));

                        // Takes the ID name part of the array and stores it in the IDName array
                        idName.add(dividedChunks[i].substring(dividedChunks[i].indexOf(']') + 2));
                    }
                }

                // Start a counter for the number of users
                int numUsers = 0;

                // While loop for each row immediately after the header which represent user data
                while ((row = fileStream.readLine()) != null)
                {
                    // Increment user counter
                    numUsers++;

                    // Constructor creates first instance
                    if (numUsers != 1)
                    {
                        // Prepare map for next user
                        currentUser++;
                        this.allMeasurements.add(new HashMap<>());
                    }

                    // Splits the list into a string array dividing on every tab
                    String[] userValues = row.split("\t");

                    // Check to make sure we have the same number of measurements are we did tags in the header
                    if (dividedChunks.length != userValues.length) throw new Exception("Header does not match data!");

                    // Splits first chunk by spaces to extract the user name
                    String[] userInfo = userValues[0].split(" ");
                    String userID = userInfo[0];
                    userNames.add(userID);

                    // Loop over the valid measurement list
                    int count = 0;
                    for (int pos : validMeasurementPositions)
                    {
                        // Get the measurement
                        String value = userValues[pos];

                        // Handle cases where the measurement is unavailable (computer-generated scan input)
                        if (value.equals("Unavailable") || value.equals("null"))
                        {
                            System.out.println(
                                    "Measurement " + idName.get(count) + " is not available for user " +
                                            userNames.get(numUsers - 1)
                            );
                            value = "0.0";
                        }

                        // Add a mew measurement to the store
                        this.allMeasurements.get(currentUser).put(
                                idNumber.get(count),
                                new Measurement(idNumber.get(count), idName.get(count), Double.valueOf(value))
                        );

                        count++;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}