package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/20/2023 20:05
 * @Description boss的buff 、属性
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serializable
@TableName("boss_usage")
data class BossUsage (
    @TableId(type = IdType.AUTO)
    @SerialName("id")
    val id: Long,
    @TableField("boss_id")
    @SerialName("bossId")
    val bossId: Long, // 物品id
    @TableField("usage_id")
    @SerialName("usageId")
    val usageId: Long, // buff or debuff
    @SerialName("attribute")
    val attribute: Long, //buff or debuff 的 值
)
