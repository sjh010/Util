package com.mobileleader.rpa.view.aspect;

import com.mobileleader.rpa.utils.message.BindingResultMessageUtils;
import com.mobileleader.rpa.view.exception.RpaViewError;
import com.mobileleader.rpa.view.exception.RpaViewException;
import com.mobileleader.rpa.view.exception.RpaViewRestError;
import com.mobileleader.rpa.view.exception.RpaViewRestException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Aspect
@Component
public class BindingResultAspect {
    /**
     * View BindingResult 에러 체크 Aspect.
     *
     * @param joinPoint {@link JoinPoint}
     */
    @Before("execution(public * com.mobileleader.rpa.view.controller.web.**.*Controller.*(..))")
    public void checkViewBindingResultErrors(JoinPoint joinPoint) {
        BindingResult bindingResult = getBindingResult(joinPoint.getArgs());
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new RpaViewException(RpaViewError.BAD_REQUEST,
                    BindingResultMessageUtils.getErrorMessage(bindingResult));
        }
    }

    /**
     * Rest BindingResult 에러 체크 Aspect.
     *
     * @param joinPoint {@link JoinPoint}
     */
    @Before("execution(public * com.mobileleader.rpa.view.controller.rest.**.*Controller.*(..))")
    public void checkRestBindingResultErrors(JoinPoint joinPoint) {
        BindingResult bindingResult = getBindingResult(joinPoint.getArgs());
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new RpaViewRestException(RpaViewRestError.ILLEGAL_ARGUMENT,
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
