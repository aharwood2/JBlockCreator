package jblockmain;

import jblockenums.EUnitType;

/**
 * A measurement associated with a pattern which can be mapped to an input value via an input ID.
 */
public class Measurement
{
    public final String name;
    private double value;
    private String inputId;
    private EUnitType units = EUnitType.CENTIMETRES;

    public Measurement(String name, double value, String inputId, EUnitType units)
    {
        this(name, value, inputId);
        this.units = units;
    }

    public Measurement(String name, double value, String inputId)
    {
        this(name, value);
        this.inputId = inputId;
    }

    public Measurement(String name, double value, EUnitType units)
    {
        this(name, value);
        this.units = units;
    }

    public Measurement(String name, String inputId, EUnitType units)
    {
        this(name, inputId);
        this.units = units;
    }

    public Measurement(String name, double value)
    {
        this(name);
        this.value = value;
    }

    public Measurement(String name, String inputId)
    {
        this(name);
        this.inputId = inputId;
    }

    public Measurement(String name)
    {
        this.name = name;
    }

    public double getValue()
    {
        return value;
    }

    public Measurement setValue(double value)
    {
        this.value = value;
        return this;
    }

    public String getInputId()
    {
        return inputId;
    }

    public Measurement setInputId(String id)
    {
        this.inputId = id;
        return this;
    }

    public String getUnitName()
    {
        if (units == null) return null;
        return units.toString();
    }
}
