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
 * @Date  2/19/2023 17:50
 * @Description 物品属性 列表
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serializable
@TableName("item_usage")
data class ItemUsage (
    @TableId(type = IdType.AUTO)
    @SerialName("id")
    val id: Long,
    @TableField("item_id")
    @SerialName("itemId")
    val itemId: Long, // 物品id
    @TableField("usage_id")
    @SerialName("usageId")
    val usageId: Long, // buff or debuff
    @SerialName("attribute")
    val attribute: Long, //buff or debuff 的 值
)
