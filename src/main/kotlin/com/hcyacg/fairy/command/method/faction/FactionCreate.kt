package com.hcyacg.fairy.command.method.faction

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.entity.Faction
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/25/2023 20:41
 * @Description
 **/
@Service
@Command("","创建宗派 [a-zA-Z0-9_\u4e00-\u9fa5]+","创建宗派")
class FactionCreate: GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val name = message.replace("创建宗派 ","")
        val account = accountService.info(sender)
        account?.let {
            val one = factionService.getOne(QueryWrapper<Faction>().eq("own_id", account.account.id))
            if (one == null){
                if (!factionService.save(Faction(name,account.account.id))){
                    throw RuntimeException("创建宗派失败")
                }
                //TODO("需要扣除物品或者灵石")
                return "创建宗派成功"
            }else {
                return "您已经创建过宗派了"
            }
        }
        return "创建宗派失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
