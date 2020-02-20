package com.mobileleader.rpa.api.aspect;

import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.api.support.AuthenticationTokenSupport;
import com.mobileleader.rpa.utils.message.BindingResultMessageUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Aspect
@Component
public class BindingResultAspect {
    private static final Logger logger = LoggerFactory.getLogger(BindingResultAspect.class);

    /**
     * BindingResult 에러 체크 Aspect.
     *
     * @param joinPoint joinPoint
     */
    @Before("execution(public * com.mobileleader.rpa.api.controller.**.*Controller.*(..))")
    public void checkBindingResultErrors(JoinPoint joinPoint) {
        BindingResult bindingResult = getBindingResult(joinPoint.getArgs());
        if (bindingResult != null && bindingResult.hasErrors()) {
            logger.info(AuthenticationTokenSupport.getTokenInfoLog());
            throw new RpaApiException(RpaApiError.INVALID_PARAMETER,
                    BindingResultMessageUtils.getErrorMessage(bindingResult));
        }
    }

    private BindingResult getBindingResult(Object[] args) {
        BindingResult bindingResult = null;
        for (Object object : args) {
            if (object instanceof BindingResult) {
                bindingResult = (BindingResult) object;
                break;
            }
        }
        return bindingResult;
    }
}
