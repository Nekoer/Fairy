package com.hcyacg.fairy.websocket

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory


/**
 * @Author Nekoer
 * @Date  2/11/2023 12:12
 * @Description
 **/
@Slf4j
class HeartBeatHandler: ChannelInboundHandlerAdapter() {
    private val log = LoggerFactory.getLogger("心跳")
    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲）
        if (evt is IdleStateEvent) {
            // 强制类型转换
            if (evt.state() === IdleState.READER_IDLE) {
                log.debug("进入读空闲...")
            } else if (evt.state() === IdleState.WRITER_IDLE) {
                log.debug("进入写空闲...")
            } else if (evt.state() === IdleState.ALL_IDLE) {
                log.debug("进入读写空闲...")
                log.debug("channel关闭前users数量为：" + WebSocketMessageHandler.users.size)
                val channel: Channel = ctx!!.channel()
                //关闭无用的channel，以防资源浪费
                channel.close()
                log.debug("channel关闭后users数量为：" + WebSocketMessageHandler.users.size)
            }
        }
    }
}
