package jblockenums;

/**
 * Enumeration of the known pattern types
 */
public enum EPattern
{
    AHMED_BODICE("Ahmed Bodice"),
    ALDRICH_SKIRT("Aldrich Skirt"),
    ALDRICH_TROUSER("Aldrich Trouser"),
    BEAZLEYBOND_BODICE("Beazley Bond Bodice"),
    BEAZLEYBOND_SKIRT("Beazley Bond Skirt"),
    BEAZLEYBOND_STRAIGHTSLEEVE("Beazley Bond Straight Sleeve"),
    BEAZLEYBOND_TROUSER("Beazley Bond Trouser"),
    GILL_SKIRT("Gill Skirt"),
    GILL_SWEATSHIRT("Gill Sweatshirt"),
    GILL_TROUSER("Gill Trouser"),
    GILL_TROUSER2("Gill Trouser 2"),
    GILL_TROUSER3("Gill Trouser 3"),
    GILL_NECK_SHOULDER("Gill Neck and Shoulder");

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
