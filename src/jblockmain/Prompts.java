package jblockmain;

import javax.swing.*;

/**
 * Class to wrap various message box initialisations.
 */
public class ErrorPrompt
{
    /**
     * Create a message box.
     * @param infoMessage   Body text of box.
     * @param titleBar      Error title.
     */
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Error: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
