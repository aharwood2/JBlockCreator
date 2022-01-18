package jblockmain;

/**
 * A value supplied in an input file
 */
public class InputValue
{
    public final String name;
    public final String id;
    public final double value;

    public InputValue(String id, String name, double value)
    {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
