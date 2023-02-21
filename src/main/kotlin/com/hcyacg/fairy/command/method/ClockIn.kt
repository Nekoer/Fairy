package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 11:51
 * @Description
 **/
@Service
@Command("签到","")
class ClockIn : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val senderInfo = accountService.info(sender)
        return signService.sign(senderInfo!!.account.id)
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
