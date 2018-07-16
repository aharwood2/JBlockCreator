package jblockmain;

import jblockenums.EMsgType;

import javax.swing.*;

/**
 * Class to wrap various message box initialisations.
 */
public class Prompts
{
    /**
     * Create a message box.
     * @param infoMessage   Body text of box.
     * @param titleBar      Error title.
     * @param msgtype       Message type based on enumeration.
     */
    public static void infoBox(String infoMessage, String titleBar, EMsgType type)
    {


        JOptionPane.showMessageDialog(null, infoMessage, "Error: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
