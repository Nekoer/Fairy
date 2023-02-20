package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.dto.AccountItem
import com.hcyacg.fairy.dto.ItemDTO
import com.hcyacg.fairy.dto.ItemUsageDTO
import com.hcyacg.fairy.dto.Usage
import com.hcyacg.fairy.entity.AccountPackage
import com.hcyacg.fairy.entity.ItemUsage
import com.hcyacg.fairy.mapper.AccountMapper
import com.hcyacg.fairy.mapper.AccountPackageMapper
import com.hcyacg.fairy.service.AccountPackageService
import com.hcyacg.fairy.service.ItemService
import com.hcyacg.fairy.service.ItemTypeService
import com.hcyacg.fairy.service.ItemUsageService
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
class AccountPackageServiceImpl @Autowired constructor(
    protected  var accountMapper: AccountMapper,
    private  var itemService: ItemService,
    private  var itemUsageService: ItemUsageService,
    private  var itemTypeService: ItemTypeService
) : ServiceImpl<AccountPackageMapper, AccountPackage>(), AccountPackageService {


    override fun getPackageList(accountId: Long): List<AccountItem> {
        return try{
            val list = list(QueryWrapper<AccountPackage>().eq("account_id", accountId))
            val accountItems = mutableListOf<AccountItem>()
            list.forEach { accountPackage ->

                itemService.getById(accountPackage.itemId)?.let {item ->
                    val itemUsages = itemUsageService.list(QueryWrapper<ItemUsage>().eq("item_id",item.id))
                    val itemUsageDTOs = mutableListOf<ItemUsageDTO>()
                    itemUsages.forEach { itemUsage ->

                        val usage = Usage.getUsageById(itemUsage.usageId)
                        itemUsageDTOs.add(
                            ItemUsageDTO(
                                usage.name,
                                itemUsage.attribute
                            ))

                    }
                    itemTypeService.getItemTypeById(item.itemTypeId)?.let { itemType ->
                        accountItems.add(AccountItem(
                            id=item.id,
                            accountId=accountId,
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
            accountItems
        }catch (e:Exception){
            log.error("获取 $accountId 用户背包时异常 => {}",e)
            return mutableListOf()
        }
    }

    override fun deleteAccountPackage(accountId: Long): Boolean {
        return try {
            remove(QueryWrapper<AccountPackage>().eq("account_id", accountId))
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }
}
