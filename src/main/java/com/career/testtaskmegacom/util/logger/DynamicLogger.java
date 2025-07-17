package com.career.testtaskmegacom.util.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class DynamicLogger {
    private static final Logger logger = LoggerFactory.getLogger(DynamicLogger.class);

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)" +
            " || within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void loggingTargets() {
    }

    @Before("loggingTargets()")
    public void logBeforeExecuting(JoinPoint joinPoint) {
        logger.debug("Executing method: " + joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName() +
                " || with argument: " + Arrays.toString(joinPoint.getArgs()));
    }

    @Around("loggingTargets()")
    public Object logAroundExecuting(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.nanoTime();
        Object process = proceedingJoinPoint.proceed();
        long end = System.nanoTime();

        logger.debug("During method: " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." +
                proceedingJoinPoint.getSignature().getName() +
                "|| with argument: " + Arrays.toString(proceedingJoinPoint.getArgs()) +
                "|| time: " + (end - start));

        return process;
    }

    @AfterReturning(value = "loggingTargets()", returning = "value")
    public void logAfterExecution(JoinPoint joinPoint, Object value) {
        logger.debug("method " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName() +
                "|| executed and return value [" + value + "]");
    }

    @AfterThrowing(value = "loggingTargets()", throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Exception exception) {
        logger.error("method " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName() + " threw " + exception);
    }
}