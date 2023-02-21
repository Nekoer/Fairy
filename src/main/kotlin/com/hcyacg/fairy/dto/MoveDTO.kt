package com.hcyacg.fairy.dto

import com.hcyacg.fairy.entity.WorldMap
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/21/2023 10:12
 * @Description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
data class MoveDTO(
    val move: Boolean, //是否移动成功
    val before: WorldMap, //移动之前的位置
    val after: WorldMap //移动之后的位置
)
