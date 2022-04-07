package com.ll.service;

/**
 * @author liulei
 * @Description
 * @create 2022/4/1 21:34
 */
public class MyServiceImpl implements MyService{
    @Override
    public String run() {
        System.out.println("run....");
        return "run";
    }
}
