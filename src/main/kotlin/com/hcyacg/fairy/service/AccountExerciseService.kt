package com.hcyacg.fairy.service

import com.baomidou.mybatisplus.extension.service.IService
import com.hcyacg.fairy.dto.AccountExerciseDTO
import com.hcyacg.fairy.entity.AccountExercise

/**
 * @Author Nekoer
 * @Date  2/24/2023 14:14
 * @Description
 **/
interface AccountExerciseService:IService<AccountExercise> {

    fun accountExercise(exerciseId: Long,accountId: Long):AccountExerciseDTO

}
