package com.github.princesslana.karmaleon

data class Karma(val to: Snowflake)

fun Message.toKarma(): Karma? {
    if (!content.startsWith("^helpful")) {
        return null
    }

    if (mentions.size != 1) {
        return null
    }

    return Karma(mentions.get(0).id)
}
