package com.hcyacg.fairy.core.command.method

import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import org.springframework.stereotype.Service
import kotlin.math.ceil

/**
 * @Author Nekoer
 * @Date  2/22/2023 15:08
 * @Description
 **/
@Service
@Command("突破","","突破 描述: 突破当前境界")
class LevelUp : GameCommandService, DependenceService(){
    override fun group(sender: Long, group: Long, message: String): String {
        try{
            val accountDTO = accountService.info(sender)
            accountDTO?.let {
                if (it.account.exp < it.level.exp){
                    return "您的修为目前还无法支撑您突破当前境界"
                }

                if (it.upgrade == null){
                    return "您已经是最高境界了"
                }


                if (it.levelUp() > 50){
                    it.account.level = it.upgrade.level
                    it.account.probability -= 2
                    if(accountService.updateById(it.account)){
                        return "恭喜您突破到了${it.upgrade.name}"
                    }else{
                        throw RuntimeException("恭喜您成功突破境界,但是数据更新错误")
                    }
                }else{
                    val exp = ceil(it.level.exp * 0.25).toLong()
                    it.account.exp -= exp
                    it.account.probability += 2
                    if(accountService.updateById(it.account)){
                        return "突破失败,扣除您${exp}的修为"
                    }else{
                        throw RuntimeException("突破失败,但是数据更新错误")
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return "目前您无法突破"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
