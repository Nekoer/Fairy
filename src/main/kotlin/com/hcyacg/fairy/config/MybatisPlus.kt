package com.hcyacg.fairy.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * @Author Nekoer
 * @Date  2/8/2023 19:01
 * @Description
 **/
// MybatisPlus 配置注入springboot
@Configuration
@EnableTransactionManagement
@MapperScan("com.hcyacg.fairy.mapper") // 注入mybatis-plus到spring容器中
class MybatisPlus {

    /**
     * mybatis-plus分页插件, 老版本已经失效
     */
    @Bean
    fun paginationInnerInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
        return interceptor
    }

    /**
     * 乐观锁mybatis插件
     */
    @Bean
    fun optimisticLockerInnerInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(OptimisticLockerInnerInterceptor())
        return interceptor
    }
}
