package org.sableos.reader.input

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import org.sableos.reader.capability.ReaderCapabilities
import org.sableos.reader.device.DeviceProfile

class DeviceKeysTest {
    private val titan = DeviceProfile.TITAN_2
    private val epub = KeyContext(ReaderSurface.EPUB_PAGED, ReaderCapabilities.EPUB, letterMoves = true)

    private fun letter(c: Char, shift: Boolean = false, alt: Boolean = false, repeated: Boolean = false) =
        KeyInput(ReaderKey.CHARACTER, character = c, shift = shift, alt = alt, repeated = repeated)

    @Test
    fun ijklMoveOnAPhoneWithoutArrows() {
        assertEquals(ReaderKey.UP, DeviceKeys.translate(letter('i'), titan).key)
        assertEquals(ReaderKey.LEFT, DeviceKeys.translate(letter('j'), titan).key)
        assertEquals(ReaderKey.DOWN, DeviceKeys.translate(letter('k'), titan).key)
        assertEquals(ReaderKey.RIGHT, DeviceKeys.translate(letter('l'), titan).key)
        assertEquals(ReaderKey.RIGHT, DeviceKeys.translate(letter('L'), titan).key)
    }

    @Test
    fun aHeldLetterRepeatsLikeAHeldArrow() {
        assertEquals(true, DeviceKeys.translate(letter('l', repeated = true), titan).repeated)
    }

    @Test
    fun theMovedKeysTurnPagesAndTheOldActionsMoveToQAndG() {
        fun action(input: KeyInput) =
            (ReaderKeyMap.resolve(DeviceKeys.translate(input, titan), epub) as KeyResolution.Handled).action
        assertEquals(ReaderAction.PAGE_NEXT, action(letter('l')))
        assertEquals(ReaderAction.PAGE_PREVIOUS, action(letter('j')))
        assertEquals(ReaderAction.SHOW_KEYS, action(letter('q')))
        assertEquals(ReaderAction.OPEN_HIGHLIGHTS, action(letter('g')))
        assertEquals(ReaderAction.OPEN_BOOKMARKS, action(letter('g', shift = true)))
    }

    @Test
    fun aCommandModifierKeepsTheLetter() {
        val alt = letter('l', alt = true)
        assertSame(alt, DeviceKeys.translate(alt, titan))
    }

    @Test
    fun phonesWithArrowsAreUntouched() {
        for (profile in listOf(DeviceProfile.Q25, DeviceProfile.Q27, DeviceProfile.OTHER)) {
            for (c in "ijklqg") {
                val input = letter(c)
                assertSame(input, DeviceKeys.translate(input, profile))
            }
        }
    }

    @Test
    fun hintsNameTheRightKeys() {
        val on = KeyHints.forContext(epub).map { it.keys }
        val off = KeyHints.forContext(epub.copy(letterMoves = false)).map { it.keys }
        assertEquals(true, "J / L" in on && "Q" in on && "G / Shift+G" in on)
        assertEquals(true, "Left / Right" in off && "K" in off && "L / Shift+L" in off)
    }
}
