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
import java.util.Collections;

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
    private double CrownWidthMultiplier;

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
        try {
            BustChestEase = easeMeasurements.get(0).getValue();
            BackWidthEase = easeMeasurements.get(1).getValue();
            CBToUnderArmEase = easeMeasurements.get(2).getValue();
            NeckWidthEase = easeMeasurements.get(3).getValue();
            HipCircumEase = easeMeasurements.get(4).getValue();
            OptionalSoBWaistCircEase = easeMeasurements.get(5).getValue();
            WristEase = easeMeasurements.get(6).getValue();
            BackShouldWidthEase = easeMeasurements.get(7).getValue();
            CBNeckDepthDefault = easeMeasurements.get(8).getValue();
            CrownWidthMultiplier = easeMeasurements.get(9).getValue();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Ease array out of bound");
        }
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
        double largestShoulderDrop = LeftShoulderDrop > RightShoulderDrop ? LeftShoulderDrop : RightShoulderDrop;
        double largestArmLength = ArmLengthLeft > ArmLengthRight ? ArmLengthLeft : ArmLengthRight;
        double largestWristCircum = WristCircumL > WristCircumR ? WristCircumL : WristCircumR;

        // All of the keypoints added in as Vectors
        Vector2D point1 = new Vector2D(CBNeckDepthDefault, 0.0);
        Vector2D point2 = new Vector2D(HalfBackCentreTapeMeasure + WaistToHipLength + CBNeckDepthDefault - 6.5, 0.0);
        Vector2D point3 = new Vector2D(point2.getX(), ChestBustCircumTapeMeasure / 4.0 + BustChestEase / 4.0);
        Vector2D point4 = new Vector2D(HalfBackCentreTapeMeasure, OptionalSmallBackWaistTapeMeasure / 4.0 + OptionalSoBWaistCircEase / 4.0);
        Vector2D point5 = new Vector2D(ScyeDepth + CBToUnderArmEase, point3.getY());
        Vector2D point6 = new Vector2D(ScyeDepth / 2.0 + CBToUnderArmEase, BackShoulderWidthHorizontal / 2.0 - 2.0);
        Vector2D point7 = new Vector2D(largestShoulderDrop, BackShoulderWidthHorizontal / 2.0);
        Vector2D point8 = new Vector2D(0.0, MidNeckBaseWidth / 2.0 + NeckWidthEase / 2.0);

        // All the Keypoints added to the block
        backBlock.addKeypoint(point1);
        backBlock.addKeypoint(point2);
        backBlock.addKeypoint(point3);
        backBlock.addKeypoint(point5);
        backBlock.addKeypoint(point6);
        backBlock.addKeypoint(point7);
        backBlock.addKeypoint(point8);

        backBlock.addQuadraticBezierCurve(point5, new Vector2D(point5.getX(), point6.getY()), point6);

        backBlock.addDirectedCurve(point6, point7,
                new Vector2D(-1.0, 0.0),
                new Vector2D(point8.subtract(point7)),
                new double[] {0.0, 90.0});

        backBlock.addDirectedCurve(point8, point1, new double[] {90.0,90.0});

        // Get the max and min y with a small additional buffer of 5cm for the construction lines
        double maxY = Collections.max(backBlock.getPlottableKeypointsY()) + 5.0;
        double minY = Collections.min(backBlock.getPlottableKeypointsY()) - 5.0;

        // Front Sweatshirt Block
        Block frontBlock = new Block(userName + "_Gill_Front_SweatShirt_Block");
        blocks.add(frontBlock);

        // Most of the keypoints added as vectors, a lot of similarities with backblock but kept for readability
        Vector2D point9 = new Vector2D(NeckCircumference / 5.0 - 1.5, 0);
        Vector2D point10 = new Vector2D(point2.getX(), 0);
        Vector2D point11 = new Vector2D(point2.getX(), point3.getY());
        Vector2D point12 = point4;
        Vector2D point13 = point5;
        Vector2D point14 = point6;
        Vector2D point15 = point7;
        Vector2D point16 = point8;

        Vector2D point1314 = new Vector2D(point14.getX() + ((point13.getX() - point14.getX()) / 3.0), point14.getY() + ((point13.getY() - point14.getY())/ 20.0));


        // Addition of all the points as keypoints
        frontBlock.addKeypoint(point9);
        frontBlock.addKeypoint(point10);
        frontBlock.addKeypoint(point11);
        frontBlock.addKeypoint(point13);
        frontBlock.addKeypoint(point1314);
        frontBlock.addKeypoint(point14);
        frontBlock.addKeypoint(point15);
        frontBlock.addKeypoint(point16);

        // Calculating extension for the tangent offset
        double extension = Math.sqrt(Math.abs((point13.getY() - point14.getY()) - (point13.getX() - point14.getX()))) / 2.0;
        extension = point13.getX() - point14.getX() > point13.getY() - point14.getY() ? -extension : extension;

        frontBlock.addDirectedCurveWithApexTangent(point13, point1314,
                new Vector2D(point13.getX(), point1314.getY()),
                4.0 + extension,
                new double[]{90.0, 0.0}, new int[]{-1, 1});

        frontBlock.addDirectedCurve(point14, point15,
                new Vector2D(-1.0, 0.0),
                new Vector2D(point8.subtract(point15)),
                new double[] {0.0, 90.0});

        frontBlock.addBlendedCurve(point1314, point14);

        frontBlock.addDirectedCurve(point16, point9, new double[] {90.0,90.0});

        // All the construction keypoints added for both front and back since they are similar
        backBlock.addConstructionPoint(new Vector2D(point8.getX(), minY), new Vector2D(point8.getX(), maxY), "Side Nk");
        backBlock.addConstructionPoint(new Vector2D(point1.getX(), minY), new Vector2D(point1.getX(), maxY), "Bk Nk Depth");
        backBlock.addConstructionPoint(new Vector2D(point7.getX(), minY), new Vector2D(point7.getX(), maxY), "Shldr Depth");
        backBlock.addConstructionPoint(new Vector2D(point9.getX(), minY), new Vector2D(point9.getX(), maxY), "Fr Nk Depth");
        backBlock.addConstructionPoint(new Vector2D(point6.getX(), minY), new Vector2D(point6.getX(), maxY), "Midway Nk To U-Arm");
        backBlock.addConstructionPoint(new Vector2D(point5.getX(), minY), new Vector2D(point5.getX(), maxY), "U-Arm");
        backBlock.addConstructionPoint(new Vector2D(point4.getX(), minY), new Vector2D(point4.getX(), maxY), "Waist");
        backBlock.addConstructionPoint(new Vector2D(point3.getX(), minY), new Vector2D(point3.getX(), maxY), "Hem");

        frontBlock.addConstructionPoint(new Vector2D(point8.getX(), minY), new Vector2D(point8.getX(), maxY), "Side Nk");
        frontBlock.addConstructionPoint(new Vector2D(point1.getX(), minY), new Vector2D(point1.getX(), maxY), "Bk Nk Depth");
        frontBlock.addConstructionPoint(new Vector2D(point7.getX(), minY), new Vector2D(point7.getX(), maxY), "Shldr Depth");
        frontBlock.addConstructionPoint(new Vector2D(point9.getX(), minY), new Vector2D(point9.getX(), maxY), "Fr Nk Depth");
        frontBlock.addConstructionPoint(new Vector2D(point6.getX(), minY), new Vector2D(point6.getX(), maxY), "Midway Nk To U-Arm");
        frontBlock.addConstructionPoint(new Vector2D(point5.getX(), minY), new Vector2D(point5.getX(), maxY), "U-Arm");
        frontBlock.addConstructionPoint(new Vector2D(point4.getX(), minY), new Vector2D(point4.getX(), maxY), "Waist");
        frontBlock.addConstructionPoint(new Vector2D(point3.getX(), minY), new Vector2D(point3.getX(), maxY), "Hem");

        // Construction keypoint representing point4 which is used in shaping
        backBlock.addConstructionPoint(
                new Vector2D(point4.getX() - 2.0, point4.getY()),
                new Vector2D(point4.getX() + 2.0, point4.getY()),
                "");

        frontBlock.addConstructionPoint(
                new Vector2D(point4.getX() - 2.0, point4.getY()),
                new Vector2D(point4.getX() + 2.0, point4.getY()),
                "");

        /////////////////////////////////////SLEEVE BLOCK////////////////////////////////////////////////////////////////

        Block sleeveBlock = new Block(userName + "_Gill_Sleeve_Block");
        blocks.add(sleeveBlock);

        // Calculates the length between point 13 and point 15 and point 1 to point5 but only in X
        double sleeveWidthAid = new Vector2D(point13.subtract(point15)).norm();
        double crownHeightAid = (point5.getX() - point1.getX()) * CrownWidthMultiplier;

        // Calculation of point1
        Vector2D sleevePoint9 = new Vector2D(-(point5.getX() / 2.0), 0.0);
        // 9 is the Multiplier to adjust the amount as a percentage that is used
        Vector2D sleevePoint1 = new Vector2D(0.0, -(
                Math.sqrt(
                Math.abs    (
                Math.pow(sleeveWidthAid + 2.0, 2) - Math.pow(crownHeightAid / 2.0, 2)
                            )
                         )
        ));

        Vector2D sleevePoint3 = new Vector2D(largestArmLength - (sleevePoint9.getX() / 2.0), -((largestWristCircum + WristEase) / 2.0));
        Vector2D sleevePoint4 = new Vector2D(sleevePoint3.getX(), 0.0);
        Vector2D sleevePoint5 = new Vector2D(sleevePoint3.getX(), -(sleevePoint3.getY()));
        Vector2D sleevePoint7 = new Vector2D(0.0, -(sleevePoint1.getY()));

        // Create a direction vector from point1 to point3
        Vector2D D1_3 = new Vector2D(sleevePoint3.subtract(sleevePoint1));
        // Calculate point 2 as halfway
        Vector2D sleevePoint2 = new Vector2D(sleevePoint1.add(D1_3.divide(2.0)));
        // Point6 is just point 2 reflected in the x-axis
        Vector2D sleevePoint6 = new Vector2D(sleevePoint2.getX(), -(sleevePoint2.getY()));

        Vector2D D7_8 = new Vector2D(sleevePoint9.subtract(sleevePoint7));
        // Point8 is just 1/3rd the distance of 7 to 9
        Vector2D sleevePoint8 = new Vector2D(sleevePoint7.add(D7_8.divide(3.0)));
        Vector2D sleevePoint10 = new Vector2D(sleevePoint8.getX(), -(sleevePoint8.getY() - 0.01));

        sleeveBlock.addKeypoint(sleevePoint1);
        sleeveBlock.addKeypoint(sleevePoint2);
        sleeveBlock.addKeypoint(sleevePoint3);
        sleeveBlock.addKeypoint(sleevePoint4);
        sleeveBlock.addKeypoint(sleevePoint5);
        sleeveBlock.addKeypoint(sleevePoint6);
        sleeveBlock.addKeypoint(sleevePoint7);
        sleeveBlock.addKeypoint(sleevePoint8);
        sleeveBlock.addKeypoint(sleevePoint10);

        sleeveBlock.addCircularCurve(sleevePoint8, sleevePoint10, point9.getX(), true, true);
        sleeveBlock.addDirectedCurve(sleevePoint7, sleevePoint8, new double[] {90.0, 0.0});
        sleeveBlock.addDirectedCurve(sleevePoint10, sleevePoint1, new double[] {0.0, 90.0});

        //Block sleeveBlock2 = new Block(userName + "_Gill_Sleeve2_Block");
        //blocks.add(sleeveBlock2);
    }

    protected static ArrayList<easeMeasurement> easeMeasurements = new ArrayList<>();

    public static void populateEaseMeasurements()
    {
        // Check to see it hasn't already been populated / it is empty
        if (easeMeasurements.size() > 0) {return;}
        easeMeasurements.add(new easeMeasurement("Bust/Chest Ease", 16.0));
        easeMeasurements.add(new easeMeasurement("Back Width Ease", 2.5));
        easeMeasurements.add(new easeMeasurement("CB to Under Arm Ease", 3.0));
        easeMeasurements.add(new easeMeasurement("Neck Width Ease", 1.0));
        easeMeasurements.add(new easeMeasurement("Hip Circ Ease", 12.0));
        easeMeasurements.add(new easeMeasurement("Optional SoB Waist Circ Ease", 18.0));
        easeMeasurements.add(new easeMeasurement("Wrist Ease", 8.0));
        easeMeasurements.add(new easeMeasurement("Back Shoulder Width Horizontal Ease", 0.0));
        easeMeasurements.add(new easeMeasurement("CB Neck Depth Default", 2.0));
        easeMeasurements.add(new easeMeasurement("Crown Height Multiplier (Abs)", 1.0));
    }

    public static ArrayList<easeMeasurement> getEaseMeasurement()
    {
        return easeMeasurements;
    }
}
