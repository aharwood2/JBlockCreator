package jblockmain;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

import analysis.RectanglePlot;
import beazleybond.BodicePattern;
import beazleybond.SkirtPattern;
import beazleybond.StraightSleevePattern;
import beazleybond.TrouserPattern;

import jblockenums.EMsgType;

/**
 * Class bound the GUI form
 */
public class JBlockCreator
        extends JFrame
{

    /**
     * Declaration of backend components.
     */

    // Panels
    private JPanel panelMain;
    private JPanel panelPatterns;
    private JPanel panelCommonHeader;
    private JPanel panelPatternOutputOptions;
    private JPanel panelPlotOutputOptions;
    private JPanel panelAnalysis;

    // Labels
    private JLabel labAldrich;
    private JLabel labBeazleyBond;
    private JLabel labGill;
    private JLabel labOpenPath;
    private JLabel labSavePath;
    private JLabel labXID;
    private JLabel labYID;

    // Check boxes
    private JCheckBox checkAldrichSkirt;
    private JCheckBox checkAldrichTrousers;
    private JCheckBox checkBeazleySkirt;
    private JCheckBox checkGillSkirt;
    private JCheckBox checkGillTrouserOne;
    private JCheckBox checkBeazleyStraightSleeve;
    private JCheckBox checkBeazleyTrousers;
    private JCheckBox checkBeazleyBodice;
    private JCheckBox checkScaleBoxAndUser;
    private JCheckBox checkPatternOutline;
    private JCheckBox checkKeypointsAsCircles;
    private JCheckBox checkKeypointCoordinates;
    private JCheckBox checkConstructionLines;
    private JCheckBox checkRectanglePlot;
    private JCheckBox checkLayeredRectPlot;
    private JCheckBox checkScaleBoxAndUserAnalysis;
    private JCheckBox checkConnectingLinesAnalysis;
    private JCheckBox checkKeypointsAsCirclesAnalysis;
    private JCheckBox checkKeypointCoordinatesAnalysis;
    private JCheckBox checkGillSweatshirt;

    // Buttons
    private JButton butRun;
    private JButton butSave;
    private JButton butLoad;

    // Tabbed panes
    private JTabbedPane tabbedPane;

    // Text Input Fields
    private JTextField textFieldPlotXID;
    private JTextField textFieldPlotYID;

    // Images
    private JLabel imageUomLogo;
    private JPanel panelPatternsWrapper;
    private JPanel imagePatternWrapper;
    private JPanel panelAnalysisWrapper;
    private JPanel imageAnalysisWrapper;
    private JLayeredPane stackedPatternSample;
    private JLayeredPane stackedAnalysisSample;
    private JCheckBox timeStampCheckBox;

    // Ease Buttons
    private JButton aldrichSkirtEaseButton;
    private JButton aldrichTrouserEaseButton;
    private JButton gillSweatShirtEaseButton;
    private JButton beazleyBondSkirtEaseButton;
    private JButton beazleyBondTrouserEaseButton;
    private JButton beazleyBondBodiceEaseButton;
    private JButton beazleyBondStraightSleeveEaseButton;
    private JButton gillSkirtEaseButton;
    private JButton gillTrouserOneEaseButton;
    private JLabel labAhmed;
    private JCheckBox checkAhmedBodice;
    private JButton ahmedBodiceEaseButton;
    private JCheckBox checkGillTrouserTwo;
    private JButton gillTrouserTwoEaseButton;

    // Layers
    private ArrayList<Component> paneLayers;

    // Internal fields
    private File fileOutput = null;
    private File fileInput = null;
    private boolean[] dxfLayerChoices = new boolean[5];
    private boolean[] dxfLayersAnalysis = new boolean[4];
    private boolean isLayeredRectPlot;
    private boolean isRectanglePlot;
    private boolean isRunning = false;
    public static ResourceBundle bundle = ResourceBundle.getBundle("strings");

    /**
     * Name of failed output file
     */
    static final String failedOutputsFilename = bundle.getString("failed_out_file");

    /**
     * Limit of characters to display in file paths
     */
    private final int charDisplayLimit = 100;

    /**
     * Global tolerance for some numerical operations
     */
    public static final double tol = 10e-8;

    /**
     * Entry point
     *
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        // Create a JFrame instance
        JFrame frame = new JFrame(bundle.getString("app_name") + " - v"
                                          + bundle.getString("maj_ver") + "."
                                          + bundle.getString("min_ver"));
        final JBlockCreator block = new JBlockCreator();
        frame.setContentPane(block.panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        // Centre on screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
                          dim.height / 2 - frame.getSize().height / 2);

        /* MENU BAR SETUP */

        // Create a window for the menu
        JFrame frame1 = new JFrame("Menu");
        JPanel panelpattern = new JPanel();
        frame1.getContentPane().add(panelpattern, "Center");

        // Create an action listener for the menu items we will create
        ActionListener listener = e ->
        {
            // If statements for if the toolbar menu items are clicked
            JMenuItem item = (JMenuItem) e.getSource();
            String cmd = item.getActionCommand();
            if (cmd.equals("Exit"))
            {
                System.exit(0);
            }
            if (cmd.equals("View help"))
            {
                String url = bundle.getString("guide_url");
                Prompts.infoBox(bundle.getString("help_text") + "See: " + url, "Help", EMsgType.Info);
            }
            if (cmd.equals("Open"))
            {
                block.openClickedEvent();
            }
            if (cmd.equals("Save"))
            {
                block.saveClickedEvent();
            }
            if (cmd.equals("Run"))
            {
                block.runClickedEvent();
            }
        };

        // Create some menu panes, and fill them with menu items
        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        file.add(menuItem("Run", listener, "Run", 'R', KeyEvent.VK_R));
        file.addSeparator();
        file.add(menuItem("Exit", listener, "Exit", 'E', KeyEvent.VK_E));

        JMenu help = new JMenu("Help");
        help.setMnemonic('H');
        help.add(menuItem("View Help", listener, "View help", 'H', KeyEvent.VK_H));

        // Create a menubar and add menus
        JMenuBar menubar = new JMenuBar();
        menubar.add(file);
        menubar.add(help);

        // Add menubar to the main window
        frame.setJMenuBar(menubar);

        // Finally, make our main window
        frame.pack();
        block.setComponentsInvisible(block.panelPatterns, new JButton());
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * To deal with resizing issues whereby components which cause jframe to change size when set to visible cause bits to push out
     * @param whichPanel the Panel in which everything underneath it will be searched for the particular component but not including the panel
     * @param type the type of component you want to set invisibile, i.e JButton will set all buttons to invisible
     */
    private void setComponentsInvisible(Container whichPanel, Component type)
    {
        // Go through all the components in the container
        for (int i = 0; i < whichPanel.getComponentCount(); i++)
        {
            // If the type of component is either a JPanel or JTabbedPane, go through that panel (recursively)
            if (whichPanel.getComponent(i) instanceof JPanel || whichPanel.getComponent(i) instanceof JTabbedPane)
            {
                setComponentsInvisible((JPanel)whichPanel.getComponent(i), type);
            }
            // If the type of component in the containing panel is of type type, set that component invisible
            if (whichPanel.getComponent(i).getClass() == type.getClass())
            {
                ((whichPanel).getComponent(i)).setVisible(false);
            }
        }
    }

    /**
     * Background thread to perform operations when run button is pressed.
     */
    private class RunThread
            extends Thread
    {
        @Override
        public void run()
        {
            String timeStamp = null;
            if (timeStampCheckBox.isSelected())
            {timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());}
            try
            {
                // Handle missing options
                if (fileInput == null)
                {
                    Prompts.infoBox("Please choose your input file before running the software.",
                                    "Input File Needed",
                                    EMsgType.Error);
                    throw new Exception("Tried to run without selecting input file.");
                }
                if (fileOutput == null)
                {
                    Prompts.infoBox("Please choose a directory in which to write the output files.",
                                    "Output Directory Needed",
                                    EMsgType.Error);
                    throw new Exception("Tried to run without selecting output folder.");
                }

                if (fileOutput != null && fileInput != null)
                {
                    // Update run button text to running
                    setRunButtonText("Running...");

                    // Create a new measurements instance from the input file selected
                    Measurements measurements = new Measurements(JBlockCreator.this.fileInput.toString());

                    // Populate the boolean arrays from the chosen output options
                    getLayerInformationPatterns();
                    getLayerInformationAnalysis();

                    // Create the plot if necessary
                    RectanglePlot plot = null;
                    if (checkRectanglePlot.isSelected() || checkLayeredRectPlot.isSelected())
                        plot = new RectanglePlot(measurements,
                                                 labXID.getText(),
                                                 labYID.getText(),
                                                 isLayeredRectPlot, isRectanglePlot);

                    // Create patterns
                    for (int i = 0; i < measurements.getNames().size(); i++)
                    {
                        measurements.setCurrentUser(i);

                        // Creates patterns depending on which checkboxes are ticked
                        if (checkBeazleySkirt.isSelected())
                        {
                            SkirtPattern bb_skirt = new SkirtPattern(measurements);
                            bb_skirt.writeToDXF(fileOutput, dxfLayerChoices,timeStamp);
                        }

                        if (checkBeazleyTrousers.isSelected())
                        {
                            TrouserPattern bb_trouser = new TrouserPattern(measurements);
                            bb_trouser.writeToDXF(fileOutput, dxfLayerChoices,timeStamp);
                        }

                        if (checkBeazleyBodice.isSelected())
                        {
                            BodicePattern bb_bodice = new BodicePattern(measurements);
                            bb_bodice.writeToDXF(fileOutput, dxfLayerChoices,timeStamp);
                        }

                        if (checkBeazleyStraightSleeve.isSelected())
                        {
                            StraightSleevePattern bb_sleeve = new StraightSleevePattern(measurements);
                            bb_sleeve.writeToDXF(fileOutput, dxfLayerChoices,timeStamp);
                        }

                        if (checkGillSkirt.isSelected())
                        {
                            gill.SkirtPattern gill_skirt = new gill.SkirtPattern(measurements);
                            gill_skirt.writeToDXF(fileOutput, dxfLayerChoices,timeStamp);
                        }

                        if (checkGillTrouserOne.isSelected())
                        {
                            gill.TrouserPattern gill_trousers = new gill.TrouserPattern(measurements);
                            gill_trousers.writeToDXF(fileOutput, dxfLayerChoices,timeStamp);
                        }

                        if (checkGillTrouserTwo.isSelected())
                        {
                            gill.TrouserPatternTwo gill_trousersTwo = new gill.TrouserPatternTwo(measurements);
                            gill_trousersTwo.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkAldrichSkirt.isSelected())
                        {
                            aldrich.SkirtPattern aldrich_skirt = new aldrich.SkirtPattern(measurements);
                            aldrich_skirt.writeToDXF(fileOutput, dxfLayerChoices,timeStamp);
                        }

                        if (checkAldrichTrousers.isSelected())
                        {
                            aldrich.TrouserPattern aldrich_trousers = new aldrich.TrouserPattern(measurements);
                            aldrich_trousers.writeToDXF(fileOutput, dxfLayerChoices,timeStamp);
                        }

                        if (checkGillSweatshirt.isSelected())
                        {
                            gill.SweatShirtPattern gill_sweatshirt = new gill.SweatShirtPattern(measurements);
                            gill_sweatshirt.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkAhmedBodice.isSelected())
                        {
                            ahmed.BodicePattern ahmed_bodice = new ahmed.BodicePattern(measurements);
                            ahmed_bodice.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        // Creates analysis outputs depending on which checkboxes are ticked
                        if (plot != null) plot.addNewRectangle();
                    }

                    // Write the plot if we created one
                    if (plot != null)
                    {
                        plot.writeToDXF(fileOutput, dxfLayersAnalysis, timeStamp);
                    }

                    // Write out to a text file the patterns that could not be made
                    Pattern.printMissingMeasurements(fileOutput);

                    // Prompt for finishing, two options depending on if some patterns could not be made
                    if (Files.exists(Paths.get(fileOutput + "/" + failedOutputsFilename)))
                    {
                        // Create done prompt
                        Prompts.infoBox(
                                bundle.getString("failed_output_msg"),
                                "Done", EMsgType.Warning);
                    }
                    else
                    {
                        // Create done prompt without error indication
                        Prompts.infoBox("Done!", "Done", EMsgType.Info);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                setRunButtonText("Run");
                isRunning = false;
            }
        }
    }

    /**
     * Event method for updating dynamic text on UI when X ID changed
     */
    private void onXIdEntered()
    {
        onIdEntered(textFieldPlotXID, labXID);
    }

    /**
     * Event method for updating dynamic text on UI when Y ID changed
     */
    private void onYIdEntered()
    {
        onIdEntered(textFieldPlotYID, labYID);
    }

    /**
     * Event method for updating dynamic text on UI when X or Y ID changed
     */
    private void onIdEntered(JTextField textBox, JLabel outLabel)
    {
        String id = textBox.getText();
        try
        {
            if (id.length() == 0)
            {
                outLabel.setText("None");
                return;
            }
            if (Measurements.checkIdFormat(id)) outLabel.setText(id);
            else throw new Exception();
        }
        catch (Exception e)
        {
            textBox.setText("");
            Prompts.infoBox(bundle.getString("format_msg"), "Invalid ID", EMsgType.Error);
        }
    }

    /**
     * Method run when the save button is clicked
     */
    private void saveClickedEvent()
    {
        // Choose a folder location to save the output files
        // Opens a file explorer for users to choose directory
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(fileOutput);
        fileChooser.setDialogTitle("Select Save Location");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            // Update save path
            String filepath = fileChooser.getSelectedFile().toString();
            if (filepath.length() > charDisplayLimit)
            {
                filepath = filepath.substring(0, charDisplayLimit) + "...";
            }
            labSavePath.setText(filepath);
            JBlockCreator.this.fileOutput = fileChooser.getSelectedFile();
        }
    }

    /**
     * Method run when the open button is clicked
     */
    private void openClickedEvent()
    {
        // Choose a folder input
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(fileInput);
        fileChooser.setDialogTitle("Select Input Measurements File");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            // Store input file name and path
            JBlockCreator.this.fileInput = fileChooser.getSelectedFile();
            String filename = fileChooser.getSelectedFile().toString();
            if (filename.length() > charDisplayLimit)
            {
                filename = filename.substring(0, charDisplayLimit) + "...";
            }
            labOpenPath.setText(filename);
        }
    }

    /**
     * Method to update text on run buttons
     *
     * @param text Text to set on the button
     */
    private void setRunButtonText(String text)
    {
        butRun.setText(text);
    }

    /**
     * Method run when the run button clicked.
     */
    private void runClickedEvent()
    {
        if (!isRunning)
        {
            isRunning = true;
            new RunThread().start();
        }
    }

    /**
     * Method run when an ease button is clicked.
     * @param easeMeasurements The ease measurements to display
     */
    private void easeButtonClickedEvent(ArrayList<easeMeasurement> easeMeasurements)
    {
        if (isRunning) {return;}
        try {
            isRunning = true;
            createEaseForm(easeMeasurements);
        } catch (Exception j)
        {
            isRunning = false;
            j.printStackTrace();
            Prompts.infoBox("No Associated Ease", "Ease", EMsgType.Error);
        }
    }

    /**
     * Method to set ifLayeredRectanglePlot boolean
     */
    private void layeredRectanglePlot()
    {
        if (checkLayeredRectPlot.isSelected())
        {
            isLayeredRectPlot = true;
        }
        else if (!checkLayeredRectPlot.isSelected())
        {
            isLayeredRectPlot = false;
        }
    }

    /**
     * Method to set isRectanglePlot boolean
     */
    private void rectanglePlot()
    {
        if (checkRectanglePlot.isSelected())
        {
            isRectanglePlot = true;
        }
        else if (!checkRectanglePlot.isSelected())
        {
            isRectanglePlot = false;
        }
    }

    /**
     * Method to populate the pattern boolean array of DXF layer configuration
     */
    private void getLayerInformationPatterns()
    {
        dxfLayerChoices[0] = checkScaleBoxAndUser.isSelected();
        dxfLayerChoices[1] = checkPatternOutline.isSelected();
        dxfLayerChoices[2] = checkKeypointsAsCircles.isSelected();
        dxfLayerChoices[3] = checkKeypointCoordinates.isSelected();
        dxfLayerChoices[4] = checkConstructionLines.isSelected();
    }

    /**
     * Method to populate the analysis boolean array of DXF layer configuration
     */
    private void getLayerInformationAnalysis()
    {
        dxfLayersAnalysis[0] = checkScaleBoxAndUserAnalysis.isSelected();
        dxfLayersAnalysis[1] = checkConnectingLinesAnalysis.isSelected();
        dxfLayersAnalysis[2] = checkKeypointsAsCirclesAnalysis.isSelected();
        dxfLayersAnalysis[3] = checkKeypointCoordinatesAnalysis.isSelected();
    }

    /**
     * Private constructor for the form-bound class
     */
    private JBlockCreator()
    {
        /* Add listeners */

        // Listener for the Run button
        butRun.addActionListener(e -> runClickedEvent());

        // Attach listener to open button
        butLoad.addActionListener(e -> openClickedEvent());

        // Attach listener to save button
        butSave.addActionListener(e -> saveClickedEvent());

        // Attach listener to rectangle plot x-axis text field
        textFieldPlotXID.addActionListener(e -> onXIdEntered());

        // Attach listener to rectangle plot y-axis
        textFieldPlotYID.addActionListener(e -> onYIdEntered());

        // Attach listener to rectangle plot layered checkbox
        checkLayeredRectPlot.addActionListener(e -> layeredRectanglePlot());

        // Attach listener to rectangle plot (plain) checkbox
        checkRectanglePlot.addActionListener(e -> rectanglePlot());

        // Attached listener to the ID text boxes
        textFieldPlotXID.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                onXIdEntered();
            }
        });

        textFieldPlotYID.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                onYIdEntered();
            }
        });

        // Attach listener to DXF layer checkboxes //

        // Pattern pane
        checkScaleBoxAndUser.addActionListener(e ->
                                               {
                                                   if (!checkScaleBoxAndUser.isSelected())
                                                       stackedPatternSample.setLayer(paneLayers.get(5), 0);
                                                   else stackedPatternSample.setLayer(paneLayers.get(5), 1);
                                               });
        checkPatternOutline.addActionListener(e ->
                                              {
                                                  if (!checkPatternOutline.isSelected())
                                                      stackedPatternSample.setLayer(paneLayers.get(4), 0);
                                                  else stackedPatternSample.setLayer(paneLayers.get(4), 1);
                                              });
        checkKeypointsAsCircles.addActionListener(e ->
                                                  {
                                                      if (!checkKeypointsAsCircles.isSelected())
                                                          stackedPatternSample.setLayer(paneLayers.get(3), 0);
                                                      else stackedPatternSample.setLayer(paneLayers.get(3), 1);
                                                  });
        checkKeypointCoordinates.addActionListener(e ->
                                                   {
                                                       if (!checkKeypointCoordinates.isSelected())
                                                           stackedPatternSample.setLayer(paneLayers.get(2), 0);
                                                       else stackedPatternSample.setLayer(paneLayers.get(2), 1);
                                                   });
        checkConstructionLines.addActionListener(e ->
                                                 {
                                                     if (!checkConstructionLines.isSelected())
                                                         stackedPatternSample.setLayer(paneLayers.get(1), 0);
                                                     else stackedPatternSample.setLayer(paneLayers.get(1), 1);
                                                 });

        // Analysis pane
        checkScaleBoxAndUserAnalysis.addActionListener(e ->
                                                       {
                                                           if (!checkScaleBoxAndUserAnalysis.isSelected())
                                                               stackedAnalysisSample.setLayer(paneLayers.get(10), 0);
                                                           else stackedAnalysisSample.setLayer(paneLayers.get(10), 1);
                                                       });
        checkConnectingLinesAnalysis.addActionListener(e ->
                                                       {
                                                           if (!checkConnectingLinesAnalysis.isSelected())
                                                               stackedAnalysisSample.setLayer(paneLayers.get(9), 0);
                                                           else stackedAnalysisSample.setLayer(paneLayers.get(9), 1);
                                                       });
        checkKeypointsAsCirclesAnalysis.addActionListener(e ->
                                                          {
                                                              if (!checkKeypointsAsCirclesAnalysis.isSelected())
                                                                  stackedAnalysisSample.setLayer(paneLayers.get(8), 0);
                                                              else stackedAnalysisSample.setLayer(paneLayers.get(8), 1);
                                                          });
        checkKeypointCoordinatesAnalysis.addActionListener(e ->
                                                           {
                                                               if (!checkKeypointCoordinatesAnalysis.isSelected())
                                                                   stackedAnalysisSample.setLayer(paneLayers.get(7), 0);
                                                               else
                                                                   stackedAnalysisSample.setLayer(paneLayers.get(7), 1);
                                                           });
        checkAldrichSkirt.addActionListener(e ->
        {
            aldrich.SkirtPattern.populateEaseMeasurements();
            aldrichSkirtEaseButton.setVisible(!aldrichSkirtEaseButton.isVisible());
        });

        checkAldrichTrousers.addActionListener(e -> {
            aldrich.TrouserPattern.populateEaseMeasurements();
            aldrichTrouserEaseButton.setVisible(!aldrichTrouserEaseButton.isVisible());
        });

        checkBeazleySkirt.addActionListener(e -> {
            beazleybond.SkirtPattern.populateEaseMeasurements();
            beazleyBondSkirtEaseButton.setVisible(!beazleyBondSkirtEaseButton.isVisible());
        });

        checkBeazleyTrousers.addActionListener(e -> {
            beazleybond.TrouserPattern.populateEaseMeasurements();
            beazleyBondTrouserEaseButton.setVisible(!beazleyBondTrouserEaseButton.isVisible());
        });

        checkBeazleyBodice.addActionListener(e -> {
            beazleybond.BodicePattern.populateEaseMeasurements();
            beazleyBondBodiceEaseButton.setVisible(!beazleyBondBodiceEaseButton.isVisible());
        });

        checkBeazleyStraightSleeve.addActionListener(e -> {
            beazleybond.StraightSleevePattern.populateEaseMeasurements();
            beazleyBondStraightSleeveEaseButton.setVisible(!beazleyBondStraightSleeveEaseButton.isVisible());
        });

        checkGillSkirt.addActionListener(e -> {
            gill.SkirtPattern.populateEaseMeasurements();
            gillSkirtEaseButton.setVisible(!gillSkirtEaseButton.isVisible());
        });

        checkGillTrouserOne.addActionListener(e -> {
            gill.TrouserPattern.populateEaseMeasurements();
            gillTrouserOneEaseButton.setVisible(!gillTrouserOneEaseButton.isVisible());
        });

        checkGillTrouserTwo.addActionListener(e -> {
            gill.TrouserPatternTwo.populateEaseMeasurements();
            gillTrouserTwoEaseButton.setVisible(!gillTrouserTwoEaseButton.isVisible());
        });

        checkGillSweatshirt.addActionListener(e -> {
            gill.SweatShirtPattern.populateEaseMeasurements();
            gillSweatShirtEaseButton.setVisible(!gillSweatShirtEaseButton.isVisible());
        });

        checkAhmedBodice.addActionListener(e -> {
            ahmed.BodicePattern.populateEaseMeasurements();
            ahmedBodiceEaseButton.setVisible(!ahmedBodiceEaseButton.isVisible());
        });

        // Aldrich Ease Button Listeners
        aldrichSkirtEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(aldrich.SkirtPattern.getEaseMeasurement());
        });

        aldrichTrouserEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(aldrich.TrouserPattern.getEaseMeasurement());
        });

        // Beazley bond Ease button listeners

        beazleyBondSkirtEaseButton.addActionListener(e ->
        {
                easeButtonClickedEvent(beazleybond.SkirtPattern.getEaseMeasurement());
        });

        beazleyBondTrouserEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(beazleybond.TrouserPattern.getEaseMeasurement());
        });

        beazleyBondBodiceEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(beazleybond.BodicePattern.getEaseMeasurement());
        });

        beazleyBondStraightSleeveEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(beazleybond.StraightSleevePattern.getEaseMeasurement());
        });

        // Gill Ease Buttons
        gillSkirtEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(gill.SkirtPattern.getEaseMeasurement());
        });

        gillTrouserOneEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(gill.TrouserPattern.getEaseMeasurement());
        });

        gillTrouserTwoEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(gill.TrouserPatternTwo.getEaseMeasurement());
        });

        gillSweatShirtEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(gill.SweatShirtPattern.getEaseMeasurement());
        });

        ahmedBodiceEaseButton.addActionListener(e ->
        {
            easeButtonClickedEvent(ahmed.BodicePattern.getEaseMeasurement());
        });

    }

    /**
     * A utility method for creating menu items
     *
     * @param label          menu label
     * @param listener       click listener
     * @param command        name of command
     * @param mnemonic       underlined letter
     * @param acceleratorKey key binding
     * @return menu item
     */
    private static JMenuItem menuItem(String label,
                                      ActionListener listener, String command,
                                      int mnemonic, int acceleratorKey)
    {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(listener);
        item.setActionCommand(command);
        if (mnemonic != 0) item.setMnemonic((char) mnemonic);
        if (acceleratorKey != 0)
            item.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey,
                                                       InputEvent.CTRL_MASK));
        return item;
    }

    /**
     * A utility method for creating radio button menu items
     *
     * @param label      button label
     * @param listener   click listener
     * @param command    name of command
     * @param mutExGroup group tag
     * @return radio button
     */
    private static JMenuItem radioItem(String label, ActionListener listener,
                                       String command, ButtonGroup mutExGroup)
    {
        JMenuItem item = new JRadioButtonMenuItem(label);
        item.addActionListener(listener);
        item.setActionCommand(command);
        mutExGroup.add(item);
        return item;
    }

    /**
     * Method to create an initialise a layer in the layered pane
     *
     * @param filename name of image file to load
     * @param size     size of the layer
     * @return image as a JLabel
     */
    private JLabel createLayer(String filename, Dimension size)
    {
        // Get scaled image icon
        ImageIcon imageIcon = new ImageIcon(filename);
        Image origImg = imageIcon.getImage();
        Image newImg = origImg.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);

        // Create the JLabel as a layer
        JLabel layer = new JLabel(new ImageIcon(newImg));

        // No layout manager so initialise this stuff myself
        layer.setVerticalAlignment(JLabel.TOP);
        layer.setHorizontalAlignment(JLabel.CENTER);
        layer.setOpaque(true);
        layer.setForeground(Color.black);
        layer.setBorder(BorderFactory.createLineBorder(Color.black));
        layer.setPreferredSize(size);
        layer.setBounds(0, 0, size.width, size.height);
        layer.setBackground(new Color(0, 0, 0, 0));  // Important for transparency to work!

        // Add to layer list
        if (paneLayers == null) paneLayers = new ArrayList<>();
        paneLayers.add(layer);

        return layer;
    }

    /**
     * Method to add assets to image components of UI
     */
    private void createUIComponents()
    {
        // Setup the layered images and stash

        // Pattern pane
        stackedPatternSample = new JLayeredPane();
        stackedPatternSample.setPreferredSize(new Dimension(435, 435));
        stackedPatternSample.add(
                createLayer("./images/PatternSample_00000.png", stackedPatternSample.getPreferredSize()), 0);
        stackedPatternSample.add(
                createLayer("./images/PatternSample_00001.png", stackedPatternSample.getPreferredSize()), 0);
        stackedPatternSample.add(
                createLayer("./images/PatternSample_00010.png", stackedPatternSample.getPreferredSize()), 0);
        stackedPatternSample.add(
                createLayer("./images/PatternSample_00100.png", stackedPatternSample.getPreferredSize()), 0);
        stackedPatternSample.add(
                createLayer("./images/PatternSample_01000.png", stackedPatternSample.getPreferredSize()), 0);
        stackedPatternSample.add(
                createLayer("./images/PatternSample_10000.png", stackedPatternSample.getPreferredSize()), 0);

        // Analysis pane
        stackedAnalysisSample = new JLayeredPane();
        stackedAnalysisSample.setPreferredSize(stackedPatternSample.getPreferredSize());
        stackedAnalysisSample.add(
                createLayer("./images/AnalysisSample_0000.png", stackedPatternSample.getPreferredSize()), 0);
        stackedAnalysisSample.add(
                createLayer("./images/AnalysisSample_0001.png", stackedPatternSample.getPreferredSize()), 0);
        stackedAnalysisSample.add(
                createLayer("./images/AnalysisSample_0010.png", stackedPatternSample.getPreferredSize()), 0);
        stackedAnalysisSample.add(
                createLayer("./images/AnalysisSample_0100.png", stackedPatternSample.getPreferredSize()), 0);
        stackedAnalysisSample.add(
                createLayer("./images/AnalysisSample_1000.png", stackedPatternSample.getPreferredSize()), 0);

        // Create logo
        imageUomLogo = new JLabel(new ImageIcon("./images/logo_small.jpg"));
    }

    protected void createEaseForm(ArrayList<easeMeasurement> easeMeasurements)
    {
        // If Empty, just pop-up a message box to dev to notify their measurements are empty
        int size = easeMeasurements.size();
        if (size ==0) {
            Prompts.infoBox("No Associated Ease", "Ease", EMsgType.Info);
            isRunning = false;
            return;}

        // Create a new Frame which will contain the ease components
        JFrame easeFrame = new JFrame();
        easeFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // An array of sliders, used to store and then modify the measurements
        JSlider[] newValuesSlider = new JSlider[size];

        // Borders for all the main Text areas
        Border nameBorders = BorderFactory.createEtchedBorder();

        JPanel main = new JPanel(new GridBagLayout());

        JLabel easeNamesLabel = new JLabel("Measurement Name", SwingConstants.LEADING);
        easeNamesLabel.setBorder(nameBorders);
        JLabel currentValuesLabel = new JLabel("Current Value", SwingConstants.CENTER);
        currentValuesLabel.setBorder(nameBorders);
        JLabel newValuesLabel = new JLabel("Adjustment", SwingConstants.CENTER);
        newValuesLabel.setBorder(nameBorders);

        // First add the main names at the top
        // Give them all the same weight
        gbc.weightx = 0.2;
        gbc.gridy = 0;

        // Add them in horizontal order
        gbc.gridx = 0;
        main.add(easeNamesLabel, gbc);

        gbc.gridx = 1;
        main.add(currentValuesLabel, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        main.add(newValuesLabel, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.ipadx = 0;

        // Mainly just for visual appeal
        JSeparator menuSeperator = new JSeparator();
        menuSeperator.setPreferredSize(new Dimension(1,1));
        main.add(menuSeperator, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.2;

        // Loop through the arraylist of easeMeasurements and add them in consecutive vertical order
        for (int i = 0; i < size; i++) {

            // Relative positioning so all the measurements are stacked below each other
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.gridx = 0;
            JLabel nameLabel = new JLabel(easeMeasurements.get(i).getName(), SwingConstants.TRAILING);
            main.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.CENTER;

            // Multiply then divide by 10 to truncate the value shown to 1 DP since for now, only allows adjustments of 0.1
            JTextArea currentValText = new JTextArea(((int)((easeMeasurements.get(i)).getValue() * 10.0) / 10.0) + " cm");
            currentValText.setEditable(false);
            main.add(currentValText, gbc);

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            gbc.weightx = 0.8;
            gbc.gridx = 3;
            gbc.ipadx = (int) (dim.getWidth() / 2);

            //set the max and min of the sliders to +-50% of the current plus an additional border of +- 100 which would be +-10.0 in decimal since
            // Get the current value and increase by 50%, use *5 because all values are multiplied by 10 for the slider
            int limits = (int)(Math.abs(easeMeasurements.get(i).getValue() * 5));

            // Set the Min and max values on the slider as the current value +-50% of the current value and a border of +- 100 which is +- 10.0
            int min = ((int)(easeMeasurements.get(i).getValue() * 10.0)) - limits - 100;
            int max = ((int)(easeMeasurements.get(i).getValue() * 10.0)) + limits + 100;

            // For creating the hash table, we get the sliders to mark every 10% of the way
            int interval = (max / 10);
            Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
            //create the Hashtable for each of the sliders
            for (int j = min; j <= max; j += interval) {
                labelTable.put(j, new JLabel(Double.toString(j / 10.0)));
            }

            // Create the new slider component with set min, max and current value
            newValuesSlider[i] = new JSlider((min), (max), (int) (easeMeasurements.get(i).getValue() * 10.0));

            // This would mean 0.1 in double terms
            newValuesSlider[i].setMinorTickSpacing(1);
            newValuesSlider[i].setMajorTickSpacing(interval);
            newValuesSlider[i].setLabelTable(labelTable);
            newValuesSlider[i].setSnapToTicks(true);
            newValuesSlider[i].setPaintLabels(true);
            newValuesSlider[i].setPaintTicks(true);
            main.add(newValuesSlider[i], gbc);

            // Addition of a text box to read from the JSlider
            gbc.gridx = 4;
            gbc.weightx = 0.2;
            gbc.ipadx = 10;
            gbc.anchor = GridBagConstraints.WEST;
            JTextArea newValText = new JTextArea((double)newValuesSlider[i].getValue() / 10.0 + "");
            newValText.setEditable(false);
            main.add(newValText, gbc);

            // Add a listener for the slider and textbox to update whenever the JSlider is moved to show the value of the slider
            newValuesSlider[i].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (e.getSource() instanceof JSlider)
                    {
                        newValText.setText("" + (double)((JSlider) e.getSource()).getValue() / 10.0 );
                    }
                }
            });
        }

        // The confirm button to confirm your changes
        JButton confirmButton = new JButton("Confirm");

        // Window listener for closing
        easeFrame.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) { }
            @Override public void windowClosing(WindowEvent e) { }
            @Override public void windowIconified(WindowEvent e) { }
            @Override public void windowDeiconified(WindowEvent e) { }
            @Override public void windowActivated(WindowEvent e) { }
            @Override public void windowDeactivated(WindowEvent e) { }

            @Override
            public void windowClosed(WindowEvent e) {
                // If they simply close the window, keep old values.
                isRunning = false;
            }
        });

        // When button is pressed, disable the button and alter the values in the static easeMeasurement Array List
        // To the Values changed to
        confirmButton.addActionListener(e -> {
            confirmButton.setEnabled(false);
            alterValues(newValuesSlider, easeMeasurements);
            easeFrame.dispose();
        });

        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridx = GridBagConstraints.RELATIVE;
        main.add(confirmButton);

        // Finally finish up the ease frame and make it visible
        easeFrame.getContentPane().removeAll();
        easeFrame.getContentPane().add(main);
        easeFrame.pack();
        easeFrame.setLocationRelativeTo(null);
        easeFrame.setResizable(false);
        easeFrame.setVisible(true);
    }

    private void alterValues(JSlider[] newValues, ArrayList<easeMeasurement> alteringValues)
    {
        for (int i = 0; i < alteringValues.size(); i++)
        {
            // Divide by 10.0 since the slider only deals with integers, hence initially multiplied by 10 to get it to emulate decimals
            alteringValues.get(i).setValue((double)newValues[i].getValue() / 10.0);
        }
    }
}