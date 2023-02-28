package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:13
 * @Description
 **/
@Service
@Command("重入仙途","","重入仙途 描述: 删号")
class ReEnterThePathOfImmortality: GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val senderInfo = accountService.info(sender)
        return if (accountService.rebirth(senderInfo!!.account.id)) {
            "您的账号已注销,可以重新加入修仙之旅了"
        } else {
            "重入仙途失败,可能您还未加入修仙之旅"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
