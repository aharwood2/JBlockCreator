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

            new File("./output/BBSkirt/").mkdirs();
            SkirtPattern bb_skirt = new SkirtPattern(measurements);
            bb_skirt.writeToDXF("./output/BBSkirt/");

            new File("./output/BBTrousers/").mkdirs();
            TrouserPattern bb_trouser = new TrouserPattern(measurements);
            bb_trouser.writeToDXF("./output/BBTrousers/");

            new File("./output/BBBodice/").mkdirs();
            BodicePattern bb_bodice = new BodicePattern(measurements);
            bb_bodice.writeToDXF("./output/BBBodice/");

            new File("./output/GillSkirt/").mkdirs();
            gill.SkirtPattern gill_skirt = new gill.SkirtPattern(measurements);
            gill_skirt.writeToDXF("./output/GillSkirt/");

            new File("./output/AldrichSkirt/").mkdirs();
            aldrich.SkirtPattern aldrich_skirt = new aldrich.SkirtPattern(measurements);
            aldrich_skirt.writeToDXF("./output/AldrichSkirt/");
        }
    }

}