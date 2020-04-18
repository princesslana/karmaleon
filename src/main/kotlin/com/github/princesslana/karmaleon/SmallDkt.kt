package com.github.princesslana.karmaleon

import com.github.princesslana.smalld.SmallD
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.toJson
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

val gson = Gson()

typealias Snowflake = String

data class User(val id: Snowflake, val username: String, val discriminator: String) {
    val tag: String
        get() = "$username#$discriminator"
}

data class GatewayPayload(
    val op: Long,
    val d: JsonElement,
    val t: String?
)

data class Message(
    val id: Snowflake,
    val author: User,
    @SerializedName("channel_id") val channelId: Snowflake,
    val content: String,
    val mentions: List<User>
)

data class CreateMessage(val content: String)

fun SmallD.post(path: String, payload: Any) {
    post(path, gson.toJson(payload))
}

fun SmallD.put(path: String, payload: Any) {
    put(path, gson.toJson(payload))
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
