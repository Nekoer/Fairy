package com.hcyacg.fairy.command

import com.hcyacg.fairy.service.*
import com.hcyacg.fairy.utils.Base64Util
import com.hcyacg.fairy.utils.SkikoUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @Author Nekoer
 * @Date  2/21/2023 11:46
 * @Description
 **/
@Service
class DependenceService {
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

}
