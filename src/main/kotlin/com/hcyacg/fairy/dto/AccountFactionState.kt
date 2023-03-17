package com.hcyacg.fairy.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  3/17/2023 17:07
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
enum class AccountFactionState (
    val id: Long,
    val description: String
){
    UNKNOWN(-1, "未知"),
    AUDIT(0,"审核"),
    ACCEPT(1,"通过"),
    REFUSE(2,"拒绝")

    ;

    companion object {
        private val codeMap = AccountFactionState.values().associateBy { it.id }
        fun getAccountFactionStateById(id: Long): AccountFactionState {
            return codeMap[id] ?: AccountFactionState.UNKNOWN
        }
    }
}
