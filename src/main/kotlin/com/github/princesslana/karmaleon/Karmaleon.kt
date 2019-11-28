package com.github.princesslana.karmaleon

import org.slf4j.impl.SimpleLogger

fun main(args: Array<String>) {
    if (System.getenv("KRML_DEBUG")?.toBoolean() ?: false) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
    }

    run(System.getenv("KRML_TOKEN")) {
        onMessageCreate { msg ->
            msg.toKarma()?.let {
                val response =
                    "**${msg.author.tag}** awarded helpful karma to **${it.to.tag}**"
                post("/channels/${msg.channelId}/messages", CreateMessage(response))
            }
        }
    }
}
