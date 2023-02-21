package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.dto.PositionDTO
import com.hcyacg.fairy.entity.WorldMap

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:49
 * @Description
 **/
interface WorldMapService : IService<WorldMap> {

    fun position(id:Long): PositionDTO

}
