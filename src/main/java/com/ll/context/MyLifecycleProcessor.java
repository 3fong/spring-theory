package com.ll.context;

import org.springframework.context.LifecycleProcessor;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.stereotype.Component;

/**
 * @author liulei
 * @Description spring 声明周期实现 由于生命周期定义是按id="lifecycleProcessor"获取,
 *                      否则会直接新建默认声明周期: DefaultLifecycleProcessor
 * @create 2022/3/27 16:35
 */
//@Component("lifecycleProcessor")
public class MyLifecycleProcessor implements LifecycleProcessor {
    @Override
    public void onRefresh() {
        System.out.println("MyLifecycleProcessor onRefresh ");
    }

    @Override
    public void onClose() {
        System.out.println("MyLifecycleProcessor onClose ");
    }

    @Override
    public void start() {
        System.out.println("MyLifecycleProcessor start ");
    }

    @Override
    public void stop() {
        System.out.println("MyLifecycleProcessor stop ");
    }

    @Override
    public boolean isRunning() {
        System.out.println("MyLifecycleProcessor isRunning ");
        return false;
    }
}
