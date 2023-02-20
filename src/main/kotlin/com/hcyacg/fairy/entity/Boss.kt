package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/20/2023 19:51
 * @Description 怪物
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("boss")
data class Boss(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String,
    val level: Long,//等级
    val health: Long,//生命值
    val mana: Long, //法力
    val attack: Long, //攻击力
    val defensive: Long, //防御力
)
