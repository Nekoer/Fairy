package com.hcyacg.fairy.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.entity.Faction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/27/2023 15:49
 * @Description
 **/
//需要校验功能
@Service
@Command("","转让宗门\\[CQ:at,qq=[0-9]+\\]","转让宗门@玩家 描述: 将宗主身份转交给你@的人")
class FactionTransfer : GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        try {
            val other = message.replace("转让宗门[CQ:at,qq=","").replace("]","").toLong()
            val account = accountService.info(sender)
            val otherAccount = accountService.info(other)
            account?.let {
                val one = factionService.getOne(QueryWrapper<Faction>().eq("own_id", account.account.id))
                if (one == null){
                    return "您没有创建宗门"
                }

                otherAccount?.let {

                    if (otherAccount.account.factionId != null && otherAccount.account.factionId != one.id){
                        return "对方已经加入了其他宗门,无法接受您的转让"
                    }

                    val otherFaction = factionService.getOne(QueryWrapper<Faction>().eq("own_id", otherAccount.account.id))
                    if (otherFaction != null){
                        return "对方已经创建了宗门,无法转让"
                    }


                    one.ownId = otherAccount.account.id
                    otherAccount.account.factionId = one.id
                    account.account.factionId = null
                    if (!factionService.updateById(one)){
                        throw RuntimeException("转让失败,数据更新失败")
                    }

                    if (!accountService.updateById(otherAccount.account)){
                        throw RuntimeException("转让失败,数据更新失败")
                    }

                    if (!accountService.updateById(account.account)){
                        throw RuntimeException("转让失败,数据更新失败")
                    }
                    return "转让成功"
                }
                return "转让失败,对方可能还未加入修仙世界"
            }
            return "转让失败"
        }catch (e:Exception){
            e.printStackTrace()
            return "转让失败"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
