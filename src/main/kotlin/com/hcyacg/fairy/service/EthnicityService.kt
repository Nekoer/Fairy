package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.entity.Ethnicity

/**
 * @Author Nekoer
 * @Date  2/17/2023 17:54
 * @Description
 **/
interface EthnicityService: IService<Ethnicity> {

    fun randomEthnicity() : Ethnicity?

}
