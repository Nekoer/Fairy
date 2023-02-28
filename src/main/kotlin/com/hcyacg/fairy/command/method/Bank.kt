package com.hcyacg.fairy.command.method

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.dto.Architecture
import com.hcyacg.fairy.entity.Bank
import com.hcyacg.fairy.entity.Wallet
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/25/2023 11:32
 * @Description
 **/
@Service
@Command("", "(存|取)[0-9]+ [0-9]+", "(存|取)灵石id 数量 描述: 将灵石或仙石存到钱庄又或者从钱庄取出来")
class Bank : GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        try {
            val account = accountService.info(sender)
            account?.let {
                val worldMap = worldMapService.getById(account.account.worldMapId)
                if (Architecture.getArchitectureById(worldMap.architectureId) == Architecture.BANK) {
                    val command = if (message.contains("存")) {
                        "存"
                    } else {
                        "取"
                    }

                    val stoneType = message.replace(command, "").split(" ")[0].toLong()
                    val money = message.split(" ")[1].toLong()
                    return if (bank(account.account.id,command, stoneType, money)){
                        "${command}款成功"
                    }else{
                        "${command}款失败,请检查是否存在足够的额度"
                    }
                }
            }
            return "抱歉,本区域没有钱庄"
        } catch (e: Exception) {
            e.printStackTrace()
            return "抱歉,本次操作出现异常"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }

    fun bank(accountId: Long,command: String, stoneType: Long, money: Long) :Boolean{
        try {
            val stone = stoneService.getById(stoneType)
            stone?.let {
                when (command) {
                    "存" -> {
                        val wallet =  walletService.getOne(QueryWrapper<Wallet>().eq("stone_id",stone.id).eq("account_id",accountId))
                        wallet?.let {
                            if (money > wallet.quantity){
                                return false
                            }

                            val bank = bankService.getOne(QueryWrapper<Bank>().eq("stone_id",stone.id).eq("account_id",accountId))
                            if(bank == null){
                                if (!bankService.save(Bank(0,accountId,stoneType,money))){
                                    throw RuntimeException("存款失败")
                                }
                                wallet.quantity -= money
                                if (!walletService.updateById(wallet)){
                                    throw RuntimeException("存款失败")
                                }
                            }else{
                                bank.quantity += money
                                wallet.quantity -= money

                                if (!bankService.updateById(bank) || !walletService.updateById(wallet)){
                                    throw RuntimeException("存款失败")
                                }
                            }
                            return true
                        }
                        return false
                    }
                    "取" -> {
                        val wallet =  walletService.getOne(QueryWrapper<Wallet>().eq("stone_id",stone.id).eq("account_id",accountId))
                        wallet?.let {
                            val bank = bankService.getOne(QueryWrapper<Bank>().eq("stone_id",stone.id).eq("account_id",accountId))

                            bank?.let {
                                if (money > bank.quantity){
                                    return false
                                }

                                bank.quantity -= money
                                it.quantity += money

                                if (!bankService.updateById(bank) || !walletService.updateById(wallet)){
                                    throw RuntimeException("取款失败")
                                }
                                return true
                            }
                            return false
                        }
                    }

                    else -> {
                        return false
                    }
                }
            }
            return false
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }
}
