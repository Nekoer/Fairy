package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 11:49
 * @Description
 **/
@Service
@Command("背包","","查看背包")
class Backpack: GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val senderInfo = accountService.info(sender)
        senderInfo?.let { accountDTO ->
            accountDTO.account.id.let {
                val packageList = accountPackageService.getPackageList(
                    it
                )
                val sb = StringBuffer()
                packageList.forEachIndexed { index, accountItem ->
                    sb.append("${accountItem.item.name} ×${accountItem.quantity}")
                    if (packageList.size - 1 != index) {
                        sb.append("\n")
                    }
                }
                return "你的背包内容如下\n$sb"
            }
        }
        return ""
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
