package com.github.princesslana.karmaleon

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.GsonBuilder
import java.io.File

class Repository private constructor(val file: File) {
    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .create()

    val events = mutableListOf<Karma>()

    companion object Factory {
        fun load(file: File = File("karma.json")): Repository {
            if (!file.exists()) {
                file.writeText("")
            }

            return Repository(file).apply { reload() }
        }
    }

    fun reload() {
        events.clear()
        file.readLines().map { gson.fromJson<Karma>(it) }.forEach { events.add(it) }
    }

    fun add(karma: Karma) {
        events.add(karma)

        gson.toJson(karma).let { file.appendText("$it\n") }
    }

    fun count(who: User): Int {
        return events.filter { it.to.id == who.id }.size
    }

    fun leaderboard(): List<Pair<User, Int>> {
        return events
            .groupingBy { it.to }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(10)
    }
}
