package com.ll.service;

import org.springframework.stereotype.Service;

@Service
public class CarService {

    public String run(){
        System.out.println("i am running");
        return "run";
    }
}
