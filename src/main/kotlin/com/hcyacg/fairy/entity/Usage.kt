package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/19/2023 16:44
 * @Description 道具用途 或 buff
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("usage")
data class Usage(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String,
    val description: String
)
