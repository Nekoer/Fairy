package com.hcyacg.fairy.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/20/2023 21:08
 * @Description 建筑种类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
enum class Architecture(
    val id:Long,
    val value: String
){
    /**
     * 不允许修改id id可以看作主键
     */
    UNKNOWN(-1, "未知"),
    BANK(1,"钱庄"),
    HOTEL(2,"旅馆"),
    TRANSFER_POINT(3,"传送点"),
    SPAWN_POINT(4,"刷怪点"),
    AUCTION_HOUSE(5,"拍卖场")
    ;

    companion object {
        private val codeMap = values().associateBy { it.id }
        fun getArchitectureById(id: Long): Architecture {
            return codeMap[id] ?: UNKNOWN
        }
    }
}
