package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.FactionMission
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/27/2023 18:39
 * @Description
 **/
@Mapper
@Repository
interface FactionMissionMapper:BaseMapper<FactionMission> {
}
