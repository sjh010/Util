package com.mobileleader.rpa.view.aspect;

import com.mobileleader.rpa.utils.aspect.AbstractLoggingAspect;
import com.mobileleader.rpa.view.support.UserDetailsSupport;
import java.util.Arrays;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RpaViewLoggingAspect extends AbstractLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RpaViewLoggingAspect.class);

    private static final List<String> EXCLUDE_METHOD_NAMES = Arrays.asList("");

    public RpaViewLoggingAspect() {
        super("com.mobileleader.rpa.view.model.request", "com.mobileleader.rpa.view.model.form");
    }

    /**
     * Service request argument logging.
     *
     * @param joinPoint before joinPoint
     */
    @Before("execution(public * com.mobileleader.rpa.view.service.**.*ServiceImpl.*(..))")
    public void loggingBeforeService(JoinPoint joinPoint) {
        if (!EXCLUDE_METHOD_NAMES.contains(joinPoint.getSignature().getName())) {
            logger.info(UserDetailsSupport.getUserInfoLog() + buildBeforeLog(joinPoint));
        }
    }

    /**
     * Service response logging.
     *
     * @param joinPoint afterReturning joinPoint
     * @param response response object
     * @return response
     */
    @AfterReturning(pointcut = "execution(public * com.mobileleader.rpa.view.service.**.*ServiceImpl.*(..))",
            returning = "response")
    public Object loggingAfterReturningService(JoinPoint joinPoint, Object response) {
        if (!EXCLUDE_METHOD_NAMES.contains(joinPoint.getSignature().getName())) {
            logger.info(buildAfterReturningLog(joinPoint, response));
        }
        return response;
    }
}
