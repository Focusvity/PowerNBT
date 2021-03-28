package me.focusvity.powernbt.exception;

public class ParseException extends SourceException
{

    public ParseException(String source, int row, int col, String reason)
    {
        super(source, row, col, reason);
    }

    @Override
    public String getMessage()
    {
        String msg, reason;
        if (getCause() == null)
        {
            msg = super.getMessage();
            reason = "";
        }
        else
        {
            msg = getCause().getMessage();
            reason = getCause().getClass().getSimpleName();

        }
        return reason + " at [" + (row + 1) + ':' + (col + 1) + "]\n" + getErrorString() + (msg == null ? "" : '\n' + msg);
    }
}
