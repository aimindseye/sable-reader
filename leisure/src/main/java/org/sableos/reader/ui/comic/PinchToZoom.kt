package org.sableos.reader.ui.comic

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import org.sableos.reader.comic.ComicZoom
import kotlin.math.hypot

/**
 * Two-finger pinch on the touchscreen. It watches before the page sees the touches (so one-finger scrolling,
 * swipes and taps are untouched) and takes over only once a second finger is down. When the fingers lift, the
 * spread over the start distance goes through [ComicZoom.afterPinch] and [onPinch] gets the new percent.
 */
internal fun Modifier.pinchToZoom(currentPercent: Int, onPinch: (ratio: Float, percent: Int) -> Unit): Modifier =
    pointerInput(currentPercent) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
            var startSpan = 0f
            var span = 0f
            var pinched = false
            do {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                val down = event.changes.filter { it.pressed }
                if (down.size >= 2) {
                    pinched = true
                    span = hypot(down[0].position.x - down[1].position.x, down[0].position.y - down[1].position.y)
                    if (startSpan == 0f) startSpan = span
                    event.changes.forEach { it.consume() }
                } else if (pinched) {
                    event.changes.forEach { it.consume() }
                }
            } while (event.changes.any { it.pressed })
            if (pinched && startSpan > 0f && span > 0f) {
                val ratio = span / startSpan
                onPinch(ratio, ComicZoom.afterPinch(currentPercent, ratio))
            }
        }
    }
