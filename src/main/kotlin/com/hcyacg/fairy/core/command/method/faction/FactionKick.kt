package com.hcyacg.fairy.core.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import com.hcyacg.fairy.dto.Patriarch
import com.hcyacg.fairy.entity.AccountFaction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/27/2023 19:13
 * @Description
 **/
//TODO("需要测试")
@Service
@Command("","踢出宗门\\[CQ:at,qq=[0-9]+\\]","踢出宗门@玩家 描述: 将你@的玩家踢出宗门")
class FactionKick : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        try{
            val other = message.replace("踢出宗门[CQ:at,qq=","").replace("]","").toLong()
            val account = accountService.info(sender)
            val otherAccount = accountService.info(other)
            account?.let {
                val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))

                val faction = factionService.getById(accountFaction.factionId)
                faction?.let {
                    otherAccount?.let {
                        val otherAccountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",it.account.id))

                        if (otherAccountFaction.factionId == faction.id && otherAccountFaction.accountId == faction.ownId){

                            //TODO 判断权限
                            if (accountFaction.patriarchId != Patriarch.SUZERAIN.id && accountFaction.patriarchId != Patriarch.ELDER.id){
                                return "你没有权限踢出该宗门弟子"
                            }

                            //对方是长老而你不是宗主
                            if (accountFaction.patriarchId != Patriarch.SUZERAIN.id && otherAccountFaction.patriarchId == Patriarch.ELDER.id){
                                return "你没有权限踢出该宗门长老"
                            }

                            if (otherAccountFaction.patriarchId == Patriarch.SUZERAIN.id){
                                return "你没有权限踢出该宗门宗主"
                            }


                            if (!accountFactionService.removeById(otherAccountFaction)){
                                throw RuntimeException("踢出失败,数据更新失败")
                            }
                            return "踢出成功"
                        }
                    }
                }
            }
            return "踢出失败"
        }catch (e:Exception){
            e.printStackTrace()
            return "踢出失败"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
