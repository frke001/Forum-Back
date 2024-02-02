package org.unibl.etf.forumback.exceptions;

public class NotApprovedException extends RuntimeException{
    public NotApprovedException()
    {
        super("Account not approved");
    }

    public NotApprovedException(String message)
    {
        super(message);
    }
}
