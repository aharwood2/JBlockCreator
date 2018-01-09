package dxfwriter;

import mathcontainers.Vector2D;

import java.io.IOException;

/**
 * A class that encapsulates a DXF file and the methods required to write it from arrays of points which represent line
 * segments.
 */
public class DxfFile
{
    // Flag indicating whether file is ready
    private boolean bIsOpen;

    // Some means of holding the information...


    /**
     * Constructor to open a DXF file.
     * @param filename  name of the file including path
     */
    public DxfFile(String filename)
    {
        bIsOpen = false;

        // TODO: implement the file creation process
    }

    /**
     * Adds a line to the DXF drawing given the coordinates of the end points.
     * @param startPoint    start 2D coordinate
     * @param endPoint      end 2D coordinate
     */
    public void addLine(Vector2D startPoint, Vector2D endPoint)
    {
        // TODO: implement the addition of a line to the container
    }

    /**
     * Writes the contents of the DXF file.
     */
    public void writeFile() throws IOException
    {
        if (bIsOpen)
        {
            // TODO write file and close it -- set closed flag to prevent rewriting
        }
        else
            throw new IOException("File is not open for writing!");
    }


}
