package dxfwriter;

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
     * Creation time
     */
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
            cDate = formatter.format(new Date());
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
            writeDxfLine("1", "AC1009");

            // Write the insertion base (I assume this is some kind of origin offset for x,y,z equiv 10,20,30)
            writeDxfLine("9", "$INSBASE");
            writeDxfLine("10", "0.0");
            writeDxfLine("20", "0.0");
            writeDxfLine("30", "0.0");

            // Write extremities
            writeDxfLine("9", "$EXTMIN");
            writeDxfLine("10", Double.toString(minX));
            writeDxfLine("20", Double.toString(minY));

            writeDxfLine("9", "$EXTMAX");
            writeDxfLine("10", Double.toString(maxX));
            writeDxfLine("20", Double.toString(maxY));

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

            // Spec of layer 1
            writeDxfLine("2", "Line");          // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "7");            // Colour number?
            writeDxfLine("6", "CONTINUOUS");    // Linetype name (as specified above?)

            // Spec of layer 2
            writeDxfLine("2", "Extras");        // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "8");            // Colour number?
            writeDxfLine("6", "DASHED");        // Linetype name (as specified above?)

            // Spec of layer 3
            writeDxfLine("2", "Keypoints");     // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "9");            // Colour number?
            writeDxfLine("6", "DASHED");        // Linetype name (as specified above?)

            // Spec of layer 4
            writeDxfLine("2", "Coordinates");   // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "9");            // Colour number?
            writeDxfLine("6", "DASHED");        // Linetype name (as specified above?)

            // Spec of layer 5
            writeDxfLine("2", "Construction Lines");        // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "9");            // Colour number?
            writeDxfLine("6", "DASHED");        // Linetype name (as specified above?)

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
            writeDxfLine("0", "ENDSEC");


            /* Write Entities section */
            writeDxfLine("0", "SECTION");
            writeDxfLine("2", "ENTITIES");

            if (dxfLayerChooser[1] == true)
            {
                // Start of polyline
                writeDxfLine("0","POLYLINE");
                writeDxfLine("8", "1");     // Layer on which to draw line (layer 1)
                writeDxfLine("66","1");     // Closed polyline flag

                // Add vertices to polyline
                for (int i = 0; i < linesX.size(); i++)
                {
                    writeDxfLine("0", "VERTEX");
                    writeDxfLine("8","1");  // Write on layer 1
                    writeDxfLine("10", Double.toString(linesX.get(i))); // X coordinate start
                    writeDxfLine("20", Double.toString(linesY.get(i))); // Y coordinate start
                }

                // End the polyline
                writeDxfLine("0","SEQEND");
            }

            if (dxfLayerChooser[4] == true)
            {
                // Add construction line entities one at a time
                for (int i = 0; i < ConX.size() - 1; i++)
                {
                    writeDxfLine("0", "LINE");
                    writeDxfLine("8", "Construction Lines");     // Layer on which to draw line (layer 1)
                    writeDxfLine("62", "8");  // Colour of line using index colour (255 = black)
                    writeDxfLine("10", Double.toString(ConX.get(i))); // X coordinate start
                    writeDxfLine("20", Double.toString(ConY.get(i))); // Y coordinate start
                    writeDxfLine("11", Double.toString(ConX.get(i + 1))); // X coordinate end
                    writeDxfLine("21", Double.toString(ConY.get(i + 1))); // Y coordinate end
                    i++;
                }

                // Add construction point names one at a time
                for (int i = 0; i < ConX.size() - 1; i++)
                {
                    writeDxfLine("0", "TEXT");
                    writeDxfLine("8", "Construction Lines");     // Layer on which to draw line (layer 4)
                    writeDxfLine("62", "8");  // Colour of line using index colour
                    writeDxfLine("1", names.get(i / 2));
                    writeDxfLine("40", "0.75"); // Text height (i.e size)
                    writeDxfLine("50", "0"); // Text rotation angle
                    writeDxfLine("72", "3");
                    writeDxfLine("73", "3");
                    writeDxfLine("10", Double.toString(ConX.get(i))); // X coordinate start
                    writeDxfLine("20", Double.toString(ConY.get(i))); // Y coordinate start
                    writeDxfLine("11", Double.toString(ConX.get(i))); // X coordinate end
                    writeDxfLine("21", Double.toString(ConY.get(i))); // Y coordinate end
                }
            }

            if (dxfLayerChooser[2] == true)
            {
                // Marks the keypoints used as individual circles on a separate layer
                for (int i = 0; i < linesX.size() - 1; i++)
                {
                    writeDxfLine("0", "CIRCLE");
                    writeDxfLine("8", "Keypoints");     // Layer on which to draw (layer 3)
                    writeDxfLine("40", "0.25");
                    writeDxfLine("62", "3");  // Colour of points using index colour)
                    writeDxfLine("10", Double.toString(linesX.get(i))); // X coordinate start
                    writeDxfLine("20", Double.toString(linesY.get(i))); // Y coordinate start
                    writeDxfLine("11", Double.toString(linesX.get(i + 1))); // X coordinate end
                    writeDxfLine("21", Double.toString(linesY.get(i + 1))); // Y coordinate end
                }
            }

            if (dxfLayerChooser[3] == true)
            {
                // Add point coordinates one at a time
                for (int i = 0; i < linesX.size() - 1; i++)
                {
                    writeDxfLine("0", "TEXT");
                    writeDxfLine("8", "Coordinates");     // Layer on which to draw line (layer 4)
                    writeDxfLine("62", "256");  // Colour of line using index colour
                    writeDxfLine("1", "(" + String.format("%.2f", linesX.get(i)) + ", " + String.format("%.2f", linesY.get(i)) + ")");
                    writeDxfLine("40", "0.15"); // Text height (i.e size)
                    writeDxfLine("50", "45"); // Text rotation angle
                    writeDxfLine("10", Double.toString(linesX.get(i))); // X coordinate start
                    writeDxfLine("20", Double.toString(linesY.get(i))); // Y coordinate start
                    writeDxfLine("11", Double.toString(linesX.get(i + 1))); // X coordinate end
                    writeDxfLine("21", Double.toString(linesY.get(i + 1))); // Y coordinate end
                }
            }

            if (dxfLayerChooser[0] == true)
            {
                // Write lines to create a 10 x 10 cm square off to bottom left of pattern
                float[] scaleSqX = {-5.0f, -5.0f, 5.0f, 5.0f};
                float[] scaleSqY = {-5.0f, 5.0f, 5.0f, -5.0f};
                for (int i = 0; i < 4; i++)
                {
                    int j = i + 1;
                    if (j > 3) j = 0;
                    writeDxfLine("0", "LINE");
                    writeDxfLine("8", "Extras");     // Layer on which to draw line (layer 2)
                    writeDxfLine("62", "1");  // Colour of line using index colour (1 = red)
                    writeDxfLine("10", Double.toString(scaleSqX[i])); // X coordinate start
                    writeDxfLine("20", Double.toString(scaleSqY[i])); // Y coordinate start
                    writeDxfLine("11", Double.toString(scaleSqX[j])); // X coordinate end
                    writeDxfLine("21", Double.toString(scaleSqY[j])); // Y coordinate end
                }

                // Add text
                writeDxfLine("0", "TEXT");
                writeDxfLine("8", "Extras");     // Layer
                writeDxfLine("62", "140");  // Colour of line using index colour (100 = ?)
                writeDxfLine("39", "1.0");
                writeDxfLine("10", Float.toString(scaleSqX[2] + 1.0f));
                writeDxfLine("20", Float.toString(scaleSqY[0]));
                writeDxfLine("40", Float.toString((scaleSqY[1] - scaleSqY[0]) * 0.1f));
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


}
