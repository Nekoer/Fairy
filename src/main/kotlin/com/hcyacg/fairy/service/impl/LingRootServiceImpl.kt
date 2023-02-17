package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.LingRoot
import com.hcyacg.fairy.mapper.LingRootMapper
import com.hcyacg.fairy.service.LingRootService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:46
 * @Description
 **/

@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class LingRootServiceImpl : ServiceImpl<LingRootMapper, LingRoot>(), LingRootService {


    override fun randomLingRoot(): LingRoot? {
        try{
            val num = (0 until count()).random() //随机ID
            val wight = (1 ..10).random() //权重
            val lingRoot = getById(num + 1)

            //灵根权重 小于等于 算法权重 则抽到该灵根
            if (lingRoot.weight <= wight){
                return randomLingRoot()
            }
            return lingRoot
        }catch (e:Exception){
            e.printStackTrace()
            log.error("随机抽灵根 => {}",e)
            return null
        }
    }

}
