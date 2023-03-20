package com.hcyacg.fairy.dto

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
enum class ItemType(
    val id: Long,
    val value: String,
    val description: String
){
    UNKNOWN(-1, "未知","未知"),
    MATERIAL(1,"素材","可合成装备、武器"),
    PROP(2,"道具","道具"),
    CLOTHES(3,"衣服","衣服"),
    TROUSERS(4,"裤子","裤子"),
    HELMET(5,"头盔","头盔"),
    BOOTS(6,"靴子","靴子"),
    ORNAMENT(7,"饰品","饰品"),
    WEAPON(8,"武器","武器"),
    MEDICINAL(9,"药材","药材"),

    ;
    companion object {
        private val codeMap = ItemType.values().associateBy { it.id }
        fun getItemTypeById(id: Long): ItemType {
            return codeMap[id] ?: UNKNOWN
        }
    }
}
