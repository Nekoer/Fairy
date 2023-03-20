package com.hcyacg.fairy.core.command.method.faction.authority

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
 * @Date  3/20/2023 15:32
 * @Description
 **/

@Service
@Command("","(同意|拒绝)[0-9]+","同意|拒绝qq号 描述: 审核是否允许加入宗门")
class AuditMember : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val account = accountService.info(sender)
        account?.let {
            val accountFaction  = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",it.account.id)) ?: return "您还未加入任何宗门"
            if (accountFaction.patriarchId != Patriarch.SUZERAIN.id && accountFaction.patriarchId != Patriarch.ELDER.id){
                return "你没有权限操作"
            }

            val other = message.replace("同意","").replace("拒绝","").toLong()

            val otherAccount = accountService.info(other) ?: return "对方还未加入修仙"
            val otherAccountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",otherAccount.account.id).eq("faction_id",accountFaction.factionId))?: return "对方还未加入任何宗门"


            when{
                message.contains("同意") -> {
                    if (otherAccountFaction.state != AccountFactionState.AUDIT.id){
                        return "对方不处在申请状态中"
                    }
                    otherAccountFaction.state = AccountFactionState.ACCEPT.id

                    if (!accountFactionService.updateById(otherAccountFaction)){
                        throw RuntimeException("玩家宗派审核异常")
                    }
                }
                message.contains("拒绝") -> {
                    if (!accountFactionService.removeById(otherAccountFaction)){
                        throw RuntimeException("玩家宗派审核异常")
                    }
                }
            }
            return "操作成功"
        }
        return "操作失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
