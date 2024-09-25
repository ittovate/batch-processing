package com.ittovative.batchprocessing.ascpet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ittovative.batchprocessing.util.AspectUtil;


/**
 * The type Logging aspect.
 */
@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Around advice for logging
     * before and after executing project methods.
     *
     * @param joinPoint which contains details about method called
     * @return the return value of the method
     * @throws Throwable the throwable
     */
    @Around("execution(* com.ittovative.batchprocessing.*.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = AspectUtil.getClassName(joinPoint);
        String methodName = AspectUtil.getMethodName(joinPoint);
        StringBuilder args = AspectUtil.getMethodArgs(joinPoint);
        Object returnVal = null;

        LOGGER.info("Executing ===> {}.{} with arguments: [{}]", className, methodName, args);
        try {
            returnVal = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            LOGGER.error("Exception {} in ===> {}.{} with arguments: [{}]", throwable, className, methodName, args);
            throw throwable;
        }
        LOGGER.info("Finished ===> {}.{} with arguments: [{}] and returned {}", className, methodName, args, returnVal);

        return returnVal;
    }
}
