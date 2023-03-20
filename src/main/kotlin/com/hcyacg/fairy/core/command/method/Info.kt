package com.hcyacg.fairy.core.command.method

import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 11:44
 * @Description
 **/
@Service
@Command("我的修仙信息","","我的修仙信息 描述: 查看详细数据")
class Info : GameCommandService, DependenceService() {

    override fun group(sender: Long, group: Long, message: String): String {
        val senderInfo = accountService.info(sender)
        return if (senderInfo != null) {
            "修士你好,以下是你的信息\n${senderInfo.toMessageString()}"
        } else {
            "获取个人信息失败,您可能还没进入修真界,请输入: 我要修仙"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
