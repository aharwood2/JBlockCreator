package gill;

import aldrich.SkirtPattern;
import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.Block;
import jblockmain.Measurements;
import jblockmain.Pattern;
import jblockmain.easeMeasurement;
import mathcontainers.Vector2D;

import javax.swing.text.html.Option;
import java.util.ArrayList;

public class SweatShirtPattern extends Pattern
{
    private double BustChestEase;
    private double BackWidthEase;
    private double CBToUnderArmEase;
    private double NeckWidthEase;
    private double HipCircumEase;
    private double OptionalSoBWaistCircEase;
    private double WristEase;
    private double BackShouldWidthEase;

    private double ChestBustCircumTapeMeasure;
    private double HipCircumTapeMeasure;
    private double NeckCircumference;
    private double CBNeckDepthDefault;
    private double HalfBackCentreTapeMeasure;
    private double WaistToHipLength;
    private double ScyeDepth;
    private double MidNeckBaseWidth;
    private double BackShoulderWidthHorizontal;
    private double LeftShoulderDrop;
    private double RightShoulderDrop;
    private double AcrossBackTapeMeasurement;
    private double OptionalSmallBackWaistTapeMeasure;
    private double ArmLengthLeft;
    private double ArmLengthRight;
    private double WristCircumL;
    private double WristCircumR;

    public SweatShirtPattern(Measurements dataStore)
    {
        if (!readMeasurements(dataStore)) return;
        addEasement();
        createBlocks();
    }

    /* Implement abstract methods from super class */
    @Override
    protected EMethod assignMethod()
    {
        return EMethod.GILL;
    }

    @Override
    protected EGarment assignGarment()
    {
        return EGarment.SWEATSHIRT;
    }

    @Override
    protected void addEasement() throws IndexOutOfBoundsException
    {
          BustChestEase = easeMeasurements.get(0).getValue();
          BackWidthEase = easeMeasurements.get(1).getValue();
          CBToUnderArmEase = easeMeasurements.get(2).getValue();
          NeckWidthEase = easeMeasurements.get(3).getValue();
          HipCircumEase = easeMeasurements.get(4).getValue();
          OptionalSoBWaistCircEase = easeMeasurements.get(5).getValue();
          WristEase = easeMeasurements.get(6).getValue();
          BackShouldWidthEase = easeMeasurements.get(7).getValue();
          CBNeckDepthDefault = easeMeasurements.get(8).getValue();
    }

    @Override
    protected boolean readMeasurements(Measurements dataStore)
    {
        try {
            // Based on measurements for this pattern we can read the following from the scan:
            ChestBustCircumTapeMeasure = dataStore.getMeasurement("A01").value;
            OptionalSmallBackWaistTapeMeasure = dataStore.getMeasurement("A02").value;
            HipCircumTapeMeasure = dataStore.getMeasurement("A03").value;
            HalfBackCentreTapeMeasure = dataStore.getMeasurement("A04").value;
            NeckCircumference = dataStore.getMeasurement("A05").value;
            ScyeDepth = dataStore.getMeasurement("A06").value;
            AcrossBackTapeMeasurement = dataStore.getMeasurement("A09").value;
            WaistToHipLength = dataStore.getMeasurement("A15").value;
            MidNeckBaseWidth = dataStore.getMeasurement("A63").value;
            BackShoulderWidthHorizontal = dataStore.getMeasurement("A64").value;
            LeftShoulderDrop = dataStore.getMeasurement("A65").value;
            RightShoulderDrop = dataStore.getMeasurement("A66").value;
            ArmLengthLeft = dataStore.getMeasurement("A67").value;
            ArmLengthRight = dataStore.getMeasurement("A68").value;
            WristCircumL = dataStore.getMeasurement("A69").value;
            WristCircumR = dataStore.getMeasurement("A70").value;


            // Get name
            userName = dataStore.getName();

            return true;
        }
        catch(MeasurementNotFoundException e)
        {
            addMissingMeasurement(dataStore.getName(), e.getMeasurementId());
            return false;
        }
    }

