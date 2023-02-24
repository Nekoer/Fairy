package com.hcyacg.fairy.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/24/2023 12:13
 * @Description 战斗结果及过程
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
data class FightBossResponse (
    val victory : Boolean,
    val process : StringBuffer
)
