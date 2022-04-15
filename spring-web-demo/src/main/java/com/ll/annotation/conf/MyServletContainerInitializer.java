package com.ll.annotation.conf;

import com.ll.service.MyService;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author liulei
 * @Description
 * @create 2022/3/31 20:42
 */
@HandlesTypes(value = {MyService.class})
public class MyServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        for (Class<?> clz : set) {
            System.out.println("myserivce: " + clz.getName());
        }
        System.out.println(servletContext);
        // 注册组件
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("myHttpServlet", new MyHttpServlet());
        dynamic.addMapping("/liu");

        // 注册监听器
        servletContext.addListener(CreateListener.class);

        // 注册过滤器
        FilterRegistration.Dynamic filter = servletContext.addFilter("createFilter", CreateFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true,"/*");
    }
}
