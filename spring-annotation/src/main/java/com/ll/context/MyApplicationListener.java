package com.ll.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;

/**
 * @author liulei
 * @Description
 * @create 2022/3/27 9:32
 */
@Component
public class MyApplicationListener {
    @EventListener
    public ApplicationEvent[] eventListener(ContextRefreshedEvent event){
        System.out.println("i am a eventListener.................." );
        ApplicationContext context = event.getApplicationContext();
        return new ApplicationEvent[] {new ContextStartedEvent(context),new ContextStoppedEvent(context)};
    }

    @EventListener
    public void listener(ApplicationEvent event){
        System.out.println("i am a listener.................." +event);
    }
}
