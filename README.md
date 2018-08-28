# About #
JBlock 2D is an open-source framework for custom pattern drafting and fit analysis. Framework is based on a high-level `Block` class
which contains geometric elements necessary for recreating pattern drafting techniques digitally. Software also reads
measurements from input files exported from body scanning cameras to facilitate custom fit. It is compatible with the SizeStream scanning system output format at present.    
A selection of existing drafting methods will be shipped with the source code, the framework allows the creation of
custom methods as desired. Software copyright is retained by the The University of Manchester.

# Compiling and Running #
Release builds are available from the master branch at tagged commits. These releases are shipped with *.jar
which may be executed directly to run the application. You may also wish to build from source which you can do using
`javac` from the command line.

## Release Notes ##

**Version: 1.0**

Issue #4: Batch files of scan data can now be used.    
Issue #5: Keypoints have been added as a new layer on the DXF output files.    
Issue #6: Construction lines have been added as a new layer on the DXF output files.    
Issue #8: Darts have been fixed so they work as intended in all cases.    
Issue #9: Single scan data set files now work without manual manipulation.    
Issue #10: GUI added to the software for ease of use.    
Issue #13: Coordinates of keypoints have been added as a new layer on the DXF output files.    
Issue #17: Circular curves work as intended.    
Issue #21: Batch files of any size now work as intended.    
Issue #23: Output DXF files are now organised in a Method/Pattern/ file directory system.    
Issue #30: All custom measurements needed for the Beazley Bond sleeve now available.    
Issue #34: File input and outputs for the GUI fixed so they work as intended.    

Patterns added:    

Beazley Bond Straight Sleeve    
Aldrich Skirt    
Gill Skirt    

**Version: 0.2**
Issue #1: Patterns can now store the name of the input file and use it when writing DXFs.    
Issue #2: Patterns now have a 10cm square drawn on a separate layer to allow scaling.    
Issue #3: Patterns now have their name written on a separate layer.    

**Version: 0.1**
Initial Evaluation Version
