package com.github.princesslana.karmaleon

data class Karma(val to: User, val from: User, val channel: Snowflake)

val THANKS = setOf("ty", "thanks", "thank you")
val THANKS_RE = THANKS.map { "\\b$it\\b".toRegex(RegexOption.IGNORE_CASE) }

fun Message.toKarma(): Collection<Karma> {
    if (THANKS_RE.any { it.containsMatchIn(content) }) {
        return mentions.filter { author.id != it.id }
            .map { Karma(it, author, channelId) }
    } else {
        return emptyList<Karma>()
    }
}
