package com.hcyacg.fairy.dto

import com.hcyacg.fairy.entity.AccountExercise
import com.hcyacg.fairy.entity.Exercise
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @Author Nekoer
 * @Date  2/24/2023 14:03
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
data class AccountExerciseDTO(
    val exercise: Exercise,
    val accountExercise: AccountExercise
) {
    //计算熟练度 功法属性
    fun calculation(): ExerciseDTO {
        val healthBuff = (accountExercise.skillful / 100) + exercise.healthBuff
        val manaBuff = (accountExercise.skillful / 100) + exercise.manaBuff
        val attackBuff = (accountExercise.skillful / 100) + exercise.attackBuff
        val defensiveBuff = (accountExercise.skillful / 100) + exercise.defensiveBuff
        return ExerciseDTO(
            exercise.id,
            exercise.name,
            healthBuff,
            manaBuff,
            attackBuff,
            defensiveBuff,
            exercise.maxLevel,
            accountExercise.skillful
        )
    }
}
