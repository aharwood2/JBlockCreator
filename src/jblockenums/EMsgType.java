package jblockenums;

/**
 * Typing for messages.
 */
public enum EMsgType
{
    INFO,
    WARNING,
    ERROR;

    /**
     * Method to convert the message enum to a string.
     *
     * @param type enumeration.
     * @return string representation.
     */
    public String getStringType(EMsgType type)
    {
        switch (type)
        {
            case INFO:
                return "Info";

            case WARNING:
                return "Warning";

            case ERROR:
                return "Error";

            default:
                return "";
        }
    }
}
