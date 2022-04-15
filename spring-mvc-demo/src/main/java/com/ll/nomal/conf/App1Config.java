package com.ll.nomal.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * @author liulei
 * @Description spring mvc配置类
 *              useDefaultFilters = false 必须禁用默认过滤器,才能使自定义过滤规则生效
 * @create 2022/4/11 20:51
 */
@ComponentScan(basePackages="com.ll.biz",includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})}
        ,useDefaultFilters = false)
public class App1Config {
}
