package com.hcyacg.fairy.server.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.math.BigDecimal

/**
 * @Author Nekoer
 * @Date  2/10/2023 15:42
 * @Description 修真者信息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account")
data class Account(
    @TableId
    val uin: Long,
    val level: Long,
    val exp: Long, // 拥有的经验值
    val health: Long,//生命值
    val mana: Long, //法力
    val attack: Long, //攻击力
    val defensive: Long, //防御力
    val point: BigDecimal //点数 只能充值

)
