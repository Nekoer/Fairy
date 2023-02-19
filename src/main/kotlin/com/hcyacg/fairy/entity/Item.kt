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
 * @Date  2/19/2023 16:27
 * @Description 物品
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("item")
data class Item(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String,
    @TableField("item_type_id")
    val itemTypeId: Long, //物品种类
    val description: String
)
