package org.sableos.reader.ui.comic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/** How much the lens enlarges the page, and the width of the lens. */
internal const val LENS_ZOOM = 2.5f
private const val LENS_DIAMETER_DP = 160
private const val RING_WIDTH_DP = 3

/**
 * A round lens over a comic page. [pageRect] is where the whole page sits in this area, in pixels (so it moves with
 * the scroll of a zoomed page); the lens shows the part of the page under its centre at [LENS_ZOOM] times the size
 * on screen. [sharp] is a higher-resolution copy of the page; until it is ready the lens uses [base], which is
 * blurrier. Touch moves the lens: tap or drag anywhere; the arrow keys move it too (see ComicKeyboard).
 */
@Composable
internal fun MagnifierLens(
    base: ImageBitmap,
    sharp: ImageBitmap?,
    pageRect: Rect,
    state: ComicReaderUiState,
    onAction: (ComicUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val diameter = with(density) { LENS_DIAMETER_DP.dp.toPx() }
    val ringWidth = with(density) { RING_WIDTH_DP.dp.toPx() }
    val ring = MaterialTheme.colorScheme.primary
    val source = sharp ?: base
    Canvas(
        modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { at -> onAction(ComicUiAction.SetLens(at.x / size.width, at.y / size.height)) }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    onAction(ComicUiAction.SetLens(change.position.x / size.width, change.position.y / size.height))
                }
            },
    ) {
        if (pageRect.width <= 0f || pageRect.height <= 0f) return@Canvas
        val centre = Offset(state.lensX * size.width, state.lensY * size.height)
        val u = ((centre.x - pageRect.left) / pageRect.width).coerceIn(0f, 1f)
        val v = ((centre.y - pageRect.top) / pageRect.height).coerceIn(0f, 1f)
        val srcW = (diameter / LENS_ZOOM / pageRect.width * source.width).coerceIn(1f, source.width.toFloat())
        val srcH = (diameter / LENS_ZOOM / pageRect.height * source.height).coerceIn(1f, source.height.toFloat())
        val srcX = (u * source.width - srcW / 2).coerceIn(0f, source.width - srcW)
        val srcY = (v * source.height - srcH / 2).coerceIn(0f, source.height - srcH)
        val circle = Path().apply { addOval(Rect(centre, diameter / 2)) }
        clipPath(circle) {
            drawRect(Color.Black)
            drawImage(
                image = source,
                srcOffset = IntOffset(srcX.roundToInt(), srcY.roundToInt()),
                srcSize = IntSize(srcW.roundToInt().coerceAtLeast(1), srcH.roundToInt().coerceAtLeast(1)),
                dstOffset = IntOffset((centre.x - diameter / 2).roundToInt(), (centre.y - diameter / 2).roundToInt()),
                dstSize = IntSize(diameter.roundToInt(), diameter.roundToInt()),
                filterQuality = FilterQuality.High,
            )
        }
        drawCircle(ring, radius = diameter / 2, center = centre, style = Stroke(ringWidth))
    }
}
