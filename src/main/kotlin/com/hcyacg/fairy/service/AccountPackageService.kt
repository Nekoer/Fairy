package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.dto.AccountItem
import com.hcyacg.fairy.entity.AccountPackage

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:04
 * @Description
 **/
interface AccountPackageService : IService<AccountPackage> {

    fun getPackageList(accountId: Long) : List<AccountItem>

    fun deleteAccountPackage(accountId: Long): Boolean
}
