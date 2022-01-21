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

    public void mapFromInputData(String userName, InputFileData inputData) throws MeasurementNotFoundException
    {
        for (var m : measurements)
        {
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

    public List<Measurement> getAllMeasurements()
    {
        return measurements;
    }
}
