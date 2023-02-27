package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.entity.FactionMission
import com.hcyacg.fairy.mapper.FactionMissionMapper
import com.hcyacg.fairy.service.FactionMissionService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/27/2023 18:40
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class FactionMissionServiceImpl : ServiceImpl<FactionMissionMapper, FactionMission>(), FactionMissionService {
}
