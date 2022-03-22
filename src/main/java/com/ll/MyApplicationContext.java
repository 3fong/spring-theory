package com.ll;

import com.ll.bean.Car;
import com.ll.config.ContextConf;
import com.ll.service.CarService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyApplicationContext {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConf.class);
        Car bean = context.getBean(Car.class);
        System.out.println(bean);
        CarService carService = context.getBean(CarService.class);
        String run = carService.run();
        System.out.println(run);
    }
}
