package com.github.princesslana.karmaleon

class Repository {

    val events = mutableListOf<Karma>()

    fun add(karma: Karma) {
        events.add(karma)
    }

    fun count(who: User): Int {
        return events.filter { it.to.id == who.id }.size
    }
}
