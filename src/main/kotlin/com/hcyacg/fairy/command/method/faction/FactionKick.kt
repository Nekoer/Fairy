package com.hcyacg.fairy.command.method.faction

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
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
                val faction = factionService.getById(account.account.factionId)
                faction?.let {
                    otherAccount?.let {
                        if (otherAccount.account.factionId != null && otherAccount.account.factionId == faction.id && otherAccount.account.id == faction.ownId){
                            otherAccount.account.factionId = null
                            otherAccount.account.contribution = 0
                            if (!accountService.updateById(otherAccount.account)){
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
