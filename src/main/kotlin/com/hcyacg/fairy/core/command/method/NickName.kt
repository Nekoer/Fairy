package com.hcyacg.fairy.core.command.method

import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  3/20/2023 16:49
 * @Description
 **/
@Service
@Command("", "设置道号 [a-zA-Z0-9_\u4e00-\u9fa5]+","设置道号 描述: 设置道号")
class NickName : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val account = accountService.info(sender)
        account?.let {
            val name = message.replace("设置道号","")

            if (name.isEmpty() || name.isBlank()){
                return "道号不能为空"
            }

            account.account.nickname = name

            if (!accountService.updateById(account.account)){
                throw RuntimeException("设置道号失败")
            }
            return "设置道号成功"
        }
        return "您还未加入修仙"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
