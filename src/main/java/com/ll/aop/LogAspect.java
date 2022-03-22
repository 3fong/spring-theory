package com.ll.aop;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author liulei
 * @Description
 * @create 2022/3/21 22:03
 */
@Aspect
@Component// 组件标识,用于将对象放入容器中
public class LogAspect {

    // 切入点表达式要注意返回值,类,方法
    @Pointcut("execution(* com.ll.service.*.*(..))")
    public void pointCut() {}

    @Before("pointCut()")
    public void before() {
        System.out.println("LogAspect before");
    }

    @After("pointCut()")
    public void after() {
        System.out.println("LogAspect after");
    }

    @AfterReturning("pointCut()")
    public void afterReturning(){
        System.out.println("LogAspect afterReturning...");
    }

    @AfterThrowing("pointCut()")
    public void afterThrowing(){
        System.out.println("LogAspect afterThrowing...");
    }
    // around会覆盖其他切入注解
//    @Around("pointCut()")
//    public void around(){
//        System.out.println("LogAspect around...");
//    }
}
