package jblockenums;

public enum EUnitType
{
    NONE(""),
    CENTIMETRES("cm"),
    PERCENTAGE("%");

    EUnitType(String s)
    {
        stringName = s;
    }

    private final String stringName;

    @Override
    public String toString()
    {
        return stringName;
    }
}
