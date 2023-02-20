package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/10/2023 17:16
 * @Description 等级体系
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("hierarchical")
data class Hierarchical(
    @TableId
    val level: Long,
    val name: String,
    val exp: Long, //该等级升级所需经验值
    val health: Long,//生命值
    val mana: Long, //法力
    val attack: Long, //攻击力
    val defensive: Long, //防御力

)
