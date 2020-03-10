package com.github.princesslana.karmaleon

import java.util.NoSuchElementException
import java.util.Scanner
import java.util.regex.Pattern

enum class KarmaType {
    HELPFUL;
}

data class Karma(val type: KarmaType, val to: User)

fun Message.toKarma(prefix: String): Karma? {
    val s = Scanner(content)

    try {
        s.skip(Pattern.quote(prefix))

        val type = enumValueOf<KarmaType>(s.next().toUpperCase())

        if (mentions.size != 1) {
            return null
        }

        return Karma(type, mentions.get(0))
    } catch (e: IllegalArgumentException) {
        return null
    } catch (e: NoSuchElementException) {
        return null
    }
}
