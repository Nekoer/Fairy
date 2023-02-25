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
 * @Date  2/25/2023 13:09
 * @Description 个人钱包
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("wallet")
data class Wallet (
    @TableId(type = IdType.AUTO)
    val id: Long,
    @TableField("account_id")
    val accountId:Long,
    @TableField("stone_id")
    val stoneId: Long,
    var quantity: Long // 数量
)
