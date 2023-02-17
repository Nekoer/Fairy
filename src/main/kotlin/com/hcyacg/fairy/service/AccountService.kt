package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.dto.AccountDTO
import com.hcyacg.fairy.entity.Account

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:09
 * @Description
 **/
interface AccountService : IService<Account> {

    fun register(uin: Long,isRebirth: Boolean = false): Boolean

    fun info(uin: Long): AccountDTO?

    fun rebirth(uin: Long): Boolean

    fun deleteAccount(uin: Long): Boolean
}
