package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 10:43
 * @Description
 **/
@Service
@Command("我要修仙","")
class Register : GameCommandService, DependenceService() {


    override fun group(sender: Long, group: Long, message: String): String {

        return if (accountService.register(sender)) {
            val info = accountService.info(sender)
            "欢迎加入修真界,以下是你的初始信息\n${info?.toMessageString()}"
        } else {
            "注册失败,请勿重复注册"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }

}