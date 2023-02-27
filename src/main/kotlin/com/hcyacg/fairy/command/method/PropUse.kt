package com.hcyacg.fairy.command.method

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.dto.Usage
import com.hcyacg.fairy.dto.Usage.BLOOD_RETURN
import com.hcyacg.fairy.dto.Usage.MANA_RETURN
import com.hcyacg.fairy.entity.ItemUsage
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/25/2023 10:00
 * @Description
 **/
@Service
@Command("", "使用 [a-zA-Z0-9_\u4e00-\u9fa5]+","例: 使用 1;使用道具,数字为背包中的物品ID")
class PropUse : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        try {
            val account = accountService.info(sender)
            account?.let {
                val packageId = message.replace("使用 ","").toLong()
                val accountPackage = accountPackageService.getById(packageId)
                accountPackage?.let {
                    val item = itemService.getById(accountPackage.itemId)
                    val list = itemUsageService.list(QueryWrapper<ItemUsage>().eq("item_id",item.id))
                    val buffString = StringBuffer()

                    list.forEach {
                        //TODO("后期持续更新buff")
                        when (Usage.getUsageById(it.usageId)){
                            BLOOD_RETURN -> {
                                if (account.account.subtractHealth == 0L){
                                    return "您的血量是满的"
                                }


                                if (it.attribute - account.account.subtractHealth > 0){
                                    account.account.subtractHealth = 0
                                    buffString.append("您的血量已回满").append("\n")
                                }else {
                                    account.account.subtractHealth -= it.attribute
                                    buffString.append("您的血量回复了${it.attribute}点").append("\n")
                                }

                            }
                            MANA_RETURN -> {
                                if (account.account.subtractMana == 0L){
                                    return "您的法力是满的"
                                }

                                if (it.attribute - account.account.subtractMana > 0){
                                    account.account.subtractMana = 0
                                    buffString.append("您的法力已回满").append("\n")
                                }else {
                                    account.account.subtractMana -= it.attribute
                                    buffString.append("您的法力回复了${it.attribute}点").append("\n")
                                }
                            }
                            else -> {}
                        }
                    }

                    if (accountPackage.quantity - 1 < 1){
                        if (!accountPackageService.removeById(accountPackage)){
                            throw RuntimeException("使用道具失败,删除道具异常")
                        }
                    }else{
                        accountPackage.quantity -= 1
                        if (!accountPackageService.updateById(accountPackage)){
                            throw RuntimeException("使用道具失败,数据更新异常")
                        }
                    }

                    if (!accountService.updateById(account.account)){
                        throw RuntimeException("使用道具失败,玩家属性更新异常")
                    }
                    return "${item.name}使用成功,".plus(buffString.removeRange(buffString.length -1,buffString.length))
                }
            }
            return "使用道具失败,您可能未加入修仙"
        }catch (e:Exception){
            return e.message ?: "使用道具失败,您可能未加入修仙"
        }
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
