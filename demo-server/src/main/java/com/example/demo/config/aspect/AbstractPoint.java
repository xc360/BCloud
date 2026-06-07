package com.example.demo.config.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * <p>切面切点配置</p>
 *
 * @author xc
 * @version v1.0
 */
public abstract class AbstractPoint {
    @Pointcut("execution(public * com.example..web..*(..)) && !execution(public * com.example..web.socket..*(..))")
    void writeLog() {
    }
}
