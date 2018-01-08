package jblockmain;

import java.util.ArrayList;

/** Interface to be implemented by every pattern added to the module. */
public abstract class Pattern
{
    // Blocks that comprise the pattern
    protected ArrayList<Block> blocks;

    // Obtain measurements from the body scan required by the pattern
    protected abstract void readMeasurements(Measurements dataStore);

    // Modify any measurements read from the scan by adding ease
    protected abstract void addEasement();

    // Start the creation of the blocks for the pattern
    protected abstract void createBlocks();

}
