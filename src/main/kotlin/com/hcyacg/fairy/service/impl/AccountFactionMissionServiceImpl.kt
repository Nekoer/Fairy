package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.AccountFactionMission
import com.hcyacg.fairy.mapper.AccountFactionMissionMapper
import com.hcyacg.fairy.service.AccountFactionMissionService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/27/2023 19:36
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class AccountFactionMissionServiceImpl : ServiceImpl<AccountFactionMissionMapper, AccountFactionMission>(), AccountFactionMissionService {
}
