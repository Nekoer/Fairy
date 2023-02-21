package com.hcyacg.fairy.command

import com.hcyacg.fairy.websocket.WsRequest
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.regex.Pattern

/**
 * @Author Nekoer
 * @Date  2/11/2023 21:16
 * @Description 游戏命令中转站
 **/
@Component
@Slf4j
class GameCommandHandler:DependenceService() {
    @Autowired
    lateinit var gameContext: GameContext
    private val log = LoggerFactory.getLogger(this::class.java)

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    // 分发
    fun distribute(ctx: ChannelHandlerContext, type: String, sender: Long, group: Long, message: String) {

        //不是本游戏的命令
        if (!gameContext.isCommand(message)){
            return
        }


        //群消息
        //TODO 可能要解耦
        if (type.contentEquals("group")) {
            val params = mutableMapOf<String, Any?>()
            params["group_id"] = group.toString()
            params["auto_escape"] = false // false 解析cq码 true 不解析cq码

            val gameCommandService = gameContext.getInstance(message)
            gameCommandService?.let {
                val senderInfo = accountService.info(sender)
                if (senderInfo == null && !message.contentEquals("我要修仙")){
                    params["message"] = "您还没进入修真界,请输入: 我要修仙"
                } else {
                    params["message"] = it.group(sender, group, message)
                }
            }


            //TODO 下列没有用了,写完各个命令功能就可以删除，目前作为备份
            when {

                message.contentEquals("金银阁") -> {}
                message.contentEquals("改名xx") -> {}
                message.contentEquals("突破") -> {}
                message.contentEquals("直接突破") -> {}
                message.contentEquals("闭关") -> {}
                message.contentEquals("出关") -> {}
                message.contentEquals("灵石出关") -> {}
                message.contentEquals("排行榜") -> {}
                message.contentEquals("悬赏令帮助") -> {}
                message.contentEquals("我的状态") -> {}
                message.contentEquals("宗门系统") -> {}
                message.contentEquals("灵庄系统") -> {}
                message.contentEquals("世界BOSS") -> {}
                message.contentEquals("我的功法") -> {}
                message.contentEquals("秘境系统") -> {}
                message.contentEquals("炼丹帮助") -> {}
                /**
                 * 9、送灵石100@xxx,偷灵石@xxx,抢灵石@xxx
                 */

            }

            var data = params["message"].toString()
            if (data.isNotBlank() and !data.equals(null) and (data != "null")) {
                //判断是否是base64编码
                data = data.toImageBase64().toString()
                if (data.isBase64()) {
                    params["message"] = "[CQ:at,qq=$sender]\n[CQ:image,file=base64://${data}]"
                } else {
                    params["message"] = "[CQ:at,qq=$sender]\n文转图服务异常"
                }
                //发送数据返回
                ctx.channel().writeAndFlush(
                    TextWebSocketFrame(
                        json.encodeToString(
                            WsRequest(
                                action = "send_group_msg",
                                echo = ctx.channel().id().asLongText(),
                                params = params
                            )
                        )
                    )
                )
            }

        }
    }


    fun String.toImageBase64(): String? {
        log.debug("文转Base64编码 => {}", this)
        val data = skikoUtil.textToImage(null,this).makeImageSnapshot().encodeToData()
        return data?.let { base64Util.encodeBytes2Base64(it.bytes) }
    }

    private fun String.isBase64(): Boolean {
        val base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"
        return Pattern.matches(base64Pattern, this)
    }

}
