package com.github.princesslana.karmaleon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KarmaTest {
    val dummyUser = User("Username", "1234")

    fun mkMessage(content: String, mentions: List<String> = listOf()): Message {
        return Message(dummyUser, "1", content, mentions.map { Mention(it) })
    }

    @Test
    fun `toKarma when wrong prefix should be null`() {
        assertNull(mkMessage("++helpful <@123>", mentions = listOf("123")).toKarma())
    }

    @Test
    fun `toKarma when no mention should be null`() {
        assertNull(mkMessage("^helpful").toKarma())
    }

    @Test
    fun `toKarma when single mention should parse`() {
        val k = mkMessage("^helpful <@123>", listOf("123")).toKarma()

        assertNotNull(k)
        assertEquals(Karma("123"), k)
    }

    @Test
    fun `toKarma when more than one mention should be null`() {
        val k = mkMessage("^helpful <@123> <@456>", mentions = listOf("123", "456"))
            .toKarma()

        assertNull(k)
    }
}
