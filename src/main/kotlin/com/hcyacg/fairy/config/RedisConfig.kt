package com.hcyacg.fairy.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.*
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


/**
 * @Author Nekoer
 * @Date  2/12/2023 19:49
 * @Description
 **/
@Configuration
class RedisConfig {

    @Bean
    @ConditionalOnMissingBean(name = ["redisTemplate"])
    fun redisTemplate(factory: RedisConnectionFactory?): RedisTemplate<String, Any>? {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(factory!!)

        //String的序列化方式
        val stringRedisSerializer = StringRedisSerializer()
        // 使用GenericJackson2JsonRedisSerializer 替换默认序列化(默认采用的是JDK序列化)
        val genericJackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer()

        //key序列化方式采用String类型
        template.keySerializer = stringRedisSerializer
        //value序列化方式采用jackson类型
        template.valueSerializer = genericJackson2JsonRedisSerializer
        //hash的key序列化方式也是采用String类型
        template.hashKeySerializer = stringRedisSerializer
        //hash的value也是采用jackson类型
        template.hashValueSerializer = genericJackson2JsonRedisSerializer
        template.afterPropertiesSet()
        return template
    }


    /**
     * 对hash类型的数据操作
     *
     */
    @Bean
    fun hashOperations(redisTemplate: RedisTemplate<String?, Any?>): HashOperations<String?, String?, Any?>? {
        return redisTemplate.opsForHash()
    }

    /**
     * 对redis字符串类型数据操作
     */
    @Bean
    fun valueOperations(redisTemplate: RedisTemplate<String?, Any?>): ValueOperations<String?, Any?>? {
        return redisTemplate.opsForValue()
    }

    /**
     * 对链表类型的数据操作
     */
    @Bean
    fun listOperations(redisTemplate: RedisTemplate<String?, Any?>): ListOperations<String?, Any?>? {
        return redisTemplate.opsForList()
    }

    /**
     * 对无序集合类型的数据操作
     */
    @Bean
    fun setOperations(redisTemplate: RedisTemplate<String?, Any?>): SetOperations<String?, Any?>? {
        return redisTemplate.opsForSet()
    }

    /**
     * 对有序集合类型的数据操作
     */
    @Bean
    fun zSetOperations(redisTemplate: RedisTemplate<String?, Any?>): ZSetOperations<String?, Any?>? {
        return redisTemplate.opsForZSet()
    }
}
