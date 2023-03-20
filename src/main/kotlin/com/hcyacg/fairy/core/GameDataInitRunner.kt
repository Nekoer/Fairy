package com.hcyacg.fairy.core

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @Author Nekoer
 * @Date  3/14/2023 12:13
 * @Description 游戏数据初始化
 **/
@Order(1)
@Component
class GameDataInitRunner: ApplicationRunner , DependenceService(){
    override fun run(args: ApplicationArguments?) {

    }
}
