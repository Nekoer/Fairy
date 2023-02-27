package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/27/2023 18:37
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("faction_mission")
data class FactionMission(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String,
    val description: String,
    val type: Long,// type=1：需要扣气血，type=2：需要扣灵石
    val cost: Double,//消耗，type=1时，气血百分比，type=2时，消耗灵石
    val give: Double,//给与玩家当前修为的百分比修为
    val sect: Long, // 给与所在宗门 储备的灵石，同时会增加灵石 * 10 的建设度
)
