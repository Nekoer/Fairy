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
    val top: Long, //往上走的地址id
    val left: Long,//往左走的地址id
    val right: Long,//往右走的地址id
    val bottom: Long,//往下走的地址id
    @TableField("architecture_id")
    val architectureId: Long
)
