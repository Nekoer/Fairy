package com.hcyacg.fairy.core.command.method

import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import com.hcyacg.fairy.dto.ItemType
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:15
 * @Description
 **/
@Service
@Command("","查询物品 [a-zA-Z0-9_\u4e00-\u9fa5]+","查询物品 物品名 描述: 查看物品的信息及用途")
class QueryItem : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        val name = message.replace("查询物品 ", "")
        val sb = StringBuffer()
        val list = itemService.info(name)
        list.forEachIndexed { index, item ->
            sb.append("ID: ${item.id} 名称: ${item.name} 类别: ${ItemType.getItemTypeById(item.itemTypeId).value} 描述: ${item.description}")
            if (list.size - 1 != index) {
                sb.append("\n\r")
            }
        }
        return "查询到以下内容: \n$sb"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
