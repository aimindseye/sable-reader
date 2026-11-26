package org.sableos.reader.input

import org.sableos.reader.device.DeviceProfile

/**
 * The setup wizard's key tour: the user presses a key and is told, in words that can also be spoken, what it does in
 * a book. Uses the same fitting and map as the reading screens, so it can never disagree with them.
 */
object KeyTour {
    private val bookContext = KeyContext(ReaderSurface.EPUB_PAGED, org.sableos.reader.capability.ReaderCapabilities.EPUB)

    /** What [input] does in an open book on [profile], or null when the Reader leaves that key alone. */
    fun describe(input: KeyInput, profile: DeviceProfile): String? {
        val fitted = DeviceKeys.translate(input, profile)
        val resolution = ReaderKeyMap.resolve(fitted, bookContext.copy(letterMoves = profile.letterMoves))
        return (resolution as? KeyResolution.Handled)?.action?.let(::phrase)
    }

    fun phrase(action: ReaderAction): String =
        when (action) {
            ReaderAction.PAGE_NEXT -> "Next page"
            ReaderAction.PAGE_PREVIOUS -> "Previous page"
            ReaderAction.ADVANCE -> "Forward"
            ReaderAction.REVERSE -> "Back"
            ReaderAction.SCROLL_UP -> "Scroll up"
            ReaderAction.SCROLL_DOWN -> "Scroll down"
            ReaderAction.SCROLL_PAGE_UP -> "Scroll up a page"
            ReaderAction.SCROLL_PAGE_DOWN -> "Scroll down a page"
            ReaderAction.TOGGLE_BOOKMARK -> "Add or remove a bookmark"
            ReaderAction.OPEN_CONTENTS -> "Table of contents"
            ReaderAction.OPEN_FIND -> "Find in the book"
            ReaderAction.OPEN_APPEARANCE -> "Font, size and spacing"
            ReaderAction.OPEN_HIGHLIGHTS -> "List of highlights"
            ReaderAction.OPEN_BOOKMARKS -> "List of bookmarks"
            ReaderAction.TEXT_LARGER -> "Bigger text"
            ReaderAction.TEXT_SMALLER -> "Smaller text"
            ReaderAction.CYCLE_THEME -> "Switch between the normal page and Sepia"
            ReaderAction.TOGGLE_SPEECH -> "Start or stop reading aloud"
            ReaderAction.SPEECH_PREVIOUS_SENTENCE -> "Previous sentence while reading aloud"
            ReaderAction.SPEECH_NEXT_SENTENCE -> "Next sentence while reading aloud"
            ReaderAction.SPEECH_SLOWER -> "Read aloud slower"
            ReaderAction.SPEECH_FASTER -> "Read aloud faster"
            ReaderAction.SHOW_KEYS -> "List of keys"
            ReaderAction.HIGHLIGHT -> "Highlight"
            ReaderAction.LOOKUP_WORD -> "Look up the selected word"
            ReaderAction.FIND_NEXT -> "Next find result"
            ReaderAction.FIND_PREVIOUS -> "Previous find result"
            ReaderAction.TOGGLE_CHROME -> "Show or hide the bars"
            ReaderAction.DISMISS_OR_BACK -> "Close, then leave the book"
            ReaderAction.PLAY_PAUSE -> "Play or pause"
            ReaderAction.SEEK_BACKWARD -> "Skip back"
            ReaderAction.SEEK_FORWARD -> "Skip forward"
            ReaderAction.CHAPTER_PREVIOUS -> "Previous chapter"
            ReaderAction.CHAPTER_NEXT -> "Next chapter"
        }

    /** The keys to try, as the sentence the wizard says and shows, for [profile]. */
    fun tryTheseKeys(profile: DeviceProfile): String =
        if (profile.letterMoves) {
            "Press J or L to turn pages, S for bigger text, V to read aloud, Q for the list of keys."
        } else {
            "Press the left or right arrow to turn pages, S for bigger text, V to read aloud, K for the list of keys."
        }
}
