package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.AccountFaction
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  3/17/2023 16:58
 * @Description
 **/
@Mapper
@Repository
interface AccountFactionMapper:BaseMapper<AccountFaction> {
}
