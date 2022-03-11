# About #
JBlockCreator is an open-source framework for custom pattern drafting and fit analysis. Framework is based on a 
high-level `Block` class which contains geometric elements necessary for recreating pattern drafting techniques 
digitally. Software also reads measurements from input files exported from body scanning cameras to facilitate custom 
fit. A selection of existing drafting methods are shipped with the source code, but the framework allows the creation of
custom methods as desired by extending the `Pattern` class and adding the relevant UI code. Software copyright is 
retained by the The University of Manchester.

# Journal Publication #
This software is [published in SoftwareX](https://www.sciencedirect.com/science/article/pii/S2352711018302528). 
If you use or modify our software (which we hope you will), we ask that you cite this article somewhere.

# Compiling and Running #
Release builds are available from the master branch at tagged commits. These releases are shipped with *.jar 
which may be executed directly to run the application. Images are not embedded so the *.jar file must be in the same 
directory as the `images` directory when run. If you wish to build form the latest source code, since v1.4 we now 
provide Ant configuration files and a build script to allow users to build from source. Instructions are available 
on the Wiki.

# Known Issues #
The current release has no known bugs, however testing has identified a number of limitations in the drafting procedures 
which are recorded on the issue tracker where appropriate. Please add more issues to the tracker if you find any problems.

# Release Notes #    
**Version 1.6**    
Issue #7: Fixed armhole issue with new relational rule    
Issue #78: Reduced text size on plots    
Issue #82: Extended Bexier functions to take angle arguments    
Issue #83: Rationalised methods in the block base class    
Issue #89: Fixed missing measurements in sample input file    
Issue #91: Made it possible to see measurements required for patterns    
Issue #92: Added new trouser projection pattern    
Issue #93: Reworked UI to make it more modular    
Issue #97: Added popup to warn of a broken input file    

**Version 1.5**    
Issue #76: Add timestamp to pattern outputs.    
Issue #74: Added Gill sweatshirt pattern.    
Issue #62: Added UI for adding custom ease values for most of the patterns.    
Issue #58/#72: Fixed issues with Gill trouser draft.    
Issue #71: Fixed issues with Aldrich trouser draft.    
Issue #70: Added Ahmed bodice draft.    
Issue #69: Added Gill trouser draft type 2.    
Issue #87: Fixed typographical errors.    

**Version 1.4**    
Addition of the Gill experimental patterns for public consumption.     
New Ant build files added and wiki material to assist with building from source.    

**Version 1.3**    
Issue #59: Additional armhole width rule added to fix an issue with armhole being too wide.    
Issue #63: Issues with the hip to waist curve on the BB trouser pattern have been replaced with a new solution.    
Issue #7: Fixed an issue with the touch point placements on the armhole which affected the reliability of the curves.    

**Version: 1.2**            
Issue #37: Added dynamic preview capability as output options are selected with checkbox. Removed redundant box on analysis pane.    
Issue #57: Reworked output to conform to ASTM standard. Testing shows it can be read into Lectra as an AAMA file so there is still a deficiency in the output and issue remains open.    

Small fixes to Aldrich trouser pattern and Gill skirt pattern.
Proprietary pattern information removed for future releases prior to open source publication.    

**Version: 1.1**    
Issue #41: Added more input files to the input folder for users to test behaviour.    
Issue #39: Menu bar added to UI to provide alternative navigation.    
Issue #35: Added option to choose the information layers written out to the DXF files.    
Issue #46: File chooser has been made easier to use and folders are now remembered.    
Issue #40: Measurements which are marked as "unavailable" by computer-generated data files are now handled correctly.    
Issue #36: A help button has been added to GUI.    
Issue #38: Run button now shows "running" when in progress and dialog box alerts user on completion. Also moved execution to a background thread to stop UI locking.    
Issue #51: Analysis tab added which allows pairs of measurements to be compared in rectangle plots.    
Issue #37: Some visual guide to what the output will look like added to the UI.    

**Version: 1.0**
First release for public use.    
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
Issue #42: Created a user guide for v1.0.

New patterns added include:    
Beazley Bond Straight Sleeve    
Aldrich Skirt    
Gill Skirt    

**Version: 0.2**    
Issue #1: Patterns can now store the name of the input file and use it when writing DXFs.    
Issue #2: Patterns now have a 10cm square drawn on a separate layer to indicate scaling.    
Issue #3: Patterns now have their name written on a separate layer.    

**Version: 0.1**    
Initial Evaluation Version
