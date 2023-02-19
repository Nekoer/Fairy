package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.Item
import com.hcyacg.fairy.mapper.ItemMapper
import com.hcyacg.fairy.service.ItemService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:11
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class ItemServiceImpl: ServiceImpl<ItemMapper, Item>(), ItemService {
    override fun info(name: String): List<Item> {
        return try {
            list(QueryWrapper<Item>().eq("name",name))
        }catch (e:Exception){
            e.printStackTrace()
            log.error("根据物品名称查询异常 => {}",e)
            mutableListOf()
        }
    }
}
