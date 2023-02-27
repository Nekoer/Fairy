package com.hcyacg.fairy.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/19/2023 16:44
 * @Description 道具用途 或 buff
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
enum class Usage(
    val id: Long,
    val value: String,
    val description: String
){
    /**
     * 不允许修改id id可以看作主键
     */
    UNKNOWN(-1, "未知","未知"),
    BLOOD_RETURN(1,"血量回复","恢复玩家的血量"),
    MANA_RETURN(2,"法力回复","恢复玩家的法力"),
    EXPERIENCE_ADD(3,"经验增加","给予玩家一定的经验"),
    EXPERIENCE_REDUCE(4,"经验减少","扣除玩家一定的经验"),
    FACTION_MEMBER_ADD(5,"宗派人数上限增加","增加宗派人数上限")
    ;

    companion object {
        private val codeMap = values().associateBy { it.id }
        fun getUsageById(id: Long): Usage {
            return codeMap[id] ?: UNKNOWN
        }
    }

}
