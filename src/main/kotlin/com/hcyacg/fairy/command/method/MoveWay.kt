package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.dto.MoveDTO
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:14
 * @Description
 **/
@Service
@Command("","移动 [上|下|左|右]")
class MoveWay: GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val direction = message.replace("移动 ","")
        lateinit var move : MoveDTO
        val senderInfo = accountService.info(sender)
        when(direction){
            "上" -> {
                move = moveService.moveToTop(senderInfo!!.account.id)
            }
            "下" -> {
                move = moveService.moveToBottom(senderInfo!!.account.id)
            }
            "左" -> {
                move = moveService.moveToLeft(senderInfo!!.account.id)
            }
            "右" -> {
                move = moveService.moveToRight(senderInfo!!.account.id)
            }
        }
        val pos = worldMapService.position(senderInfo!!.account.worldMapId)
        return if (move.move){
            "您已从${move.before.name}移动到${move.after.name}".plus("\n")
                .plus("上:${pos.top?.name ?: "道路不通"}").plus("\n")
                .plus("下:${pos.bottom?.name ?: "道路不通"}").plus("\n")
                .plus("左:${pos.left?.name ?: "道路不通"}").plus("\n")
                .plus("右:${pos.right?.name ?: "道路不通"}").plus("\n")
                .plus(if (pos.now.isSafe == AppConstant.WORLD_MAP_SAFE) "本区域为安全区" else "本区域不是安全区")
        }else {
            "非常抱歉,前方道路不通~".plus("\n")
                .plus("上:${pos.top?.name ?: "道路不通"}").plus("\n")
                .plus("下:${pos.bottom?.name ?: "道路不通"}").plus("\n")
                .plus("左:${pos.left?.name ?: "道路不通"}").plus("\n")
                .plus("右:${pos.right?.name ?: "道路不通"}").plus("\n")
                .plus(if (pos.now.isSafe == AppConstant.WORLD_MAP_SAFE) "本区域为安全区" else "本区域不是安全区")

        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
