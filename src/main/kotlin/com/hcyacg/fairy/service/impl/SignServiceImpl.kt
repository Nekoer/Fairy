package com.hcyacg.fairy.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hcyacg.fairy.constant.AppConstant
import com.hcyacg.fairy.entity.Sign
import com.hcyacg.fairy.mapper.SignMapper
import com.hcyacg.fairy.service.SignService
import com.hcyacg.fairy.utils.RedisUtil
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs


/**
 * @Author Nekoer
 * @Date  2/12/2023 20:46
 * @Description 签到服务
 **/
@Slf4j
@Service
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Transactional(rollbackFor = [Exception::class])
class SignServiceImpl : ServiceImpl<SignMapper, Sign>(), SignService {

    @Autowired
    private lateinit var redisUtil: RedisUtil

    private fun parseTime(now: LocalTime, end: LocalTime): String {
        val dsf: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        log.debug("parseTime => 目前时间：${dsf.format(now)},本日结束时间：${dsf.format(end)}")
        //获取秒数
        val nowSecond = now.toEpochSecond(LocalDate.now(), ZoneOffset.of("+8"))
        val endSecond = end.toEpochSecond(LocalDate.now(), ZoneOffset.of("+8"))
        val absSeconds = abs(nowSecond - endSecond)

        //获取秒数
        val s = absSeconds % 60
        //获取分钟数
        val m = absSeconds / 60 % 60
        //获取小时数
        val h = absSeconds / 60 / 60 % 24
        //获取天数
        val d = absSeconds / 60 / 60 / 24

        return "$d 天 $h 时 $m 分 $s 秒"
    }

    override fun sign(uin: Long): String {
        //TODO 签到奖励
        return try{
            val sign = getById(uin)
            return if (null == sign) {
                //初始化签到
                //插入数据库
                if (save(Sign(uin, System.currentTimeMillis(), 1))) {
                    redisUtil["${AppConstant.SIGN}:${uin}"] = 1
                    redisUtil.expire("${AppConstant.SIGN}:${uin}", (System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                    "恭喜您签到成功,您已签到1天"
                } else {
                    "签到失败,插入数据异常"
                }

            } else {
                //判断redis是否存在签到，不存在则重新计算连续签到次数
                if (redisUtil.hasKey("${AppConstant.SIGN}:${uin}")) {
                    //查询数据库中该用户签到时间能否签到

                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val dsf: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val localTime = LocalTime.parse(simpleDateFormat.format(Date(sign.time)), dsf)
                    val startTime = LocalTime.of(0, 0, 0)
                    val endTime = LocalTime.of(23, 59, 59)
                    if (localTime.isAfter(startTime) && localTime.isBefore(endTime)) {
                        return "签到失败,您今天已签到过了,剩余时间:${parseTime(localTime, endTime)}"
                    }

                    sign.time = System.currentTimeMillis()
                    sign.signDay += 1

                    if (updateById(sign)) {
                        redisUtil["${AppConstant.SIGN}:${uin}"] = 1
                        redisUtil.expire("${AppConstant.SIGN}:${uin}", (System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                        "恭喜您签到成功,您已连续签到${sign.signDay}天"
                    } else {
                        "签到失败,插入数据异常"
                    }

                } else {
                    //重新计算连续签到次数
                    sign.time = System.currentTimeMillis()
                    sign.signDay = 1
                    if (updateById(sign)) {
                        redisUtil["${AppConstant.SIGN}:${uin}"] = 1
                        redisUtil.expire("${AppConstant.SIGN}:${uin}", (System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                        "恭喜您签到成功,由于您中断了签到,本次签到重新计算,您已连续签到${sign.signDay}天"
                    } else {
                        "签到失败,插入数据异常"
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            log.error("签到系统 =>{}",e)
            "签到失败，可能您还未加入修真界！"
        }
    }

    override fun deleteSign(uin: Long): Boolean {
        return try {
            removeById(uin)
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }
}
