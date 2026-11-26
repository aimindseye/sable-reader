package org.sableos.reader.input

import org.sableos.reader.device.DeviceProfile

/**
 * Fits a key press to the phone's keyboard before [ReaderKeyMap] sees it.
 *
 * On a phone without arrow keys ([DeviceProfile.letterMoves]) I J K L become Up, Left, Down and Right, and the two
 * actions those letters used to carry move: key list K to Q, highlights and bookmarks L to G.
 */
object DeviceKeys {
    /** The key list, which is K elsewhere. */
    const val SHOW_KEYS_ON_LETTER_MOVES = 'q'

    /** Highlights (and Shift: bookmarks), which are L elsewhere. */
    const val HIGHLIGHTS_ON_LETTER_MOVES = 'g'

    fun translate(input: KeyInput, profile: DeviceProfile): KeyInput {
        if (!profile.letterMoves || input.key != ReaderKey.CHARACTER || input.hasCommandModifier) return input
        return when (input.character?.lowercaseChar()) {
            'i' -> input.copy(key = ReaderKey.UP, character = null)
            'j' -> input.copy(key = ReaderKey.LEFT, character = null)
            'k' -> input.copy(key = ReaderKey.DOWN, character = null)
            'l' -> input.copy(key = ReaderKey.RIGHT, character = null)
            SHOW_KEYS_ON_LETTER_MOVES -> input.copy(character = 'k')
            HIGHLIGHTS_ON_LETTER_MOVES -> input.copy(character = 'l')
            else -> input
        }
    }
}
