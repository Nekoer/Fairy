package com.hcyacg.fairy.websocket

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement


/**
 * @Author Nekoer
 * @Date  2/11/2023 16:45
 * @Description
 **/
@Serializable
data class WsRequest(
    @SerialName("action")
    val action: String,
    @SerialName("echo")
    val echo: String? = null,
    @SerialName("params")
    val params: Map<String,@Serializable(with= AnySerializer::class) Any?>
)
//    val params: Map<String,@Serializable(with= AnySerializer::class) Any>
object AnySerializer: JsonContentPolymorphicSerializer<Any>(Any::class){
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Any> {
        return String.serializer()
    }


}
