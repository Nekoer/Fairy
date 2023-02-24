package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.Exercise
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/24/2023 14:12
 * @Description
 **/
@Mapper
@Repository
interface ExerciseMapper : BaseMapper<Exercise> {
}
