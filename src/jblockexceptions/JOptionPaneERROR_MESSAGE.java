package jblockexceptions;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JOptionPaneERROR_MESSAGE
        extends  MeasurementNotFoundException

{

    /**
     * Constructor
     *
     * @param _measurementIdTrigger ID of the measurement which trigger the exception.
     */
    public JOptionPaneERROR_MESSAGE(String _measurementIdTrigger) {
        super(_measurementIdTrigger);
    }

    public void main(String[] args) {
        final JPanel panel = new JPanel();

        JOptionPane.showMessageDialog(panel, "Measurement " + getMeasurementId()+
                " not found in the measurement store. Is it missing from the input file?", "Error", JOptionPane.ERROR_MESSAGE);

    }
}
