package com.hcyacg.fairy.core.command

/**
 * @Author Nekoer
 * @Date  2/21/2023 10:40
 * @Description
 **/
interface GameCommandService{

    //群
    fun group(sender: Long, group: Long, message: String) : String
    //频道
    fun channel(sender: Long, guild: Long,channel:Long,message: String) : String
    //私聊
    fun chat(sender: Long, message: String) : String

}
