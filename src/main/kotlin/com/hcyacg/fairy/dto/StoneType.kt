package com.hcyacg.fairy.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/10/2023 16:46
 * @Description 货币的种类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
enum class StoneType(
    val id: Long,
    val value: String
){
    UNKNOWN(-1, "未知",),
    SPIRIT_STONE(1, "灵石"),
    FAIRY_STONE(2, "仙石");

    companion object {
        private val codeMap = StoneType.values().associateBy { it.id }
        fun getStoneTypeById(id: Long): StoneType {
            return codeMap[id] ?: UNKNOWN
        }
    }
}
