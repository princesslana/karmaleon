package com.github.princesslana.karmaleon

import java.util.NoSuchElementException
import java.util.Scanner
import java.util.regex.Pattern

data class Karma(val to: User)

fun Message.toKarma(prefix: String): Karma? {
    val s = Scanner(content)

    try {
        s.skip(Pattern.quote(prefix))
        s.next("helpful")

        if (mentions.size != 1) {
            return null
        }

        return Karma(mentions.get(0))
    } catch (e: NoSuchElementException) {
        return null
    }
}
