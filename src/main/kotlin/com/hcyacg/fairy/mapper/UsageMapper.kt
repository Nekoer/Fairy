package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.Usage
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/19/2023 17:59
 * @Description
 **/
@Mapper
@Repository
interface UsageMapper : BaseMapper<Usage> {
}
