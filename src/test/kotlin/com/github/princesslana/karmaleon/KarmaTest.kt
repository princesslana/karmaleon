package com.github.princesslana.karmaleon

import java.util.Date
import kotlin.test.*

class KarmaTest {
    val dummyUser = User("123", "Username", "1234")
    val dummyFrom = dummyUser.copy(id = "789")
    val dummyChannelId = "000"
    val dummyMessageId = "111"
    val now = Date()
    val dummyMessage = Message(dummyMessageId, dummyFrom, dummyChannelId, "",
        listOf(), now)

    fun karma(to: User): Karma {
        return Karma(to, dummyFrom, dummyChannelId, dummyMessageId, now)
    }

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

        assertEquals(listOf(karma(dummyUser)), k)
    }

    @Test
    fun `toKarma when single mention with other thanks should parse`() {
        val k = dummyMessage.copy(
                content = "thank you <@123>", mentions = listOf(dummyUser)
            ).toKarma()

        assertEquals(listOf(karma(dummyUser)), k)
    }

    @Test
    fun `toKarma when more than one mention should give multiple karma`() {
        val k = dummyMessage.copy(
                content = "thanks <@123> <@456>",
                mentions = listOf(dummyUser, dummyUser.copy(id = "456"))
            ).toKarma()

        assertEquals(listOf(karma(dummyUser), karma(dummyUser.copy(id = "456"))), k)
    }
}
