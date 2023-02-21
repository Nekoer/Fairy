package com.hcyacg.fairy.service

import com.hcyacg.fairy.dto.MoveDTO

/**
 * @Author Nekoer
 * @Date  2/21/2023 09:38
 * @Description
 **/
interface MoveService {

    fun moveToTop(accountId: Long): MoveDTO
    fun moveToLeft(accountId: Long): MoveDTO
    fun moveToRight(accountId: Long): MoveDTO
    fun moveToBottom(accountId: Long): MoveDTO

}
