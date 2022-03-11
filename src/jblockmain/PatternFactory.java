package jblockmain;

import jblockenums.EPattern;

public class PatternFactory
{
    public static Pattern Create(EPattern type, String userName, InputFileData data, MeasurementSet template)
    {
        switch (type)
        {
            case AHMED_BODICE -> { return new ahmed.BodicePattern(userName, data, template); }
            case ALDRICH_SKIRT -> { return new aldrich.SkirtPattern(userName, data, template); }
            case ALDRICH_TROUSER -> { return new aldrich.TrouserPattern(userName, data, template); }
            case BEAZLEYBOND_SKIRT -> { return new beazleybond.SkirtPattern(userName, data, template); }
            case BEAZLEYBOND_BODICE -> { return new beazleybond.BodicePattern(userName, data, template); }
            case BEAZLEYBOND_STRAIGHTSLEEVE -> { return new beazleybond.StraightSleevePattern(userName, data, template); }
            case BEAZLEYBOND_TROUSER -> { return new beazleybond.TrouserPattern(userName, data, template); }
            case GILL_SKIRT -> { return new gill.SkirtPattern(userName, data, template); }
            case GILL_SWEATSHIRT -> { return new gill.SweatShirtPattern(userName, data, template); }
            case GILL_TROUSER -> { return new gill.TrouserPattern(userName, data, template); }
            case GILL_TROUSER2 -> { return new gill.TrouserPatternTwo(userName, data, template); }
            case GILL_TROUSER3 -> { return new gill.TrouserPatternThree(userName, data, template); }
        }
        return null;
    }
}
