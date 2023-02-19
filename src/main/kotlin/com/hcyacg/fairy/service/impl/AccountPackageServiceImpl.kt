package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.dto.AccountItem
import com.hcyacg.fairy.dto.ItemDTO
import com.hcyacg.fairy.dto.ItemUsageDTO
import com.hcyacg.fairy.entity.AccountPackage
import com.hcyacg.fairy.entity.ItemUsage
import com.hcyacg.fairy.mapper.AccountPackageMapper
import com.hcyacg.fairy.service.*
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @Author Nekoer
 * @Date  2/19/2023 18:10
 * @Description
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class AccountPackageServiceImpl : ServiceImpl<AccountPackageMapper, AccountPackage>(), AccountPackageService {
    @Autowired
    private lateinit var accountService: AccountService
    @Autowired
    private lateinit var itemService: ItemService
    @Autowired
    private lateinit var itemUsageService: ItemUsageService
    @Autowired
    private lateinit var itemTypeService: ItemTypeService
    @Autowired
    private lateinit var usageService: UsageService
    override fun getPackageList(uin: Long): List<AccountItem> {
        return try{
            accountService.getById(uin)?.let {account ->
                val list = list(QueryWrapper<AccountPackage>().eq("uin", account.uin))
                val accountItems = mutableListOf<AccountItem>()
                list.forEach { accountPackage ->

                    itemService.getById(accountPackage.itemId)?.let {item ->
                        val itemUsages = itemUsageService.list(QueryWrapper<ItemUsage>().eq("item_id",item.id))
                        val itemUsageDTOs = mutableListOf<ItemUsageDTO>()
                        itemUsages.forEach { itemUsage ->
                            val usage = usageService.getById(itemUsage.usageId)
                            itemUsageDTOs.add(
                                ItemUsageDTO(
                                    usage.name,
                                    itemUsage.attribute
                                ))

                        }
                        itemTypeService.getItemTypeById(item.itemTypeId)?.let { itemType ->
                            accountItems.add(AccountItem(
                                id=item.id,
                                uin=accountPackage.uin,
                                ItemDTO(
                                    id = item.id,
                                    name = item.name,
                                    description = item.description,
                                    itemType = itemType,
                                    itemUsages = itemUsageDTOs
                                ),
                                accountPackage.quantity
                            ))
                        }
                    }
                }
                return accountItems
            }
            mutableListOf()
        }catch (e:Exception){
            log.error("获取 $uin 用户背包时异常 => {}",e)
            return mutableListOf()
        }
    }
}
