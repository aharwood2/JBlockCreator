package jblockmain;

import jblockexceptions.MeasurementNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of measurements
 */
public class MeasurementSet
{
    private final List<Measurement> measurements = new ArrayList<>();

    public Measurement getMeasurement(String name) throws MeasurementNotFoundException
    {
        for (var m : measurements)
        {
            if (m.name.equals(name)) return m;
        }
        throw new MeasurementNotFoundException(name);
    }

    public void setValue(String name, double value) throws MeasurementNotFoundException
    {
        for (var m : measurements)
        {
            if (m.name.equals(name))
            {
                m.setValue(value);
                return;
            }
        }
        throw new MeasurementNotFoundException(name);
    }

    public void addMeasurement(Measurement newM) throws Exception
    {
        for (var m : measurements)
        {
            if (m.name.equals(newM.name)) throw new Exception("Measurement already exists in this measurement set!");
        }
        measurements.add(newM);
    }

    /**
     * Assigns values to the measurements mapped by an ID
     * @param userName the username to lookup in the input data
     * @param inputData the input data source
     * @throws MeasurementNotFoundException measurement not found in the input data
     */
    public void mapFromInputData(String userName, InputFileData inputData) throws MeasurementNotFoundException
    {
        for (var m : measurements)
        {
            if (m.getInputId() != null)
                m.setValue(inputData.getInputValue(userName, m.getInputId()).value);
        }
    }

    /**
     * Gets measurements for which there is an ID
     * @return List of IDs
     */
    public List<String> getIds()
    {
        var ids = new ArrayList<String>();
        for (var m : measurements)
        {
            var id = m.getInputId();
            if (id != null) ids.add(id);
        }
        return ids;
    }

    /**
     * Return list of all measurements
     * @return list of all measurements in the set
     */
    public List<Measurement> getAllMeasurements()
    {
        return measurements;
    }

    /**
     * Assign all values from a template where not mapped by an ID
     */
    public void mapFromTemplate(MeasurementSet template) throws MeasurementNotFoundException
    {
        // Loop through all measurements
        for (var m : template.measurements)
        {
            // Only assign from template if not mapped to an ID
            if (m.getInputId() == null)
                setValue(m.name, m.getValue());
        }
    }
}
