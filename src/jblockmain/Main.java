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
        // System to test all inputs
        String[] inputFiles = {
                "Alva12",
                "A003FA52",
                "A676FR39",
                "A674FA46",
                "A808FZ31",
                "A809FA52"
        };

        // Create patterns
        for (String name : inputFiles)
        {
            SkirtPattern bb_skirt = new SkirtPattern(new Measurements(name + ".txt"));
            bb_skirt.writeToDXF("./output/");

            TrouserPattern bb_trouser = new TrouserPattern(new Measurements(name + ".txt"));
            bb_trouser.writeToDXF("./output/");

            BodicePattern bb_bodice = new BodicePattern(new Measurements(name + ".txt"));
            bb_bodice.writeToDXF("./output/");
        }
    }

}