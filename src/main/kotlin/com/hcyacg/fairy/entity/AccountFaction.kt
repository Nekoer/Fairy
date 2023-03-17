package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  3/17/2023 16:52
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account_faction")
data class AccountFaction(
    @TableId(type = IdType.AUTO)
    val id: Long?,
    @TableField(value = "faction_id", updateStrategy = FieldStrategy.IGNORED)
    var factionId: Long,
    @TableField("account_id")
    val accountId: Long,
    var contribution: Long, //贡献值
    var state: Long, //状态
    @TableField("patriarch_id")
    var patriarchId : Long //职位
){
    constructor(
         factionId: Long,
         accountId: Long,
         contribution: Long, //贡献值
         state: Long, //状态
         patriarchId : Long //职位
    ):this(null,factionId, accountId, contribution, state, patriarchId)
}
