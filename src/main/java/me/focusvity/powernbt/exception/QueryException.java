package me.focusvity.powernbt.exception;

public class QueryException extends Exception
{

    public QueryException()
    {
        super();
    }

    public QueryException(String message)
    {
        super(message);
    }

    public QueryException(Throwable cause)
    {
        super(cause);
    }

    public QueryException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
