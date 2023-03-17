package com.hcyacg.fairy.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.DependenceService
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.dto.AccountFactionState
import com.hcyacg.fairy.entity.Account
import com.hcyacg.fairy.entity.AccountFaction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/27/2023 16:09
 * @Description
 **/
@Service
@Command("","加入宗门 [0-9]+","加入宗门 ID 描述: 加入指定的宗门")
class FactionJoin : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        try{
            val account = accountService.info(sender)
            val factionId = message.replace("加入宗门 ","").toLong()
            account?.let {
                val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))

                if (accountFaction != null){
                    return "加入失败,您已经加入过宗派了"
                }

                val faction = factionService.getById(factionId) ?: return "加入失败,没有该ID的宗门"

//                if (accountFaction.factionId == factionId){
//                    return "加入失败,您已经是该宗门的弟子了"
//                }

                //TODO("后期可以出增加宗派人数的道具")
                val count = accountService.count(QueryWrapper<Account>().eq("faction_id",faction.id))
                if (count + 1 > faction.member){
                    return "该宗门弟子已满"
                }

                if (!accountFactionService.saveOrUpdate(AccountFaction(faction.id,it.account.id,0,
                        AccountFactionState.AUDIT.id,1))){
                    throw RuntimeException("加入失败,数据更新失败")
                }

                return "加入${faction.name}成功"
            }
            return "加入失败"
        }catch (e:Exception){
            e.printStackTrace()
            return "加入失败"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
