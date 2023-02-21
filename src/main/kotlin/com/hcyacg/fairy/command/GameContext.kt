package com.hcyacg.fairy.command

import com.hcyacg.fairy.utils.SpringBeanUtil
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*


/**
 * @Author Nekoer
 * @Date  2/21/2023 10:47
 * @Description
 **/
@Component
@Slf4j
class GameContext {
    private val serviceMap: MutableMap<Command, GameCommandService> = HashMap<Command, GameCommandService>()
    private val commandType:MutableList<String> = mutableListOf()
    private val commandRegex:MutableList<String> = mutableListOf()
    private val log = LoggerFactory.getLogger(this::class.java)
    /**
     * 开局将所有策略注入到hashmap中
     *
     * @param springBeanUtil SpringBeanUtil
     */
    @Autowired
    fun GameContext(springBeanUtil: SpringBeanUtil) {
        val rpcBeans = springBeanUtil.getApplicationContext().getBeanNamesForAnnotation(
            Command::class.java
        )
        Arrays.stream(rpcBeans).toList()
            .forEach { name ->
                log.debug("name:{}", name)
                val bean = springBeanUtil.getApplicationContext().getBean(name)
                val clazz: Class<*> = bean.javaClass
                val serviceType: Command = clazz.getAnnotation(Command::class.java)

                if (serviceType.type.isNotBlank()){
                    commandType.add(serviceType.type)
                }
                if (serviceType.regex.isNotBlank()){
                    commandRegex.add(serviceType.regex)
                }
                serviceMap[serviceType] = bean as GameCommandService
            }
    }

    /**
     * 取策略
     *
     * @param type 策略类型
     * @return 策略
     */

    fun getInstance(type: String): GameCommandService? {
        var service : GameCommandService? = null
        serviceMap.forEach { (t, u) ->
            log.debug("type: {},regex : {}",t.type,t.regex)
            if (t.regex.isNotBlank()){
                if (Regex(t.regex).matches(type)){
                    service = u
                }
            }else{
                if (t.type.contentEquals(type)){
                    service = u
                }
            }
        }

        return service ?: throw RuntimeException("can not find game command")
    }

    fun isCommand(command: String):Boolean{
        if (commandType.contains(command)){
            return true
        }
        commandRegex.forEach {
            if (Regex(it).matches(command)){
                return true
            }
        }
        return false
    }
}
