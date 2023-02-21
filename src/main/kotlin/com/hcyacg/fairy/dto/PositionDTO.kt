package com.hcyacg.fairy.dto

import com.hcyacg.fairy.entity.WorldMap
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/21/2023 12:51
 * @Description 当前位置的前后左右
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
data class PositionDTO (
    val now: WorldMap,
    val top: WorldMap?,
    val left: WorldMap?,
    val right: WorldMap?,
    val bottom: WorldMap?,
)
