package com.mobileleader.rpa.api.exception;

import com.mobileleader.rpa.api.model.response.RpaApiExceptionResponse;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class RpaApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpaApiExceptionHandler.class);

    /**
     * API Server Exception Response Handler.
     *
     * @param e {@link RpaApiException}
     * @return {@link RpaApiExceptionResponse}
     */
    @ExceptionHandler(RpaApiException.class)
    @ResponseBody
    public ResponseEntity<RpaApiExceptionResponse> handleRpaApiException(RpaApiException e) {
        RpaApiError error = RpaApiError.INTERNAL_SERVER_ERROR;
        if (e.getRpaApiError() != null) {
            error = e.getRpaApiError();
        }
        switch (error) {
            case AUTHENTICATION_ERROR:
            case INVALID_USER_ID_OR_PASSWORD:
            case AUTHORITY_ERROR:
            case INVALID_PARAMETER:
                logger.warn("[RpaApiException]", e);
                break;
            default:
                logger.error("[RpaApiException]", e);
                break;
        }
        return new ResponseEntity<RpaApiExceptionResponse>(buildResponse(error, e.getMessage()), HttpStatus.OK);
    }

    /**
     * IllegalArgumentException Response Handler.
     *
     * @param e {@link IllegalArgumentException}
     * @return {@link RpaApiExceptionResponse}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<RpaApiExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("[IllegalArgumentException]", e);
        return new ResponseEntity<RpaApiExceptionResponse>(buildResponse(RpaApiError.ILLEGAL_ARGUMENT, e.getMessage()),
                HttpStatus.OK);
    }

    /**
     * SQLException Response Handler.
     *
     * @param e {@link SQLException}
     * @return {@link RpaApiExceptionResponse}
     */
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public ResponseEntity<RpaApiExceptionResponse> handleSqlServerException(SQLException e) {
        logger.error("[SQLException]", e);
        return new ResponseEntity<RpaApiExceptionResponse>(buildResponse(RpaApiError.SQL_ERROR, e.getMessage()),
                HttpStatus.OK);
    }

    /**
     * DataAccessException Response Handler.
     *
     * @param e {@link DataAccessException}
     * @return {@link RpaApiExceptionResponse}
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public ResponseEntity<RpaApiExceptionResponse> handleDataAccessException(DataAccessException e) {
        logger.error("[DataAccessException]", e);
        return new ResponseEntity<RpaApiExceptionResponse>(buildResponse(RpaApiError.SQL_ERROR, e.getMessage()),
                HttpStatus.OK);
    }

    /**
     * AccessDeniedException Response Handler.
     *
     * @param e {@link AccessDeniedException}
     * @return {@link RpaApiExceptionResponse}
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<RpaApiExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
        logger.error("[AccessDeniedException]", e);
        return new ResponseEntity<RpaApiExceptionResponse>(buildResponse(RpaApiError.AUTHORITY_ERROR, e.getMessage()),
                HttpStatus.OK);
    }

    /**
     * NullPointerException Response Handler.
     *
     * @param e {@link NullPointerException}
     * @return {@link RpaApiExceptionResponse}
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResponseEntity<RpaApiExceptionResponse> handleNullPointerException(NullPointerException e) {
        logger.error("[NullPointerException]", e);
        return new ResponseEntity<RpaApiExceptionResponse>(
                buildResponse(RpaApiError.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.OK);
    }

    /**
     * MultipartException Response Handler.
     *
     * @param e {@link MultipartException}
     * @return {@link RpaApiExceptionResponse}
     */
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public ResponseEntity<RpaApiExceptionResponse> handleMultipartException(MultipartException e) {
        logger.error("[MultipartException]", e);
        return new ResponseEntity<RpaApiExceptionResponse>(
                buildResponse(RpaApiError.MAX_UPLOAD_SIZE_PER_FILE_EXCEEDS, e.getMessage()), HttpStatus.OK);
    }

    private RpaApiExceptionResponse buildResponse(RpaApiError error, String exceptionMessage) {
        return new RpaApiExceptionResponse.Builder().exceptionMessage(exceptionMessage)
                .errorMessage(error.getErrorMessage()).errorCode(error.getErrorCode()).build();
    }
}
