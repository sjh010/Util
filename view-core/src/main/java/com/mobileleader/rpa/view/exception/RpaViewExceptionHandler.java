package com.mobileleader.rpa.view.exception;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.mobileleader.rpa.view.controller.web")
public class RpaViewExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpaViewExceptionHandler.class);


    /**
     * RpaViewException Handler.
     *
     * @param e {@link RpaViewException}
     * @return view
     */
    @ExceptionHandler(RpaViewException.class)
    public String handleRpaViewException(RpaViewException e) {
        logger.error("[RpaViewException]", e);
        return "/error/500";
    }

    /**
     * IllegalArgumentException Handler.
     *
     * @param e {@link IllegalArgumentException}
     * @return view
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("[IllegalArgumentException]", e);
        return "/error/500";
    }

    /**
     * SQLException Handler.
     *
     * @param e {@link SQLException}
     * @return view
     */
    @ExceptionHandler(SQLException.class)
    public String handleSqlServerException(SQLException e) {
        logger.error("[SQLException]", e);
        return "/error/500";
    }

    /**
     * DataAccessException Handler.
     *
     * @param e {@link DataAccessException}
     * @return view
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public String handleDataAccessException(DataAccessException e) {
        logger.error("[DataAccessException]", e);
        return "/error/500";
    }

    /**
     * NullPointerException Handler.
     *
     * @param e {@link NullPointerException}
     * @return view
     */
    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException e) {
        logger.error("[NullPointerException]", e);
        return "/error/500";
    }

    /**
     * AccessDeniedException Handler.
     *
     * @param e {@link AccessDeniedException}
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e) {
        logger.warn("[AccessDeniedException]", e);
        return "/error/403";
    }
}
