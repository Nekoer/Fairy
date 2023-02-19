package com.hcyacg.fairy.dto

import com.hcyacg.fairy.entity.ItemType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:25
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serializable
data class AccountItem(
    @SerialName("info")
    val id: Long,
    @SerialName("uin")
    val uin: Long,
    @SerialName("item")
    val item: ItemDTO, // 物品id
    @SerialName("quantity")
    val quantity: Long // 数量
)
@Serializable
data class ItemDTO(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("itemType")
    val itemType: ItemType, //物品种类
    @SerialName("itemUsages")
    val itemUsages: List<ItemUsageDTO>,//物品属性
    @SerialName("description")
    val description: String
)
@Serializable
data class ItemUsageDTO(
    @SerialName("name")
    val name:String,
    @SerialName("attribute")
    val attribute: Long, //buff or debuff 的 值
)
