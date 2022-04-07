package com.ll.config;

import com.ll.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

@Configuration
@ComponentScan("com.ll")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class ContextConf {

    @Bean
    public Car car(){
        return new Car("congaroo");
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
               System.out.println("getTransaction..............");
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus transactionStatus) throws TransactionException {
                System.out.println("commit..............");
            }

            @Override
            public void rollback(TransactionStatus transactionStatus) throws TransactionException {
                System.out.println("rollback..............");
            }
        };
    }
}
