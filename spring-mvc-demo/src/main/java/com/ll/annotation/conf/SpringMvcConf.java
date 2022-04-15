package com.ll.annotation.conf;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liulei
 * @Description WebMvcConfigurer 是springmvc定制扩展类
 * @create 2022/4/11 20:19
 */
@EnableWebMvc
public class SpringMvcConf implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }
}
