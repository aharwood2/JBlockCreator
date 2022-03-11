package gill;

import jblockenums.EPattern;
import jblockenums.EUnitType;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;
import java.util.Collections;

public class TrouserPatternThree
        extends Pattern
{
    public TrouserPatternThree(String userName, InputFileData dataStore, MeasurementSet template)
    {
        super(userName, dataStore, template);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.GILL_TROUSER3;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("optSmallBackWaistTapeMeasure", "A02"));
        measurements.addMeasurement(new Measurement("thighCircumR", "A17"));
        measurements.addMeasurement(new Measurement("kneeCircumR", "A18"));
        measurements.addMeasurement(new Measurement("ankleCircumR", "A19"));
        measurements.addMeasurement(new Measurement("frontAbdomenArc", "A28"));
        measurements.addMeasurement(new Measurement("backAbdomenArc", "A29"));
        measurements.addMeasurement(new Measurement("backSeatArc", "A30"));
        measurements.addMeasurement(new Measurement("waistToAbdomen", "A33"));
        measurements.addMeasurement(new Measurement("waistToSeat", "A34"));
        measurements.addMeasurement(new Measurement("bodyRise", "A38"));
        measurements.addMeasurement(new Measurement("seatDepth", "A39"));
        measurements.addMeasurement(new Measurement("ankleCircumHeightR", "A41"));
        measurements.addMeasurement(new Measurement("kneeCircumHeightR", "A42"));
        measurements.addMeasurement(new Measurement("seatCircumHeight", "A45"));
        measurements.addMeasurement(new Measurement("frontSeatArc", "A46"));
        measurements.addMeasurement(new Measurement("calfCircumR", "A85"));
        measurements.addMeasurement(new Measurement("calfCircumRHeight", "A86"));
        measurements.addMeasurement(new Measurement("midThighCircumR", "A87"));
        measurements.addMeasurement(new Measurement("midThighCircumRHeight", "A88"));
        measurements.addMeasurement(new Measurement("minLowerLegCircumR", "A89"));
        measurements.addMeasurement(new Measurement("minLowerLegCircumRHeight", "A90"));
        measurements.addMeasurement(new Measurement("crotchCircumMinus1cm", "A91"));
        measurements.addMeasurement(new Measurement("thighCircumRHeight", "A92"));

        // Arbitrary
        measurements.addMeasurement(new Measurement("Arb_FrontLegPercent", 24, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_BackLegPercent", 26, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_FrontCrotchExtPercent", 38, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_CFSeatToCreaseLinePercent", 9.45, EUnitType.PERCENTAGE));
        measurements.addMeasurement(new Measurement("Arb_CBSeatToCreaseLinePercent", 8.42, EUnitType.PERCENTAGE));
    }

    @Override
    public void createBlocks()
    {
        // Pull from store
        var optSmallBackWaistTapeMeasure = get("optSmallBackWaistTapeMeasure");
        var thighCircumR = get("thighCircumR");
        var kneeCircumR = get("kneeCircumR");
        var ankleCircumR = get("ankleCircumR");
        var frontAbdomenArc = get("frontAbdomenArc");
        var backAbdomenArc = get("backAbdomenArc");
        var backSeatArc = get("backSeatArc");
        var waistToAbdomen = get("waistToAbdomen");
        var waistToSeat = get("waistToSeat");
        var bodyRise = get("bodyRise");
        var seatDepth = get("seatDepth");
        var ankleCircumHeightR = get("ankleCircumHeightR");
        var kneeCircumHeightR = get("kneeCircumHeightR");
        var seatCircumHeight = get("seatCircumHeight");
        var frontSeatArc = get("frontSeatArc");
        var calfCircumR = get("calfCircumR");
        var calfCircumRHeight = get("calfCircumRHeight");
        var midThighCircumR = get("midThighCircumR");
        var midThighCircumRHeight = get("midThighCircumRHeight");
        var minLowerLegCircumR = get("minLowerLegCircumR");
        var minLowerLegCircumRHeight = get("minLowerLegCircumRHeight");
        var crotchCircumMinus1cm = get("crotchCircumMinus1cm");
        var thighCircumRHeight = get("thighCircumRHeight");

        // Arbitrary
        var arbFrontLegPercent = get("Arb_FrontLegPercent") / 100;
        var arbBackLegPercent = get("Arb_BackLegPercent") / 100;
        var arbFrontCrotchExtPercent = get("Arb_FrontCrotchExtPercent") / 100;
        var arbCFSeatToCreaseLinePercent = get("Arb_CFSeatToCreaseLinePercent") / 100;
        var arbCBSeatToCreaseLinePercent = get("Arb_CBSeatToCreaseLinePercent") / 100;

        // Define levels
        var waistLevel = waistToSeat;
        var abdomenLevel = waistToSeat - waistToAbdomen;
        var seatLevel = 0;
        var crotchLevel = waistToSeat - bodyRise;
        var upperThighLevel = thighCircumRHeight - seatCircumHeight;
        var midThighLevel = midThighCircumRHeight - seatCircumHeight;
        var kneeLevel = kneeCircumHeightR - seatCircumHeight;
        var calfLevel = calfCircumRHeight - seatCircumHeight;
        var lowerLegLevel = calfCircumRHeight - seatCircumHeight;
        var ankleLeve = ankleCircumHeightR - seatCircumHeight;

        // Build block
        Block frontBlock = new Block(userName + "_Gill_FrontBlock");
        blocks.add(frontBlock);

        // Add points
        var point1X = (frontSeatArc + backSeatArc) * arbCFSeatToCreaseLinePercent;
        frontBlock.addKeypoint(new Vector2D(point1X, seatLevel));   // Point 1
        frontBlock.addKeypoint(new Vector2D(point1X, abdomenLevel));    // Point 2
        frontBlock.addKeypoint(new Vector2D(point1X, waistLevel)); // Point 3
        frontBlock.addKeypoint(new Vector2D(((frontSeatArc + backSeatArc) * arbCFSeatToCreaseLinePercent) - (optSmallBackWaistTapeMeasure * 0.25), waistLevel)); // Point 4
        frontBlock.addKeypoint(new Vector2D(((frontSeatArc + backSeatArc) * arbCFSeatToCreaseLinePercent) - ((frontAbdomenArc + backAbdomenArc) * 0.25), abdomenLevel)); // Point 5
        var point6X = ((frontSeatArc + backSeatArc) * arbCFSeatToCreaseLinePercent) - ((frontSeatArc + backSeatArc) * 0.25);
        frontBlock.addKeypoint(new Vector2D(point6X, seatLevel));   // Point 6
        frontBlock.addKeypoint(new Vector2D(point6X, crotchLevel)); // Point 7
        var point22X = point1X + (seatDepth * arbFrontCrotchExtPercent);
        var temp = (crotchCircumMinus1cm / 2) - (-point6X + point22X);
        if (temp > 0) frontBlock.addKeypoint(new Vector2D(point6X - temp, crotchLevel)); // Point 8
        frontBlock.addKeypoint(new Vector2D(-thighCircumR * arbFrontLegPercent, upperThighLevel)); // Point 9
        frontBlock.addKeypoint(new Vector2D(-midThighCircumR * arbFrontLegPercent, midThighLevel)); // Point 10
        frontBlock.addKeypoint(new Vector2D(-kneeCircumR * arbFrontLegPercent, kneeLevel)); // Point 11
        frontBlock.addKeypoint(new Vector2D(-calfCircumR * arbFrontLegPercent, calfLevel)); // Point 12
        frontBlock.addKeypoint(new Vector2D(-minLowerLegCircumR * arbFrontLegPercent, lowerLegLevel)); // Point 13
        frontBlock.addKeypoint(new Vector2D(-ankleCircumR * arbFrontLegPercent, ankleLeve)); // Point 14
        frontBlock.addKeypoint(new Vector2D(ankleCircumR * arbFrontLegPercent, ankleLeve)); // Point 15
        frontBlock.addKeypoint(new Vector2D(minLowerLegCircumR * arbFrontLegPercent, lowerLegLevel)); // Point 16
        frontBlock.addKeypoint(new Vector2D(calfCircumR * arbFrontLegPercent, calfLevel)); // Point 17
        frontBlock.addKeypoint(new Vector2D(kneeCircumR * arbFrontLegPercent, kneeLevel)); // Point 18
        frontBlock.addKeypoint(new Vector2D(midThighCircumR * arbFrontLegPercent, midThighLevel)); // Point 19
        frontBlock.addKeypoint(new Vector2D(thighCircumR * arbFrontLegPercent, upperThighLevel)); // Point 20
        frontBlock.addKeypoint(new Vector2D(point1X, crotchLevel)); // Point 21
        frontBlock.addKeypoint(new Vector2D(point22X, crotchLevel)); // Point 22

        // Add construction lines
        var xMin = frontBlock.getMinimumX() - Arb_Con;
        var xMax = frontBlock.getMaximumX() + Arb_Con;
        frontBlock.addConstructionPoint(new Vector2D(xMin, xMax, waistLevel), "Waist Level");

    }

}