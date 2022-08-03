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

        // TODO: Daniel to finish
        measurements.addMeasurement(new Measurement("NeckBaseWidth", "A93"));
        measurements.addMeasurement(new Measurement("ShoulderLengthLeft", "A94"));
        measurements.addMeasurement(new Measurement("LShoulderPointZ", "A95"));
        measurements.addMeasurement(new Measurement("RShoulderPointZ", "A96"));
        measurements.addMeasurement(new Measurement("LSideNeckPointZ", "A97"));
        measurements.addMeasurement(new Measurement("RSideNeckPointZ", "A98"));
    }

    @Override
    public void createBlocks()
    {
        // Pull from store
        var shoulderLengthRight = get("shoulderLengthRight");
        var frontNeckDiagonal = get("frontNeckDiagonal");
        var backNeckDiagonal = get("backNeckDiagonal");

        // TODO: Daniel to finish parsing measurements into local variables
        var NeckBaseWidth = get("NeckBaseWidth");
        var ShoulderLengthLeft = get("ShoulderLengthLeft");
        var LShoulderPointZ = get("LShoulderPointZ");
        var RShoulderPointZ = get("RShoulderPointZ");
        var LSideNeckPointZ = get("LSideNeckPointZ");
        var RSideNeckPointZ = get("RSideNeckPointZ");

        // Build block
        Block fullBlock = new Block(userName + "_Gill_NeckShoulder");
        blocks.add(fullBlock);

        //If conditions
        double Rsidepoint = ((RSideNeckPointZ < 0 &&  RShoulderPointZ < 0) || (RSideNeckPointZ > 0 &&  RShoulderPointZ < 0)) ? abs(RSideNeckPointZ - RShoulderPointZ) : -abs(RSideNeckPointZ - RShoulderPointZ);
        double Lsidepoint = ((LSideNeckPointZ < 0 &&  LShoulderPointZ < 0) || (LSideNeckPointZ > 0 &&  LShoulderPointZ < 0)) ? abs(LSideNeckPointZ - LShoulderPointZ) : -abs(LSideNeckPointZ - LShoulderPointZ);

        // Defining each point
        var point1 = new Vector2D(0, backNeckDiagonal);

        // TODO: Daniel to finish
        var point2 = new Vector2D(0, -frontNeckDiagonal);
        var point3 = new Vector2D(-(NeckBaseWidth*0.5), 0);
        var point4 = new Vector2D(-(NeckBaseWidth*0.5 + shoulderLengthRight), Rsidepoint);
        var point5 = new Vector2D(NeckBaseWidth*0.5, 0);
        var point6 = new Vector2D(NeckBaseWidth*0.5 + ShoulderLengthLeft, Lsidepoint);


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
