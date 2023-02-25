package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.dto.AccountItemPage
import com.hcyacg.fairy.entity.AccountPackage

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:04
 * @Description
 **/
interface AccountPackageService : IService<AccountPackage> {

    fun getPackagePage(accountId: Long,page:Long) : AccountItemPage?
    fun deleteAccountPackage(accountId: Long): Boolean
}
