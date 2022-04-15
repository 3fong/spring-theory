package com.ll.biz.controller;

import com.ll.biz.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liulei
 * @Description
 * @create 2022/4/11 21:15
 */
@RestController
public class HelloController {
    @Autowired
    private MyService myService;

    @GetMapping("/hello")
    public String hello() {
        String run = myService.run();
        System.out.println("controller "+run);
        return run;
    }
}
