package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.Sign
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/12/2023 20:45
 * @Description
 **/
@Mapper
@Repository
interface SignMapper : BaseMapper<Sign> {
}
