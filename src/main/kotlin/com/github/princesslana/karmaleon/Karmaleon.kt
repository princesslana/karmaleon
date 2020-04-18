package com.github.princesslana.karmaleon

import org.slf4j.impl.SimpleLogger

fun main(args: Array<String>) {
    if (System.getenv("KRML_DEBUG")?.toBoolean() ?: false) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
    }

    val prefix = System.getenv("KRML_PREFIX") ?: "^"

    val repository = Repository()

    run(System.getenv("KRML_TOKEN")) {
        onMessageCreate { msg ->
            msg.toKarma(prefix)?.let {
                repository.add(it)
                val response =
                    "1 karma awared to " +
                    "**${it.to.tag}** by **${msg.author.tag}**"
                post("/channels/${msg.channelId}/messages", CreateMessage(response))
            }
            if (msg.content == "${prefix}karma") {
                val response =
                    "${msg.author.tag} has ${repository.count(msg.author)} karma"
                post("/channels/${msg.channelId}/messages", CreateMessage(response))
            }
        }
    }
}
