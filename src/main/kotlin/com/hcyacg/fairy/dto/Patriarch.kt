package com.hcyacg.fairy.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  3/17/2023 18:19
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
enum class Patriarch(
    val id: Long,
    val value: String,
) {
    UNKNOWN(-1, "未知"),
    SUZERAIN(1,"宗主"),
    ELDER(2,"长老"),
    DISCIPLE(3,"弟子"),
    ;

    companion object {
        private val codeMap = Patriarch.values().associateBy { it.id }
        fun getPatriarchById(id: Long): Patriarch {
            return codeMap[id] ?: Patriarch.UNKNOWN
        }
    }

}
