package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor


/**
 * @Author Nekoer
 * @Date  2/24/2023 12:49
 * @Description 功法
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("exercise")
data class Exercise(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String,
    @TableField("health_buff")
    val healthBuff: Double,//增益生命值
    @TableField("mana_buff")
    val manaBuff: Double, //增益法力
    @TableField("attack_buff")
    val attackBuff: Double, //增益攻击力
    @TableField("defensive_buff")
    val defensiveBuff: Double, //增益防御力
    @TableField("max_level")
    val maxLevel: Long, //最高升级等级
)
