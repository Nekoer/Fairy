package com.hcyacg.fairy.utils

import org.springframework.stereotype.Component
import java.util.*

/**
 * @Author Nekoer
 * @Date  2/12/2023 12:18
 * @Description
 **/
@Component
class Base64Util {

    fun encodeBytes2Base64(bytes: ByteArray): String{
        val encoder: Base64.Encoder = Base64.getEncoder()
        return encoder.encodeToString(bytes)
    }

}
