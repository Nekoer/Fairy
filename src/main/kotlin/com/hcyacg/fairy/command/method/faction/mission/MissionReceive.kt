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
 * @Date  2/27/2023 19:24
 * @Description
 **/
@Service
@Command("","宗门任务接取 [0-9]+","宗门任务接取 任务id 描述: 领取宗门任务")
class MissionReceive : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        try {
            val account = accountService.info(sender)
            val factionMissionId = message.replace("宗门任务接取 ","").toLongOrNull()
            factionMissionId?.let {
                account?.let {
                    if (account.account.factionId == null){
                        return "宗门任务领取失败,您还未加入宗门"
                    }

                    val count = accountFactionMissionService.count(QueryWrapper<AccountFactionMission>().eq("account_id",account.account.id))
                    if (count > 4){
                        return "今日的宗门任务已经到达领取上限"
                    }

                    val factionMission = factionMissionService.getById(factionMissionId)

                    val accountMissionList = accountFactionMissionService.list(QueryWrapper<AccountFactionMission>().eq("account_id",account.account.id))
                    val accountMission =  accountMissionList.find { a:AccountFactionMission -> a.factionMissionId == factionMission.id }
                    if (accountMission != null){
                        return "您已经接取了此任务"
                    }

                    factionMission?.let {
                        if(!accountFactionMissionService.save(AccountFactionMission(0,account.account.id,factionMission.id,AppConstant.FACTION_MISSION_STATE_PROGRESS))){
                            throw RuntimeException("宗门任务接取失败,数据更新失败")
                        }
                        return "宗门任务接取成功"
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return "任务接取失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
