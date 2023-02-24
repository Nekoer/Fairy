package com.hcyacg.fairy.dto

/**
 * @Author Nekoer
 * @Date  2/24/2023 14:05
 * @Description
 **/
data class ExerciseDTO (
    val id: Long,
    val name: String,
    val healthBuff: Double,//增益生命值
    val manaBuff: Double, //增益法力
    val attackBuff: Double, //增益攻击力
    val defensiveBuff: Double, //增益防御力
    val maxLevel: Long, //最高升级等级
    val skillful: Long //技能熟练度
)
