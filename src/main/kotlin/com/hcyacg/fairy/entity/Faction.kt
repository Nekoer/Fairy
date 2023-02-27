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
 * @Date  2/25/2023 18:03
 * @Description 宗派
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("faction")
data class Faction(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String,
    @TableField("own_id")
    var ownId: Long,
    var construction: Long, //建设度
    var material: Long, //资材
    var member: Long = 20
){

    constructor(
        name:String,
        ownId:Long
    ):this(0,name,ownId, 0, 0, 20)

}
