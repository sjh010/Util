package com.mobileleader.rpa.view.exception;

import com.mobileleader.rpa.view.model.response.RpaViewExceptionResponse;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.mobileleader.rpa.view.controller.rest")
public class RpaViewRestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpaViewRestExceptionHandler.class);

    /**
     * RpaViewExceptio handler.
     *
     * @param e {@link RpaViewException}
     * @return
     */
    @ExceptionHandler(RpaViewRestException.class)
    public ResponseEntity<RpaViewExceptionResponse> handleRpaViewRestException(RpaViewRestException e) {
        RpaViewRestError error =
                e.getRpaViewRestError() != null ? e.getRpaViewRestError() : RpaViewRestError.INTERNAL_SERVER_ERROR;
        logger.error("[RpaViewRestException]", e);
        return new ResponseEntity<RpaViewExceptionResponse>(buildResponse(error, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Warn logging Handler.
     *
     * @param e {@link Exception}
     * @return {@link RpaViewExceptionResponse}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RpaViewExceptionResponse> handleWarningLoggingException(Exception e) {
        logger.warn("[WarningLoggingException]", e);
        return new ResponseEntity<RpaViewExceptionResponse>(
                buildResponse(RpaViewRestError.ILLEGAL_ARGUMENT, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpStatus> handleAccessDeniedException(AccessDeniedException e) {
        logger.warn("[AccessDeniedException]", e);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * Error logging Handler.
     *
     * @param e {@link Exception}
     * @return {@link RpaViewExceptionResponse}
     */
    @ExceptionHandler({SQLException.class, DataAccessException.class, NullPointerException.class})
    public ResponseEntity<RpaViewExceptionResponse> handleErrorLoggingException(Exception e) {
        logger.error("[ErrorLoggingException]", e);
        return new ResponseEntity<RpaViewExceptionResponse>(
                buildResponse(RpaViewRestError.INTERNAL_SERVER_ERROR, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private RpaViewExceptionResponse buildResponse(RpaViewRestError error, String exceptionMessage) {
        return new RpaViewExceptionResponse.Builder().exceptionMessage(exceptionMessage)
                .errorMessage(error.getErrorMessage()).errorCode(error.getErrorCode()).build();
    }
}
