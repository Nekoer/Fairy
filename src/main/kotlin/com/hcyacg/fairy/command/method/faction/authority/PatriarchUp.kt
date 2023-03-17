package com.hcyacg.fairy.command.method.faction.authority

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.DependenceService
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.dto.Patriarch
import com.hcyacg.fairy.entity.AccountFaction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  3/17/2023 19:54
 * @Description
 **/
@Service
@Command("","升职\\[CQ:at,qq=[0-9]+\\]","升职@玩家 描述: 升职")
class PatriarchUp : GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val other = message.replace("升职[CQ:at,qq=","").replace("]","").toLong()
        val account = accountService.info(sender)
        account?.let {
            val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))?: return "您还未加入宗门"
            if (accountFaction.patriarchId != Patriarch.SUZERAIN.id){
                return "你没有权限将该弟子升职"
            }
            val otherAccount = accountService.info(other)
            otherAccount?.let{
                val otherAccountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",it.account.id))?: return "该玩家还未加入宗门"

                if (otherAccountFaction.factionId != accountFaction.factionId){
                    return "该玩家不是您宗门的弟子"
                }

                if (otherAccountFaction.patriarchId == Patriarch.ELDER.id){
                    return "该玩家已经成为了${Patriarch.ELDER.value}"
                }

                otherAccountFaction.patriarchId = Patriarch.ELDER.id
                if (!accountFactionService.updateById(otherAccountFaction)){
                    throw RuntimeException("玩家宗门数据更新失败")
                }

                return "您成功将该玩家升级为${Patriarch.ELDER.value}"
            }
            return "升职失败"
        }
        return "升职失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
