package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.BossUsage
import com.hcyacg.fairy.mapper.BossUsageMapper
import com.hcyacg.fairy.service.BossUsageService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/20/2023 20:29
 * @Description
**/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class BossUsageServiceImpl : ServiceImpl<BossUsageMapper, BossUsage>() , BossUsageService {
}
