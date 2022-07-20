package gill;

import jblockenums.EPattern;
import jblockmain.*;
import mathcontainers.Vector2D;

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
        measurements.addMeasurement(new Measurement("A", "A93"));
        measurements.addMeasurement(new Measurement("B", "A95"));
        measurements.addMeasurement(new Measurement("C", "A96"));
        measurements.addMeasurement(new Measurement("D", "A97"));
        measurements.addMeasurement(new Measurement("E", "A98"));
    }

    @Override
    public void createBlocks()
    {
        // Pull from store
        var shoulderLengthRight = get("shoulderLengthRight");
        var frontNeckDiagonal = get("frontNeckDiagonal");
        var backNeckDiagonal = get("backNeckDiagonal");

        // TODO: Daniel to finish parsing measurements into local variables

        // Build block
        Block block = new Block(userName + "_Gill_NeckShoulder");
        blocks.add(block);

        // Add point 1
        var point1 = new Vector2D(0, backNeckDiagonal);
        block.addKeypoint(point1);
        var point2 = new Vector2D(0, frontNeckDiagonal);
        block.addKeypoint(point2);

        // TODO: Daniel to finish

    }
}
