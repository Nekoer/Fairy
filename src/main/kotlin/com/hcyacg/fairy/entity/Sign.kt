package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/12/2023 20:40
 * @Description 签到 超过上次签到24小时后重新计算
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sign")
data class Sign(
    @TableId
    val uin:Long,
    var time:Long,
    @TableField("sign_day")
    var signDay:Long
)
