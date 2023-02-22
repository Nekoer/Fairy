package com.hcyacg.fairy.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.hcyacg.fairy.constant.AppConstant
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/20/2023 19:51
 * @Description 怪物
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("boss")
data class Boss(
    @TableId(type = IdType.AUTO)
    val id: Long,
    val name: String,
    val level: Long,//等级
    val health: Long,//生命值
    val mana: Long, //法力
    val attack: Long, //攻击力
    val defensive: Long, //防御力
) {

    fun victoryExp(accountLevel: Long): Long {
        //同等级怪
        if (accountLevel == level) {
            return AppConstant.EXP_VICTORY + (level * 5)
        }
        //高级怪 玩家小于怪物等级4级
        if (accountLevel + 4 < level) {
            return (AppConstant.EXP_VICTORY + (level * 5)) * (1 + 0.05 * (level - accountLevel)).toLong()
        }
        //玩家等级在怪物等级以下4级范围均正常获得经验
        if (level -accountLevel <= 4){
            return AppConstant.EXP_VICTORY + (level * 5)

        }

        //低级怪
        if (accountLevel > level){
            var zd = 0
            if (level in 1 ..7){
                zd = 5
            }
            if (level in 8 ..9){
                zd = 6
            }
            if (level in 10 ..11){
                zd = 7
            }
            if (level in 12 ..15){
                zd = 8
            }
            if (level in 16 ..19){
                zd = 9
            }
            if (level in 20 ..29){
                zd = 11
            }
            if (level in 30 ..39){
                zd = 12
            }
            if (level in 40 ..44){
                zd = 13
            }
            if (level in 45 ..49){
                zd = 14
            }
            if (level in 50 ..54){
                zd = 15
            }
            if (level in 55 ..59){
                zd = 16
            }
            if (level >= 60){
                zd = 17
            }
            return (AppConstant.EXP_VICTORY + (level * 5)) * (1 -  (accountLevel - level) / zd)
        }
        return AppConstant.EXP_VICTORY
    }

}
