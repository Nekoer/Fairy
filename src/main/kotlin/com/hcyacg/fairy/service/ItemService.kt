package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.entity.Item

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:04
 * @Description
 **/
interface ItemService : IService<Item> {

    fun info(name: String): List<Item>

}
