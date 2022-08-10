package gill;

import jblockenums.EPattern;
import jblockmain.*;
import mathcontainers.Vector2D;

import static java.lang.Math.abs;

public class NeckAndShoulderPattern extends Pattern
{
    public NeckAndShoulderPattern(String userName, InputFileData dataStore, MeasurementSet template)
    {
        super(userName, dataStore, template);
    }

    @Override
    protected EPattern assignPattern()
    {
        return EPattern.GILL_NECK_SHOULDER;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("shoulderLengthRight", "A11"));
        measurements.addMeasurement(new Measurement("frontNeckDiagonal", "A73"));
        measurements.addMeasurement(new Measurement("backNeckDiagonal", "A74"));
        measurements.addMeasurement(new Measurement("neckBaseWidth", "A93"));
        measurements.addMeasurement(new Measurement("shoulderLengthLeft", "A94"));
        measurements.addMeasurement(new Measurement("leftShoulderPointZ", "A95"));
        measurements.addMeasurement(new Measurement("rightShoulderPointZ", "A96"));
        measurements.addMeasurement(new Measurement("leftSideNeckPointZ", "A97"));
        measurements.addMeasurement(new Measurement("rightSideNeckPointZ", "A98"));
    }

    @Override
    public void createBlocks()
    {
        // Pull from store
        var shoulderLengthRight = get("shoulderLengthRight");
        var frontNeckDiagonal = get("frontNeckDiagonal");
        var backNeckDiagonal = get("backNeckDiagonal");
        var neckBaseWidth = get("neckBaseWidth");
        var shoulderLengthLeft = get("shoulderLengthLeft");
        var leftShoulderPointZ = get("leftShoulderPointZ");
        var rightShoulderPointZ = get("rightShoulderPointZ");
        var leftSideNeckPointZ = get("leftSideNeckPointZ");
        var rightSideNeckPointZ = get("rightSideNeckPointZ");

        // Build block
        Block fullBlock = new Block(userName + "_Gill_NeckShoulder");
        blocks.add(fullBlock);

        //If conditions
        double sidePointRight = ((rightSideNeckPointZ < 0 &&  rightShoulderPointZ < 0) || (rightSideNeckPointZ > 0 &&  rightShoulderPointZ < 0)) ? abs(rightSideNeckPointZ - rightShoulderPointZ) : -abs(rightSideNeckPointZ - rightShoulderPointZ);
        double sidePointLeft = ((leftSideNeckPointZ < 0 &&  leftShoulderPointZ < 0) || (leftSideNeckPointZ > 0 &&  leftShoulderPointZ < 0)) ? abs(leftSideNeckPointZ - leftShoulderPointZ) : -abs(leftSideNeckPointZ- leftShoulderPointZ);

        // Defining each point
        var point1 = new Vector2D(0, backNeckDiagonal);
        var point2 = new Vector2D(0, -frontNeckDiagonal);
        var point3 = new Vector2D(-(neckBaseWidth * 0.5), 0);
        var point4 = new Vector2D(-(neckBaseWidth * 0.5 + shoulderLengthRight), sidePointRight);
        var point5 = new Vector2D(neckBaseWidth * 0.5, 0);
        var point6 = new Vector2D(neckBaseWidth * 0.5 + shoulderLengthLeft, sidePointLeft);


        // All the keypoints/vectors added
        fullBlock.addKeypoint(point1);
        fullBlock.addCircularArc(point1, point3, 0.9, true);
        fullBlock.addKeypoint(point3);
        fullBlock.addCircularArc(point3, point2, 1.5, true);
        fullBlock.addKeypoint(point2);
        fullBlock.addCircularArc(point2, point5, 1.5, true);
        fullBlock.addKeypoint(point5);
        fullBlock.addCircularArc(point5, point1, 0.9, true);
        fullBlock.addKeypoint(point1);
        fullBlock.addKeypoint(point4);
        fullBlock.addKeypoint(point2);
        fullBlock.addKeypoint(point6);
        fullBlock.addKeypoint(point5);
        fullBlock.addKeypoint(point3);
        fullBlock.addKeypoint(point4);
        fullBlock.addKeypoint(point1);
        fullBlock.addKeypoint(point2);
        fullBlock.addKeypoint(point1);
        fullBlock.addKeypoint(point6);

    }
}
