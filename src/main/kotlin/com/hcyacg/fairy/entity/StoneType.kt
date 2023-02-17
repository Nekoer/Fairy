package com.hcyacg.fairy.server.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/10/2023 16:46
 * @Description 货币的种类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("stone_type")
data class StoneType(
    @TableId(type = IdType.AUTO)
    val id: Int,
    val name: String
)
