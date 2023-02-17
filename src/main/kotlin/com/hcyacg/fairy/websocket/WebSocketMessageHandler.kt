package com.hcyacg.fairy.websocket

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import io.netty.util.concurrent.GlobalEventExecutor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.InetSocketAddress
import java.util.*


/**
 * @Author Nekoer
 * @Date  2/11/2023 12:14
 * @Description
 **/
@Slf4j
@ChannelHandler.Sharable
@Component
class WebSocketMessageHandler : SimpleChannelInboundHandler<WebSocketFrame>() {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
//    @Volatile
//    private var asyncId: Long = 0
//
//    @Synchronized
//    internal fun nextAsyncId(): Long {
//        asyncId += 1
//        val new = asyncId
//        if (new > 1000000) {
//            asyncId = Random().nextLong(1060000)
//        }
//        return new
//    }
    companion object {
        var users: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    }

    @Autowired
    private lateinit var gameCommandHandler : GameCommandHandler


    override fun channelRead0(ctx: ChannelHandlerContext?, msg: WebSocketFrame?) {
        val textWebSocketFrame = msg as TextWebSocketFrame
        //该操作是为了使用textWebSocketFrame后不会 refCnt -1 导致 变为0 触发报错
        textWebSocketFrame.retain()
        log.info("服务器端收到消息: {}", textWebSocketFrame.text())
    }

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        cause?.printStackTrace()
        ctx?.close()
        log.error("异常信息：rn " + cause?.message)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        //获取每个用户端连接的ip
        val ipSocket: InetSocketAddress = ctx.channel().remoteAddress() as InetSocketAddress
        val clientIp: String = ipSocket.address.hostAddress
        log.info("获取到客户端连接：$clientIp")
        log.debug("客户端被添加，channelId为：" + ctx.channel().id().asLongText())
        users.add(ctx.channel())
        //ctx.channel().writeAndFlush(TextWebSocketFrame(json.encodeToString(VerifyRequest(syncId = nextAsyncId().toString(),command="verify",  content = Content(sessionKey = "UnVerifiedSession", qq = 2098723536, verifyKey = "huangzhiwen")))))
        //users.writeAndFlush(TextWebSocketFrame(json.encodeToString(VerifyRequest(syncId = nextAsyncId().toString(),command="verify",  content = Content(sessionKey = "UnVerifiedSession", qq = 2098723536, verifyKey = "huangzhiwen")))))

    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val ipSocket = ctx.channel().remoteAddress() as InetSocketAddress
        val clientIp = ipSocket.address.hostAddress
        log.info("客户端连接关闭：$clientIp")
        val channelId = ctx.channel().id().asLongText()
        log.debug("客户端被移除，channelId为：$channelId")

        // 当触发handlerRemoved，ChannelGroup会自动移除对应的客户端channel
        users.remove(ctx.channel())
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is FullHttpRequest) {
            msg.headers().add("Authorization", "Token fairy")
        }

        val textWebSocketFrame = msg as TextWebSocketFrame
        val data = json.parseToJsonElement(textWebSocketFrame.text())
        val postType = data.jsonObject["post_type"]?.jsonPrimitive?.content
        postType?.let {
            when (it) {
                //通知上报
                "notice" -> {}
                //元事件上报
                "meta_event" -> {}
                //消息上报
                "message" -> {
                    val messageType = data.jsonObject["message_type"]?.jsonPrimitive?.content
                    messageType?.let { type ->
                        when (type) {
                            "private" -> {

                            }
                            "group" -> {
                                val message = data.jsonObject["message"]?.jsonPrimitive?.content
                                val sender = data.jsonObject["user_id"]?.jsonPrimitive?.long
                                val group = data.jsonObject["group_id"]?.jsonPrimitive?.long

                                group?.let {
                                    sender?.let {
                                        message?.let {
                                            gameCommandHandler.distribute(ctx,type,sender,group,message)
                                        }
                                    }
                                }

                            }
                            "guild" -> {

                            }

                            else -> {}
                        }
                    }
                }
                "message_sent" -> {}
                //请求上报
                "request" -> {}
                else -> {}
            }
        }


        super.channelRead(ctx, msg)
    }
}
