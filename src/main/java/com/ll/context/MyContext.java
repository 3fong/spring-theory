package com.ll.context;

import com.ll.bean.Car;
import com.ll.config.ContextConf;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyContext {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConf.class);
        Car bean = context.getBean(Car.class);
        System.out.println(bean);
    }
}
