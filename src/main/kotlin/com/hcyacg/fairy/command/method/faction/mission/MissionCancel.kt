package com.hcyacg.fairy.command.method.faction.mission

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.entity.AccountFactionMission
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/27/2023 21:19
 * @Description
 **/
@Service
@Command("", "宗门任务取消 [0-9]+", "宗门任务取消 任务id 描述: 取消已经领取的任务,但是会被算进当天的宗门任务上限")
class MissionCancel : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        try {
            val account = accountService.info(sender)
            val factionMissionId = message.replace("宗门任务取消 ", "").toLongOrNull()
            factionMissionId?.let {
                account?.let {
                    if (account.account.factionId == null){
                        return "宗门任务查看失败,您还未加入宗门"
                    }
                    val accountMissionList = accountFactionMissionService.list(QueryWrapper<AccountFactionMission>().eq("account_id",account.account.id))
                    val accountMission =  accountMissionList.find { a:AccountFactionMission -> a.factionMissionId == factionMissionId }
                    accountMission?.let {
                        accountMission.status = AppConstant.FACTION_MISSION_STATE_CANEL
                        if (!accountFactionMissionService.updateById(accountMission)){
                            throw  RuntimeException("宗门任务取消失败")
                        }
                        return "取消宗门任务成功"
                    }

                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
        return "任务无法取消"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
