package org.sableos.reader.ui.comic

import org.sableos.reader.comic.ComicFit
import org.sableos.reader.comic.ComicReadingMode

/** Every user intent the comic reader understands; one entry point keeps the ViewModel surface small and testable. */
sealed interface ComicUiAction {
    data object NextPage : ComicUiAction

    data object PreviousPage : ComicUiAction

    /** [pageNumber] is one-based, as typed by the user. */
    data class GoToPage(val pageNumber: Int) : ComicUiAction

    data object NextChapter : ComicUiAction

    data object PreviousChapter : ComicUiAction

    /** The viewer settled on [pageIndex] by swiping or scrolling; the position is recorded, not re-applied. */
    data class PageSettled(val pageIndex: Int) : ComicUiAction

    data object ToggleBookmark : ComicUiAction

    data class DeleteBookmark(val id: String) : ComicUiAction

    data class SetMode(val mode: ComicReadingMode) : ComicUiAction

    data class SetFit(val fit: ComicFit) : ComicUiAction

    data class Show(val overlay: ComicOverlay) : ComicUiAction

    data object ToggleChrome : ComicUiAction

    data object ZoomIn : ComicUiAction

    data object ZoomOut : ComicUiAction

    data object ZoomReset : ComicUiAction

    /** A finished pinch on the touchscreen: zoom to this percent (one of [org.sableos.reader.comic.ComicZoom.steps]). */
    data class ZoomTo(val percent: Int) : ComicUiAction

    /** The magnifier lens: on or off, moved by a step (fractions of the screen), or put at a spot (fractions). */
    data object ToggleMagnifier : ComicUiAction

    data class MoveLens(val dx: Float, val dy: Float) : ComicUiAction

    data class SetLens(val x: Float, val y: Float) : ComicUiAction
}
