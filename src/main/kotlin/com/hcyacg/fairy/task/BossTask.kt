package com.hcyacg.fairy.task

import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.websocket.WebSocketMessageHandler
import com.hcyacg.fairy.websocket.WsRequest
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import kotlinx.serialization.encodeToString
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.KeyScanOptions
import org.springframework.data.redis.core.RedisCallback
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


/**
 * @Author Nekoer
 * @Date  2/21/2023 13:57
 * @Description
 **/
@Component
class BossTask: DependenceService() {

    private val log =  LoggerFactory.getLogger(this::class.java)
    /**
     * 每10分钟在地图上生成一个怪物
     */
    @Scheduled(cron = "* 0/10 * * * ? ")
    fun generateBoss(){
        val boss = bossService.randomBoss()
        val list = worldMapService.getUnSafeWorldMap()
        val num = list.indices
        val worldMap = list[num.random()]
        val key = "${AppConstant.BOSS_WORLD_MAP}${worldMap.id}:"

        val keys = redisTemplate.execute(RedisCallback<Set<String>> { connection ->
            val keysTmp: MutableSet<String> = HashSet()
            val cursor: Cursor<ByteArray> =
                connection.keyCommands().scan(KeyScanOptions.scanOptions().match("$key*").count(1000).build())

            while (cursor.hasNext()) {
                keysTmp.add(String(cursor.next()))
            }
            return@RedisCallback keysTmp
        })

        if (keys == null){
            redisUtil["$key${boss.id}"] = 1
            redisUtil.expire("$key${boss.id}",10 * 60)


            val params = mutableMapOf<String, Any?>()

            WebSocketMessageHandler.groups.forEach {
                params["group_id"] = it.toString()
                params["auto_escape"] = false // false 解析cq码 true 不解析cq码

                params["message"] = "[CQ:image,file=base64://${"世界提示：在${worldMap.name}出现了一只${boss.level}级${boss.name}".toImageBase64()}]"
                log.debug(params["message"].toString())
                WebSocketMessageHandler.users.writeAndFlush(TextWebSocketFrame(json.encodeToString(
                    WsRequest(
                        action = "send_group_msg",
                        echo = "system",
                        params = params
                    )
                )))
            }

        }else{
            //如果在该位置找到已生成boss
            if (!keys.contains("$key${boss.id}")){
                redisUtil["$key${boss.id}"] = 1
                redisUtil.expire("$key${boss.id}",10 * 60)


                val params = mutableMapOf<String, Any?>()

                WebSocketMessageHandler.groups.forEach {
                    params["group_id"] = it.toString()
                    params["auto_escape"] = false // false 解析cq码 true 不解析cq码

                    params["message"] = "[CQ:image,file=base64://${"世界提示：在${worldMap.name}出现了一只${boss.level}级${boss.name}".toImageBase64()}]"
                    log.debug(params["message"].toString())
                    WebSocketMessageHandler.users.writeAndFlush(TextWebSocketFrame(json.encodeToString(
                        WsRequest(
                            action = "send_group_msg",
                            echo = "system",
                            params = params
                        )
                    )))
                }


            }
        }



    }
    fun String.toImageBase64(): String? {
        log.debug("文转Base64编码 => {}", this)
        val data = skikoUtil.textToImage(null,this).makeImageSnapshot().encodeToData()
        return data?.let { base64Util.encodeBytes2Base64(it.bytes) }
    }
}
