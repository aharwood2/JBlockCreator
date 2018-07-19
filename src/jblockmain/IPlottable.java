package jblockmain;

import java.io.File;
import java.util.ArrayList;

/** Interface required by each pattern to ensure it is compatible with the plotting component. */
public interface IPlottable
{
    // Methods to return list of X,Y points for each block the pattern contains
    ArrayList<Double> getXPoints(int blockNumber) throws Exception;
    ArrayList<Double> getYPoints(int blockNumber) throws Exception;
    ArrayList<Double> getXCtPoints(int blockNumber) throws Exception;
    ArrayList<Double> getYCtPoints(int blockNumber) throws Exception;
    int getNumberOfBlocksToPlot();
    void writeToDXF(File fileOutput, boolean[] dxfLayerChooser);
}
