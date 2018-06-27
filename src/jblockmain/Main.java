package jblockmain;

import beazleybond.BodicePattern;
import beazleybond.SkirtPattern;
import beazleybond.TrouserPattern;

public class Main
{
    // Set a global tolerance for some operations
    public static final double tol = 10e-8;

    // Set a global resolution for some curves (points per cm)
    public static final double res = 1;

    // Entry point
    public static void main(String[] args)
    {

        Measurements measurements = new Measurements("A003FA52.txt", false);

        // Create patterns
        for (int i = 0; i < measurements.getNames().size(); i++)
        {
            measurements.setMapNumber(i);

            SkirtPattern bb_skirt = new SkirtPattern(measurements);
            bb_skirt.writeToDXF("./output/");

            TrouserPattern bb_trouser = new TrouserPattern(measurements);
            bb_trouser.writeToDXF("./output/");

            BodicePattern bb_bodice = new BodicePattern(measurements);
            bb_bodice.writeToDXF("./output/");
        }
    }

}