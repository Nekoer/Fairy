package com.hcyacg.fairy.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.hcyacg.fairy.entity.AccountPackage
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:00
 * @Description
 **/
@Mapper
@Repository
interface AccountPackageMapper: BaseMapper<AccountPackage> {
}
