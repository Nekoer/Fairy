package com.hcyacg.fairy.core.command.method.faction.authority

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import com.hcyacg.fairy.dto.Patriarch
import com.hcyacg.fairy.entity.AccountFaction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  3/17/2023 20:07
 * @Description
 **/
@Service
@Command("","降职\\[CQ:at,qq=[0-9]+\\]","降职@玩家 描述: 降职")
class PatriarchDown : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val other = message.replace("降职[CQ:at,qq=","").replace("]","").toLong()
        val account = accountService.info(sender)
        account?.let {
            val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))?: return "您还未加入宗门"
            if (accountFaction.patriarchId != Patriarch.SUZERAIN.id && accountFaction.patriarchId != Patriarch.ELDER.id){
                return "你没有权限将该弟子降职"
            }
            val otherAccount = accountService.info(other)
            otherAccount?.let{
                val otherAccountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",it.account.id))?: return "该玩家还未加入宗门"

                if (otherAccountFaction.factionId != accountFaction.factionId){
                    return "该玩家不是您宗门的弟子"
                }

                if (otherAccountFaction.patriarchId == Patriarch.DISCIPLE.id){
                    return "该玩家已经成为了${Patriarch.DISCIPLE.value}"
                }

                otherAccountFaction.patriarchId = Patriarch.DISCIPLE.id
                if (!accountFactionService.updateById(otherAccountFaction)){
                    throw RuntimeException("玩家宗门数据更新失败")
                }

                return "您成功将该玩家降级为${Patriarch.DISCIPLE.value}"
            }
            return "降职失败"
        }
        return "降职失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
