package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.entity.LingRoot

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:45
 * @Description
 **/
interface LingRootService : IService<LingRoot> {

    fun randomLingRoot(): LingRoot?

}
