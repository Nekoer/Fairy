package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/25/2023 15:40
 * @Description 闭关 出关
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("seclusion")
data class Seclusion(
    @TableId
    val id:Long,
    @TableField("account_id")
    val accountId:Long,
    val time: Long
)
