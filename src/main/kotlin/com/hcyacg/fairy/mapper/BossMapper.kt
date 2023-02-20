package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.Boss
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/20/2023 20:28
 * @Description
 **/
@Mapper
@Repository
interface BossMapper: BaseMapper<Boss> {
}
