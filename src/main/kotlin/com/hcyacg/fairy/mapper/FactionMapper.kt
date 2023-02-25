package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.Faction
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/25/2023 20:38
 * @Description
 **/
@Mapper
@Repository
interface FactionMapper : BaseMapper<Faction> {
}
