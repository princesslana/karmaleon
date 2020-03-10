package com.github.princesslana.karmaleon

import org.slf4j.impl.SimpleLogger

fun main(args: Array<String>) {
    if (System.getenv("KRML_DEBUG")?.toBoolean() ?: false) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
    }

    val prefix = System.getenv("KRML_PREFIX") ?: "^"

    run(System.getenv("KRML_TOKEN")) {
        onMessageCreate { msg ->
            msg.toKarma(prefix)?.let {
                val response =
                    "**${msg.author.tag}** awarded ${it.type} karma to **${it.to.tag}**"
                post("/channels/${msg.channelId}/messages", CreateMessage(response))
            }
        }
    }
}
