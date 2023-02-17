package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.Ethnicity
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/17/2023 17:53
 * @Description
 **/
@Mapper
@Repository
interface EthnicityMapper : BaseMapper<Ethnicity> {
}
