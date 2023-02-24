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
 * @Date  2/24/2023 16:04
 * @Description 击败怪物掉落的物品
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("boss_item")
data class BossItem(
    @TableId(type = IdType.AUTO)
    val id: Long,
    @TableField("boss_id")
    val bossId:Long,
    @TableField("item_id")
    val itemId: Long,
    val num: Long
)
