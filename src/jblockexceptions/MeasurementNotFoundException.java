package jblockexceptions;

/**
 * Wraps the NullPointerException to ensure the stack trace has a more useful name.
 */
public class MeasurementNotFoundException extends NullPointerException
{
    public MeasurementNotFoundException()
    {
        super();
    }

    public MeasurementNotFoundException(String s)
    {
        super(s);
    }
}
