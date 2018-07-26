package jblockmain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import beazleybond.BodicePattern;
import beazleybond.SkirtPattern;
import beazleybond.StraightSleevePattern;
import beazleybond.TrouserPattern;

import jblockenums.EGarment;
import jblockenums.EMethod;
import jblockenums.EMsgType;

public class JBlock extends JFrame
{
    // Declaration of used backend components
    private JPanel panelMain;
    private JLabel labAldrich;
    private JLabel labBeazleyBond;
    private JLabel labGill;
    private JCheckBox checkAldrichSkirt;
    private JCheckBox checkBeazleySkirt;
    private JCheckBox checkGillSkirt;
    private JCheckBox checkBeazleyStraightSleeve;
    private JCheckBox checkBeazleyTrousers;
    private JCheckBox checkBeazleyBodice;
    private JButton butRun;
    private JButton butSave;
    private JButton butLoad;
    private JLabel openPath;
    private JLabel savePath;
    private JCheckBox isbatchCheckbox;
    private JCheckBox scaleBoxAndUserCheckBox;
    private JCheckBox patternOutlineCheckBox;
    private JCheckBox keypointsAsCirclesCheckBox;
    private JCheckBox keypointCoordinatesCheckBox;
    private JCheckBox constructionLinesCheckBox;
    private File fileInput = null;
    private File fileOutput = null;
    private boolean[] dxfLayerChoices = new boolean[5];

    // Set a global tolerance for some operations
    public static final double tol = 10e-8;

    // Set a global resolution for some curves (points per cm)
    public static final double res = 1;

    // Version number
    private static final int majVer = 1;
    private static final int minVer = 0;

