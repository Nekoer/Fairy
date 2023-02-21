package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:45
 * @Description
 **/
@Service
@Command("位置","")
class Position: GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val senderInfo = accountService.info(sender)
        val pos = worldMapService.position(senderInfo!!.account.worldMapId)
        return "您当前在${pos.now.name}".plus("\n")
            .plus("上:${pos.top?.name ?: "道路不通"}").plus("\n")
            .plus("下:${pos.bottom?.name ?: "道路不通"}").plus("\n")
            .plus("左:${pos.left?.name ?: "道路不通"}").plus("\n")
            .plus("右:${pos.right?.name ?: "道路不通"}")
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
