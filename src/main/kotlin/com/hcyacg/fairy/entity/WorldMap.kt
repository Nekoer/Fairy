package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/20/2023 21:01
 * @Description 世界地图
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("world_map")
data class WorldMap(
    @TableId(type = IdType.AUTO)
    val id: String,
    val name: String, //地址名
    @TableField("top_id")
    val topId: Long?, //往上走的地址id
    @TableField("left_id")
    val leftId: Long?,//往左走的地址id
    @TableField("right_id")
    val rightId: Long?,//往右走的地址id
    @TableField("bottom_id")
    val bottomId: Long?,//往下走的地址id
    @TableField("architecture_id")
    val architectureId: Long, //建筑种类 功能
    @TableField("is_safe")
    val isSafe: Int //是否为安全点 不会主动生成怪物
)
