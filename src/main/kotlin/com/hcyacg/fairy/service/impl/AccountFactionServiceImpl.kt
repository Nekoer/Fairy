package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.AccountFaction
import com.hcyacg.fairy.mapper.AccountFactionMapper
import com.hcyacg.fairy.service.AccountFactionService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  3/17/2023 16:59
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class AccountFactionServiceImpl : ServiceImpl<AccountFactionMapper, AccountFaction>(),
    AccountFactionService {
}
