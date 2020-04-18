package com.github.princesslana.karmaleon

import java.net.URLEncoder
import org.slf4j.impl.SimpleLogger

fun main(args: Array<String>) {
    if (System.getenv("KRML_DEBUG")?.toBoolean() ?: false) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
    }

    val prefix = System.getenv("KRML_PREFIX") ?: "^"

    val repository = Repository()

    run(System.getenv("KRML_TOKEN")) {
        onMessageCreate { msg ->
            val karma = msg.toKarma()

            karma.forEach { repository.add(it) }

            if (!karma.none()) {
                val emoji = URLEncoder.encode("👍", "utf-8")
                put("/channels/${msg.channelId}" +
                    "/messages/${msg.id}/" +
                    "reactions/$emoji/@me", "")
            }

            if (msg.content.startsWith("${prefix}karma")) {
                val mentions =
                    if (msg.mentions.none()) listOf(msg.author)
                    else msg.mentions.orEmpty()

                val response = mentions.map {
                    "${it.tag} has ${repository.count(it)} karma"
                }.joinToString(separator = "\n")

                post("/channels/${msg.channelId}/messages", CreateMessage(response))
            }
        }
    }
}
