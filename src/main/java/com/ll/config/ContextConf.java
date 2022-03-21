package com.ll.config;

import com.ll.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.ll")
@EnableAspectJAutoProxy
public class ContextConf {

    @Bean
    public Car car(){
        return new Car("congaroo");
    }
}
