package com.hcyacg.fairy.server.websocket

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @Author Nekoer
 * @Date  2/11/2023 12:08
 * @Description
 **/


@Component
class NettyBootstrapRunner : ApplicationRunner, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Autowired
    private lateinit var nettyConfig: NettyConfig

    private var applicationContext: ApplicationContext? = null
    private var serverChannel: Channel? = null


    override fun run(args: ApplicationArguments) {
        println(nettyConfig.ip)
        //连接处理group
        val bossGroup: EventLoopGroup = NioEventLoopGroup()
        //事件处理group
        val workerGroup: EventLoopGroup = NioEventLoopGroup()
        try {

            //创建ServerBootstrap实例
            val serverBootstrap = ServerBootstrap()
            //tcp 缓冲区
            serverBootstrap
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 1024) //绑定处理group
                .group(bossGroup, workerGroup) //设置并绑定服务端channel
                .channel(NioServerSocketChannel::class.java) //设置ws工作IP及端口
                .localAddress(InetSocketAddress(nettyConfig.ip, nettyConfig.port)) //处理新连接，并设置客户端连接socket属性
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        //增加任务处理
                        val pipeline: ChannelPipeline = ch.pipeline()
                        pipeline.addLast(HttpServerCodec())
                        //支持写大数据流
                        pipeline.addLast(ChunkedWriteHandler())
                        //string对象自动编码
//                            pipeline.addLast("encoder",new StringEncoder());
//                            pipeline.addLast("decoder",new StringDecoder());
                        //http聚合器
                        pipeline.addLast(HttpObjectAggregator(65536))

                        pipeline.addLast(WebSocketServerCompressionHandler())
                        pipeline.addLast(IdleStateHandler(8, 10, 12))
                        pipeline.addLast(HeartBeatHandler())
                        //websocket支持，设置路由
                        pipeline.addLast(WebSocketServerProtocolHandler(nettyConfig.path, null, true, nettyConfig.maxFrameSize))

                        //从IOC中获取到Handler
                        pipeline.addLast(applicationContext!!.getBean(WebSocketMessageHandler::class.java))
                    }
                })
            //跑起来
            val channel: Channel = serverBootstrap.bind().sync().channel()
            serverChannel = channel
            log.info("websocket服务启动,port={}", nettyConfig.port)
            //等待监听端口关闭
            channel.closeFuture().sync()
        }catch (e:Exception){
            e.printStackTrace()
        } finally {
            //释放线程池资源
            bossGroup.shutdownGracefully().sync()
            workerGroup.shutdownGracefully().sync()
        }
    }

    override fun onApplicationEvent(event: ContextClosedEvent) {
        serverChannel?.let {
            it.close()
            log.info("websocket服务停止");
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext;
    }
}
