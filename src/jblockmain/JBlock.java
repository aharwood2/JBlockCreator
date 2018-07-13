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
    // Mass initializations
    private JPanel panelMain;
    private JLabel Gill;
    private JLabel Aldrich;
    private JLabel BeazleyBond;
    private JCheckBox ASkirt;
    private JCheckBox BBSkirt;
    private JCheckBox GSkirt;
    private JCheckBox BBStraightSleeve;
    private JCheckBox BBTrousers;
    private JCheckBox BBBodice;
    private JButton RunJBlock;
    private JButton save;
    private JButton open;
    private boolean BBSkirtbool = false;
    private boolean BBTrousersbool = false;
    private boolean BBBodicebool = false;
    private boolean BBStraightSleevebool = false;
    private boolean GSkirtbool = false;
    private boolean ASkirtbool = false;
    private File openfile = null;
    private File savefile = null;

    // Set a global tolerance for some operations
    public static final double tol = 10e-8;

    // Set a global resolution for some curves (points per cm)
    public static final double res = 1;


    public JBlock()
    {
        // Listener for the open button
        open.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Choose a folder input
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    JBlock.this.openfile = fileChooser.getSelectedFile();
                    System.out.println("Input file is: " + openfile.toString());
                }
                else
                {
                    System.out.println("No selection");
                }
            }
        });

        // Listener for the save button
        save.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Choose a folder location to save the output files
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new java.io.File(""));
                fileChooser.setDialogTitle("Select Save Location");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                JBlock.this.savefile = fileChooser.getSelectedFile();
                fileChooser.setAcceptAllFileFilterUsed(false);
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
        RunJBlock.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Measurements measurements = new Measurements(JBlock.this.openfile.toString(), true);

                // Create patterns
                for (int i = 0; i < measurements.getNames().size(); i++)
                {
                    measurements.setMapNumber(i);

                    // Creates patterns depending on which checkboxes are ticked
                    if (BBSkirt.isSelected())
                    {
                        SkirtPattern bb_skirt = new SkirtPattern(measurements);
                        bb_skirt.writeToDXF();
                    }

                    if (BBTrousers.isSelected())
                    {
                        TrouserPattern bb_trouser = new TrouserPattern(measurements);
                        bb_trouser.writeToDXF();
                    }

                    if (BBBodice.isSelected())
                    {
                        BodicePattern bb_bodice = new BodicePattern(measurements);
                        bb_bodice.writeToDXF();
                    }

                    if (BBStraightSleeve.isSelected())
                    {
                        StraightSleevePattern bb_sleeve = new StraightSleevePattern(measurements);
                        bb_sleeve.writeToDXF();
                    }

                    if (GSkirt.isSelected())
                    {
                        gill.SkirtPattern gill_skirt = new gill.SkirtPattern(measurements);
                        gill_skirt.writeToDXF();
                    }

                    if (ASkirt.isSelected())
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
        JFrame frame = new JFrame("JBlock2D - Custom Pattern Drafting");
        frame.setContentPane(new JBlock().panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600, 400);
        new JBlock();
    }
}
