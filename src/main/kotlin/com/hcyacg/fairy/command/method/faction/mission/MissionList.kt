package com.hcyacg.fairy.command.method.faction.mission

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.DependenceService
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.entity.AccountFaction
import com.hcyacg.fairy.entity.AccountFactionMission
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/27/2023 18:51
 * @Description
 **/
@Service
@Command("宗门任务列表","","宗门任务列表 描述: 查看宗门任务")
class MissionList : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val account = accountService.info(sender)
        account?.let {
            val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))
                ?: return "宗门任务查看失败,您还未加入宗门"

            val accountMissionList = accountFactionMissionService.list(QueryWrapper<AccountFactionMission>().eq("account_id",account.account.id))
            val list = factionMissionService.list()
            val sb = StringBuffer()
            list.forEach {
                val accountMission =  accountMissionList.find { a:AccountFactionMission -> a.id == it.id }
                sb.append("ID:${it.id} ${it.name} 消耗:${if (it.type == 1L) "气血" else "灵石"} ${if (it.type == 1L) "${it.cost*100}%" else it.cost} 修为增加:${it.give * 100}% 资材:${it.sect}  ${accountMission?.state() ?: "未领取"}").append("\n")
            }
            return sb.substring(0,sb.length - 1)
        }


        return "宗门任务查看失败,您还未加入宗门"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
