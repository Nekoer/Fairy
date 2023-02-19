package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.ItemUsage
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:02
 * @Description
 **/
@Mapper
@Repository
interface ItemUsageMapper:BaseMapper<ItemUsage> {
}
