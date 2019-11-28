package com.github.princesslana.karmaleon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KarmaTest {

    @Test
    fun `toKarma when wrong prefix should be null`() {
        assertNull(Message("1", "++helpful <@123>", listOf(Mention("123"))).toKarma())
    }

    @Test
    fun `toKarma when no mention should be null`() {
        assertNull(Message("1", "^helpful", listOf()).toKarma())
    }

    @Test
    fun `toKarma when single mention should parse`() {
        val k = Message("1", "^helpful <@123>", listOf(Mention("123"))).toKarma()

        assertNotNull(k)
        assertEquals(Karma("123"), k)
    }

    @Test
    fun `toKarma when more than one mention should be null`() {
        val k = Message("1", "^helpful <@123> <@456>", listOf(Mention("123"), Mention("456"))).toKarma()

        assertNull(k)
    }
}
