package com.hcyacg.fairy.command.method.admin

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.task.BossTask
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 22:13
 * @Description
 **/
@Service
@Command("生成boss","","生成boss",true)
class MakeBoss : GameCommandService, DependenceService(){
    @Autowired
    private lateinit var bossTask: BossTask
    override fun group(sender: Long, group: Long, message: String): String {
        bossTask.generateBoss()
        return "操作成功"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
