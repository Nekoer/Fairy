package com.hcyacg.fairy.core.command.method

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.core.DependenceService
import com.hcyacg.fairy.core.command.Command
import com.hcyacg.fairy.core.command.GameCommandService
import com.hcyacg.fairy.dto.AccountDTO
import com.hcyacg.fairy.dto.FightBossResponse
import com.hcyacg.fairy.entity.AccountPackage
import com.hcyacg.fairy.entity.Boss
import com.hcyacg.fairy.entity.BossItem
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.KeyScanOptions
import org.springframework.data.redis.core.RedisCallback
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.concurrent.CompletableFuture
import kotlin.math.abs
import kotlin.math.ceil

/**
 * @Author Nekoer
 * @Date  2/21/2023 18:53
 * @Description
 **/
@Service
@Command("", "对战 [0-9]+", "对战 id 描述: 与可对战对象进行战斗,id是地图上怪物的id")
class FightSystem : GameCommandService, DependenceService() {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun group(sender: Long, group: Long, message: String): String {
        val accountDTO = accountService.info(sender)
        val pos = worldMapService.position(accountDTO!!.account.worldMapId)
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

        val bossId = message.replace("对战 ", "").toInt()
        //选择打哪一只
        keys?.let {
            if (it.contains(key.plus(bossId))) {
                val boss = bossService.getById(bossId)

                //怪物和玩家同时无法破甲 双方都打不出伤害，这时可能会进入无限循环
                if (boss.attack <= accountDTO.defensive && accountDTO.attack <= boss.defensive) {
                    return "您和${boss.name}都拿对手无可奈何,双双离去~"
                }

                //TODO 还没有计算boss和玩家的增益属性 以及使用道具
                val fight = fightToBoss(accountDTO, boss)

                //等待多线程结束
                CompletableFuture.allOf(fight).join()
                if (fight.get().victory) {
                    //胜利
                    redisUtil.del(key.plus(bossId))
                    accountDTO.account.exp = accountDTO.account.exp + boss.victoryExp(accountDTO.level.level)

                    //TODO 扣除玩家战斗过程中使用的法力和血量,还有法力没算
                    accountDTO.account.subtractMana -= fight.get().accountHealth
                    if(!accountService.updateById(accountDTO.account)){
                        return "玩家属性更新失败"
                    }

                    // 添加技能熟练度
                    val accountExercise = accountExerciseService.getById(accountDTO.account.accountExerciseId)
                    val sb = StringBuffer()
                    accountExercise?.let {
                        // 总熟练度不能超过100
                        // 击败boss所提升的技能熟练度
                        val level = boss.level - accountDTO.level.level
                        val bossExercise = if (level > 0) {
                            (level * 0.5)
                        } else {
                            //取负值的绝对值
                            abs(level * 0.25)
                        }
                        log.debug("击败boss所提升的技能熟练度 => {}",bossExercise)
                        val skillful = ceil(BigDecimal.valueOf(bossExercise).multiply(BigDecimal.valueOf(accountExercise.skillful).divide(
                            BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(2))).toDouble()).toLong()

                        log.debug("熟练度 => {}",skillful)
                        if (accountExercise.skillful < 100) {
                            if (accountExercise.skillful + skillful >= 100) {
                                accountExercise.skillful = 100
                                if (!accountExerciseService.updateById(accountExercise)) {
                                    return "技能熟练度更新失败"
                                } else {
                                    sb.append(",${exerciseService.getById(accountExercise.exerciseId).name}技能熟练度增加了${skillful}%")
                                }
                            } else {
                                accountExercise.skillful += skillful
                                if (!accountExerciseService.updateById(accountExercise)) {
                                    return "技能熟练度更新失败"
                                } else {
                                    sb.append(",${exerciseService.getById(accountExercise.exerciseId).name}技能熟练度增加了${skillful}%")
                                }
                            }
                        }
                    }

                    //击败boss后的掉落物
                    val list = bossItemService.list(QueryWrapper<BossItem>().eq("boss_id", boss.id))
                    log.debug("掉落物 => {}",list)
                    // 奖励多少个物品
                    val num = (1..list.size).random()
                    log.debug("奖励多少个物品 => {}",num)
                    val bossItemPackage = StringBuffer()
                    for (i in 0 until num) {
                        val bossItem = list[list.indices.random()]
                        log.debug("击败boss获得的奖励 => {}",bossItem)
                        val one = accountPackageService.getOne(
                            QueryWrapper<AccountPackage>().eq("item_id", bossItem.itemId)
                                .eq("account_id", accountDTO.account.id)
                        )
                        if (one != null) {
                            one.quantity += bossItem.num
                            val item = itemService.getById(one.itemId)
                            if (accountPackageService.updateById(one)) {

                                bossItemPackage.append("\n").append("获得:").append("\n").append("${item.name} *${bossItem.num}")
                            } else {
                                bossItemPackage.append("\n")
                                    .append("${item.name}添加失败, 数量${bossItem.num}个.请联系管理员进行补偿")
                            }
                        } else {
                            val item = itemService.getById(bossItem.itemId)
                            if (accountPackageService.save(
                                    AccountPackage(
                                        0,
                                        accountDTO.account.id,
                                        bossItem.itemId,
                                        bossItem.num
                                    )
                                )
                            ) {
                                bossItemPackage.append("\n").append("获得:").append("\n").append("${item.name} *${bossItem.num}")
                            } else {
                                bossItemPackage.append("\n")
                                    .append("${item.name}添加失败, 数量${bossItem.num}个.请联系管理员进行补偿")
                            }
                        }
                    }

                    if (accountService.updateById(accountDTO.account)) {
                        fight.get().process.append("\n")
                            .append("恭喜您战斗成功,获得${boss.victoryExp(accountDTO.level.level)}点经验").append(sb).append(bossItemPackage)
                    } else {
                        fight.get().process.append("\n").append("恭喜您战斗成功,但是数据更新失败")
                    }
                } else {
                    //失败
                    fight.get().process.append("\n")
                        .append("非常抱歉,你打不过${boss.name}反而被${boss.name}打死了,您将在附近的传送点复活")
                }
                return fight.get().process.toString()
            } else {
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
    fun fightToBoss(account: AccountDTO, boss: Boss): CompletableFuture<FightBossResponse> {
        log.debug("玩家属性 => {}", account)
        log.debug("boss属性 => {}", boss)
        var isFightSuccess = false
        var accountHealth = account.health
        var bossHealth = boss.health
        val sb = StringBuffer()
        while (accountHealth > 0 && !isFightSuccess) {
            log.debug("玩家血量: {} boss血量: {}", accountHealth, bossHealth)
            //玩家攻击 大于 怪物防御
            sb.append("玩家血量: $accountHealth boss血量: $bossHealth").append("\n")
            if (account.attack > boss.defensive) {
                if (bossHealth > 0) {
                    val damage = account.attack - boss.defensive
                    bossHealth -= damage
                    sb.append("您对${boss.name}造成了${damage}点伤害").append("\n")
                } else {
                    isFightSuccess = true
                }
            }else {
                sb.append("您尝试对${boss.name}发起攻击,但是无法击破${boss.name}的防御").append("\n")
            }

            //如果未对战胜利 并且怪物的攻击大于玩家的防御
            if (!isFightSuccess && boss.attack > account.defensive) {
                val damage = boss.attack - account.defensive
                accountHealth -= boss.attack - account.defensive
                sb.append("${boss.name}对您造成了${damage}点伤害").append("\n")
            }else {
                sb.append("${boss.name}尝试对您发起攻击,但是无法击破您的防御").append("\n")

            }

            //如果玩家血量归零 对战结束
            if (accountHealth <= 0) {
                isFightSuccess = false
            }

        }

        return CompletableFuture.completedFuture(FightBossResponse(isFightSuccess, sb, accountHealth))
    }
}
