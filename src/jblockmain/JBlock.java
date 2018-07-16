package jblockmain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import beazleybond.BodicePattern;
import beazleybond.SkirtPattern;
import beazleybond.StraightSleevePattern;
import beazleybond.TrouserPattern;

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
    private File fileInput = null;
    private File fileOutput = null;

    // Set a global tolerance for some operations
    public static final double tol = 10e-8;

    // Set a global resolution for some curves (points per cm)
    public static final double res = 1;

    // Version number
    public static final int majVer = 0;
    public static final int minVer = 2;

    private JBlock()
    {
        // Listener for the butLoad button
        butLoad.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Choose a folder input
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
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
        });

        // Listener for the butSave button
        butSave.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Choose a folder location to save the output files
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new java.io.File(""));
                fileChooser.setDialogTitle("Select Save Location");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
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
        });

        // Listener for the Run button
        butRun.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                if (fileOutput != null && fileInput != null)
                {
                    Measurements measurements = new Measurements(JBlock.this.fileInput.toString(),
                                                                 JBlock.this.isbatchCheckbox.isSelected());

                    // Create patterns
                    for (int i = 0; i < measurements.getNames().size(); i++)
                    {
                        measurements.setMapNumber(i);

                        // Creates patterns depending on which checkboxes are ticked
                        if (checkBeazleySkirt.isSelected())
                        {
                            SkirtPattern bb_skirt = new SkirtPattern(measurements);
                            bb_skirt.writeToDXF(fileOutput);
                        }

                        if (checkBeazleyTrousers.isSelected())
                        {
                            TrouserPattern bb_trouser = new TrouserPattern(measurements);
                            bb_trouser.writeToDXF(fileOutput);
                        }

                        if (checkBeazleyBodice.isSelected())
                        {
                            BodicePattern bb_bodice = new BodicePattern(measurements);
                            bb_bodice.writeToDXF(fileOutput);
                        }

                        if (checkBeazleyStraightSleeve.isSelected())
                        {
                            StraightSleevePattern bb_sleeve = new StraightSleevePattern(measurements);
                            bb_sleeve.writeToDXF(fileOutput);
                        }

                        if (checkGillSkirt.isSelected())
                        {
                            gill.SkirtPattern gill_skirt = new gill.SkirtPattern(measurements);
                            gill_skirt.writeToDXF(fileOutput);
                        }

                        if (checkAldrichSkirt.isSelected())
                        {
                            aldrich.SkirtPattern aldrich_skirt = new aldrich.SkirtPattern(measurements);
                            aldrich_skirt.writeToDXF(fileOutput);
                        }
                    }

                    // Create done prompt
                    Prompts.infoBox("Done!", "Done", EMsgType.INFO);
                }

                // Handle missing options
                if (fileInput == null)
                {
                    Prompts.infoBox("Please choose your input file by clicking on the \"Open\" button",
                                    "Input File Needed",
                                    EMsgType.ERROR);
                }
                if (fileOutput == null)
                {
                    Prompts.infoBox("Please choose a directory to write the patterns to by clicking on \"Save\"",
                                    "Output Directory Needed",
                                    EMsgType.ERROR);
                }
            }
        });
    }

    // PSVM to run the application
    public static void main(String[] args)
    {
        // Create a JFrame instance
        JFrame frame = new JFrame("JBlock2D - Custom Pattern Drafting (Version "
                                          + majVer + "." + minVer + ")");
        frame.setContentPane(new JBlock().panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        // Centre on screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
                          dim.height / 2 - frame.getSize().height / 2);

        // Sets the frame as visible
        frame.setVisible(true);

        // Sets the frame size
        frame.setSize(400, 400);
    }
}
