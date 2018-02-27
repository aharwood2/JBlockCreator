package jblockmain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Class to hold the measurements used by the block packages.
 * For now this essentially encapsulates the sizestream scan data file. */
public class Measurements
{
    
    // Nested structure
    public class Store
    {
        private String scanDataName;
        public double value;
        
        public Store(String _scanDataName, double _value)
        {
            this.scanDataName = _scanDataName;
            this.value = _value;
        }
    }

    /* Core Measurements */
    public Store CollarCircumference;
    public Store NeckCircumference;
    public Store BackShoulderWidthHorizontal;
    public Store ShoulderLengthLeft;
    public Store ShoulderLengthRight;
    public Store SideNecktoBustlengthLeft;
    public Store SideNecktoBustlengthRight;
    public Store ArmHoleCircumferenceLeft;
    public Store ArmHoleCircumferenceRight;
    public Store OuterArmHoleCircumferenceLeft;
    public Store OuterArmHoleCircumferenceRight;
    public Store ArmLengthLeft;
    public Store ArmLengthRight;
    public Store ArmUnderLengthLeft;
    public Store ArmUnderLengthRight;
    public Store SleeveLengthLeft;
    public Store SleeveLengthRight;
    public Store AbdomenHeight;
    public Store AbdomenRise;
    public Store CrotchHeight;
    public Store CrotchLengthFull;
    public Store FrontVerticalRise;
    public Store SeatFoldHeight;
    public Store InseamLeft;
    public Store InseamRight;
    public Store OutsideLegLengthLeft;
    public Store OutsideLegLengthRight;
    public Store ThighCircumferenceLeft;
    public Store ThighCircumferenceRight;
    public Store UnderKneeCircumferenceLeft;
    public Store UnderKneeCircumferenceRight;
    public Store UnderKneeHeightLeft;
    public Store UnderKneeHeightRight;
    public Store CalfCircumferenceLeft;
    public Store CalfCircumferenceRight;
    public Store MinLowerLegGirthLeft;
    public Store MinLowerLegGirthRight;
    public Store MinlowerlegHeightLeft;
    public Store MinlowerlegHeightRight;
    public Store ChestOrBustCircumTapeMeasure;
    public Store OverArmCircumTapeMeasure;
    public Store BustGirthWithDropTapeMeasure;
    public Store UnderbustCircumTapeMeasure;
    public Store WaistCircumTapeMeasure;
    public Store ElbowHeightWaistTapeMeasure;
    public Store ElbowHeightWaist;
    public Store StomachFPCircumTapeMeasure;
    public Store AbdomenCircumTapeMeasure;
    public Store HipCircumTapeMeasure;
    public Store SeatCircumTapeMeasure;
    public Store AcrossBackTapeMeasurement;
    public Store BicepCircumferenceLeft;
    public Store BicepCircumferenceRight;
    public Store WristCircumferenceLeft;
    public Store WristCircumferenceRight;
    public Store HalfBackCenterTapeMeasure;
    public Store BackNecktoBackChestContourLength;
    public Store SubjectHeight;
    public Store BackNecktoShoulderBladeTapeMeasure;
    public Store OptSmallofBackWaistTapeMeasure;
    public Store OptSmallWaistBackHeight;
    public Store OptSmallWaistFrontHeight;
    public Store OptSmallWaistLeftHeight;
    public Store OptSmallWaistRightHeight;
    public Store ForearmCircumferenceLeft;
    public Store ForearmCircumferenceRight;
    public Store FrontHipTapeMeasure;
    public Store FrontWaistTapeMeasure;
    public Store ElbowCircumferenceTapeMeasureLeft;
    public Store ElbowCircumferenceTapeMeasureRight;
    public Store ChinHeight;
    public Store AxillaChestCircumferenceTapeMeasure;
    public Store HingedUpperBustCircumference;
    public Store HighHip;
    public Store HighHiptoSmallOfBackOptimizedWaist;
    public Store LowHip;
    public Store LowHiptoSmallOfBackOptimizedWaist;
    public Store HipsEightInchesDownfromSmallofBack;
    public Store HipsTwoInchesAboveCrotch;
    public Store BackVerticalRise;
    public Store UpperBustFrontLength;
    public Store ShoulderCircumference;
    public Store ShoulderCircumferenceHeight;
    public Store ActualAnkleCircumferenceLeft;
    public Store ActualAnkleCircumferenceRight;
    public Store ActualKneeCircumferenceLeft;
    public Store ActualKneeCircumferenceRight;
    public Store ActualMidThighCircumferenceLeft;
    public Store ActualMidThighCircumferenceRight;
    public Store AcrossChestArmtoArmLength;


