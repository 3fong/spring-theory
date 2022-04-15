package com.ll.nomal.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * @author liulei
 * @Description spring配置类 用于加载数据源和中间件 这里为了和springmvc配置区别,不扫描controller
 * @create 2022/4/11 20:51
 */
@ComponentScan(basePackages="com.ll.biz",excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})})
public class RootConfig {
}
