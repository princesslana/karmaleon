package com.github.princesslana.karmaleon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KarmaTest {
    val dummyUser = User("123", "Username", "1234")
    val dummyMessage = Message("1", dummyUser.copy(id = "789"), "1", "", listOf())

    @Test
    fun `toKarma when no thanks should be none`() {
        assertTrue(
            dummyMessage.copy(
                content = "that was pretty helpful <@123>", mentions = listOf(dummyUser)
            ).toKarma().none())
    }

    @Test
    fun `toKarma when no mention should be none`() {
        assertTrue(dummyMessage.copy(content = "thanks").toKarma().none())
    }

    @Test
    fun `toKarma when single mention should parse`() {
        val k = dummyMessage.copy(
                content = "ty <@123>", mentions = listOf(dummyUser)
            ).toKarma()

        assertEquals(listOf(Karma(dummyUser)), k)
    }

    @Test
    fun `toKarma when single mention with other thanks should parse`() {
        val k = dummyMessage.copy(
                content = "thank you <@123>", mentions = listOf(dummyUser)
            ).toKarma()

        assertEquals(listOf(Karma(dummyUser)), k)
    }

    @Test
    fun `toKarma when more than one mention should give multiple karma`() {
        val k = dummyMessage.copy(
                content = "thanks <@123> <@456>",
                mentions = listOf(dummyUser, dummyUser.copy(id = "456"))
            ).toKarma()

        assertEquals(listOf(Karma(dummyUser), Karma(dummyUser.copy(id = "456"))), k)
    }
}
