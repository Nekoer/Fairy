package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.dto.Architecture
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.KeyScanOptions
import org.springframework.data.redis.core.RedisCallback
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:45
 * @Description
 **/
@Service
@Command("位置", "","查看当前的位置")
class Position : GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val senderInfo = accountService.info(sender)
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
        sb.append("您当前在${pos.now.name}(${Architecture.getArchitectureById(pos.now.architectureId).value})").append("\n")
            .append("上:${pos.top?.name ?: "道路不通"}").append("\n")
            .append("下:${pos.bottom?.name ?: "道路不通"}").append("\n")
            .append("左:${pos.left?.name ?: "道路不通"}").append("\n")
            .append("右:${pos.right?.name ?: "道路不通"}").append("\n")

        if (pos.now.isSafe == AppConstant.WORLD_MAP_SAFE) {
            sb.append("本区域为安全区")
        } else {
            sb.append("本区域不是安全区")
            sb.append("\n").append("怪物:")
            keys?.forEach {
                val id = it.replace(key, "").toInt()
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
