package com.github.princesslana.karmaleon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KarmaTest {
    val dummyUser = User("123", "Username", "1234")
    val dummyMessage = Message(dummyUser, "1", "", listOf())

    @Test
    fun `toKarma when wrong prefix should be null`() {
        assertNull(
            dummyMessage.copy(
                content = "++helpful <@123>", mentions = listOf(dummyUser)
            ).toKarma(prefix = "^"))
    }

    @Test
    fun `toKarma when no mention should be null`() {
        assertNull(dummyMessage.copy(content = "^helpful").toKarma("^"))
    }

    @Test
    fun `toKarma when single mention should parse`() {
        val k = dummyMessage.copy(
                content = "^helpful <@123>", mentions = listOf(dummyUser)
            ).toKarma("^")

        assertNotNull(k)
        assertEquals(Karma(dummyUser), k)
    }

    @Test
    fun `toKarma when single mention with other prefix should parse`() {
        val k = dummyMessage.copy(
                content = "!helpful <@123>", mentions = listOf(dummyUser)
            ).toKarma("!")

        assertNotNull(k)
        assertEquals(Karma(dummyUser), k)
    }

    @Test
    fun `toKarma when more than one mention should be null`() {
        val k = dummyMessage.copy(
                content = "^helpful <@123> <@456>",
                mentions = listOf(dummyUser, dummyUser.copy(id = "456"))
            ).toKarma("^")

        assertNull(k)
    }
}
