package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.LingRoot
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:18
 * @Description
 **/
@Mapper
@Repository
interface LingRootMapper : BaseMapper<LingRoot> {
}
