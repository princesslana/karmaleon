package com.github.princesslana.karmaleon

import com.github.princesslana.smalld.SmallD
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.toJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.slf4j.impl.SimpleLogger

val gson = Gson()

data class GatewayPayload(
    val op: Long,
    val d: JsonObject,
    val t: String?
) {
    fun ifMessageCreate(f: (Message) -> Unit) {
        if (t == "MESSAGE_CREATE") {
            f(gson.fromJson<Message>(d))
        }
    }
}

data class Message(
    @SerializedName("channel_id") val channelId: String,
    val content: String
)

data class CreateMessage(val content: String)

fun SmallD.post(path: String, payload: Any) {
    post(path, gson.toJson(payload))
}

fun SmallD.onMessageCreate(f: (Message) -> Unit) {
    onGatewayPayload { p ->
        gson.fromJson<GatewayPayload>(p).ifMessageCreate { f(it) }
    }
}

fun run(token: String, f: SmallD.() -> Unit) {
    SmallD.run(token) { f(it) }
}

fun main(args: Array<String>) {
    if (System.getenv("KRML_DEBUG")?.toBoolean() ?: false) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
    }

    run(System.getenv("KRML_TOKEN")) {
        onMessageCreate { msg ->
                when (msg.content) {
                    "^helpful" -> post("/channels/${msg.channelId}/messages", CreateMessage("helpful karma awarded"))
                }
        }
    }
}
