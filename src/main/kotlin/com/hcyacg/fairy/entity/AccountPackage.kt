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
 * @Date  2/19/2023 17:56
 * @Description 背包 ps: 背包不应该涉及数据修改
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account_package")
data class AccountPackage(
    @TableId(type = IdType.AUTO)
    val id: Long,
    @TableField("account_id")
    val accountId: Long,
    @TableField("item_id")
    val itemId: Long, // 物品id
    val quantity: Long // 数量
)
