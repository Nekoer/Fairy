package com.hcyacg.fairy.dto

import com.hcyacg.fairy.entity.Account
import com.hcyacg.fairy.entity.Ethnicity
import com.hcyacg.fairy.entity.Hierarchical
import com.hcyacg.fairy.entity.LingRoot
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:34
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
data class AccountDTO(
    val account: Account,
    val lingRoot: LingRoot, //灵根
    val ethnicity: Ethnicity, //种族
    val level: Hierarchical,  //当前级别
    val upgrade:Hierarchical, // 下一个级别
    val health: Long,//生命值
    val mana: Long ,//法力
    val attack: Long, //攻击力
    val defensive: Long, //防御力
) {
    fun toMessageString():String{
        return "等级: ${level.level}".plus("\n")
            .plus("境界: ${level.name}").plus("\n")
            .plus("种族: ${ethnicity.name}").plus("\n")
            .plus("灵根: ${lingRoot.name}").plus("\n")
            .plus("经验值: ${account.exp}/${upgrade.exp}").plus("\n")
            .plus("生命值: $health").plus("\n")
            .plus("法力: $mana").plus("\n")
            .plus("攻击力: $attack").plus("\n")
            .plus("防御力: $defensive").plus("\n")
            .plus("充值点数: ${account.point}")
    }
}
