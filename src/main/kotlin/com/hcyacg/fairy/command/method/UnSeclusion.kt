package com.hcyacg.fairy.command.method

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.entity.Seclusion
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/25/2023 15:48
 * @Description
 **/
@Service
@Command("出关","","出关")
class UnSeclusion : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val account = accountService.info(sender)
        account?.let {
            val seclusionNum = seclusionService.count(QueryWrapper<Seclusion>().eq("account_id",account.account.id))
            if (seclusionNum < 1){
                return "你不在闭关"
            }
            val seclusion = seclusionService.getOne(QueryWrapper<Seclusion>().eq("account_id",account.account.id))
            if (seclusion != null){
                val now = System.currentTimeMillis()
                //大于1分钟
                if (now - seclusion.time > 60000){
                    //计算收益
                    val time = now - seclusion.time
                    val num = time / 60000
                    //固定值15
                    val exp = (account.account.level + 15) * num
                    account.account.exp += exp

                    val health = if (account.account.subtractHealth - exp < 1){
                        0
                    }else{
                        account.account.subtractHealth - exp
                    }

                    val mana = if (account.account.subtractMana - exp < 1){
                        0
                    }else{
                        account.account.subtractMana - exp
                    }
                    account.account.subtractHealth = health
                    account.account.subtractMana = mana

                    if (!accountService.updateById(account.account)){
                        throw RuntimeException("出关失败")
                    }
                    if (!seclusionService.removeById(seclusion)){
                        throw RuntimeException("出关失败")
                    }
                    return "出关成功,本次收益: 修为增加${exp}点,血量回复了${health}点,法力回复了${mana}点"
                }else{
                    if (!seclusionService.removeById(seclusion)){
                        throw RuntimeException("出关失败")
                    }
                    return "出关成功,由于闭关时间太短,本次闭关没有收益"
                }
            }else{
                return "出关失败"
            }
        }
        return "出关失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
