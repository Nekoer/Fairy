package com.hcyacg.fairy.core.command.method

import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 19:37
 * @Description
 **/
@Service
@Command("帮助", "", "帮助 描述: 包含所有命令及描述")
class Help : GameCommandService{

    override fun group(sender: Long, group: Long, message: String): String {
        val sb = StringBuffer()
        val map = AppConstant.COMMAND_DESCRIPTION
        var num = 0
        map.forEach { (t, u) ->
            num += 1
            sb.append("命令:$u")
            if (map.size != num) {
                sb.append("\n")
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
