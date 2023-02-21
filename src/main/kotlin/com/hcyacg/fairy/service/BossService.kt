package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.entity.Boss

/**
 * @Author Nekoer
 * @Date  2/20/2023 20:27
 * @Description
 **/
interface BossService : IService<Boss> {

    fun randomBoss() :Boss

}
