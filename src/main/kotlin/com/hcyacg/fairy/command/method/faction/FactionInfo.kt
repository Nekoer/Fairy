package com.hcyacg.fairy.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.entity.Account
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/27/2023 16:48
 * @Description
 **/
@Service
@Command("宗门信息","","宗门信息")
class FactionInfo : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        try {
            val account = accountService.info(sender)
            account?.let {
                if (account.account.factionId == null){
                    return "您未加入宗门"
                }

                val faction = factionService.getById(account.account.factionId)
                val master = accountService.getById(faction.ownId)
                val count = accountService.count(QueryWrapper<Account>().eq("faction_id",faction.id))

                return "宗门: ${faction.name}".plus("\n")
                    .plus("宗主: ${master.uin}").plus("\n")
                    .plus("建设度: ${faction.construction}").plus("\n")
                    .plus("贡献值(你/全体): ${account.account.contribution}/${faction.construction}").plus("\n")
                    .plus("资材: ${faction.material}").plus("\n")
                    .plus("成员: ${count}/${faction.member}")
            }

            return "未查到宗门信息"
        }catch (e:Exception){
            e.printStackTrace()
            return "未查到宗门信息"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
