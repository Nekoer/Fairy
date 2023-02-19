package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/19/2023 16:43
 * @Description 物品种类 例如： 装备 道具 素材
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serializable
@TableName("item_type")
data class ItemType(
    @TableId(type = IdType.AUTO)
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String
)
