package com.hcyacg.fairy.server.websocket

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Configuration

/**
 * @Author Nekoer
 * @Date  2/11/2023 13:31
 * @Description
 **/
@ConfigurationProperties(prefix = "netty.websocket")
data class NettyConfig @ConstructorBinding constructor(
    val port: Int,
    val ip: String,
    val path: String,
    val maxFrameSize: Long
)
