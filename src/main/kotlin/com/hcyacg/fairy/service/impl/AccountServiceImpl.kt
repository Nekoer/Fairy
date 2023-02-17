package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.dto.AccountDTO
import com.hcyacg.fairy.entity.Account
import com.hcyacg.fairy.entity.Hierarchical
import com.hcyacg.fairy.mapper.AccountMapper
import com.hcyacg.fairy.service.*
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:09
 * @Description
 **/

@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class AccountServiceImpl : ServiceImpl<AccountMapper, Account>(), AccountService {
    @Autowired
    private lateinit var hierarchicalService: HierarchicalService

    @Autowired
    private lateinit var lingRootService: LingRootService

    @Autowired
    private lateinit var signService: SignService

    @Autowired
    private lateinit var ethnicityService: EthnicityService

    override fun register(
        uin: Long,
        isRebirth: Boolean
    ): Boolean {
        try {

            if (!isRebirth) {
                if (getById(uin) != null) {
                    log.debug("用户已注册")
                    return false
                }
            }


            log.debug("注册的用户uin => $uin")
            val lingRoot = lingRootService.randomLingRoot()

            lingRoot?.let {
                ethnicityService.randomEthnicity()?.let {
                    return save(
                        Account(
                            uin,
                            0,
                            BigDecimal.valueOf(0),
                            lingRoot.id,
                            it.id
                        )
                    )
                }
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("用户注册异常 => {}", e)
            return false
        }
    }

    override fun info(uin: Long): AccountDTO? {
        return try {
            val account = getById(uin)
            account?.let {
                val lingRoot = lingRootService.getById(account.lingRootId)
                val ethnicity = ethnicityService.getById(account.ethnicityId)
                var hierarchical: Hierarchical = hierarchicalService.getById(1)
                var upgrade: Hierarchical = hierarchicalService.getById(2)
                var health = 0L//生命值
                var mana = 0L //法力
                var attack = 0L //攻击力
                var defensive = 0L //防御力

                run outside@{
                    //等级体系 计算 玩家基本数据
                    hierarchicalService.list().forEachIndexed { index, h ->
                        health += h.health
                        mana += h.mana
                        attack += h.attack
                        defensive += h.defensive
                        if (h.exp >= account.exp) {
                            hierarchical = h
                            upgrade = hierarchicalService.getById(index + 1)
                            return@outside
                        }
                    }
                }
                //TODO 计算玩家装备等数据

                AccountDTO(
                    account,
                    lingRoot,
                    ethnicity,
                    hierarchical,
                    upgrade,
                    health,
                    mana,
                    attack,
                    defensive
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("获取修仙信息异常 => {}", e)
            null
        }
    }

    override fun rebirth(uin: Long): Boolean {

        //TODO 重生需要消耗
        return try {
            val info = info(uin)
            if (info != null) {
                if (!deleteAccount(uin)) return false
                if (!signService.deleteSign(uin)) return false
            } else return false

            if (!register(uin, true)) return false
            true
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("重入仙途异常 => {}", e)
            false
        }
    }

    override fun deleteAccount(uin: Long): Boolean {
        return try {
            removeById(uin)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("删除账号异常 => {}", e)
            return false
        }
    }

}
