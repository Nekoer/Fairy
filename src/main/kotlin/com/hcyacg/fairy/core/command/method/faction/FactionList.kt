package com.hcyacg.fairy.core.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import com.hcyacg.fairy.entity.AccountFaction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  3/20/2023 17:10
 * @Description
 **/
@Service
@Command("宗门列表","","宗门列表 描述: 查看宗门列表")
class FactionList : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val list = factionService.list()
        val sb = StringBuffer("现有宗门:\n")
        list.forEach {
            val count = accountFactionService.count(QueryWrapper<AccountFaction>().eq("faction_id",it.id))
            sb.append("ID:${it.id} 宗派:${it.name} 宗主:${accountService.getById(it.ownId).nickname} 成员:${count}/${it.member}").append("\n")
        }
        return sb.toString()
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
