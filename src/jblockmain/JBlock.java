package jblockmain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
public class JBlock extends JFrame
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
    private JCheckBox checkGillTrousers;
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
    private JCheckBox checkConstructionLinesAnalysis;

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
    private JLabel imagePatternSample;
    private JLabel imageAnalysisSample;
    private JLabel imageUomLogo;
    private JPanel panelPatternsWrapper;
    private JPanel imagePatternWrapper;
    private JPanel panelAnalysisWrapper;
    private JPanel imageAnalysisWrapper;

    // Internal fields
    private File fileOutput = null;
    private File fileInput = null;
    private boolean[] dxfLayerChoices = new boolean[5];
    private boolean[] dxfLayersAnalysis = new boolean[5];
    private boolean isLayeredRectPlot;
    private boolean isRectanglePlot;
    private boolean isRunning = false;
    private static ResourceBundle bundle = ResourceBundle.getBundle("strings");

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
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        // Create a JFrame instance
        JFrame frame = new JFrame(bundle.getString("app_name") + " - v"
                                          + bundle.getString("maj_ver") + "."
                                          + bundle.getString("min_ver"));
        final JBlock block = new JBlock();
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
                Prompts.infoBox(bundle.getString("help_text"), "Help", EMsgType.Info);
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
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    /**
     * Background thread to perform operations when run button is pressed.
     */
    private class RunThread extends Thread
    {
        @Override
        public void run()
        {
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
                    Measurements measurements = new Measurements(JBlock.this.fileInput.toString());

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
                            bb_skirt.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkBeazleyTrousers.isSelected())
                        {
                            TrouserPattern bb_trouser = new TrouserPattern(measurements);
                            bb_trouser.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkBeazleyBodice.isSelected())
                        {
                            BodicePattern bb_bodice = new BodicePattern(measurements);
                            bb_bodice.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkBeazleyStraightSleeve.isSelected())
                        {
                            StraightSleevePattern bb_sleeve = new StraightSleevePattern(measurements);
                            bb_sleeve.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkGillSkirt.isSelected())
                        {
                            gill.SkirtPattern gill_skirt = new gill.SkirtPattern(measurements);
                            gill_skirt.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkGillTrousers.isSelected())
                        {
                            gill.TrouserPattern gill_trousers = new gill.TrouserPattern(measurements);
                            gill_trousers.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkAldrichSkirt.isSelected())
                        {
                            aldrich.SkirtPattern aldrich_skirt = new aldrich.SkirtPattern(measurements);
                            aldrich_skirt.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        if (checkAldrichTrousers.isSelected())
                        {
                            aldrich.TrouserPattern aldrich_trousers = new aldrich.TrouserPattern(measurements);
                            aldrich_trousers.writeToDXF(fileOutput, dxfLayerChoices);
                        }

                        // Creates analysis outputs depending on which checkboxes are ticked
                        if (plot != null) plot.addNewRectangle();
                    }

                    // Write the plot if we created one
                    if (plot != null)
                    {
                        plot.writeToDXF(fileOutput, dxfLayersAnalysis);
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
            } catch (Exception e)
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
            if(Measurements.checkIdFormat(id)) outLabel.setText(id);
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
            JBlock.this.fileOutput = fileChooser.getSelectedFile();
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
            JBlock.this.fileInput = fileChooser.getSelectedFile();
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
        // Class for selecting which dxf layers to show
        if (checkScaleBoxAndUser.isSelected())
        {
            dxfLayerChoices[0] = true;
        }
        else if (!checkScaleBoxAndUser.isSelected())
        {
            dxfLayerChoices[0] = false;
        }
        if (checkPatternOutline.isSelected())
        {
            dxfLayerChoices[1] = true;
        }
        else if (!checkPatternOutline.isSelected())
        {
            dxfLayerChoices[1] = false;
        }
        if (checkKeypointsAsCircles.isSelected())
        {
            dxfLayerChoices[2] = true;
        }
        else if (!checkKeypointsAsCircles.isSelected())
        {
            dxfLayerChoices[2] = false;
        }
        if (checkKeypointCoordinates.isSelected())
        {
            dxfLayerChoices[3] = true;
        }
        else if (!checkKeypointCoordinates.isSelected())
        {
            dxfLayerChoices[3] = false;
        }
        if (checkConstructionLines.isSelected())
        {
            dxfLayerChoices[4] = true;
        }
        else if (!checkConstructionLines.isSelected())
        {
            dxfLayerChoices[4] = false;
        }
    }

    /**
     * Method to populate the analysis boolean array of DXF layer configuration
     */
    private void getLayerInformationAnalysis()
    {
        // Class for selecting which dxf layers to show
        if (checkScaleBoxAndUserAnalysis.isSelected())
        {
            dxfLayersAnalysis[0] = true;
        }
        else if (!checkScaleBoxAndUserAnalysis.isSelected())
        {
            dxfLayersAnalysis[0] = false;
        }
        if (checkConnectingLinesAnalysis.isSelected())
        {
            dxfLayersAnalysis[1] = true;
        }
        else if (!checkConnectingLinesAnalysis.isSelected())
        {
            dxfLayersAnalysis[1] = false;
        }
        if (checkKeypointsAsCirclesAnalysis.isSelected())
        {
            dxfLayersAnalysis[2] = true;
        }
        else if (!checkKeypointsAsCirclesAnalysis.isSelected())
        {
            dxfLayersAnalysis[2] = false;
        }
        if (checkKeypointCoordinatesAnalysis.isSelected())
        {
            dxfLayersAnalysis[3] = true;
        }
        else if (!checkKeypointCoordinatesAnalysis.isSelected())
        {
            dxfLayersAnalysis[3] = false;
        }
        if (checkConstructionLinesAnalysis.isSelected())
        {
            dxfLayersAnalysis[4] = true;
        }
        else if (!checkConstructionLinesAnalysis.isSelected())
        {
            dxfLayersAnalysis[4] = false;
        }
    }

    /**
     * Private constructor for the form-bound class
     */
    private JBlock()
    {
        /* Add listeners */

        // Listener for the Run button
        butRun.addActionListener(e -> new RunThread().start());

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
    }

    /**
     * A utility method for creating menu items
     * @param label             menu label
     * @param listener          click listener
     * @param command           name of command
     * @param mnemonic          underlined letter
     * @param acceleratorKey    key binding
     * @return                  menu item
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
     * @param label         button label
     * @param listener      click listener
     * @param command       name of command
     * @param mutExGroup    group tag
     * @return              radio button
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
     * Method to add assets to image components of UI
     */
    private void createUIComponents()
    {
        imagePatternSample = new JLabel(new ImageIcon("./images/sample_pattern.jpg"));
        imageAnalysisSample = new JLabel(new ImageIcon("./images/sample_analysis.jpg"));
        imageUomLogo = new JLabel(new ImageIcon("./images/logo_small.jpg"));
    }
}