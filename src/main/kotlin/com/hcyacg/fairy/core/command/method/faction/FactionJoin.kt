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
                val faction = factionService.getById(factionId) ?: return "加入失败,没有该ID的宗门"

                if (accountFaction != null && accountFaction.state == AccountFactionState.ACCEPT.id){
                    return "加入失败,您已经加入过宗派了"
                }



//                if (accountFaction.factionId == factionId){
//                    return "加入失败,您已经是该宗门的弟子了"
//                }

                //TODO("后期可以出增加宗派人数的道具")
                val count = accountFactionService.count(QueryWrapper<AccountFaction>().eq("faction_id",faction.id))
                if (count + 1 > faction.member){
                    return "该宗门弟子已满"
                }

                val af = if (accountFaction == null){
                    AccountFaction(faction.id,it.account.id,0,AccountFactionState.AUDIT.id,Patriarch.DISCIPLE.id)
                }else{
                    accountFaction.factionId = faction.id
                    accountFaction
                }

                if (!accountFactionService.saveOrUpdate(af)){
                    throw RuntimeException("加入失败,数据更新失败")
                }

                return "加入${faction.name}成功,正在等待审核"
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
