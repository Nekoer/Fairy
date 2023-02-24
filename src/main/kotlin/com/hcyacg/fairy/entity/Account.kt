package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
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
    @TableId(type = IdType.AUTO)
    val id: Long,
    val uin: Long,
    var exp: Long, // 拥有的经验值
    var level: Long, // 等级
    @TableField("subtract_health")
    var subtractHealth:Long, // 扣除的血量
    @TableField("subtract_mana")
    var subtractMana:Long, // 扣除的法力
    var point: BigDecimal, //点数 只能充值
    @TableField("ling_root_id")
    val lingRootId: Long, //灵根
    @TableField("ethnicity_id")
    val ethnicityId: Long, //种族
    @TableField("world_map_id")
    var worldMapId: Long, //位置
    var probability:Long, //突破概率
    @TableField("account_exercise_id")
    val accountExerciseId: Long?
) {
    constructor(
        uin: Long,
        exp: Long,
        point: BigDecimal,
        lingRootId: Long,
        ethnicityId: Long,
        worldMapId: Long
    ) : this(0, uin, exp,1,0,0, point, lingRootId, ethnicityId, worldMapId,100,null) {

    }
}
