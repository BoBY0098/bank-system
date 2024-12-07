package com.banksystem.config.threadpool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public static ExecutorService executorService() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = coreCount * 2;
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
