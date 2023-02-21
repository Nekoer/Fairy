package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.dto.PositionDTO
import com.hcyacg.fairy.entity.WorldMap
import com.hcyacg.fairy.mapper.WorldMapMapper
import com.hcyacg.fairy.service.WorldMapService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:50
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class WorldMapServiceImpl : ServiceImpl<WorldMapMapper, WorldMap>(), WorldMapService {
    override fun position(id: Long): PositionDTO {
        val now = getById(id)
        val top = getById(now.topId)
        val left = getById(now.leftId)
        val right = getById(now.rightId)
        val bottom = getById(now.bottomId)
        return PositionDTO(now, top, left, right, bottom)
    }
}