    /* Custom Measurements */





    // Constructor
    public Measurements(String scanDataFileName)
    {
        try
        {
            // Open file and get an input stream
            FileReader file = new FileReader(scanDataFileName);
            BufferedReader fileStream = new BufferedReader(file);

            // Go through each line in turn and assign information about the measurement
            CollarCircumference = assignValueFromLine(fileStream);
            NeckCircumference = assignValueFromLine(fileStream);
            BackShoulderWidthHorizontal = assignValueFromLine(fileStream);
            ShoulderLengthLeft = assignValueFromLine(fileStream);
            ShoulderLengthRight = assignValueFromLine(fileStream);
            SideNecktoBustlengthLeft = assignValueFromLine(fileStream);
            SideNecktoBustlengthRight = assignValueFromLine(fileStream);
            ArmHoleCircumferenceLeft = assignValueFromLine(fileStream);
            ArmHoleCircumferenceRight = assignValueFromLine(fileStream);
            OuterArmHoleCircumferenceLeft = assignValueFromLine(fileStream);
            OuterArmHoleCircumferenceRight = assignValueFromLine(fileStream);
            ArmLengthLeft = assignValueFromLine(fileStream);
            ArmLengthRight = assignValueFromLine(fileStream);
            ArmUnderLengthLeft = assignValueFromLine(fileStream);
            ArmUnderLengthRight = assignValueFromLine(fileStream);
            SleeveLengthLeft = assignValueFromLine(fileStream);
            SleeveLengthRight = assignValueFromLine(fileStream);
            AbdomenHeight = assignValueFromLine(fileStream);
            AbdomenRise = assignValueFromLine(fileStream);
            CrotchHeight = assignValueFromLine(fileStream);
            CrotchLengthFull = assignValueFromLine(fileStream);
            FrontVerticalRise = assignValueFromLine(fileStream);
            SeatFoldHeight = assignValueFromLine(fileStream);
            InseamLeft = assignValueFromLine(fileStream);
            InseamRight = assignValueFromLine(fileStream);
            OutsideLegLengthLeft = assignValueFromLine(fileStream);
            OutsideLegLengthRight = assignValueFromLine(fileStream);
            ThighCircumferenceLeft = assignValueFromLine(fileStream);
            ThighCircumferenceRight = assignValueFromLine(fileStream);
            UnderKneeCircumferenceLeft = assignValueFromLine(fileStream);
            UnderKneeCircumferenceRight = assignValueFromLine(fileStream);
            UnderKneeHeightLeft = assignValueFromLine(fileStream);
            UnderKneeHeightRight = assignValueFromLine(fileStream);
            CalfCircumferenceLeft = assignValueFromLine(fileStream);
            CalfCircumferenceRight = assignValueFromLine(fileStream);
            MinLowerLegGirthLeft = assignValueFromLine(fileStream);
            MinLowerLegGirthRight = assignValueFromLine(fileStream);
            MinlowerlegHeightLeft = assignValueFromLine(fileStream);
            MinlowerlegHeightRight = assignValueFromLine(fileStream);
            ChestOrBustCircumTapeMeasure = assignValueFromLine(fileStream);
            OverArmCircumTapeMeasure = assignValueFromLine(fileStream);
            BustGirthWithDropTapeMeasure = assignValueFromLine(fileStream);
            UnderbustCircumTapeMeasure = assignValueFromLine(fileStream);
            WaistCircumTapeMeasure = assignValueFromLine(fileStream);
            ElbowHeightWaistTapeMeasure = assignValueFromLine(fileStream);
            ElbowHeightWaist = assignValueFromLine(fileStream);
            StomachFPCircumTapeMeasure = assignValueFromLine(fileStream);
            AbdomenCircumTapeMeasure = assignValueFromLine(fileStream);
            HipCircumTapeMeasure = assignValueFromLine(fileStream);
            SeatCircumTapeMeasure = assignValueFromLine(fileStream);
            AcrossBackTapeMeasurement = assignValueFromLine(fileStream);
            BicepCircumferenceLeft = assignValueFromLine(fileStream);
            BicepCircumferenceRight = assignValueFromLine(fileStream);
            WristCircumferenceLeft = assignValueFromLine(fileStream);
            WristCircumferenceRight = assignValueFromLine(fileStream);
            HalfBackCenterTapeMeasure = assignValueFromLine(fileStream);
            BackNecktoBackChestContourLength = assignValueFromLine(fileStream);
            SubjectHeight = assignValueFromLine(fileStream);
            BackNecktoShoulderBladeTapeMeasure = assignValueFromLine(fileStream);
            OptSmallofBackWaistTapeMeasure = assignValueFromLine(fileStream);
            OptSmallWaistBackHeight = assignValueFromLine(fileStream);
            OptSmallWaistFrontHeight = assignValueFromLine(fileStream);
            OptSmallWaistLeftHeight = assignValueFromLine(fileStream);
            OptSmallWaistRightHeight = assignValueFromLine(fileStream);
            ForearmCircumferenceLeft = assignValueFromLine(fileStream);
            ForearmCircumferenceRight = assignValueFromLine(fileStream);
            FrontHipTapeMeasure = assignValueFromLine(fileStream);
            FrontWaistTapeMeasure = assignValueFromLine(fileStream);
            ElbowCircumferenceTapeMeasureLeft = assignValueFromLine(fileStream);
            ElbowCircumferenceTapeMeasureRight = assignValueFromLine(fileStream);
            ChinHeight = assignValueFromLine(fileStream);
            AxillaChestCircumferenceTapeMeasure = assignValueFromLine(fileStream);
            HingedUpperBustCircumference = assignValueFromLine(fileStream);
            HighHip = assignValueFromLine(fileStream);
            HighHiptoSmallOfBackOptimizedWaist = assignValueFromLine(fileStream);
            LowHip = assignValueFromLine(fileStream);
            LowHiptoSmallOfBackOptimizedWaist = assignValueFromLine(fileStream);
            HipsEightInchesDownfromSmallofBack = assignValueFromLine(fileStream);
            HipsTwoInchesAboveCrotch = assignValueFromLine(fileStream);
            BackVerticalRise = assignValueFromLine(fileStream);
            UpperBustFrontLength = assignValueFromLine(fileStream);
            ShoulderCircumference = assignValueFromLine(fileStream);
            ShoulderCircumferenceHeight = assignValueFromLine(fileStream);
            ActualAnkleCircumferenceLeft = assignValueFromLine(fileStream);
            ActualAnkleCircumferenceRight = assignValueFromLine(fileStream);
            ActualKneeCircumferenceLeft = assignValueFromLine(fileStream);
            ActualKneeCircumferenceRight = assignValueFromLine(fileStream);
            ActualMidThighCircumferenceLeft = assignValueFromLine(fileStream);
            ActualMidThighCircumferenceRight = assignValueFromLine(fileStream);
            AcrossChestArmtoArmLength = assignValueFromLine(fileStream);
            
            fileStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Method to inspect each line and assign a value to the variable named
    private Store assignValueFromLine(BufferedReader fileStream)
    {
        try
        {
            // Read the next line in the file stream
            String line = fileStream.readLine();

            // If the line does not start with 1 then read next line
            while (line.charAt(0) != '1') line = fileStream.readLine();
            
            if (line != null)
            {
                // Split the line into the name and the value
                int splitPoint = line.indexOf(":");
                String name = line.substring(3, splitPoint);
                String val = line.substring(splitPoint + 2);

                return new Store(name, Double.valueOf(val));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new Store("NULL", 0.0);
    }

}