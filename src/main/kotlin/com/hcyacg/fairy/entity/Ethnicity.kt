package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/17/2023 17:52
 * @Description 种族
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ethnicity")
data class Ethnicity(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String
)
