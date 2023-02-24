package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.dto.Architecture
import com.hcyacg.fairy.dto.MoveDTO
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.KeyScanOptions
import org.springframework.data.redis.core.RedisCallback
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:14
 * @Description
 **/
@Service
@Command("","移动 [上|下|左|右]","移动位置,境界到达一定程度、使用传送石均可瞬移")
class MoveWay: GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val direction = message.replace("移动 ","")
        lateinit var move : MoveDTO
        var senderInfo = accountService.info(sender)
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
        senderInfo = accountService.info(sender)
        val pos = worldMapService.position(senderInfo!!.account.worldMapId)

        val key = "${AppConstant.BOSS_WORLD_MAP}${pos.now.id}:"

        val keys = redisTemplate.execute(RedisCallback<Set<String>> { connection ->
            val keysTmp: MutableSet<String> = HashSet()
            val cursor: Cursor<ByteArray> =
                connection.keyCommands().scan(KeyScanOptions.scanOptions().match("$key*").count(1000).build())

            while (cursor.hasNext()) {
                keysTmp.add(String(cursor.next()))
            }
            return@RedisCallback keysTmp
        })


        val sb = StringBuffer()
        if (move.move){
            sb.append("您已从${move.before.name}(${Architecture.getArchitectureById(move.before.architectureId).value})移动到${move.after.name}(${Architecture.getArchitectureById(move.after.architectureId).value})")
        }else {
            sb.append("非常抱歉,前方道路不通~")
        }
        sb.append("\n")
            .append("上:${pos.top?.name ?: "道路不通"}").append("\n")
            .append("下:${pos.bottom?.name ?: "道路不通"}").append("\n")
            .append("左:${pos.left?.name ?: "道路不通"}").append("\n")
            .append("右:${pos.right?.name ?: "道路不通"}").append("\n")
        if (pos.now.isSafe == AppConstant.WORLD_MAP_SAFE) {
            sb.append("本区域为安全区" )
        }else {
            sb.append("本区域不是安全区")
            sb.append("\n").append("怪物:")
            keys?.forEach {
                val id = it.replace(key,"").toInt()
                val boss = bossService.getById(id)
                sb.append("\n").append("ID:${boss.id} ${boss.level}级${boss.name}")
            }
        }

        return sb.toString()
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
