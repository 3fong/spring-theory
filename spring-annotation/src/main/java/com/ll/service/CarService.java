package com.ll.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarService {

    @Transactional
    public String run(){
        System.out.println("i am running");
//        System.out.println("i am running"+1/0);
        return "run";
    }

    public void rollbackFunc() {
        System.out.println("i am rollback..............................");
    }
}
