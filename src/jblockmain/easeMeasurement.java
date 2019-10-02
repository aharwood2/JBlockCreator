package jblockmain;

/**
 * Wrapper class for ease measurements
 */
public class easeMeasurement
{
    private String name;
    private double value;

    public easeMeasurement(String name, double value)
    {
        this.name = name;
        this.value = value;
    }

    public easeMeasurement(String name)
    {
        this.name = name;
        this.value = 0;
    }

    public String getName()
    {
        return this.name;
    }

    public double getValue()
    {
        return this.value;
    }

    public void setValue(double newValue)
    {
        this.value = newValue;
    }

}
