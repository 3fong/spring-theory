package com.ll.nomal.conf;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author liulei
 * @Description
 * @create 2022/4/11 20:51
 */
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        System.out.println("RootConfig...........................");
        return new Class<?>[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        System.out.println("App1Config...........................");
        return new Class<?>[] { App1Config.class };
    }

    /**
     * servlet映射: /: 拦截除jsp之外的所有静态资源.jsp由tomcat处理
     *             /*:拦截所有静态请求,包括jsp
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        System.out.println("getServletMappings...........................");
        return new String[] { "/" };
    }
}
