package com.hcyacg.fairy.core.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import com.hcyacg.fairy.dto.AccountFactionState
import com.hcyacg.fairy.dto.Patriarch
import com.hcyacg.fairy.entity.AccountFaction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  3/20/2023 16:02
 * @Description
 **/
@Service
@Command("","宗门成员查看","宗门成员查看 描述: 查看宗门全部成员")
class FactionMember : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val account = accountService.info(sender)
        account?.let {
            val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))
                ?: return "您未加入宗门"

            val faction = factionService.getById(accountFaction.factionId)
            val list = accountFactionService.list(QueryWrapper<AccountFaction>().eq("faction_id", faction.id))
            val sb = StringBuffer("成员如下:\n")
            list.forEach {
                val member = accountService.getById(it.accountId)
                if (it.state == AccountFactionState.ACCEPT.id){
                    sb.append("${Patriarch.getPatriarchById(it.patriarchId).value} ${member.nickname ?: member.uin}").append("\n")
                }
            }
            return sb.toString()
        }
        return "您还未加入修仙"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
