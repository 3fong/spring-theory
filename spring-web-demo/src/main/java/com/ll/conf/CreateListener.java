package com.ll.conf;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author liulei
 * @Description
 * @create 2022/4/1 22:04
 */
public class CreateListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("CreateListener contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("CreateListener contextDestroyed");
    }
}
