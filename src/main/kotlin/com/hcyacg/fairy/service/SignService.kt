package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.entity.Sign

/**
 * @Author Nekoer
 * @Date  2/12/2023 20:45
 * @Description
 **/
interface SignService : IService<Sign> {

    fun sign(uin: Long): String

    fun deleteSign(uin: Long): Boolean
}
