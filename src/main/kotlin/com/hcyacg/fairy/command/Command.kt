package com.hcyacg.fairy.command

import java.lang.annotation.Inherited

/**
 * @Author Nekoer
 * @Date  2/21/2023 10:41
 * @Description
 **/
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class Command(
    val type: String,
    val regex: String
)
