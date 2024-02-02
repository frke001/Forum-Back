package org.unibl.etf.forumback.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.unibl.etf.forumback.exceptions.AccountBlockedException;
import org.unibl.etf.forumback.exceptions.NotApprovedException;
import org.unibl.etf.forumback.exceptions.NotFoundException;
import org.unibl.etf.forumback.exceptions.UnauthorizedException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotApprovedException.class)
    public ResponseEntity<Object> handleNotApprovedException(NotApprovedException e)
    {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<Object> handleAccountBlockedException(AccountBlockedException e)
    {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e)
    {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
}
