package gill;

import jblockenums.EPattern;
import jblockexceptions.MeasurementNotFoundException;
import jblockmain.*;
import mathcontainers.Vector2D;

import java.util.ArrayList;
import java.util.Collections;

public class SweatShirtPattern
        extends Pattern
{
    public SweatShirtPattern(String userName, InputFileData dataStore)
    {
        super(userName, dataStore);
    }

    /* Implement abstract methods from super class */
    @Override
    protected EPattern assignPattern()
    {
        return EPattern.GILL_SWEATSHIRT;
    }

    @Override
    protected void defineRequiredMeasurements() throws Exception
    {
        measurements.addMeasurement(new Measurement("ChestBustCircumTapeMeasure", "A01"));
        measurements.addMeasurement(new Measurement("OptionalSmallBackWaistTapeMeasure", "A02"));
        measurements.addMeasurement(new Measurement("HalfBackCentreTapeMeasure", "A04"));
        measurements.addMeasurement(new Measurement("NeckCircumference", "A05"));
        measurements.addMeasurement(new Measurement("ScyeDepth", "A06"));
        measurements.addMeasurement(new Measurement("WaistToHipLength", "A15"));
        measurements.addMeasurement(new Measurement("MidNeckBaseWidth", "A63"));
        measurements.addMeasurement(new Measurement("BackShoulderWidthHorizontal", "A64"));
        measurements.addMeasurement(new Measurement("LeftShoulderDrop", "A65"));
        measurements.addMeasurement(new Measurement("RightShoulderDrop", "A66"));
        measurements.addMeasurement(new Measurement("ArmLengthLeft", "A67"));
        measurements.addMeasurement(new Measurement("ArmLengthRight", "A68"));
        measurements.addMeasurement(new Measurement("WristCircumL", "A69"));
        measurements.addMeasurement(new Measurement("WristCircumR", "A70"));

        // Arbitrary
        measurements.addMeasurement(new Measurement("CBNeckDepthDefault", 2.0));
        measurements.addMeasurement(new Measurement("CrownWidthMultiplier", 1.0));

        // Ease
        measurements.addMeasurement(new Measurement("BustChestEase", 16.0));
        measurements.addMeasurement(new Measurement("CBToUnderArmEase", 3.0));
        measurements.addMeasurement(new Measurement("NeckWidthEase", 1.0));
        measurements.addMeasurement(new Measurement("OptionalSoBWaistCircEase", 18.0));
        measurements.addMeasurement(new Measurement("WristEase", 8.0));
    }

    /**
     * The actual block creation process following the drafting method of Gill.
     */
    @Override
    protected void createBlocks()
    {
        // Pull from store
        var BustChestEase = get("BustChestEase");
        var CBToUnderArmEase = get("CBToUnderArmEase");
        var NeckWidthEase = get("NeckWidthEase");
        var OptionalSoBWaistCircEase = get("OptionalSoBWaistCircEase");
        var WristEase = get("WristEase");
        var ChestBustCircumTapeMeasure = get("ChestBustCircumTapeMeasure");
        var NeckCircumference = get("NeckCircumference");
        var CBNeckDepthDefault = get("CBNeckDepthDefault");
        var HalfBackCentreTapeMeasure = get("HalfBackCentreTapeMeasure");
        var WaistToHipLength = get("WaistToHipLength");
        var ScyeDepth = get("ScyeDepth");
        var MidNeckBaseWidth = get("MidNeckBaseWidth");
        var BackShoulderWidthHorizontal = get("BackShoulderWidthHorizontal");
        var LeftShoulderDrop = get("LeftShoulderDrop");
        var RightShoulderDrop = get("RightShoulderDrop");
        var OptionalSmallBackWaistTapeMeasure = get("OptionalSmallBackWaistTapeMeasure");
        var ArmLengthLeft = get("ArmLengthLeft");
        var ArmLengthRight = get("ArmLengthRight");
        var WristCircumL = get("WristCircumL");
        var WristCircumR = get("WristCircumR");
        var CrownWidthMultiplier = get("CrownWidthMultiplier");

        Block backBlock = new Block(userName + "_Gill_Back_SweatShirt_Block");
        blocks.add(backBlock);

        // Conditional to set a position based on large of 2 values
        double largestShoulderDrop = Math.max(LeftShoulderDrop, RightShoulderDrop);
        double largestArmLength = Math.max(ArmLengthLeft, ArmLengthRight);
        double largestWristCircum = Math.max(WristCircumL, WristCircumR);

        // All of the keypoints added in as Vectors
        Vector2D point1 = new Vector2D(CBNeckDepthDefault, 0.0);
        Vector2D point2 = new Vector2D(HalfBackCentreTapeMeasure + WaistToHipLength + CBNeckDepthDefault - 6.5, 0.0);
        Vector2D point3 = new Vector2D(point2.getX(), ChestBustCircumTapeMeasure / 4.0 + BustChestEase / 4.0);
        Vector2D point4 = new Vector2D(HalfBackCentreTapeMeasure,
                                       OptionalSmallBackWaistTapeMeasure / 4.0 + OptionalSoBWaistCircEase / 4.0);
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
                                   new double[]{0.0, 90.0});

        backBlock.addDirectedCurve(point8, point1, new double[]{90.0, 90.0});

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

        // Addition of all the points as keypoints
        frontBlock.addKeypoint(point9);
        frontBlock.addKeypoint(point10);
        frontBlock.addKeypoint(point11);
        frontBlock.addKeypoint(point13);
        frontBlock.addKeypoint(point14);
        frontBlock.addKeypoint(point15);
        frontBlock.addKeypoint(point16);

        frontBlock.addQuadraticBezierCurve(point13, new Vector2D(point13.getX(), point14.getY()), point14);

        frontBlock.addDirectedCurve(point14, point15,
                                    new Vector2D(-1.0, 0.0),
                                    new Vector2D(point8.subtract(point15)),
                                    new double[]{0.0, 90.0});

        frontBlock.addDirectedCurveWithApexTangent(point16, point9, new Vector2D(point9.getX(), point16.getY()),
                                                   Math.sqrt(point9.getX() - point16.getX()), new double[]{90.0, 90.0},
                                                   new int[]{-1, -1});

        // All the construction keypoints added for both front and back since they are similar
        backBlock.addConstructionPoint(new Vector2D(point8.getX(), minY), new Vector2D(point8.getX(), maxY), "Side Nk");
        backBlock.addConstructionPoint(new Vector2D(point1.getX(), minY), new Vector2D(point1.getX(), maxY),
                                       "Bk Nk Depth");
        backBlock.addConstructionPoint(new Vector2D(point7.getX(), minY), new Vector2D(point7.getX(), maxY),
                                       "Shldr Depth");
        backBlock.addConstructionPoint(new Vector2D(point9.getX(), minY), new Vector2D(point9.getX(), maxY),
                                       "Fr Nk Depth");
        backBlock.addConstructionPoint(new Vector2D(point6.getX(), minY), new Vector2D(point6.getX(), maxY),
                                       "Midway Nk To U-Arm");
        backBlock.addConstructionPoint(new Vector2D(point5.getX(), minY), new Vector2D(point5.getX(), maxY), "U-Arm");
        backBlock.addConstructionPoint(new Vector2D(point4.getX(), minY), new Vector2D(point4.getX(), maxY), "Waist");
        backBlock.addConstructionPoint(new Vector2D(point3.getX(), minY), new Vector2D(point3.getX(), maxY), "Hem");

        frontBlock.addConstructionPoint(new Vector2D(point8.getX(), minY), new Vector2D(point8.getX(), maxY),
                                        "Side Nk");
        frontBlock.addConstructionPoint(new Vector2D(point1.getX(), minY), new Vector2D(point1.getX(), maxY),
                                        "Bk Nk Depth");
        frontBlock.addConstructionPoint(new Vector2D(point7.getX(), minY), new Vector2D(point7.getX(), maxY),
                                        "Shldr Depth");
        frontBlock.addConstructionPoint(new Vector2D(point9.getX(), minY), new Vector2D(point9.getX(), maxY),
                                        "Fr Nk Depth");
        frontBlock.addConstructionPoint(new Vector2D(point6.getX(), minY), new Vector2D(point6.getX(), maxY),
                                        "Midway Nk To U-Arm");
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

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////// SLEEVE BLOCK ///////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Block sleeveBlock = new Block(userName + "_Gill_Sleeve_Block");
        blocks.add(sleeveBlock);

        // Calculates the length between point 13 and point 15 and point 1 to point5 but only in X
        double sleeveWidthAid = new Vector2D(point13.subtract(point15)).norm();
        double crownHeightAid = (point5.getX() - point1.getX()) * CrownWidthMultiplier;

        // Calculation of point1
        point1 = new Vector2D(0.0, -(
                Math.sqrt(
                        Math.abs(
                                Math.pow(sleeveWidthAid + 2.0, 2) - Math.pow(crownHeightAid / 2.0, 2)
                        )
                )
        ));

        point9 = new Vector2D(-(crownHeightAid / 2.0), 0.0);
        point3 = new Vector2D(largestArmLength - (point9.getX() / 2.0), -((largestWristCircum + WristEase) / 2.0));
        point4 = new Vector2D(point3.getX(), 0.0);
        point5 = new Vector2D(point3.getX(), -(point3.getY()));
        point7 = new Vector2D(0.0, -(point1.getY()));

        // Create a direction vector from point1 to point3
        Vector2D D1_3 = new Vector2D(point3.subtract(point1));
        // Calculate point 2 as halfway
        point2 = new Vector2D(point1.add(D1_3.divide(2.0)));
        // Point6 is just point 2 reflected in the x-axis
        point6 = new Vector2D(point2.getX(), -(point2.getY()));

        Vector2D D7_8 = new Vector2D(point9.subtract(point7));
        // Point8 is just 1/3rd the distance of 7 to 9
        point8 = new Vector2D(point7.add(D7_8.divide(3.0)));
        point10 = new Vector2D(point8.getX(), -(point8.getY() - 0.01));

        Vector2D internalPoint9_10 = new Vector2D(point10.getX() + ((point9.getX() - point10.getX()) / 2.0),
                                                  point1.getY() / 2.0);
        Vector2D internalPoint8_9 = new Vector2D(internalPoint9_10.getX(), -(internalPoint9_10.getY()));

        sleeveBlock.addKeypoint(point1);
        sleeveBlock.addKeypoint(point2);
        sleeveBlock.addKeypoint(point3);
        sleeveBlock.addKeypoint(point4);
        sleeveBlock.addKeypoint(point5);
        sleeveBlock.addKeypoint(point6);
        sleeveBlock.addKeypoint(point7);
        sleeveBlock.addKeypoint(point8);
        sleeveBlock.addKeypoint(internalPoint8_9);
        sleeveBlock.addKeypoint(internalPoint9_10);
        sleeveBlock.addKeypoint(point10);

        sleeveBlock.addCircularCurve(internalPoint8_9, internalPoint9_10,
                                     (point9.getX() - (internalPoint8_9.getX())),
                                     false, true);

        sleeveBlock.addDirectedCurve(point7, point8, new double[]{90.0, 0.0});
        sleeveBlock.addDirectedCurve(point10, point1, new double[]{0.0, 90.0});

        maxY = Collections.max(sleeveBlock.getPlottableKeypointsY()) + 5.0;
        minY = Collections.min(sleeveBlock.getPlottableKeypointsY()) - 5.0;
        double maxX = Collections.max(sleeveBlock.getPlottableKeypointsX()) + 5.0;
        double minX = Collections.min(sleeveBlock.getPlottableKeypointsX()) - 5.0;

        sleeveBlock.addConstructionPoint(new Vector2D(point9.getX(), minY), new Vector2D(point9.getX(), maxY),
                                         "Crown Height");
        sleeveBlock.addConstructionPoint(new Vector2D(point8.getX(), minY), new Vector2D(point8.getX(), maxY),
                                         "Angle Change");
        sleeveBlock.addConstructionPoint(new Vector2D(point1.getX(), minY), new Vector2D(point1.getX(), maxY),
                                         "Underarm");
        sleeveBlock.addConstructionPoint(new Vector2D(point2.getX(), minY), new Vector2D(point2.getX(), maxY), "Elbow");
        sleeveBlock.addConstructionPoint(new Vector2D(point3.getX(), minY), new Vector2D(point3.getX(), maxY),
                                         "Sleeve Hem");

        sleeveBlock.addConstructionPoint(new Vector2D(point4.add(point5.subtract(point4).multiply(0.5))),
                                         internalPoint8_9, "");
        sleeveBlock.addConstructionPoint(new Vector2D(point4.add(point3.subtract(point4).multiply(0.5))),
                                         internalPoint9_10, "");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////// SLEEVE BLOCK TWO ////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Block sleeveBlockTwo = new Block(userName + "_Gill_SleeveTwo_Block");
        blocks.add(sleeveBlockTwo);

        double cuffOpening = (WristEase + largestWristCircum) / 2.0;
        double sleeveHemXPosition = point3.getX();

        // As above for readability
        // Similarities as above sleeve patten, but they are rewritten here so as to not
        // Be Re-written later with the new values
        point11 = point8;
        point15 = point10;
        point10 = point7;
        point13 = point9;

        // A lot of similarities as above sleeve pattern, explicitly written down for readability
        point1 = point1;
        double sleeveWidth = point10.getY() - point1.getY();
        double cuffSpacing = (sleeveWidth - (cuffOpening * 2.0)) / 2.0;

        point2 = new Vector2D(point2.getX(), point1.getY());
        point3 = new Vector2D(sleeveHemXPosition, point1.getY());
        point4 = new Vector2D(sleeveHemXPosition, -((cuffSpacing) + cuffOpening / 2.0));
        point5 = new Vector2D(sleeveHemXPosition, point4.getY() + cuffSpacing);
        point6 = new Vector2D(sleeveHemXPosition, point5.getY() + cuffOpening);
        point7 = new Vector2D(sleeveHemXPosition, point6.getY() + cuffSpacing);
        point8 = new Vector2D(sleeveHemXPosition, point7.getY() + cuffOpening / 2.0);
        point9 = new Vector2D(point2.getX(), point8.getY());

        sleeveBlockTwo.addKeypoint(point8);
        sleeveBlockTwo.addKeypoint(point9);
        sleeveBlockTwo.addKeypoint(point10);
        sleeveBlockTwo.addKeypoint(point11);
        sleeveBlockTwo.addKeypoint(internalPoint8_9);
        sleeveBlockTwo.addKeypoint(internalPoint9_10);
        sleeveBlockTwo.addKeypoint(point15);
        sleeveBlockTwo.addKeypoint(point1);
        sleeveBlockTwo.addKeypoint(point2);
        sleeveBlockTwo.addKeypoint(point3);
        sleeveBlockTwo.addKeypoint(point4);
        sleeveBlockTwo.addKeypoint(internalPoint9_10);
        sleeveBlockTwo.addKeypoint(point5);
        sleeveBlockTwo.addKeypoint(point6);
        sleeveBlockTwo.addKeypoint(internalPoint8_9);
        sleeveBlockTwo.addKeypoint(point7);

        sleeveBlockTwo.addCircularCurve(internalPoint8_9, internalPoint9_10,
                                        point13.getX() - internalPoint8_9.getX(),
                                        false, true);

        sleeveBlockTwo.addDirectedCurve(point10, point11, new double[]{90.0, 0.0});
        sleeveBlockTwo.addDirectedCurve(point15, point1, new double[]{0.0, 90.0});


        sleeveBlockTwo.addConstructionPoint(new Vector2D(point13.getX(), minY), new Vector2D(point13.getX(), maxY),
                                            "Crown Height");
        sleeveBlockTwo.addConstructionPoint(new Vector2D(point14.getX(), minY), new Vector2D(point14.getX(), maxY),
                                            "Angle Change 1");
        sleeveBlockTwo.addConstructionPoint(new Vector2D(point15.getX(), minY), new Vector2D(point15.getX(), maxY),
                                            "Angle Change 2");
        sleeveBlockTwo.addConstructionPoint(new Vector2D(point1.getX(), minY), new Vector2D(point1.getX(), maxY),
                                            "Underarm");
        sleeveBlockTwo.addConstructionPoint(new Vector2D(point2.getX(), minY), new Vector2D(point2.getX(), maxY),
                                            "Elbow");
        sleeveBlockTwo.addConstructionPoint(new Vector2D(point3.getX(), minY), new Vector2D(point3.getX(), maxY),
                                            "Sleeve Hem");

        sleeveBlockTwo.addConstructionPoint(new Vector2D(minX, internalPoint8_9.getY()),
                                            new Vector2D(maxX, internalPoint8_9.getY()), "");
        sleeveBlockTwo.addConstructionPoint(new Vector2D(minX, internalPoint9_10.getY()),
                                            new Vector2D(maxX, internalPoint9_10.getY()), "");
    }
}
