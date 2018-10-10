package dxfwriter;

import jblockmain.JBlockCreator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * A class that encapsulates a DXF file and the methods required to write it from arrays of points which represent line
 * segments.
 */
public class DxfFile
{
    /**
     * Flag indicating whether file is ready
     */
    private boolean bIsOpen;

    /**
     * File writer
     */
    private FileWriter file;

    /**
     * Printer
     */
    private PrintWriter printer;

    /**
     * Creation date and time
     */
    private Date dDate;
    private String cDate;

    /**
     * Extremes of the drawing
     */
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    /**
     * Keypoint line coordinates
     */
    private ArrayList<Double> linesX = new ArrayList<>();
    private ArrayList<Double> linesY = new ArrayList<>();

    /**
     * Construction line coordinates
     */
    private ArrayList<Double> ConX = new ArrayList<>();
    private ArrayList<Double> ConY = new ArrayList<>();

    /**
     * Construction point names list
     */
    private ArrayList<String> names;


    /**
     * Some parameters for the annotation text writing
     */
    double baselineSkip = 10.0;
    double textHeight = baselineSkip * 0.8;
    double currentBaseline = maxY * 10.0;


    /**
     * Constructor to open a DXF file.
     * @param filename  name of the file including path
     */
    public DxfFile(String filename)
    {
        bIsOpen = false;

        // Create file
        try
        {
            file = new FileWriter(filename + ".dxf", false);
            printer = new PrintWriter(file);
            bIsOpen = true;
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            dDate = new Date();
            cDate = formatter.format(dDate);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to take a list of coordinates of line endings and add them to the DXF file
     * @param xPts  coordinates of x
     * @param yPts  coordinates of y
     */
    public void addLines(ArrayList<Double> xPts, ArrayList<Double> yPts)
    {
        this.linesX.addAll(xPts);
        this.linesY.addAll(yPts);

        // Set the min and max
        minX = Collections.min(linesX) < minX ? Collections.min(linesX) : minX;
        maxX = Collections.max(linesX) > maxX ? Collections.max(linesX) : maxX;
        minY = Collections.min(linesY) < minY ? Collections.min(linesY) : minY;
        maxY = Collections.max(linesY) > maxY ? Collections.max(linesY) : maxY;
    }

    /**
     * Method to take a list of coordinates for construction points and add them to the DXF file
     * @param xPts coordinates of x
     * @param yPts coordinates of y
     * @param names names of the points
     */
    public void addConstructionPoints(ArrayList<Double> xPts, ArrayList<Double> yPts, ArrayList<String> names)
    {
        this.ConX.addAll(xPts);
        this.ConY.addAll(yPts);
        this.names = names;

        if (ConX.size() != 0 && ConY.size() != 0 && names.size() != 0)
        {
            // Set the min and max
            minX = Collections.min(ConX) < minX ? Collections.min(ConX) : minX;
            maxX = Collections.max(ConX) > maxX ? Collections.max(ConX) : maxX;
            minY = Collections.min(ConY) < minY ? Collections.min(ConY) : minY;
            maxY = Collections.max(ConY) > maxY ? Collections.max(ConY) : maxY;
        }
    }

    /**
     * Writes the contents of the DXF file.
     * @param blockName     name of the block to be overlaid on the DXF drawing
     * @param dxfLayerChooser   array of flags indicating which features should be written
     */
    public void writeFile(String blockName, boolean[] dxfLayerChooser)
    {
        if (bIsOpen)
        {
            /* DXF format:
             * 4 sections minimum -- header, tables, blocks, entities.
             * Each section contains a set of groups which occupy 2 lines each. First line is the group code (int) and
             * the second is the value. Group code indicates the type of value.
             *      0-9 = string
             *      10-59 = double
             *      60-79 = int16
             *      90-99 = int32
             *      210-239 = double
             *      999 = comment
             *
             */

            // Start with printing date and time
            writeDxfLine("999", "Created on " + cDate);

            /* Write Header section */
            writeDxfLine("0", "SECTION");
            writeDxfLine("2", "HEADER");

            // Write the version number
            writeDxfLine("9", "$ACADVER");
            writeDxfLine("1", "AC1009");        // Version R12

            // Write the insertion base (I assume this is some kind of origin offset for x,y,z equiv 10,20,30)
            writeDxfLine("9", "$INSBASE");
            writeDxfLine("10", "0.0");
            writeDxfLine("20", "0.0");
            writeDxfLine("30", "0.0");

            // Write extremities (must convert to mm from cm)
            writeDxfLine("9", "$EXTMIN");
            writeDxfLine("10", Double.toString(minX * 10.0));
            writeDxfLine("20", Double.toString(minY * 10.0));

            writeDxfLine("9", "$EXTMAX");
            writeDxfLine("10", Double.toString(maxX * 10.0));
            writeDxfLine("20", Double.toString(maxY * 10.0));

            // End section
            writeDxfLine("0", "ENDSEC");


            /* Write Tables section */
            writeDxfLine("0", "SECTION");
            writeDxfLine("2", "TABLES");

            // Write LTYPE table first
            writeDxfLine("0", "TABLE");

            // Some kind of header???
            writeDxfLine("2", "LTYPE");
            writeDxfLine("70", "1");            // A set of flags of some kind (bit code)
            writeDxfLine("0", "LTYPE");

            // Write line type spec
            writeDxfLine("2", "CONTINUOUS");
            writeDxfLine("70", "64");           // Bit code again???
            writeDxfLine("3", "Solid Line");    // Description of line
            writeDxfLine("72", "65");           // Alignment code -- always 65
            writeDxfLine("73", "0");            // Number of line type elements???
            writeDxfLine("40", "0.0");          // Total pattern length (I assume things like dashes etc.)

            writeDxfLine("2", "DASHED");
            writeDxfLine("70", "64");           // Bit code again???
            writeDxfLine("3", "Dashed Line");   // Description of line
            writeDxfLine("72", "65");           // Alignment code -- always 65
            writeDxfLine("73", "1");            // Number of line type elements???
            writeDxfLine("40", "1.0");          // Total pattern length (I assume things like dashes etc.)
            writeDxfLine("49", "1.0");          // Dot, dash or space length

            // End the table
            writeDxfLine("0","ENDTAB");

            // Configure layer
            writeDxfLine("0", "TABLE");

            // Some kind of header???
            writeDxfLine("2", "LAYER");
            writeDxfLine("70", "6");            // Some bit code for the layer???
            writeDxfLine("0", "LAYER");

            // Spec of layer
            writeDxfLine("2", "Block");         // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "7");            // Colour number (uses AutoCAD colour number scale)
            writeDxfLine("6", "CONTINUOUS");    // Linetype name

            // Spec of layer
            writeDxfLine("2", "Extras");        // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "8");            // Colour number
            writeDxfLine("6", "DASHED");        // Linetype name

            // Spec of layer
            writeDxfLine("2", "Keypoints");     // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "9");            // Colour number
            writeDxfLine("6", "DASHED");        // Linetype name

            // Spec of layer
            writeDxfLine("2", "Coordinates");   // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "9");            // Colour number
            writeDxfLine("6", "DASHED");        // Linetype name

            // Spec of layer
            writeDxfLine("2", "Construction Lines");        // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "9");            // Colour number
            writeDxfLine("6", "DASHED");        // Linetype name

            // End table
            writeDxfLine("0", "ENDTAB");

            // Table for style -- not sure why we need this
            writeDxfLine("0", "TABLE");
            writeDxfLine("2", "STYLE");
            writeDxfLine("70", "0");

            // End table
            writeDxfLine("0", "ENDTAB");

            // End section
            writeDxfLine("0", "ENDSEC");


            /* Write Blocks section */
            writeDxfLine("0", "SECTION");
            writeDxfLine("2", "BLOCKS");

            writeDxfLine("0", "BLOCK");         // One and only block
            writeDxfLine("8", "1");             // Layer 1 associated with block?
            writeDxfLine("2", blockName);             // Name of block
            writeDxfLine("10", "0.0");          // Base point (X)
            writeDxfLine("20", "0.0");          // Base point (Y)
            writeDxfLine("70", "0");            // Block flag (see DXF references)

            // Add the pattern outline to the block definition
            if (dxfLayerChooser.length > 1 && dxfLayerChooser[1])
            {
                // Start of polyline
                writeDxfLine("0", "POLYLINE");
                writeDxfLine("8", "1");     // Layer 1 as per the ASTM spec
                writeDxfLine("66", "1");    // Obsolete "entities follow" flag
                writeDxfLine("70", "1");    // Closed polyline flag

                // Add vertices to polyline
                for (int i = 0; i < linesX.size(); i++)
                {
                    writeDxfLine("0", "VERTEX");
                    writeDxfLine("8", "1");  // Write on layer 1
                    writeDxfLine("10", Double.toString(linesX.get(i) * 10.0)); // X coordinate start
                    writeDxfLine("20", Double.toString(linesY.get(i) * 10.0)); // Y coordinate start
                }

                // End the polyline
                writeDxfLine("0", "SEQEND");

                // Adds the keypoints as POINT entities which are mandatory according to ASTM spec
                for (int i = 0; i < linesX.size(); i++)
                {
                    writeDxfLine("0", "POINT");
                    writeDxfLine("8", "2");     // Layer 2 is turn points layer from ASTM spec
                    writeDxfLine("62", "3");    // Colour of points using index colour)
                    writeDxfLine("10", Double.toString(linesX.get(i) * 10.0)); // X coordinate start
                    writeDxfLine("20", Double.toString(linesY.get(i) * 10.0)); // Y coordinate start
                }

            }

            // Piece system text now is added on layer 1 as per ASTM spec
            writeDxfLine("0", "TEXT");
            writeDxfLine("8", "1");
            writeDxfLine("39", "10.0");
            writeDxfLine("10", "0.0");
            writeDxfLine("20", Double.toString(maxY * 10.0 + 10.0));
            writeDxfLine("40", Double.toString(textHeight));
            writeDxfLine("1", "Piece Name: " + blockName);


            // End block and section
            writeDxfLine("0", "ENDBLK");
            writeDxfLine("0", "ENDSEC");


            /* Write Entities section */
            writeDxfLine("0", "SECTION");
            writeDxfLine("2", "ENTITIES");

            // Insert the block defined earlier
            writeDxfLine("0", "INSERT");
            writeDxfLine("8","1");
            writeDxfLine("2", blockName);
            writeDxfLine("10", "0.0");
            writeDxfLine("20", "0.0");

            /* Add specific annotation text on layer 15 as per the ASTM spec */
            writeAnnotationText("ASTM/D13 Proposal 1 VERSION:10");
            writeAnnotationText("AUTHOR:JBLOCKCREATOR_" + JBlockCreator.bundle.getString("maj_ver") + "_"
                                        + JBlockCreator.bundle.getString("min_ver"));
            writeAnnotationText("CREATION DATE:" + new SimpleDateFormat("dd-MM-yyyy").format(dDate));
            writeAnnotationText("CREATION TIME:" + new SimpleDateFormat("hh:mm").format(dDate));
            writeAnnotationText("UNITS:METRIC");
            writeAnnotationText("GRADE RULE TABLE:");
            writeAnnotationText("SAMPLE SIZE:0");

            // Write the rest of the information on custom layers
            if (dxfLayerChooser.length > 4 && dxfLayerChooser[4])
            {
                // Add construction line entities one at a time
                for (int i = 0; i < ConX.size() - 1; i++)
                {
                    writeDxfLine("0", "LINE");
                    writeDxfLine("8", "Construction Lines");        // Layer on which to draw line (layer 1)
                    writeDxfLine("62", "8");            // Colour of line using index colour (255 = black)
                    writeDxfLine("10", Double.toString(ConX.get(i) * 10.0));     // X coordinate start
                    writeDxfLine("20", Double.toString(ConY.get(i) * 10.0));     // Y coordinate start
                    writeDxfLine("11", Double.toString(ConX.get(i + 1) * 10.0)); // X coordinate end
                    writeDxfLine("21", Double.toString(ConY.get(i + 1) * 10.0)); // Y coordinate end
                    i++;
                }

                // Add construction point names one at a time
                for (int i = 0; i < ConX.size() - 1; i++)
                {
                    writeDxfLine("0", "TEXT");
                    writeDxfLine("8", "Construction Lines");        // Layer on which to draw line (layer 4)
                    writeDxfLine("62", "8");                        // Colour of line using index colour
                    writeDxfLine("1", names.get(i / 2));
                    writeDxfLine("40", Double.toString(textHeight));      // Text height (i.e size)
                    writeDxfLine("50", "0");                        // Text rotation angle
                    writeDxfLine("72", "3");
                    writeDxfLine("73", "3");
                    writeDxfLine("10", Double.toString(ConX.get(i) * 10.0)); // X coordinate start
                    writeDxfLine("20", Double.toString(ConY.get(i) * 10.0)); // Y coordinate start
                    writeDxfLine("11", Double.toString(ConX.get(i) * 10.0)); // X coordinate end
                    writeDxfLine("21", Double.toString(ConY.get(i) * 10.0)); // Y coordinate end
                }
            }

            if (dxfLayerChooser.length > 2 && dxfLayerChooser[2])
            {
                // Marks the keypoints used as individual circles on a separate layer
                for (int i = 0; i < linesX.size(); i++)
                {
                    writeDxfLine("0", "CIRCLE");
                    writeDxfLine("8", "Keypoints");     // Layer on which to draw (layer 3)
                    writeDxfLine("40", "2.5");         // Radius
                    writeDxfLine("62", "3");  // Colour of points using index colour)
                    writeDxfLine("10", Double.toString(linesX.get(i) * 10.0)); // X coordinate centre
                    writeDxfLine("20", Double.toString(linesY.get(i) * 10.0)); // Y coordinate centre
                }
            }

            if (dxfLayerChooser.length > 3 && dxfLayerChooser[3])
            {
                // Add point coordinates one at a time
                for (int i = 0; i < linesX.size() - 1; i++)
                {
                    writeDxfLine("0", "TEXT");
                    writeDxfLine("8", "Coordinates");       // Layer on which to draw line (layer 4)
                    writeDxfLine("62", "256");              // Colour of line using index colour
                    writeDxfLine("1", "(" + String.format("%.2f", linesX.get(i) * 10.0) + ", " +
                            String.format("%.2f", linesY.get(i) * 10.0) + ")");
                    writeDxfLine("40", "5.0");              // Text height (i.e size)
                    writeDxfLine("50", "45");               // Text rotation angle
                    writeDxfLine("10", Double.toString(linesX.get(i) * 10.0)); // X coordinate start
                    writeDxfLine("20", Double.toString(linesY.get(i) * 10.0)); // Y coordinate start
                    writeDxfLine("11", Double.toString(linesX.get(i + 1) * 10.0)); // X coordinate end
                    writeDxfLine("21", Double.toString(linesY.get(i + 1) * 10.0)); // Y coordinate end
                }
            }

            if (dxfLayerChooser.length > 0 && dxfLayerChooser[0])
            {
                // Write lines to create a 100 x 100 mm square off to bottom left of pattern
                float[] scaleSqX = {-50.0f, -50.0f, 50.0f, 50.0f};
                float[] scaleSqY = {-50.0f, 50.0f, 50.0f, -50.0f};
                for (int i = 0; i < 4; i++)
                {
                    int j = i + 1;
                    if (j > 3) j = 0;
                    writeDxfLine("0", "LINE");
                    writeDxfLine("8", "Extras");    // Layer on which to draw line (layer 2)
                    writeDxfLine("62", "1");        // Colour of line using index colour (1 = red)
                    writeDxfLine("10", Double.toString(scaleSqX[i])); // X coordinate start
                    writeDxfLine("20", Double.toString(scaleSqY[i])); // Y coordinate start
                    writeDxfLine("11", Double.toString(scaleSqX[j])); // X coordinate end
                    writeDxfLine("21", Double.toString(scaleSqY[j])); // Y coordinate end
                }

                // Add text
                writeDxfLine("0", "TEXT");
                writeDxfLine("8", "Extras");        // Layer
                writeDxfLine("62", "140");          // Colour of line using index colour
                writeDxfLine("39", "10.0");
                writeDxfLine("10", Float.toString(scaleSqX[2] + 10.0f));
                writeDxfLine("20", Float.toString(scaleSqY[0]));
                writeDxfLine("40", Double.toString(textHeight));
                writeDxfLine("1", blockName);
            }

            // End section
            writeDxfLine("0", "ENDSEC");

            // End of file
            writeDxfLine("0", "EOF");

            // Close off file
            bIsOpen = false;
            printer.close();
            try
            {
                file.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to print a line to a file giving it a string format
     * @param str   string to print
     */
    private void writeLine(String str)
    {
        if(bIsOpen)
        {
            printer.printf("%s" + "%n", str);
        }
    }

    /**
     * Wrapper to write a pair of lines in a DXF file
     * @param code      group code
     * @param value     value
     */
    private void writeDxfLine(String code, String value)
    {
        writeLine(code);
        writeLine(value);
    }

    /**
     * Write a line of annotation text and adjust internal parameters ready for next line.
     * @param text  Text line to write
     */
    private void writeAnnotationText(String text)
    {
        writeDxfLine("0", "TEXT");
        writeDxfLine("8", "15");
        writeDxfLine("10", "0.0");
        writeDxfLine("20", Double.toString(currentBaseline));
        writeDxfLine("40", Double.toString(textHeight));
        writeDxfLine("1", text);
        currentBaseline -= baselineSkip;     // Decrement the text vertical position by the baseline skip
    }


}
