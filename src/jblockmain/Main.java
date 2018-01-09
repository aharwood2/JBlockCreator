package jblockmain;

import beazleybond.SkirtPattern;

public class Main
{
    // Set a global tolerance for some operations
    public static final double tol = 10e-8;

    // Set a global resolution for some curves (points per cm)
    public static final double res = 1;

    // Entry point
    public static void main(String[] args)
    {
        // Create a pattern
        SkirtPattern bb_skirt = new SkirtPattern(new Measurements("ScanData.out"));
        bb_skirt.writeToDXF("./");
    }

}