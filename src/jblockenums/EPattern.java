package jblockenums;

/**
 * Enumeration of the known pattern types
 */
public enum EPattern
{
    AHMED_BODICE("Ahmed Bodice"),
    ALDRICH_SKIRT("Alrich Skirt"),
    ALDRICH_TROUSER("Alrich Trouser"),
    BEAZLEYBOND_BODICE("Beazley Bond Bodice"),
    BEAZLEYBOND_SKIRT("Beazley Bond Skirt"),
    BEAZLEYBOND_STRAIGHTSLEEVE("Beazley Bond Straight Sleeve"),
    BEAZLEYBOND_TROUSER("Beazley Bond Trouser"),
    GILL_SKIRT("Gill Skirt"),
    GILL_SWEATSHIRT("Gill Sweatshirt"),
    GILL_TROUSER("Gill Trouser"),
    GILL_TROUSER2("Gill Trouser 2");

    EPattern(String s)
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