    /**
     * The actual block creation process following the drafting method of Gill.
     */
    @Override
    protected void createBlocks()
    {
        Block backBlock = new Block(userName + "_Gill_Back_SweatShirt_Block");
        blocks.add(backBlock);

        // Conditional to set a position based on large of 2 values
        double shoulderDepthXPosition = LeftShoulderDrop > RightShoulderDrop ? LeftShoulderDrop : RightShoulderDrop;

        // All of the keypoints added in as Vectors
        Vector2D point1 = new Vector2D(CBNeckDepthDefault, 0.0);
        Vector2D point2 = new Vector2D(HalfBackCentreTapeMeasure + WaistToHipLength + CBNeckDepthDefault - 6.5, 0.0);
        Vector2D point3 = new Vector2D(point2.getX(), ChestBustCircumTapeMeasure / 4.0 + BustChestEase / 4.0);
        Vector2D point4 = new Vector2D(HalfBackCentreTapeMeasure, OptionalSmallBackWaistTapeMeasure / 4.0 + OptionalSoBWaistCircEase / 4.0);
        Vector2D point5 = new Vector2D(ScyeDepth + CBToUnderArmEase, point3.getY());
        Vector2D point6 = new Vector2D(ScyeDepth / 2.0 + CBToUnderArmEase, BackShoulderWidthHorizontal / 2.0 - 2.0);
        Vector2D point7 = new Vector2D(shoulderDepthXPosition, BackShoulderWidthHorizontal / 2.0);
        Vector2D point8 = new Vector2D(0.0, MidNeckBaseWidth / 2.0 + NeckWidthEase / 2.0);

        // All the Keypoints added to the block
        backBlock.addKeypoint(point1);
        backBlock.addKeypoint(point2);
        backBlock.addKeypoint(point3);
        backBlock.addKeypoint(point5);
        backBlock.addKeypoint(point6);
        backBlock.addKeypoint(point7);
        backBlock.addKeypoint(point8);

        // Bezier curve to angle the curve for 90/0 degrees since directed curve was not working
        // + Rest of the curves
        backBlock.addQuadraticBezierCurve(point5, new Vector2D(point5.getX(), point6.getY()), point6);
        backBlock.addDirectedCurve(point6, point7, new Vector2D(-1.0, 0.0), new Vector2D(point8.subtract(point7)), new double[] {0.0, 90.0});
        backBlock.addDirectedCurve(point8, point1, new double[] {90.0,90.0});

        Block frontBlock = new Block(userName + "_Gill_Front_SweatShirt_Block");
        blocks.add(frontBlock);

        Vector2D point9 = new Vector2D(NeckCircumference / 5.0 - 1.5, 0);
        Vector2D point10 = new Vector2D(point2.getX(), 0);
        Vector2D point11 = new Vector2D(point2.getX(), point3.getY());
        Vector2D point12 = point4;
        Vector2D point13 = point5;
        Vector2D point14 = point6;
        Vector2D point15 = point7;
        Vector2D point16 = point8;

        frontBlock.addKeypoint(point9);
        frontBlock.addKeypoint(point10);
        frontBlock.addKeypoint(point11);
        frontBlock.addKeypoint(point13);
        frontBlock.addKeypoint(point14);
        frontBlock.addKeypoint(point15);
        frontBlock.addKeypoint(point16);

        frontBlock.addQuadraticBezierCurve(point13, new Vector2D(point13.getX(), point14.getY()), point14);
        frontBlock.addDirectedCurve(point14, point15, new Vector2D(-1.0, 0.0), new Vector2D(point16.subtract(point15)), new double[] {0.0, 90.0});
        frontBlock.addDirectedCurve(point16, point9, new double[] {90.0,90.0});
    }

    protected static ArrayList<easeMeasurement> easeMeasurements = new ArrayList<>();

    public static void populateEaseMeasurements()
    {
        // Check to see it hasn't already been populated
        if (easeMeasurements.size() > 0) {return;}
        easeMeasurements.add(new easeMeasurement("Bust/Chest Ease", 16));
        easeMeasurements.add(new easeMeasurement("Back Width Ease", 2.5));
        easeMeasurements.add(new easeMeasurement("CB to Under Arm Ease", 3));
        easeMeasurements.add(new easeMeasurement("Neck Width Ease", 1));
        easeMeasurements.add(new easeMeasurement("Hip Circ Ease", 12));
        easeMeasurements.add(new easeMeasurement("Optional SoB Waist Circ Ease", 18));
        easeMeasurements.add(new easeMeasurement("Wrist Ease", 8));
        easeMeasurements.add(new easeMeasurement("Back Shoulder Width Horizontal Ease", 0));
        easeMeasurements.add(new easeMeasurement("CBNeckDepthDefault", 2));
    }

    public static ArrayList<easeMeasurement> getEaseMeasurement()
    {
        return easeMeasurements;
    }

}
