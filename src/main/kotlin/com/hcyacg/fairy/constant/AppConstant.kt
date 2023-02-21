package com.hcyacg.fairy.constant

/**
 * @Author Nekoer
 * @Date  2/12/2023 20:59
 * @Description
 **/
class AppConstant {

    companion object {

        /**
         * Redis 签到
         */
        val SIGN = "SIGN"

        val WORLD_MAP_SAFE = 1
        val WORLD_MAP_UNSAFE = 0

        val BOSS_WORLD_MAP = "boss:live:"

        val COMMAND_DESCRIPTION: MutableMap<String, String> = mutableMapOf()

    }



}
