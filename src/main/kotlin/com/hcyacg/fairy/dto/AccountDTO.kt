package com.hcyacg.fairy.dto

import com.hcyacg.fairy.entity.*
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
    val faction: Faction?,
    val accountFaction: AccountFaction?,
    val level: Hierarchical,  //当前级别
    val upgrade: Hierarchical?, // 下一个级别
    val exerciseDTO: ExerciseDTO?, //功法属性
    var health: Long,//生命值
    val mana: Long,//法力
    val attack: Long, //攻击力
    val defensive: Long, //防御力

    // 加成
    val healthAddition: Long,
    val manaAddition: Long,
    val attackAddition: Long,
    val defensiveAddition: Long,

    val maxHealth : Long,
    val maxMana : Long,
) {

    fun toMessageString(): String {
        val sb = StringBuffer()

        sb.append("等级: ${level.level}").append("\n")
            .append("道号: ${account.nickname ?: "无"}").append("\n")
            .append("境界: ${level.name}").append("\n")
            .append("种族: ${ethnicity.name}").append("\n")
            .append("灵根: ${lingRoot.name}").append("\n")
            .append("宗派: ${faction?.name ?: "未加入宗派"}").append("\n")
            .append("职位: ${accountFaction?.let { Patriarch.getPatriarchById(it.patriarchId).value } ?: "散修"}").append("\n")
            .append("修为: ${account.exp} / ${upgrade?.exp ?: "位面至高"}")

        if (upgrade == null) {
            sb.append("\n")
        } else {
            if (account.exp >= upgrade.exp) {
                sb.append(" 可突破率${levelUp()}%").append("\n")
            } else {
                sb.append("\n")
            }
        }

        exerciseDTO?.let {
            sb.append("功法: ${it.name} (${it.skillful}%)").append("\n")
        }

        sb.append("生命值: $health / $maxHealth (${if (healthAddition > 0) "+$healthAddition" else healthAddition})").append("\n")
            .append("法力: $mana / $maxMana (${if (manaAddition > 0) "+$manaAddition" else manaAddition})").append("\n")
            .append("攻击力: $attack (${if (attackAddition > 0) "+$attackAddition" else attackAddition})").append("\n")
            .append("防御力: $defensive (${if (defensiveAddition > 0) "+$defensiveAddition" else defensiveAddition})").append("\n")
            .append("充值点数: ${account.point}")
        return sb.toString()
    }

    //突破的概率
    fun levelUp(): Long {
        return ceil((100 * ((account.probability + level.probability) / 100)) * 0.75).toLong()
    }
}
