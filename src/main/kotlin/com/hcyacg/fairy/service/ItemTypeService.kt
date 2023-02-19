package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.entity.ItemType

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:04
 * @Description
 **/
interface ItemTypeService : IService<ItemType> {

    fun getItemTypeById(id: Long): ItemType?

}
