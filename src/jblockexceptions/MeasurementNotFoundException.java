package jblockexceptions;

/**
 * Wraps the NullPointerException to ensure the stack trace has a more useful name.
 */
public class MeasurementNotFoundException extends NullPointerException
{
    /**
     * Measurement ID which triggered the exception.
     */
    private String measurementId;

    /**
     * Constructor
     * @param _measurementIdTrigger     ID of the measurement which trigger the exception.
     */
    public MeasurementNotFoundException(String _measurementIdTrigger)
    {
        super("Measurement " + _measurementIdTrigger +
                      " not found in the measurement store. Is it missing from the input file?");
        measurementId = _measurementIdTrigger;
    }

    /**
     * Get the measurement ID which triggered the exception.
     * @return  measurement ID
     */
    public String getMeasurementId()
    {
        return measurementId;
    }
}
