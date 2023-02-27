package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.AccountFactionMission
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/27/2023 19:35
 * @Description
 **/
@Mapper
@Repository
interface AccountFactionMissionMapper :BaseMapper<AccountFactionMission>{
}
