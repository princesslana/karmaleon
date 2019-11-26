package com.github.princesslana.karmaleon

import com.github.princesslana.smalld.SmallD
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.toJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

val gson = Gson()

data class GatewayPayload(
    val op: Long,
    val d: JsonObject,
    val t: String?
)

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
        val jsonp = gson.fromJson<GatewayPayload>(p)

        if (jsonp.t == "MESSAGE_CREATE") {
            f(gson.fromJson<Message>(jsonp.d))
        }
    }
}

fun run(token: String, f: SmallD.() -> Unit) {
    SmallD.run(token) { f(it) }
}
