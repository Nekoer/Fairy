package com.hcyacg.fairy.utils

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * @Author Nekoer
 * @Date  2/21/2023 10:44
 * @Description
 **/
@Component
class SpringBeanUtil: ApplicationContextAware {
    private lateinit var applicationContext: ApplicationContext
    fun getApplicationContext(): ApplicationContext {
        return applicationContext
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}
