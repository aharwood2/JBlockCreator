package jblockmain;

import jblockenums.EPattern;

public class PatternFactory
{
    public static Pattern Create(EPattern type, String userName, InputFileData data)
    {
        switch (type)
        {
            case AHMED_BODICE -> { return new ahmed.BodicePattern(userName, data); }
            case ALDRICH_SKIRT -> { return new aldrich.SkirtPattern(userName, data); }
            case ALDRICH_TROUSER -> { return new aldrich.TrouserPattern(userName, data); }
            case BEAZLEYBOND_SKIRT -> { return new beazleybond.SkirtPattern(userName, data); }
            case BEAZLEYBOND_BODICE -> { return new beazleybond.BodicePattern(userName, data); }
            case BEAZLEYBOND_STRAIGHTSLEEVE -> { return new beazleybond.StraightSleevePattern(userName, data); }
            case BEAZLEYBOND_TROUSER -> { return new beazleybond.TrouserPattern(userName, data); }
            case GILL_SKIRT -> { return new gill.SkirtPattern(userName, data); }
            case GILL_SWEATSHIRT -> { return new gill.SweatShirtPattern(userName, data); }
            case GILL_TROUSER -> { return new gill.TrouserPattern(userName, data); }
            case GILL_TROUSER2 -> { return new gill.TrouserPatternTwo(userName, data); }
        }
        return null;
    }
}
