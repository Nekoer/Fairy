package com.hcyacg.fairy.command.method.faction.mission

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.hcyacg.fairy.DependenceService
import com.hcyacg.fairy.command.Command
import com.hcyacg.fairy.command.GameCommandService
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.entity.AccountFaction
import com.hcyacg.fairy.entity.AccountFactionMission
import com.hcyacg.fairy.entity.Wallet
import org.springframework.stereotype.Service
import java.math.BigDecimal

/**
 * @Author Nekoer
 * @Date  2/27/2023 19:55
 * @Description
 **/
@Service
@Command("", "宗门任务完成 [0-9]+", "宗门任务完成 任务id 描述: 完成任务")
class MissionComplete : GameCommandService, DependenceService() {
    override fun group(sender: Long, group: Long, message: String): String {
        try {
            val account = accountService.info(sender)
            val factionMissionId = message.replace("宗门任务完成 ", "").toLongOrNull()
            factionMissionId?.let {
                account?.let {
                    val accountFaction = accountFactionService.getOne(QueryWrapper<AccountFaction>().eq("account_id",account.account.id))

                    val redisKey = "${AppConstant.FACTION_MISSION_CD}:${account.account.id}"
                    if (redisUtil.hasKey(redisKey)) {
                        return "请等待任务完成CD,每次提交任务需间隔30分钟"
                    }


                    val faction = factionService.getById(accountFaction.factionId)
                    faction?.let {
                        //玩家已经领取的任务
                        val accountMissionList = accountFactionMissionService.list(
                            QueryWrapper<AccountFactionMission>().eq(
                                "account_id",
                                account.account.id
                            )
                        )
                        //找到目前的任务
                        val accountMission =
                            accountMissionList.find { a: AccountFactionMission -> a.factionMissionId == factionMissionId }
                        accountMission?.let {
                            //找到任务
                            val factionMission = factionMissionService.getById(factionMissionId)

                            factionMission?.let {
                                when (it.type) {
                                    //气血
                                    1L -> {

                                        val hp = BigDecimal.valueOf(account.health).multiply(
                                            BigDecimal.valueOf(it.cost)
                                        ).toLong()
                                        if (account.health - hp <= 0) {
                                            return "您的血量不够完成本次任务"
                                        }

                                        account.account.subtractHealth += hp
                                        account.account.exp += BigDecimal.valueOf(account.account.exp).multiply(
                                            BigDecimal.valueOf(it.give)
                                        ).toLong()
                                        //给与所在宗门 储备的灵石，同时会增加灵石 * 10 的建设度
                                        faction.material += it.sect
                                        faction.construction += it.sect * 10
                                        accountFaction.contribution += it.sect * 10

                                        if (!factionService.updateById(faction)) {
                                            throw RuntimeException("完成任务失败,宗门数据更新失败")
                                        }
                                        if (!accountService.updateById(account.account)) {
                                            throw RuntimeException("完成任务失败,玩家数据更新失败")
                                        }
                                        if (!accountFactionService.updateById(accountFaction)) {
                                            throw RuntimeException("完成任务失败,玩家宗门数据更新失败")
                                        }
                                        accountMission.status = AppConstant.FACTION_MISSION_STATE_SUCCESS
                                        if (!accountFactionMissionService.updateById(accountMission)) {
                                            throw RuntimeException("完成任务失败,宗门任务数据更新失败")
                                        }
                                        redisUtil[redisKey] = 1
                                        redisUtil.expire(redisKey, 1800L)
                                        return "任务已完成"
                                    }
                                    //灵石
                                    2L -> {
                                        val wallet = walletService.getOne(QueryWrapper<Wallet>().eq("account_id",account.account.id).eq("stone_id",1).orderByAsc("id"))
                                        var money = 0L

                                        wallet?.let {
                                            val stone = stoneService.getById(wallet.stoneId)
                                            money += stone.stoneProportion * wallet.quantity
                                        }

                                        if (money < BigDecimal.valueOf(factionMission.cost).toLong()){
                                            return "您没有足够的灵石来完成本次任务(默认使用下品灵石)"
                                        }

                                        val nowMoney = money - BigDecimal.valueOf(factionMission.cost).toLong()
                                        wallet.quantity = nowMoney
                                        if (!walletService.updateById(wallet)){
                                            throw RuntimeException("钱包更新数据失败")
                                        }
                                        account.account.exp += BigDecimal.valueOf(account.account.exp).multiply(
                                            BigDecimal.valueOf(it.give)
                                        ).toLong()
                                        //给与所在宗门 储备的灵石，同时会增加灵石 * 10 的建设度
                                        faction.material += it.sect
                                        faction.construction += it.sect * 10
                                        accountFaction.contribution += it.sect * 10
                                        if (!factionService.updateById(faction)) {
                                            throw RuntimeException("完成任务失败,宗门数据更新失败")
                                        }
                                        if (!accountFactionService.updateById(accountFaction)) {
                                            throw RuntimeException("完成任务失败,玩家宗门数据更新失败")
                                        }
                                        accountMission.status = AppConstant.FACTION_MISSION_STATE_SUCCESS
                                        if (!accountFactionMissionService.updateById(accountMission)) {
                                            throw RuntimeException("完成任务失败,宗门任务数据更新失败")
                                        }
                                        redisUtil[redisKey] = 1
                                        redisUtil.expire(redisKey, 1800L)
                                        return "任务已完成"
                                    }
                                    else -> {
                                        return "任务已失败"
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "完成任务失败"
    }

    override fun channel(sender: Long, guild: Long, channel: Long, message: String): String {
        TODO("Not yet implemented")
    }

    override fun chat(sender: Long, message: String): String {
        TODO("Not yet implemented")
    }
}
