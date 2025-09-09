package com.alala.checkpointbackend.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// 標記這個類別為一個 Aspect
@Aspect
// 讓 Spring 掃描並管理這個 Bean
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 定義一個切點 (Pointcut)，這是一個表達式，用來指定在哪裡執行這個 AOP
    // 這裡指定所有在 com.example.controller 套件下的 public 方法都會被攔截
    private static final String controllerPointcut = "execution(public * com.alala.checkpointbackend.controller.*(..))";

    // ----------------------
    // 方法一：使用 @Before (在方法執行前)
    // ----------------------
    @Before(controllerPointcut)
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Before method execution: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    // ----------------------
    // 方法二：使用 @AfterReturning (在方法成功返回後)
    // ----------------------
    @AfterReturning(pointcut = controllerPointcut, returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("After method execution: {}.{} finished. Result: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }

    // ----------------------
    // 方法三：使用 @Around (在方法前後都執行)
    // 這是最常用且功能最強大的方式，可以控制方法的執行
    // ----------------------
    @Around(controllerPointcut)
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 取得方法名稱
        String methodName = joinPoint.getSignature().toShortString();

        logger.info("Request started: {}", methodName);

        Object result = null;
        try {
            // 執行被攔截的方法
            result = joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Request failed: {} with exception: {}", methodName, e.getMessage());
            // 拋出例外，讓 Spring 知道方法執行失敗
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Request finished: {} in {}ms", methodName, duration);
        }

        return result;
    }
}
