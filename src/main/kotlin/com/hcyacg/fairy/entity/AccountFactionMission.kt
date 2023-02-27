package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.hcyacg.fairy.constant.AppConstant
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/27/2023 19:33
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account_faction_mission")
data class AccountFactionMission(
    @TableId(type = IdType.AUTO)
    val id: Long,
    @TableField("account_id")
    val accountId: Long,
    @TableField("faction_mission_id")
    val factionMissionId: Long,
    var status: Long
) {
    fun state(): String {
        val sb = StringBuffer()
        when (status) {
            AppConstant.FACTION_MISSION_STATE_SUCCESS -> sb.append("已完成")
            AppConstant.FACTION_MISSION_STATE_CANEL -> sb.append("已取消")
            AppConstant.FACTION_MISSION_STATE_FAILURE -> sb.append("失败")
            AppConstant.FACTION_MISSION_STATE_PROGRESS -> sb.append("进行中")
        }
        return sb.toString()
    }
}
