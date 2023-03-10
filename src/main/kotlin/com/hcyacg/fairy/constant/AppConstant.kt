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
        //安全点
        val WORLD_MAP_SAFE = 1
        //非安全点 可生成怪物
        val WORLD_MAP_UNSAFE = 0

        //怪物生成在哪个位置并且是哪种怪物
        val BOSS_WORLD_MAP = "boss:live:"
        //命令的集合
        val COMMAND_DESCRIPTION: MutableMap<String, String> = mutableMapOf()

        // 战斗胜利的固定经验
        val EXP_VICTORY = 45L

        //宗门任务状态
        val FACTION_MISSION_STATE_SUCCESS = 1L //成功
        val FACTION_MISSION_STATE_CANEL = 2L//取消
        val FACTION_MISSION_STATE_FAILURE = -1L //失败
        val FACTION_MISSION_STATE_PROGRESS = 0L //进行中

        val FACTION_MISSION_CD = "faction:mission:"
    }



}
