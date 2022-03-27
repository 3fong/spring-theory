package com.ll.context;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author liulei
 * @Description
 * @create 2022/3/26 17:42
 */
//@Component
public class MyContextCloseEvent implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("ApplicationListener ............"+event);
    }
}
