package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.dto.AccountExerciseDTO
import com.hcyacg.fairy.entity.AccountExercise
import com.hcyacg.fairy.mapper.AccountExerciseMapper
import com.hcyacg.fairy.service.AccountExerciseService
import com.hcyacg.fairy.service.ExerciseService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/24/2023 14:15
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class AccountExerciseServiceImpl: ServiceImpl<AccountExerciseMapper, AccountExercise>(), AccountExerciseService {
    @Autowired
    private lateinit var exerciseService: ExerciseService

    override fun accountExercise(exerciseId: Long,accountId: Long): AccountExerciseDTO {
        val accountExercise = getOne(QueryWrapper<AccountExercise>().eq("account_id", accountId).eq("exercise_id", exerciseId))
        val exercise = exerciseService.getById(accountExercise.exerciseId)
        return AccountExerciseDTO(exercise, accountExercise)
    }
}
