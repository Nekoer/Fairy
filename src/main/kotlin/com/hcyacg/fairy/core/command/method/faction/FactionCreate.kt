package com.hcyacg.fairy.core.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import com.hcyacg.fairy.dto.AccountFactionState
import com.hcyacg.fairy.dto.Patriarch
import com.hcyacg.fairy.entity.AccountFaction
import com.hcyacg.fairy.entity.Faction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/25/2023 20:41
 * @Description
 **/
@Service
@Command("","创建宗门 [a-zA-Z0-9_\u4e00-\u9fa5]+","创建宗门 宗派名 描述: 创建新的宗门,同时需要消耗一定的物品")
class FactionCreate: GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val name = message.replace("创建宗门 ","")
        val account = accountService.info(sender)
        account?.let {
            val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))

            val one = factionService.getOne(QueryWrapper<Faction>().eq("own_id", account.account.id))
            val count = factionService.count(QueryWrapper<Faction>().eq("name",name))
            if (accountFaction != null){
                return "创建宗门失败,您需要退出已有的宗派才能创建新宗门"
            }

            if (one == null){
                if (count > 0){
                    return "创建宗门失败,已经有名为${name}的宗门了"
                }
                val faction = Faction(name,account.account.id)
                if (!factionService.save(faction)){
                    throw RuntimeException("创建宗门失败")
                }

                if(!accountFactionService.saveOrUpdate(AccountFaction(faction.id,it.account.id,0,
                        AccountFactionState.ACCEPT.id, Patriarch.SUZERAIN.id))){
                    throw RuntimeException("创建宗门失败")
                }
                //TODO("需要扣除物品或者灵石")
                return "创建宗门成功"
            }else {
                return "您已经创建过宗门了"
            }
        }
        return "创建宗门失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
