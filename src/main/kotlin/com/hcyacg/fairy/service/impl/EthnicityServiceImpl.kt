package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.Ethnicity
import com.hcyacg.fairy.mapper.EthnicityMapper
import com.hcyacg.fairy.service.EthnicityService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/17/2023 17:54
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class EthnicityServiceImpl:ServiceImpl<EthnicityMapper,Ethnicity>() , EthnicityService {
    override fun randomEthnicity(): Ethnicity? {
        return try {
            val num = count()
            val random = 1 .. num
            getById(random.random())
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }
}
