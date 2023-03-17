package com.hcyacg.fairy

import com.hcyacg.fairy.service.*
import com.hcyacg.fairy.utils.Base64Util
import com.hcyacg.fairy.utils.RedisUtil
import com.hcyacg.fairy.utils.SkikoUtil
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 11:46
 * @Description
 **/
@Service
class DependenceService {
    @Autowired
    lateinit var redisTemplate : RedisTemplate<String, Any>

    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    @Autowired
    lateinit var signService: SignService

    @Autowired
    lateinit var accountPackageService: AccountPackageService

    @Autowired
    lateinit var itemService: ItemService

    @Autowired
    lateinit var moveService: MoveService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var skikoUtil: SkikoUtil

    @Autowired
    lateinit var base64Util: Base64Util

    @Autowired
    lateinit var worldMapService: WorldMapService

    @Autowired
    lateinit var bossService: BossService
    @Autowired
    lateinit var redisUtil: RedisUtil
    @Autowired
    lateinit var accountExerciseService: AccountExerciseService
    @Autowired
    lateinit var exerciseService: ExerciseService

    @Autowired
    lateinit var bossUsageService: BossUsageService
    @Autowired
    lateinit var bossItemService: BossItemService
    @Autowired
    lateinit var itemUsageService: ItemUsageService
    @Autowired
    lateinit var bankService: BankService
    @Autowired
    lateinit var stoneService: StoneService
    @Autowired
    lateinit var walletService: WalletService
    @Autowired
    lateinit var seclusionService: SeclusionService
    @Autowired
    lateinit var factionService: FactionService
    @Autowired
    lateinit var factionMissionService: FactionMissionService
    @Autowired
    lateinit var accountFactionMissionService: AccountFactionMissionService
    @Autowired
    lateinit var accountFactionService: AccountFactionService
}
