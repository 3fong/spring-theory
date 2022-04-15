package com.ll.biz.service;

import org.springframework.stereotype.Service;

/**
 * @author liulei
 * @Description
 * @create 2022/4/1 21:34
 */
@Service
public class MyServiceImpl implements MyService{
    @Override
    public String run() {
        System.out.println("run....");
        return "run";
    }
}
