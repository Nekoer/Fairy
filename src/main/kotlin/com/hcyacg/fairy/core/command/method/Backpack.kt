package com.hcyacg.fairy.core.command.method

import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 11:49
 * @Description
 **/
@Service
@Command("","背包 [0-9]+","背包 页码 描述: 查看自己的背包")
class Backpack: GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        val pageString = message.replace("背包 ","")
        var page = 1L
        if (pageString.isNotBlank()){
            page = pageString.toLong()
        }
        val senderInfo = accountService.info(sender)
        senderInfo?.let { accountDTO ->
            accountDTO.account.id.let {
                val packageList = accountPackageService.getPackagePage(
                    it,page
                )
                val sb = StringBuffer()
                packageList?.let {
                    packageList.list.forEachIndexed { index, accountItem ->
                        sb.append("ID:${accountItem.id} ${accountItem.item.name} ×${accountItem.quantity}")
                        if (packageList.list.size - 1 != index) {
                            sb.append("\n")
                        }
                    }
                    sb.append("\n").append("========").append("\n").append("当前在${page}页,总共${packageList.pages}页")
                }

                return "你的背包内容如下".plus("\n").plus("========").plus("\n").plus(sb.toString())
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
