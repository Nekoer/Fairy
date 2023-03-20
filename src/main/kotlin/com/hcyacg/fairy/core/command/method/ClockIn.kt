package com.hcyacg.fairy.core.command.method

import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 11:51
 * @Description
 **/
@Service
@Command("签到","","签到 描述: 每日签到会奖励物品或灵石")
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
