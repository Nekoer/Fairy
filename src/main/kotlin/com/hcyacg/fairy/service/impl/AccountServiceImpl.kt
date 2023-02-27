package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.dto.AccountDTO
import com.hcyacg.fairy.dto.ExerciseDTO
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
import kotlin.math.ceil

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

    @Autowired
    private lateinit var accountPackageService: AccountPackageService

    @Autowired
    lateinit var accountExerciseService: AccountExerciseService

    @Autowired
    lateinit var factionService: FactionService
    override fun register(
        uin: Long,
        isRebirth: Boolean
    ): Boolean {
        try {

            if (!isRebirth) {

                if (getOne(QueryWrapper<Account>().eq("uin", uin)) != null) {
                    log.debug("用户已注册")
                    return false
                }
            }


            log.debug("注册的用户uin => $uin")
            val lingRoot = lingRootService.randomLingRoot()

            lingRoot?.let {
                //TODO worldMapId 之后获取传送点列表随机选择出生点
                ethnicityService.randomEthnicity()?.let {
                    return save(
                        Account(
                            uin,
                            0,
                            BigDecimal.valueOf(0),
                            lingRoot.id,
                            it.id,
                            1
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
            val account = getOne(QueryWrapper<Account>().eq("uin", uin))
            account?.let { it ->
                val lingRoot = lingRootService.getById(account.lingRootId)
                val ethnicity = ethnicityService.getById(account.ethnicityId)
                val hierarchical: Hierarchical = hierarchicalService.getById(it.level)

                val faction = factionService.getById(it.factionId)
                var exerciseDTO: ExerciseDTO? = null
                it.accountExerciseId?.let { id ->
                    val accountExerciseDTO = accountExerciseService.accountExercise(id, account.id)
                    exerciseDTO = accountExerciseDTO.calculation()
                }

                var upgrade: Hierarchical? = null
                if (it.level < hierarchicalService.count()) {
                    upgrade = hierarchicalService.getById(it.level + 1)
                }
                var health = 0L//生命值
                var mana = 0L //法力
                var attack = 0L //攻击力
                var defensive = 0L //防御力

                run outside@{
                    //等级体系 计算 玩家基本数据
                    hierarchicalService.list().forEachIndexed { index, h ->
                        if (h.level > account.level) {
                            return@outside
                        }
                        health += h.health
                        mana += h.mana
                        attack += h.attack
                        defensive += h.defensive
                    }
                }

                var healthAddition: Long = 0
                var manaAddition: Long = 0
                var attackAddition: Long = 0
                var defensiveAddition: Long = 0
                //计算功法加成 熟练度
                exerciseDTO?.let { exercise ->
                    healthAddition = ceil(exercise.healthBuff * health).toLong()
                    manaAddition = ceil(exercise.manaBuff * mana).toLong()
                    attackAddition = ceil(exercise.attackBuff * attack).toLong()
                    defensiveAddition = ceil(exercise.defensiveBuff * defensive).toLong()
                }
                //TODO 计算玩家装备等数据


                //减去扣除的血量和法力
                health -= it.subtractHealth
                mana -= it.subtractMana


                AccountDTO(
                    account,
                    lingRoot,
                    ethnicity,
                    faction,
                    hierarchical,
                    upgrade,
                    exerciseDTO,
                    health + healthAddition,
                    mana + manaAddition,
                    attack + attackAddition,
                    defensive + defensiveAddition,
                    healthAddition,
                    manaAddition,
                    attackAddition,
                    defensiveAddition,
                    health + healthAddition + it.subtractHealth,
                    mana + manaAddition + it.subtractMana
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("获取修仙信息异常 => {}", e)
            null
        }
    }

    override fun rebirth(id: Long): Boolean {

        //TODO 重生需要消耗
        return try {
            deleteAccount(id)
            signService.deleteSign(id)
            accountPackageService.deleteAccountPackage(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("重入仙途异常 => {}", e)
            false
        }
    }

    override fun deleteAccount(id: Long): Boolean {
        return try {
            removeById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("删除账号异常 => {}", e)
            return false
        }
    }

}
