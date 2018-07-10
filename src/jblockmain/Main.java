package jblockmain;

import beazleybond.BodicePattern;
import beazleybond.SkirtPattern;
import beazleybond.TrouserPattern;

import java.io.File;

public class Main
{
    // Set a global tolerance for some operations
    public static final double tol = 10e-8;

    // Set a global resolution for some curves (points per cm)
    public static final double res = 1;

    // Entry point
    public static void main(String[] args)
    {

        Measurements measurements = new Measurements("SpreadsheetBigger.txt", true);

        // Create patterns
        for (int i = 0; i < measurements.getNames().size(); i++)
        {
            measurements.setMapNumber(i);

            SkirtPattern bb_skirt = new SkirtPattern(measurements);
            bb_skirt.writeToDXF();

            TrouserPattern bb_trouser = new TrouserPattern(measurements);
            bb_trouser.writeToDXF();

            BodicePattern bb_bodice = new BodicePattern(measurements);
            bb_bodice.writeToDXF();

            gill.SkirtPattern gill_skirt = new gill.SkirtPattern(measurements);
            gill_skirt.writeToDXF();

            aldrich.SkirtPattern aldrich_skirt = new aldrich.SkirtPattern(measurements);
            aldrich_skirt.writeToDXF();
        }
    }

}