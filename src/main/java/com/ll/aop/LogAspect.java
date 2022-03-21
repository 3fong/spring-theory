package com.ll.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author liulei
 * @Description
 * @create 2022/3/21 22:03
 */
@Aspect
public class LogAspect {

    @Pointcut("execution(com.ll.service.*)")
    public void pointCut() {}

    @Before("pointCut()")
    public void before() {
        System.out.println("LogAspect before");
    }
}
