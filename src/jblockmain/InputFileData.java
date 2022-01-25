package jblockmain;

import javafx.util.Pair;
import jblockexceptions.MeasurementNotFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Encapsulates the input data files which follow the prescribed format.
 */
public class InputFileData
{
    /**
     * Name of the input file
     */
    private String scanDataFileName;

    /**
     * Dictionary of measurements, one for each individual read in from the input files.
     */
    private final HashMap<String, ArrayList<InputValue>> inputValuesByUser = new HashMap<>();

    /**
     * Local cache of the names and IDs of the measures available in the file
     */
    private List<InputValue> valuesAvailable;

    /**
     * Constructor which takes an input file name.
     *
     * @param scanDataFileName name of input file to read from.
     * @param headersOnly just read the headers and not the values.
     */
    public InputFileData(String scanDataFileName, boolean headersOnly)
    {
        try
        {
            this.scanDataFileName = scanDataFileName;

            // Open file and get an input stream
            FileReader fileReader = new FileReader(scanDataFileName);
            BufferedReader fileBuffer = new BufferedReader(fileReader);

            // Process the file one line at a time
            String line;
            while ((line = fileBuffer.readLine()) != null)
            {
                // Find header line indicating measurements available
                if (line.contains("[") && line.contains("]"))
                {
                    valuesAvailable = processHeadings(line);
                    if (headersOnly) break;
                }
                else if (line.length() > 0)
                {
                    processInputValues(line);
                }
            }

            // Close file
            fileBuffer.close();
            fileReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to check whether user has specified a measurement ID in the correct format. If not an exception is thrown.
     *
     * @param text ID as text.
     * @return Indication whether format is correct.
     */
    private boolean checkIdFormat(String text) throws Exception
    {
        // If it has leading and trailing brackets remove them first
        if (text.contains("[") && text.contains("]")) text = text.substring(text.indexOf('[') + 1, text.indexOf(']'));

        // Check that it is 3 characters long
        if (text.length() != 3) return false;

        // Check first character is capital letter
        if ((int) text.charAt(0) < 65 || (int) text.charAt(0) > 90) return false;

        // Check remaining characters are all numbers
        for (int ch = 1; ch < 3; ch++)
        {
            if ((int) text.charAt(ch) < 48 || (int) text.charAt(ch) > 57) return false;
        }

        // If all tests passed then should be OK
        return true;
    }

    /**
     * Getter for the list of individuals in the measurements object.
     *
     * @return list of names
     */
    public ArrayList<String> getUserNames()
    {
        return new ArrayList<String>(inputValuesByUser.keySet());
    }

    /**
     * Getter for the input file name.
     *
     * @return input file name.
     */
    public String getScanDataFileName()
    {
        return scanDataFileName;
    }

    /**
     * Getter for a specific input value for a user
     *
     * @param user user store to look at.
     * @param id ID of the value type in conforming format.
     * @return value of the input.
     * @throws MeasurementNotFoundException when value with specified user or ID does not exist.
     */
    public InputValue getInputValue(String user, String id) throws MeasurementNotFoundException
    {
        var set = inputValuesByUser.get(user);
        for (var i : set)
        {
            if (i.id.equals(id)) return i;
        }
        throw new MeasurementNotFoundException(id);
    }

    /**
     * Method to populate list of headings from input file.
     *
     * @param line heading line.
     * @return list of input values with no values
     */
    private List<InputValue> processHeadings(String line)
    {
        var inputValues = new ArrayList<InputValue>();
        // Split the line into the id and the name
        var headings = line.split(",");
        for (var h : headings)
        {
            if (h.isBlank()) continue;
            int splitEnd = h.indexOf("]");
            String id = h.substring(line.indexOf("["), splitEnd).trim();
            String name = h.substring(splitEnd + 1, h.length()).trim();
            inputValues.add(new InputValue(id, name, 0));
        }
        return inputValues;
    }

    /**
     * Method to read in the input values from the file on a given line
     * @param line input line of text
     * @throws Exception when value cannot be parsed
     */
    private void processInputValues(String line) throws Exception
    {
        // If we haven't processed the header row then skip
        if (valuesAvailable == null)
        {
            return;
        }

        // Split by comma
        var values = line.split(",");

        // Should be the same number as the headings minus the username
        assert values.length == valuesAvailable.size() - 1;

        // Process the values
        String username = null;
        ArrayList<InputValue> set = new ArrayList<>();
        for (int i = 0; i < values.length; ++i)
        {
            // First entry on a row is the username
            if (i == 0) username = values[0].trim();
            else
            {
                var val = Double.parseDouble(values[i]);
                set.add(
                        new InputValue(
                                valuesAvailable.get(i - 1).id,
                                valuesAvailable.get(i - 1).name,
                                val
                        )
                );
            }
        }
        if (!username.isBlank()) inputValuesByUser.put(username, set);
    }

    /**
     * Gets a list of input value ID and name pairs
     * @return list of pairs
     */
    public List<Pair<String, String>> getAvailableInputValues()
    {
        var pairs = new ArrayList<Pair<String, String>>();
        for (var i : valuesAvailable)
        {
            pairs.add(new Pair<>(i.id, i.name));
        }
        return pairs;
    }

    /**
     * Gets a list of IDs of the input values
     * @return list of IDs
     */
    public List<String> getAvailableInputValueIds()
    {
        var ids = new ArrayList<String>();
        for (var i : valuesAvailable)
        {
            ids.add(i.id);
        }
        return ids;
    }
}