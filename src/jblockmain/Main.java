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
        // Create a patterns
        SkirtPattern bb_skirt = new SkirtPattern(new Measurements("A674FA46.txt"));
        bb_skirt.writeToDXF("./output/");
        bb_skirt = new SkirtPattern(new Measurements("A808FZ31.txt"));
        bb_skirt.writeToDXF("./output/");
        bb_skirt = new SkirtPattern(new Measurements("A809FA52.txt"));
        bb_skirt.writeToDXF("./output/");

        TrouserPattern bb_trouser = new TrouserPattern(new Measurements("A674FA46.txt"));
        bb_trouser.writeToDXF("./output/");
        bb_trouser = new TrouserPattern(new Measurements("A808FZ31.txt"));
        bb_trouser.writeToDXF("./output/");
        bb_trouser = new TrouserPattern(new Measurements("A809FA52.txt"));
        bb_trouser.writeToDXF("./output/");

        BodicePattern bb_bodice = new BodicePattern(new Measurements("A674FA46.txt"));
        bb_bodice.writeToDXF("./output/");
        bb_bodice = new BodicePattern(new Measurements("A808FZ31.txt"));
        bb_bodice.writeToDXF("./output/");
        bb_bodice = new BodicePattern(new Measurements("A809FA52.txt"));
        bb_bodice.writeToDXF("./output/");
    }

}