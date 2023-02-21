package com.hcyacg.fairy.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler


/**
 * @Author Nekoer
 * @Date  2/21/2023 15:27
 * @Description
 **/
@Configuration
@EnableScheduling
class SchedulerConfig {
    @Bean
    fun taskScheduler(): TaskScheduler? {
        val scheduler = ThreadPoolTaskScheduler()
        //线程池大小
        scheduler.poolSize = 10
        //线程名字前缀
        scheduler.setThreadNamePrefix("spring-task-thread")
        return scheduler
    }
}
