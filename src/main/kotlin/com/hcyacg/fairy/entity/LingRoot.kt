package com.hcyacg.fairy.server.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/10/2023 17:08
 * @Description 灵根
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ling_root")
data class LingRoot(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String, //灵根名
    @TableField("gold_multiplier")
    val goldMultiplier: Float, //金灵根修炼的倍率
    @TableField("wood_multiplier")
    val woodMultiplier: Float,//木灵根修炼的倍率
    @TableField("water_multiplier")
    val waterMultiplier: Float,//水灵根修炼的倍率
    @TableField("fire_multiplier")
    val fireMultiplier: Float,//火灵根修炼的倍率
    @TableField("soil_multiplier")
    val soilMultiplier: Float,//土灵根修炼的倍率
)
