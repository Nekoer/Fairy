package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.math.BigDecimal

/**
 * @Author Nekoer
 * @Date  2/10/2023 15:42
 * @Description 修真者信息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account")
data class Account(
    @TableId
    val uin: Long,
    val exp: Long, // 拥有的经验值
    val point: BigDecimal, //点数 只能充值
    @TableField("ling_root_id")
    val lingRootId: Long, //点数 只能充值
    @TableField("ethnicity_id")
    val ethnicityId: Long //种族
)
