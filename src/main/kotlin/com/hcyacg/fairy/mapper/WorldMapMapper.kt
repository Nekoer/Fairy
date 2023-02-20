package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.WorldMap
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/20/2023 23:09
 * @Description
 **/
@Mapper
@Repository
interface WorldMapMapper : BaseMapper<WorldMap> {
}
