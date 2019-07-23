package ahmed;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockenums.EPosition;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;

public class BodicePattern extends Pattern
{
    private double sideNeckToBustLengthR;
    private double sideNeckToBustToWaistR;
    private double acrossBackTapeMeasurement;
    private double acrossChestArmToArmLength;
    private double shoulderLengthRight;
    private double bustWidth;
    private double frontWaistArc;
    private double backWaistArc;
    private double frontBustArc;
    private double backBustArc;
    private double scyeDepth;
    private double shoulderSlope;
    private double sideSeamDepth;
    private double acrossShoulderBackandFront;
    private double waistToArmpitDepth;
    private double midShoulderToShoulderBlades;
    private double neckWidthFrontandBack;
    private double frontNeckDepth;
    private double backNeckDepth;
    private double shoulderToWaistDepth;

    @Override
    protected EMethod assignMethod()
    {
        return EMethod.AHMED;
    }

    @Override
    protected EGarment assignGarment()
    {
        return EGarment.BODICE;
    }

    @Override
    protected void addEasement()
    {
        //NONE ATM
    }


    public BodicePattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();

        createBlocks();
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try
        {
            sideNeckToBustLengthR = dataStore.getMeasurement("A07").value;
            sideNeckToBustToWaistR = dataStore.getMeasurement("A08").value;
            acrossBackTapeMeasurement = dataStore.getMeasurement("A09").value;
            acrossChestArmToArmLength = dataStore.getMeasurement("A10").value;
            shoulderLengthRight = dataStore.getMeasurement("A11").value;
            bustWidth = dataStore.getMeasurement("A12").value;
            frontWaistArc = dataStore.getMeasurement("A26").value;
            backWaistArc = dataStore.getMeasurement("A27").value;
            frontBustArc = dataStore.getMeasurement("A56").value;
            backBustArc = dataStore.getMeasurement("A57").value;
            scyeDepth = dataStore.getMeasurement("B01").value;
            shoulderSlope = dataStore.getMeasurement("B04").value;
            sideSeamDepth = dataStore.getMeasurement("B05").value;
            acrossShoulderBackandFront = dataStore.getMeasurement("B06").value;
            waistToArmpitDepth = dataStore.getMeasurement("B07").value;
            midShoulderToShoulderBlades = dataStore.getMeasurement("B08").value;
            neckWidthFrontandBack = dataStore.getMeasurement("B09").value;
            frontNeckDepth = dataStore.getMeasurement("B10").value;
            backNeckDepth = dataStore.getMeasurement("B11").value;
            shoulderToWaistDepth = dataStore.getMeasurement("B16").value;

            userName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            addMissingMeasurement(dataStore.getName(), e.getMeasurementId());
            return false;
        }
    }

    @Override
    protected void createBlocks()
    {

    }

}
