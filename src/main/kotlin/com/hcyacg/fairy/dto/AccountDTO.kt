package com.hcyacg.fairy.dto

import com.hcyacg.fairy.entity.Account
import com.hcyacg.fairy.entity.Ethnicity
import com.hcyacg.fairy.entity.Hierarchical
import com.hcyacg.fairy.entity.LingRoot
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import kotlin.math.ceil

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
    val upgrade:Hierarchical?, // 下一个级别
    val health: Long,//生命值
    val mana: Long ,//法力
    val attack: Long, //攻击力
    val defensive: Long, //防御力
) {
    fun toMessageString():String{
        val sb = StringBuffer()
        sb.append("等级: ${level.level}").append("\n")
            .append("境界: ${level.name}").append("\n")
            .append("种族: ${ethnicity.name}").append("\n")
            .append("灵根: ${lingRoot.name}").append("\n")
            .append("修为: ${account.exp}/${upgrade?.exp ?: "位面至高"}")

        //TODO("后期需加上可突破率")
        if (upgrade == null){
            sb.append("\n")
        }else{
            if (account.exp >= upgrade.exp){
                sb.append(" 可突破率${levelUp()}%").append("\n")
            }else{
                sb.append("\n")
            }
        }

        sb.append("生命值: $health").append("\n")
            .append("法力: $mana").append("\n")
            .append("攻击力: $attack").append("\n")
            .append("防御力: $defensive").append("\n")
            .append("充值点数: ${account.point}")
        return sb.toString()
    }

    //突破的概率
    fun levelUp():Long{
        return ceil((100 * ((account.probability + level.probability) / 100)) * 0.75).toLong()
    }
}
