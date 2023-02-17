package com.hcyacg.fairy.server.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/10/2023 16:41
 * @Description 灵石、仙石 系统内交易货币及修炼法力所需
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("stone")
data class Stone(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String,
    @TableField("mana_proportion")
    val manaProportion: Int, // 法力兑换比例
    @TableField("stone_proportion")
    val stoneProportion: Int, // 货币兑换比例
)
