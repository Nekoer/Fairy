package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.Usage
import com.hcyacg.fairy.mapper.UsageMapper
import com.hcyacg.fairy.service.UsageService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:13
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class UsageServiceImpl: ServiceImpl<UsageMapper, Usage>(), UsageService {
}
