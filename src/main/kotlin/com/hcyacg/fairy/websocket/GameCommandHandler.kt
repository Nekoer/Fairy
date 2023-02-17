package com.hcyacg.fairy.websocket

import com.hcyacg.fairy.service.AccountService
import com.hcyacg.fairy.service.SignService
import com.hcyacg.fairy.utils.Base64Util
import com.hcyacg.fairy.utils.SkikoUtil
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
class GameCommandHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var accountService: AccountService

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }


    @Autowired
    private lateinit var skikoUtil: SkikoUtil

    @Autowired
    private lateinit var base64Util: Base64Util

    @Autowired
    private lateinit var signService: SignService


    // 分发
    fun distribute(ctx: ChannelHandlerContext, type: String, sender: Long, group: Long, message: String) {
        if (type.contentEquals("group")) {
            val params = mutableMapOf<String, Any?>()
            params["group_id"] = group.toString()
            params["auto_escape"] = false
            params["message"] = null
            when (message) {
                "我要修仙" -> {
                    if (accountService.register(sender)) {
                        val info = accountService.info(sender)
                        params["message"] = "欢迎加入修真界,以下是你的初始信息\n${info?.toMessageString()}"
                    } else {
                        params["message"] = "注册失败,请勿重复注册"
                    }
                }

                "我的修仙信息" -> {
                    val info = accountService.info(sender)

                    if (info != null) {

                        params["message"] = "修士你好,以下是你的信息\n${info.toMessageString()}"
                    } else {
                        params["message"] = "获取个人信息失败,您可能还没进入修真界,请输入: 我要修仙"

                    }
                }
                "签到"->{
                    params["message"] = signService.sign(sender)
                }
                "重入仙途"->{
                    if (accountService.rebirth(sender)){
                        val info = accountService.info(sender)
                        params["message"] = "欢迎加入修真界,以下是你的初始信息\n${info?.toMessageString()}"
                    }else{
                        params["message"] = "重入仙途失败,可能您还未加入修仙之旅"
                    }
                }
                "金银阁"->{}
                "改名xx"->{}
                "突破"->{}
                "直接突破"->{}

                "闭关"->{}
                "出关"->{}
                "灵石出关"->{}

                "排行榜"->{}
                "悬赏令帮助"->{}
                "我的状态"->{}

                "宗门系统"->{}
                "灵庄系统"->{}
                "世界BOSS"->{}
                "我的功法"->{}
                "背包"->{}
                "秘境系统"->{}
                "炼丹帮助"->{}
                /**
                 * 9、送灵石100@xxx,偷灵石@xxx,抢灵石@xxx
                 */

            }
            var data = params["message"].toString()
            if (data.isNotBlank() and !data.equals(null) and (data != "null")){
                //判断是否是base64编码
                data = data.toImageBase64().toString()
                if (data.isBase64()){
                    params["message"] = "[CQ:at,qq=$sender]\n[CQ:image,file=base64://${data}]"
                }else{
                    params["message"] = "[CQ:at,qq=$sender]\n文转图服务异常"
                }
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
        log.debug("文转Base64编码 => {}",this)
        val data = skikoUtil.textToImage(this).makeImageSnapshot().encodeToData()
        return data?.let { base64Util.encodeBytes2Base64(it.bytes) }
    }

    private fun String.isBase64(): Boolean {
        val base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"
        return Pattern.matches(base64Pattern, this)
    }

}
