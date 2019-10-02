package jblockenums;

/**
 * Typing for messages.
 */
public enum EMsgType
{
    Info,
    Warning,
    Error;

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
            case Info:
                return "Info";

            case Warning:
                return "Warning";

            case Error:
                return "Error";

            default:
                return "";
        }
    }
}
