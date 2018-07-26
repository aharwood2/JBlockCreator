package jblockmain;

import java.io.File;
import java.util.ArrayList;

public interface IPlottableAnalysis
{
    // Methods to return list of X,Y points for each block the pattern contains
    ArrayList<Double> getXPoints(int blockNumber) throws Exception;
    ArrayList<Double> getYPoints(int blockNumber) throws Exception;
    ArrayList<Double> getXCtPoints(int blockNumber) throws Exception;
    ArrayList<Double> getYCtPoints(int blockNumber) throws Exception;
    int getNumberOfBlocksToPlot();

    void writeToDXFAnalysis(File fileOutput, boolean[] dxfLayersAnalysis);    // TODO: Need to improve this definition as boolean[] should have a defined length if in an interface. Ideally should be its own class.
}