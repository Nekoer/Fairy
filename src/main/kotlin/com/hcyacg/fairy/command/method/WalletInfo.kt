package com.hcyacg.fairy.command.method

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.DependenceService
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.entity.Bank
import com.hcyacg.fairy.entity.Wallet
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/25/2023 13:32
 * @Description
 **/
@Service
@Command("荷包","","荷包 描述: 查看自己的钱包")
class WalletInfo : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val account = accountService.info(sender)
        val sb = StringBuffer()
        account?.let {
            val stoneList = stoneService.list()
            sb.append("您的钱包如下:")
            stoneList.forEach {stone->
                val wallet = walletService.getOne(QueryWrapper<Wallet>().eq("account_id",account.account.id).eq("stone_id",stone.id))
                if (wallet == null){
                    val bank = bankService.getOne(QueryWrapper<Bank>().eq("account_id",account.account.id).eq("stone_id",stone.id))
                    if (bank == null){
                        sb.append("\n").append("${stone.name}: 0 (钱庄: 0)")
                    }else {
                        sb.append("\n").append("${stone.name}: 0 (钱庄: ${bank.quantity})")
                    }
                }else {
                    val bank = bankService.getOne(QueryWrapper<Bank>().eq("account_id",account.account.id).eq("stone_id",stone.id))
                    if (bank == null){
                        sb.append("\n").append("${stone.name}: ${wallet.quantity} (钱庄: 0)")
                    }else {
                        sb.append("\n").append("${stone.name}: ${wallet.quantity} (钱庄: ${bank.quantity})")
                    }
                }
            }
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
