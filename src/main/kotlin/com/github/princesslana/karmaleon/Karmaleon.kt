package com.github.princesslana.karmaleon

import com.github.michaelbull.retry.*
import com.github.michaelbull.retry.policy.*
import com.github.michaelbull.retry.policy.limitAttempts
import com.github.princesslana.smalld.SmallD
import java.io.File
import java.net.URLEncoder
import kotlinx.coroutines.*
import org.slf4j.impl.SimpleLogger

class Karmaleon(val smalld: SmallD, val prefix: String, val repository: Repository) {

    fun onMessageCreate(msg: Message) {
        issueKarma(msg)

        val command = msg.content.split("\\s".toRegex()).firstOrNull() ?: ""

        when (command) {
            "${prefix}karma" -> displayKarma(msg)
            "${prefix}leaderboard" -> displayLeaderboard(msg)
        }
    }

    fun issueKarma(msg: Message) {
        val karma = msg.toKarma()

        karma.forEach { repository.add(it) }

        if (!karma.none()) {
            val emoji = URLEncoder.encode("👍", "utf-8")
            reliablyAsync {
                smalld.put(
                    "/channels/${msg.channelId}" +
                    "/messages/${msg.id}/" +
                    "reactions/$emoji/@me", "")
            }
        }
    }

    fun displayKarma(msg: Message) {
        val mentions =
            if (msg.mentions.none()) listOf(msg.author)
            else msg.mentions.orEmpty()

        val response = mentions.map {
            "${it.tag} has ${repository.count(it)} karma"
        }.joinToString(separator = "\n")

        sendReply(msg, response)
    }

    fun displayLeaderboard(msg: Message) {
        val response = repository.leaderboard().map { (who, count) ->
            "${who.tag}: $count"
        }.joinToString(separator = "\n")

        sendReply(msg, response)
    }

    fun sendReply(msg: Message, response: String) {
        reliablyAsync {
            smalld.post("/channels/${msg.channelId}/messages", CreateMessage(response))
        }
    }

    fun reliablyAsync(f: () -> Unit) {
        GlobalScope.launch {
            retry(limitAttempts(3) + constantDelay(delayMillis = 3000)) {
                f()
            }
        }
    }
}

fun main(args: Array<String>) {
    if (System.getenv("KRML_DEBUG")?.toBoolean() ?: false) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
    }

    val prefix = System.getenv("KRML_PREFIX") ?: "^"
    val file = System.getenv("KRML_DATA") ?: "karma.json"

    val repository = Repository.load(File(file))

    run(System.getenv("KRML_TOKEN")) {
        val karmaleon = Karmaleon(this, prefix, repository)

        onMessageCreate { karmaleon.onMessageCreate(it) }
    }
}
