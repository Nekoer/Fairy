package com.hcyacg.fairy.server.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.server.entity.Account
import com.hcyacg.fairy.server.entity.Hierarchical
import com.hcyacg.fairy.server.mapper.AccountMapper
import com.hcyacg.fairy.server.mapper.HierarchicalMapper
import com.hcyacg.fairy.server.service.AccountService
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * @Author Nekoer
 * @Date  2/11/2023 20:09
 * @Description
 **/
@Data
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class AccountServiceImpl : ServiceImpl<AccountMapper, Account>(), AccountService {
    @Autowired
    private lateinit var hierarchicalMapper: HierarchicalMapper
    override fun register(uin: Long): Boolean {
        log.debug("注册的用户uin => $uin")
        val hierarchical = hierarchicalMapper.selectById(1)
        return save(Account(
            uin,
            hierarchical.level,
            hierarchical.exp,
            hierarchical.health,
            hierarchical.mana,
            hierarchical.attack,
            hierarchical.defensive,
            BigDecimal.valueOf(0)))
    }
    
}