    // Method for when the save button is clicked
    private void saveClickedEvent()
    {
        // Choose a folder location to save the output files
        // Opens a file explorer for users to choose directory
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File(""));
        fileChooser.setDialogTitle("Select Save Location");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            // Prints out the directory chosen, purely for test purposes
            // Also stores the location in the save path gui label and the fileoutput variable
            System.out.println("Current directory is: " + fileChooser.getCurrentDirectory());
            System.out.println("Save location is: " + fileChooser.getSelectedFile());
            String file = fileChooser.getCurrentDirectory().toString();
            if (file.length() > 40)
            {
                file = file.substring(0, 40) + "...";
            }
            savePath.setText(file);
            JBlock.this.fileOutput = fileChooser.getSelectedFile();
        }
    }

    // Method for when the open button is clicked
    private void openClickedEvent()
    {
        // Choose a folder input
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            // Prints out the input file chosen, purely for test purposes
            // Also stores the location in the input path gui label and the fileinput variable
            JBlock.this.fileInput = fileChooser.getSelectedFile();
            System.out.println("Input file is: " + fileInput.toString());
            String file = fileChooser.getSelectedFile().toString();
            if (file.length() > 40)
            {
                file = file.substring(0, 40) + "...";
            }
            openPath.setText(file);
        }
    }

    // Method for when the run button is clicked
    private void runClickedEvent()
    {
        if (fileOutput != null && fileInput != null)
        {
            Measurements measurements = new Measurements(JBlock.this.fileInput.toString(),
                    JBlock.this.isbatchCheckbox.isSelected());

            // Need to populate the boolean array
            getLayerInformation();

            // Create patterns
            for (int i = 0; i < measurements.getNames().size(); i++)
            {
                measurements.setMapNumber(i);

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

                if (checkAldrichSkirt.isSelected())
                {
                    aldrich.SkirtPattern aldrich_skirt = new aldrich.SkirtPattern(measurements);
                    aldrich_skirt.writeToDXF(fileOutput, dxfLayerChoices);
                }
            }

            // Create done prompt
            Prompts.infoBox("Done!", "Done", EMsgType.Info);
        }

        // Handle missing options
        if (fileInput == null)
        {
            Prompts.infoBox("Please choose your input file by clicking on the \"Open\" button",
                    "Input File Needed",
                    EMsgType.Error);
            return;
        }
        if (fileOutput == null)
        {
            Prompts.infoBox("Please choose a directory to write the patterns to by clicking on \"Save\"",
                    "Output Directory Needed",
                    EMsgType.Error);
        }
    }
    
    // Method to populate the boolean array of DXF layer configuration
    private void getLayerInformation()
    {
        // Class for selecting which dxf layers to show
        if (scaleBoxAndUserCheckBox.isSelected())
        {
            dxfLayerChoices[0] = true;
        }
        else if (!scaleBoxAndUserCheckBox.isSelected())
        {
            dxfLayerChoices[0] = false;
        }
        if (patternOutlineCheckBox.isSelected())
        {
            dxfLayerChoices[1] = true;
        }
        else if (!patternOutlineCheckBox.isSelected())
        {
            dxfLayerChoices[1] = false;
        }
        if (keypointsAsCirclesCheckBox.isSelected())
        {
            dxfLayerChoices[2] = true;
        }
        else if (!keypointsAsCirclesCheckBox.isSelected())
        {
            dxfLayerChoices[2] = false;
        }
        if (keypointCoordinatesCheckBox.isSelected())
        {
            dxfLayerChoices[3] = true;
        }
        else if (!keypointCoordinatesCheckBox.isSelected())
        {
            dxfLayerChoices[3] = false;
        }
        if (constructionLinesCheckBox.isSelected())
        {
            dxfLayerChoices[4] = true;
        }
        else if (!constructionLinesCheckBox.isSelected())
        {
            dxfLayerChoices[4] = false;
        }
    }

    // Method containing button actionlisteners
    private JBlock()
    {
        // Listener for the Run button
        butRun.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                runClickedEvent();
            }
        });

        // Attach listener to open button
        butLoad.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                openClickedEvent();
            }
        });

        // Attach listener to save button
        butSave.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveClickedEvent();
            }
        });
    }

    // PSVM to run the application
    public static void main(String[] args)
    {
        // Create a JFrame instance
        JFrame frame = new JFrame("JBlock2D - Custom Pattern Drafting (Version "
                + majVer + "." + minVer + ")");
        final JBlock block = new JBlock();
        frame.setContentPane(block.panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        // Centre on screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
                dim.height / 2 - frame.getSize().height / 2);

        // Sets the frame as visible
        frame.setVisible(true);

        // Sets the frame size
        frame.setSize(450, 450);

        /* MENU BAR SETUP */

        // Create a window for the menu
        JFrame frame1 = new JFrame("Menu");
        JPanel panel = new JPanel();
        frame1.getContentPane().add(panel, "Center");

        // Create an action listener for the menu items we will create
        ActionListener listener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
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
                    Prompts.infoBox("PLACEHOLDER", "PLACEHOLDER", EMsgType.Info);
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
            }
        };

        // Create some menu panes, and fill them with menu items
        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        file.add(menuItem("Open", listener, "Open", 'O', KeyEvent.VK_O));
        file.addSeparator();
        file.add(menuItem("Save", listener, "Save", 'S', KeyEvent.VK_S));
        file.addSeparator();
        file.add(menuItem("Run", listener, "Run", 'R', KeyEvent.VK_R));
        file.addSeparator();
        file.add(menuItem("Exit", listener, "Exit", 'E', KeyEvent.VK_E));

        JMenu edit = new JMenu("Help");
        edit.setMnemonic('H');
        edit.add(menuItem("View Help", listener, "View help", 'H', KeyEvent.VK_H));

        // Create a menubar and add these panes to it.
        JMenuBar menubar = new JMenuBar();
        menubar.add(file);
        menubar.add(edit);

        // Add menubar to the main window.  Note special method to add menubars
        frame.setJMenuBar(menubar);

        // Now create a popup menu and add the some stuff to it
        final JPopupMenu popup = new JPopupMenu();
        popup.add(menuItem("Open", listener, "open", 0, 0));
        popup.addSeparator();                // Add a separator between items
        JMenu colors = new JMenu("Colors");  // Create a submenu
        popup.add(colors);                   // and add it to the popup menu

        // Now fill the submenu with mutually-exclusive radio buttons
        ButtonGroup colorgroup = new ButtonGroup();
        colors.add(radioItem("Red", listener, "color(red)", colorgroup));
        colors.add(radioItem("Green", listener, "color(green)", colorgroup));
        colors.add(radioItem("Blue", listener, "color(blue)", colorgroup));

        // Finally, make our main window appear
        frame.setSize(530, 350);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // A convenience method for creating menu items.
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
                    java.awt.Event.CTRL_MASK));
        return item;
    }

    // A convenience method for creating radio button menu items.
    private static JMenuItem radioItem(String label, ActionListener listener,
                                       String command, ButtonGroup mutExGroup)
    {
        JMenuItem item = new JRadioButtonMenuItem(label);
        item.addActionListener(listener);
        item.setActionCommand(command);
        mutExGroup.add(item);
        return item;
    }
}
