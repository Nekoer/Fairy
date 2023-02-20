package com.hcyacg.fairy.websocket

import com.hcyacg.fairy.service.AccountPackageService
import com.hcyacg.fairy.service.AccountService
import com.hcyacg.fairy.service.ItemService
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

    @Autowired
    private lateinit var accountPackageService: AccountPackageService

    @Autowired
    private lateinit var itemService: ItemService

    // 分发
    fun distribute(ctx: ChannelHandlerContext, type: String, sender: Long, group: Long, message: String) {
        if (type.contentEquals("group")) {
            val params = mutableMapOf<String, Any?>()
            params["group_id"] = group.toString()
            params["auto_escape"] = false // false 解析cq码 true 不解析cq码
            params["message"] = null
            when {
                message.contentEquals("我要修仙") -> {
                    if (accountService.register(sender)) {
                        val info = accountService.info(sender)
                        params["message"] = "欢迎加入修真界,以下是你的初始信息\n${info?.toMessageString()}"
                    } else {
                        params["message"] = "注册失败,请勿重复注册"
                    }
                }

                message.contentEquals("我的修仙信息") -> {
                    val info = accountService.info(sender)
                    if (info != null) {
                        params["message"] = "修士你好,以下是你的信息\n${info.toMessageString()}"
                    } else {
                        params["message"] = "获取个人信息失败,您可能还没进入修真界,请输入: 我要修仙"
                    }
                }

                message.contentEquals("背包") -> {
                    val packageList = accountPackageService.getPackageList(sender)
                    val sb = StringBuffer()
                    packageList.forEachIndexed { index, accountItem ->
                        sb.append("${accountItem.item.name} ×${accountItem.quantity}")
                        if (packageList.size - 1 != index) {
                            sb.append("\n")
                        }
                    }
                    params["message"] = "你的背包内容如下\n$sb"
                }

                message.contentEquals("签到") -> {
                    params["message"] = signService.sign(sender)
                }

                message.contentEquals("重入仙途") -> {
                    if (accountService.rebirth(sender)) {
                        val info = accountService.info(sender)
                        params["message"] = "欢迎加入修真界,以下是你的初始信息\n${info?.toMessageString()}"
                    } else {
                        params["message"] = "重入仙途失败,可能您还未加入修仙之旅"
                    }
                }

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
                message.contains(Regex("查询物品 [a-zA-Z0-9_\u4e00-\u9fa5]+")) -> {
                    val name = message.replace("查询物品 ", "")
                    val sb = StringBuffer()
                    val list = itemService.info(name)
                    list.forEachIndexed { index, item ->
                        sb.append("ID: ${item.id} 名称: ${item.name} 描述: ${item.description}")
                        if (list.size - 1 != index) {
                            sb.append("\n\r")
                        }
                    }
                    params["message"] = "查询到以下内容: \n$sb"
                }
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
