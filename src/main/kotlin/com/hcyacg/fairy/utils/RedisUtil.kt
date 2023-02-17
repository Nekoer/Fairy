package com.hcyacg.fairy.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations.TypedTuple
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import java.util.concurrent.TimeUnit


/**
 * @Author Nekoer
 * @Date  2/12/2023 19:47
 * @Description
 **/
@Component
class RedisUtil {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>


    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    fun expire(key: String, time: Long): Boolean {
        return try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    fun getExpire(key: String?): Long {
        return redisTemplate.getExpire(key!!, TimeUnit.SECONDS)
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    fun hasKey(key: String): Boolean {
        return try {
            redisTemplate.hasKey(key)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    fun del(vararg key: String?) {
        if (key.isNotEmpty()) {
            if (key.size == 1) {
                redisTemplate.delete(key[0]!!)
            } else {
                redisTemplate.delete((CollectionUtils.arrayToList(key) as Collection<String?>))
            }
        }
    }

    //============================String=============================

    //============================String=============================
    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    operator fun get(key: String?): Any? {
        return if (key == null) null else redisTemplate.opsForValue()[key]
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    operator fun set(key: String, value: Any): Boolean {
        return try {
            redisTemplate.opsForValue()[key] = value
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    operator fun set(key: String, value: Any, time: Long): Boolean {
        return try {
            if (time > 0) {
                redisTemplate.opsForValue()[key, value, time] = TimeUnit.SECONDS
            } else {
                set(key, value)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @param delta  要增加几(大于0)
     * @return
     */
    fun incr(key: String, delta: Long): Long? {
        if (delta < 0) {
            throw RuntimeException("递增因子必须大于0")
        }
        return redisTemplate.opsForValue().increment(key, delta)
    }

    /**
     * 递减
     *
     * @param key 键
     * @param delta  要减少几(小于0)
     * @return
     */
    fun decr(key: String, delta: Long): Long? {
        if (delta < 0) {
            throw RuntimeException("递减因子必须大于0")
        }
        return redisTemplate.opsForValue().increment(key, -delta)
    }


    //============================set=============================

    //============================set=============================
    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    fun sGet(key: String?): Set<Any?>? {
        return try {
            redisTemplate.opsForSet().members(key!!)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    fun sHasKey(key: String?, value: Any?): Boolean {
        return try {
            redisTemplate.opsForSet().isMember(key!!, value!!)!!
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    fun sSet(key: String?, vararg values: Any?): Long {
        return try {
            redisTemplate.opsForSet().add(key!!, *values)!!
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    fun sSetAndTime(key: String?, time: Long, vararg values: Any?): Long {
        return try {
            val count = redisTemplate.opsForSet().add(key!!, *values)
            if (time > 0) {
                expire(key, time)
            }
            count!!
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    fun sGetSetSize(key: String?): Long {
        return try {
            redisTemplate.opsForSet().size(key!!)!!
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    fun setRemove(key: String?, vararg values: Any?): Long {
        return try {
            val count = redisTemplate.opsForSet().remove(key!!, *values)
            count!!
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
    //===============================list=================================

    //===============================list=================================
    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    fun lGet(key: String?, start: Long, end: Long): List<Any?>? {
        return try {
            redisTemplate.opsForList().range(key!!, start, end)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    fun lGetListSize(key: String?): Long {
        return try {
            redisTemplate.opsForList().size(key!!)!!
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    fun lGetIndex(key: String?, index: Long): Any? {
        return try {
            redisTemplate.opsForList().index(key!!, index)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    fun lSet(key: String?, value: Any?): Boolean {
        return try {
            redisTemplate.opsForList().rightPush(key!!, value!!)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 从list左侧弹出一个值
     * @param key 键
     * @return
     */
    fun lPop(key: String?): Boolean {
        return try {
            redisTemplate.opsForList().leftPop(key!!)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    fun lSet(key: String?, value: Any?, time: Long): Boolean {
        return try {
            redisTemplate.opsForList().rightPush(key!!, value!!)
            if (time > 0) {
                expire(key, time)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    fun lSet(key: String?, value: List<Any?>?): Boolean {
        return try {
            redisTemplate.opsForList().rightPushAll(key!!, value!!)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    fun lSet(key: String?, value: List<Any?>?, time: Long): Boolean {
        return try {
            redisTemplate.opsForList().rightPushAll(key!!, value!!)
            if (time > 0) {
                expire(key, time)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    fun lUpdateIndex(key: String?, index: Long, value: Any?): Boolean {
        return try {
            redisTemplate.opsForList()[key!!, index] = value!!
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    fun lRemove(key: String?, count: Long, value: Any?): Long {
        return try {
            val remove = redisTemplate.opsForList().remove(key!!, count, value!!)
            remove!!
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    //================有序集合 sort set===================
    //================有序集合 sort set===================
    /**
     * 有序set添加元素
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    fun zSet(key: String?, value: Any?, score: Double): Boolean {
        return redisTemplate.opsForZSet().add(key!!, value!!, score)!!
    }

    fun batchZSet(key: String?, typles: Set<TypedTuple<Any?>?>?): Long {
        return redisTemplate.opsForZSet().add(key!!, typles!!)!!
    }

    fun zIncrementScore(key: String?, value: Any?, delta: Long) {
        redisTemplate.opsForZSet().incrementScore(key!!, value!!, delta.toDouble())
    }

    fun zUnionAndStore(key: String, otherKeys: Collection<String>, destKey: String) {
        redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey)
    }

    /**
     * 获取zset数量
     * @param key
     * @param value
     * @return
     */
    fun getZsetScore(key: String?, value: Any?): Long {
        val score = redisTemplate.opsForZSet().score(key!!, value!!)
        return score?.toLong() ?: 0
    }

    /**
     * 获取有序集 key 中成员 member 的排名 。
     * 其中有序集成员按 score 值递减 (从大到小) 排序。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    fun getZSetRank(key: String, start: Long, end: Long): Set<TypedTuple<Any>>? {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end)
    }
}
