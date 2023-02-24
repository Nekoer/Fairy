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
 * @Date  2/24/2023 13:15
 * @Description 玩家的功法 熟练度
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account_exercise")
data class AccountExercise(
    @TableId(type = IdType.AUTO)
    val id: Long,
    @TableField("account_id")
    val accountId: Long,
    @TableField("exercise_id")
    val exerciseId: Long,
    var skillful: Long //熟练度
)
