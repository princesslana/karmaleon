package com.github.princesslana.karmaleon

import java.util.NoSuchElementException
import java.util.Scanner
import java.util.regex.Pattern

data class Karma(val to: User)

fun Message.toKarma(prefix: String): Collection<Karma> {
    val s = Scanner(content)

    try {
        s.skip(Pattern.quote(prefix))
        s.skip("helpful")

        return mentions.filter { author.id != it.id }.map { Karma(it) }
    } catch (e: IllegalArgumentException) {
        return emptyList<Karma>()
    } catch (e: NoSuchElementException) {
        return emptyList<Karma>()
    }
}
