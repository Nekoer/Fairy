package com.hcyacg.fairy.core.command.method

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import com.hcyacg.fairy.entity.Seclusion
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/25/2023 15:37
 * @Description 闭关
 **/
@Service
@Command("闭关","","闭关")
class Seclusion : GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val account = accountService.info(sender)
        account?.let {
            val seclusion = seclusionService.count(QueryWrapper<Seclusion>().eq("account_id",account.account.id))
            if (seclusion > 0){
                return "你已经在闭关中了"
            }

            if (seclusionService.save(Seclusion(0,account.account.id,System.currentTimeMillis()))){
                return "闭关成功"
            }else{
                return "闭关失败"
            }
        }
        return "闭关失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
