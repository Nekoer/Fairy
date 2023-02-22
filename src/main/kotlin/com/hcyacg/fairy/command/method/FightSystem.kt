package com.hcyacg.fairy.command.method

import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.DependenceService
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.dto.AccountDTO
import com.hcyacg.fairy.entity.Boss
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.KeyScanOptions
import org.springframework.data.redis.core.RedisCallback
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

/**
 * @Author Nekoer
 * @Date  2/21/2023 18:53
 * @Description
 **/
@Service
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Command("","对战 [0-9]+","对战 id (与可对战对象进行战斗)")
class FightSystem : GameCommandService, DependenceService(){
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun group(sender: Long, group: Long, message: String): String {
        val senderInfo = accountService.info(sender)
        val pos = worldMapService.position(senderInfo!!.account.worldMapId)
        val key = "${AppConstant.BOSS_WORLD_MAP}${pos.now.id}:"

        val keys = redisTemplate.execute(RedisCallback<Set<String>> { connection ->
            val keysTmp: MutableSet<String> = HashSet()
            val cursor: Cursor<ByteArray> =
                connection.keyCommands().scan(KeyScanOptions.scanOptions().match("$key*").count(1000).build())

            while (cursor.hasNext()) {
                keysTmp.add(String(cursor.next()))
            }
            return@RedisCallback keysTmp
        })
        val sb = StringBuffer()

        val bossId = message.replace("对战 ","").toInt()
        //选择打哪一只
        keys?.let {
            if (it.contains(key.plus(bossId))){
                val boss = bossService.getById(bossId)

                //怪物和玩家同时无法破甲 双方都打不出伤害，这时可能会进入无限循环
                if (boss.attack <= senderInfo.defensive && senderInfo.attack <= boss.defensive){
                    return "您和${boss.name}都拿对手无可奈何,双双离去~"
                }

                //TODO 还没有计算boss和玩家的增益属性 以及使用道具
                val fight = fightToBoss(senderInfo,boss)

                //等待多线程结束
                CompletableFuture.allOf(fight).join()
                if(fight.get()){
                    //胜利

                    redisUtil.del(key.plus(bossId))
                    senderInfo.account.exp = senderInfo.account.exp + boss.victoryExp(senderInfo.level.level)
                    if (accountService.updateById(senderInfo.account)){
                        sb.append("恭喜您战斗成功,获得经验${boss.victoryExp(senderInfo.level.level)}")
                    }else{
                        sb.append("恭喜您战斗成功,但是数据更新失败")
                    }
                }else{
                    //失败
                    sb.append("非常抱歉,你打不过${boss.name}反而被${boss.name}打死了,您将在附近的传送点复活")
                }
                return sb.toString()
            }else{
                return "该地区没有ID为${bossId}的boss"
            }
        }



        return "该地区没有可以对战的对象"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }

    @Async("taskExecutor")
    fun fightToBoss(account:AccountDTO,boss:Boss): CompletableFuture<Boolean> {
        log.debug("玩家属性 => {}",account)
        log.debug("boss属性 => {}",boss)
        var isFightSuccess = false
        var accountHealth = account.health
        var bossHealth = boss.health
        while(accountHealth > 0 && !isFightSuccess){
            log.debug("玩家血量: {} boss血量: {}",accountHealth,bossHealth)
            //玩家攻击 大于 怪物防御
            if (account.attack > boss.defensive){
                if (bossHealth > 0){
                    bossHealth -= account.attack - boss.defensive
                }else {
                    isFightSuccess = true
                }
            }

            if (boss.attack > account.defensive){
                accountHealth -= boss.attack - account.defensive
            }

            if (accountHealth <= 0){
                isFightSuccess = false
            }

        }

        return CompletableFuture.completedFuture(isFightSuccess)
    }
}
