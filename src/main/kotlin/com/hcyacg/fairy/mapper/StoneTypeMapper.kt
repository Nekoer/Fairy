package com.hcyacg.fairy.server.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.server.entity.StoneType
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:20
 * @Description
 **/
@Mapper
@Repository
interface StoneTypeMapper : BaseMapper<StoneType> {
}
