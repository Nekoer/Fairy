package com.hcyacg.fairy.task

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.entity.AccountFactionMission
import com.hcyacg.fairy.websocket.WebSocketMessageHandler
import com.hcyacg.fairy.websocket.WsRequest
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import kotlinx.serialization.encodeToString
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @Author Nekoer
 * @Date  2/27/2023 21:11
 * @Description
 **/
@Component
class FactionMissionTask: DependenceService() {
    private val log =  LoggerFactory.getLogger(this::class.java)

    //每天凌晨0点清空所有除了进行中的宗门任务
    @Scheduled(cron = "0 0 0/24 1/1 * ?")
    fun clearFactionMission(){
        WebSocketMessageHandler.groups.forEach {
            val params = mutableMapOf<String, Any?>()
            params["group_id"] = it.toString()
            params["auto_escape"] = false // false 解析cq码 true 不解析cq码
            params["message"] = "[CQ:image,file=base64://${"系统提示：正在重置玩家宗门任务".toImageBase64()}]"

            accountFactionMissionService.remove(QueryWrapper<AccountFactionMission>().eq("status",AppConstant.FACTION_MISSION_STATE_SUCCESS))
            accountFactionMissionService.remove(QueryWrapper<AccountFactionMission>().eq("status", AppConstant.FACTION_MISSION_STATE_CANEL))


            log.debug(params["message"].toString())
            WebSocketMessageHandler.users.writeAndFlush(
                TextWebSocketFrame(json.encodeToString(
                WsRequest(
                    action = "send_group_msg",
                    echo = "system",
                    params = params
                )
            ))
            )
        }
    }

    fun String.toImageBase64(): String? {
        log.debug("文转Base64编码 => {}", this)
        val data = skikoUtil.textToImage(null,this).makeImageSnapshot().encodeToData()
        return data?.let { base64Util.encodeBytes2Base64(it.bytes) }
    }
}
