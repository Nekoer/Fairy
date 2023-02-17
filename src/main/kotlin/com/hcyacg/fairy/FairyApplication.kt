package com.hcyacg.fairy


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootApplication
@ServletComponentScan
@ConfigurationPropertiesScan
@EnableTransactionManagement
@EnableConfigurationProperties
class FairyApplication : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(FairyApplication::class.java)
    }

}

fun main(args: Array<String>) {
    runApplication<FairyApplication>(*args)
}


