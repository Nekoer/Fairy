package com.hcyacg.fairy.core.command

import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.utils.SpringBeanUtil
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.aop.support.AopUtils
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
    private val commandType: MutableList<String> = mutableListOf()
    private val commandRegex: MutableList<String> = mutableListOf()
    private val adminCommandList: MutableList<Command> = mutableListOf()
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
                var clazz: Class<*> = bean.javaClass
                log.debug("clazz -> {}",clazz.name)
                var serviceType: Command?
                try {
                    serviceType = clazz.getAnnotation(Command::class.java)

                    if (serviceType.command.isNotBlank()) {
                        commandType.add(serviceType.command)
                        AppConstant.COMMAND_DESCRIPTION[serviceType.command] = serviceType.description
                    }
                    if (serviceType.regex.isNotBlank()) {
                        commandRegex.add(serviceType.regex)
                        AppConstant.COMMAND_DESCRIPTION[serviceType.regex.split(" ")[0]] = serviceType.description
                    }

                    if (serviceType.isAdmin){
                        adminCommandList.add(serviceType)
                    }


                    serviceMap[serviceType] = bean as GameCommandService
                }catch (_:Exception){
                    //获取JDK动态代理对象
                    clazz = AopUtils.getTargetClass(bean)
                    serviceType = clazz.getAnnotation(Command::class.java)

                    if (serviceType.command.isNotBlank()) {
                        commandType.add(serviceType.command)
                        AppConstant.COMMAND_DESCRIPTION[serviceType.command] = serviceType.description
                    }
                    if (serviceType.regex.isNotBlank()) {
                        commandRegex.add(serviceType.regex)
                        AppConstant.COMMAND_DESCRIPTION[serviceType.regex.split(" ")[0]] = serviceType.description
                    }


                    serviceMap[serviceType] = bean as GameCommandService
                }
            }
    }

    /**
     * 取策略
     *
     * @param type 策略类型
     * @return 策略
     */

    fun getInstance(type: String): GameCommandService? {
        var service: GameCommandService? = null
        serviceMap.forEach { (t, u) ->
            log.debug("command: {},regex : {}", t.command, t.regex)
            if (t.regex.isNotBlank()) {
                if (Regex(t.regex).matches(type)) {
                    service = u
                }
            } else {
                if (t.command.contentEquals(type)) {
                    service = u
                }
            }
        }

        return service ?: throw RuntimeException("can not find game command")
    }

    fun isCommand(command: String): Boolean {
        if (commandType.contains(command)) {
            return true
        }
        commandRegex.forEach {
            if (Regex(it).matches(command)) {
                return true
            }
        }
        return false
    }

    fun isNeedAdmin(command: String):Boolean{
        if (commandType.contains(command)) {
            return true
        }
        commandRegex.forEach {
            if (Regex(it).matches(command)) {
                return true
            }
        }
        return false
    }
}
