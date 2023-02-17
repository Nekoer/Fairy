package com.hcyacg.fairy.server.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.server.entity.Account

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:09
 * @Description
 **/
interface AccountService : IService<Account> {

    fun register(uin: Long): Boolean

}
