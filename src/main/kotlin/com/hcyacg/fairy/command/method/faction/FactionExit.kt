package com.hcyacg.fairy.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.DependenceService
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.entity.AccountFaction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/27/2023 11:51
 * @Description
 **/
@Service
@Command("退出宗门","","退出宗门 描述: 退出自己加入的宗门")
class FactionExit: GameCommandService, DependenceService()  {
    override fun group(sender: Long, group: Long, message: String): String {
        try {
            val account = accountService.info(sender)
            account?.let {
                val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))
                    ?: return "退出失败,您还未加入任何宗门"

                val faction = factionService.getById(accountFaction.factionId)
                if (faction.ownId == account.account.id){
                    return "您必须转让宗主身份方可退出"
                } else {
                    if (!accountFactionService.removeById(accountFaction)){
                        throw RuntimeException("退出失败,更新数据失败")
                    }
                    return "退出宗门成功"
                }
            }
            return "退出宗门失败"
        }catch (e:Exception){
            e.printStackTrace()
            return "退出宗门失败"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
