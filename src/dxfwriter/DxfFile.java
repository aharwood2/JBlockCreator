package dxfwriter;

import mathcontainers.Vector2D;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class that encapsulates a DXF file and the methods required to write it from arrays of points which represent line
 * segments.
 */
public class DxfFile
{
    // Flag indicating whether file is ready
    private boolean bIsOpen;

    // Handle to file and printer
    private FileWriter file;
    private PrintWriter printer;

    // Creation time
    private String cDate;

    // Extremes of the drawing
    double minX;
    double maxX;
    double minY;
    double maxY;


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
        // TODO: import the list using the addLine method
    }

    /**
     * Adds a line to the DXF drawing given the coordinates of the end points.
     * @param startPoint    start 2D coordinate
     * @param endPoint      end 2D coordinate
     */
    public void addLine(Vector2D startPoint, Vector2D endPoint)
    {
        // TODO: implement the addition of a line with auto update of the extreme values
    }

    /**
     * Writes the contents of the DXF file.
     */
    public void writeFile()
    {
        if (bIsOpen)
        {
            // TODO write file based on DXF spec

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
            writeDxfLine("70", "1");            // Code translates as standard bit code???
            writeDxfLine("0", "LTYPE");

            // Write line type spec
            writeDxfLine("2", "CONTINUOUS");
            writeDxfLine("70", "64");           // Bit code again???
            writeDxfLine("3", "Solid Line");    // Description of line
            writeDxfLine("72", "65");           // Alignment code -- always 65
            writeDxfLine("73", "0");            // Number of line type elements???
            writeDxfLine("40", "0.0");          // Total pattern length (I assume things like dashes etc.)

            // End the table
            writeDxfLine("0","ENDTAB");

            // Configure layer
            writeDxfLine("0", "TABLE");

            // Some kind of header???
            writeDxfLine("2", "LAYER");
            writeDxfLine("70", "6");            // Some bit code for the layer???
            writeDxfLine("0", "LAYER");

            // Spec of layer
            writeDxfLine("2", "1");             // Layer name
            writeDxfLine("70", "64");
            writeDxfLine("62", "7");            // Colour number?
            writeDxfLine("6", "CONTINUOUS");    // Linetype name (as specified above?)

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


            // TODO add the line entities









            // Close off file
            bIsOpen = false;
            printer.close();
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
