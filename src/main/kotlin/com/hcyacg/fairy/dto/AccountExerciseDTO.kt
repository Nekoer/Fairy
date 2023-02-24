package com.hcyacg.fairy.dto

import com.hcyacg.fairy.entity.AccountExercise
import com.hcyacg.fairy.entity.Exercise
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.math.BigDecimal
import java.math.RoundingMode

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
        val healthBuff = (BigDecimal.valueOf(accountExercise.skillful).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP)).add(BigDecimal.valueOf(exercise.healthBuff))
        val manaBuff = (BigDecimal.valueOf(accountExercise.skillful).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP)).add(BigDecimal.valueOf(exercise.manaBuff))
        val attackBuff = (BigDecimal.valueOf(accountExercise.skillful).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP)).add(BigDecimal.valueOf(exercise.attackBuff))
        val defensiveBuff = (BigDecimal.valueOf(accountExercise.skillful).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP)).add(BigDecimal.valueOf(exercise.defensiveBuff))
        return ExerciseDTO(
            exercise.id,
            exercise.name,
            healthBuff.toDouble(),
            manaBuff.toDouble(),
            attackBuff.toDouble(),
            defensiveBuff.toDouble(),
            exercise.maxLevel,
            accountExercise.skillful
        )
    }
}
