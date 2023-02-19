package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.ItemType
import com.hcyacg.fairy.mapper.ItemTypeMapper
import com.hcyacg.fairy.service.ItemTypeService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:12
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class ItemTypeServiceImpl: ServiceImpl<ItemTypeMapper, ItemType>(), ItemTypeService {
    override fun getItemTypeById(id: Long): ItemType? {
        return try {
            getById(id)
        }catch (e:Exception){
            e.printStackTrace()
            log.error("获取物品种类异常 => {}",e)
            null
        }
    }
}
