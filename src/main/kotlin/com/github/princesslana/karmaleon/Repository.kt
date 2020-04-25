package com.github.princesslana.karmaleon

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import java.io.File

class Repository private constructor(val file: File) {
    val gson = Gson()
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
}
