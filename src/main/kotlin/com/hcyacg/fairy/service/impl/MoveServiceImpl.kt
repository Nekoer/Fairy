package com.hcyacg.fairy.service.impl

import com.hcyacg.fairy.dto.MoveDTO
import com.hcyacg.fairy.mapper.AccountMapper
import com.hcyacg.fairy.mapper.WorldMapMapper
import com.hcyacg.fairy.service.MoveService
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/21/2023 09:49
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class MoveServiceImpl @Autowired constructor(
    private val accountMapper: AccountMapper,
    private val worldMapMapper: WorldMapMapper
): MoveService {


    override fun moveToTop(accountId: Long): MoveDTO {
        val account = accountMapper.selectById(accountId)
        val worldMap = worldMapMapper.selectById(account.worldMapId)
        return try {

            worldMap.topId?.let {
                val top = worldMapMapper.selectById(it)
                account.worldMapId = it
                if (accountMapper.updateById(account) > 0){
                    return MoveDTO(true,worldMap,top)
                }
            }
            MoveDTO(false,worldMap,worldMap)
        }catch (e:Exception){
            e.printStackTrace()
            MoveDTO(false,worldMap,worldMap)
        }
    }

    override fun moveToLeft(accountId: Long): MoveDTO {
        val account = accountMapper.selectById(accountId)
        val worldMap = worldMapMapper.selectById(account.worldMapId)
        return try {

            worldMap.leftId?.let {
                val left = worldMapMapper.selectById(it)
                account.worldMapId = it
                if (accountMapper.updateById(account) > 0){
                    return MoveDTO(true,worldMap,left)
                }
            }
            MoveDTO(false,worldMap,worldMap)
        }catch (e:Exception){
            e.printStackTrace()
            MoveDTO(false,worldMap,worldMap)
        }
    }


    override fun moveToRight(accountId: Long): MoveDTO {
        val account = accountMapper.selectById(accountId)
        val worldMap = worldMapMapper.selectById(account.worldMapId)
        return try {

            worldMap.rightId?.let {
                val right = worldMapMapper.selectById(it)
                account.worldMapId = it
                if (accountMapper.updateById(account) > 0){
                    return MoveDTO(true,worldMap,right)
                }
            }
            MoveDTO(false,worldMap,worldMap)
        }catch (e:Exception){
            e.printStackTrace()
            MoveDTO(false,worldMap,worldMap)
        }
    }


    override fun moveToBottom(accountId: Long): MoveDTO {
        val account = accountMapper.selectById(accountId)
        val worldMap = worldMapMapper.selectById(account.worldMapId)
        return try {

            worldMap.bottomId?.let {
                val bottom = worldMapMapper.selectById(it)
                account.worldMapId = it
                if (accountMapper.updateById(account) > 0){
                    return MoveDTO(true,worldMap,bottom)
                }
            }
            MoveDTO(false,worldMap,worldMap)
        }catch (e:Exception){
            e.printStackTrace()
            MoveDTO(false,worldMap,worldMap)
        }
    }
}
