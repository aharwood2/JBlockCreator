package jblockmain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;

import beazleybond.BodicePattern;
import beazleybond.SkirtPattern;
import beazleybond.StraightSleevePattern;
import beazleybond.TrouserPattern;

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
    private File fileInput = null;

    // Set a global tolerance for some operations
    public static final double tol = 10e-8;

    // Set a global resolution for some curves (points per cm)
    public static final double res = 1;


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
                }
                else
                {
                    // TODO: Assign some default?
                    System.out.println("No selection");
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

                // TODO: Need to actually store the output path somewhere and use it


                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    System.out.println("Current directory is: " + fileChooser.getCurrentDirectory());
                    System.out.println("Save location is: " + fileChooser.getSelectedFile());
                }
                else
                {
                    System.out.println("No selection");
                }

            }
        });

        // Listener for the Run button
        butRun.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Measurements measurements = new Measurements(JBlock.this.fileInput.toString(), true);

                // Create patterns
                for (int i = 0; i < measurements.getNames().size(); i++)
                {
                    measurements.setMapNumber(i);

                    // Creates patterns depending on which checkboxes are ticked
                    if (checkBeazleySkirt.isSelected())
                    {
                        SkirtPattern bb_skirt = new SkirtPattern(measurements);
                        bb_skirt.writeToDXF();
                    }

                    if (checkBeazleyTrousers.isSelected())
                    {
                        TrouserPattern bb_trouser = new TrouserPattern(measurements);
                        bb_trouser.writeToDXF();
                    }

                    if (checkBeazleyBodice.isSelected())
                    {
                        BodicePattern bb_bodice = new BodicePattern(measurements);
                        bb_bodice.writeToDXF();
                    }

                    if (checkBeazleyStraightSleeve.isSelected())
                    {
                        StraightSleevePattern bb_sleeve = new StraightSleevePattern(measurements);
                        bb_sleeve.writeToDXF();
                    }

                    if (checkGillSkirt.isSelected())
                    {
                        gill.SkirtPattern gill_skirt = new gill.SkirtPattern(measurements);
                        gill_skirt.writeToDXF();
                    }

                    if (checkAldrichSkirt.isSelected())
                    {
                        aldrich.SkirtPattern aldrich_skirt = new aldrich.SkirtPattern(measurements);
                        aldrich_skirt.writeToDXF();
                    }
                }
            }
        });
    }

    // PSVM to run the application
    public static void main(String[] args)
    {
        // Create a JFrame instance
        JFrame frame = new JFrame("JBlock2D - Custom Pattern Drafting");
        frame.setContentPane(new JBlock().panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600, 400);

        // Instantiate backend
        new JBlock();
    }
}
