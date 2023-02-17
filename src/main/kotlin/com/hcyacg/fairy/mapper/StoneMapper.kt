package com.hcyacg.fairy.server.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.server.entity.Stone
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:19
 * @Description
 **/
@Mapper
@Repository
interface StoneMapper:BaseMapper<Stone> {
}
