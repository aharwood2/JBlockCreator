package beazleybond;

import dxfwriter.DxfFile;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

/** Class to construct a fitted bodice using the Beazley and Bond drafting method. */
public class BodicePattern
    extends Pattern
    implements IPlottable
{

    /* Pattern-specific Measurements */
    // In future will be simply extracted from the Measurements object.
    private double a_Waist                     = 70.0;
    private double b_UpperHip                  = 90.0;
    private double c_Hip                       = 96.0;
    private double d_CentreBack                = 60.0;
    private double e_SideSeam                  = 61.0;
    private double f_CentreFront               = 60.0;

    /* Arbitrary Measurements */
    // Some of the following can be inferred from body scan information but for now assume that these follow the
    // empirically driven values.

    // Ensures the waistline drops by 1cm to allow it to curve round the body. This can be informed from the body scan.
    private double Arb_WaistLevel = 1.0;

    // Generic assumption that can in future be informed from the body scan.
    private double Arb_UpperHipLevel = 10.0;

    // Generic assumption that can in future be informed from the body scan.
    private double Arb_HipLevel = 20.0;

    // Waist suppression process required calculation of a front and back dart by dividing up the circumference of the
    // waist. For now we assume a fixed percentage is assigned to each although this could be adjusted in future.
    double Arb_BackDartPercent = 0.35;
    double Arb_FrontDartPercent = 0.20;
    double Arb_SideSeamPercent = 0.45;

    // Dart length is arbitrary but can be inferred from body scan data.
    double Arb_BackDartLength = 14.0;
    double Arb_FrontDartLength = 8.0;

    // Dart placement is also arbitrary and is specified as a percentage of quarter waist as measured from the start
    // point of the waist (using strict connectivity order)
    double Arb_BackDartPlacement = 0.5;
    double Arb_FrontDartPlacement = 1.0 / 3.0;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BodicePattern(Measurements dataStore)
    {
        readMeasurements(dataStore);
        addEasement();
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected void addEasement()
    {
        // Size 12 skirt for now but should be computed by this class in the future.
        // TODO: Redo
        a_Waist += 4.0;
        b_UpperHip += 4.0;
        c_Hip += 4.0;
    }

    @Override
    protected void readMeasurements(Measurements dataStore)
    {
        // TODO: Implement when we couple to scan data
    }

    /**
     * The actual block creation process following the drafting method of Beazley and Bond.
     */
    @Override
    protected void createBlocks()
    {
        // Points that make up the shape are listed in a strict anti-clockwise order to maintain correct connectivity
        // for plotting. The bottom left corner of the space to be the origin.

        // TODO: Redo
    }



}
