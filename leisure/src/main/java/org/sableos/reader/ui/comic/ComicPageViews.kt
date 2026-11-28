package org.sableos.reader.ui.comic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.sableos.reader.comic.ComicFit
import org.sableos.reader.comic.ComicZoom

private val PROBLEM_PADDING = 16.dp
private const val DEFAULT_PAGE_ASPECT = 0.7f
private const val LENS_MAX_WIDTH_PX = 2400

/**
 * One comic page: placeholder while decoding, the picture when ready, a plain message when this page cannot be shown.
 * [strip] pages (continuous and webtoon) always fit the width and take the height of their picture.
 */
@Composable
internal fun ComicPage(
    index: Int,
    state: ComicReaderUiState,
    viewModel: ComicReaderViewModel,
    strip: Boolean,
    modifier: Modifier = Modifier,
    scroll: ScrollTarget? = null,
    active: Boolean = false,
) {
    BoxWithConstraints(modifier) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val zoom = if (strip) ComicZoom.MIN else state.zoomPercent
        val result by produceState<PageResult?>(null, index, state.layoutToken, width, height, zoom) {
            value = viewModel.pageBitmap(index, width * zoom / ComicZoom.MIN, height * zoom / ComicZoom.MIN)
        }
        when (val page = result) {
            null -> Placeholder(index, viewModel, strip)
            is PageResult.Unavailable -> ProblemMessage(index, page.problem)
            is PageResult.Ready -> PageImage(index, state, viewModel, page, strip, width, scroll.takeIf { active }, lens = active && state.magnifier && !strip)
        }
    }
}

@Composable
private fun PageImage(
    index: Int,
    state: ComicReaderUiState,
    viewModel: ComicReaderViewModel,
    page: PageResult.Ready,
    strip: Boolean,
    viewportWidthPx: Int,
    scroll: ScrollTarget?,
    lens: Boolean,
) {
    val image = remember(page.bitmap) { page.bitmap.asImageBitmap() }
    val widthFit = state.settings.effectiveFit == ComicFit.FIT_WIDTH
    val aspect = page.bitmap.width.toFloat() / page.bitmap.height.coerceAtLeast(1)
    val lensWidth = (viewportWidthPx * (if (widthFit) state.zoomPercent / ComicZoom.MIN.toFloat() else 1f) * LENS_MAX_ZOOM)
        .toInt().coerceIn(1, LENS_MAX_WIDTH_PX)
    val sharp by produceState<ImageBitmap?>(null, index, lens, lensWidth, state.layoutToken) {
        value = if (lens) {
            viewModel.magnifierBitmap(index, lensWidth, (lensWidth / aspect).toInt().coerceAtLeast(1))?.asImageBitmap()
        } else {
            null
        }
    }
    val lensLayer: (@Composable (Rect) -> Unit)? = if (lens) {
        { rect -> MagnifierLens(image, sharp, rect, state, viewModel::onAction) }
    } else {
        null
    }
    val description = "Page ${index + 1} of ${state.pageCount}"
    when {
        strip -> Image(
            bitmap = image,
            contentDescription = description,
            contentScale = ContentScale.FillWidth,
            filterQuality = FilterQuality.Medium,
            modifier = Modifier.fillMaxWidth(),
        )
        widthFit -> ScrollingPage(image, description, viewportWidthPx, state.zoomPercent, scroll, lensLayer)
        else -> BoxWithConstraints(Modifier.fillMaxSize()) {
            Image(
                bitmap = image,
                contentDescription = description,
                contentScale = ContentScale.Fit,
                filterQuality = FilterQuality.Medium,
                modifier = Modifier.fillMaxSize(),
            )
            if (lensLayer != null) {
                val w = constraints.maxWidth.toFloat()
                val h = constraints.maxHeight.toFloat()
                val fit = minOf(w / image.width, h / image.height)
                val dw = image.width * fit
                val dh = image.height * fit
                lensLayer(Rect((w - dw) / 2, (h - dh) / 2, (w + dw) / 2, (h + dh) / 2))
            }
        }
    }
}

/** A page-width (or zoomed) page: scrolls both ways, and registers with the keyboard while it is the current page. */
@Composable
private fun ScrollingPage(
    image: androidx.compose.ui.graphics.ImageBitmap,
    description: String,
    viewportWidthPx: Int,
    zoomPercent: Int,
    scroll: ScrollTarget?,
    lens: (@Composable (Rect) -> Unit)?,
) {
    val vertical = rememberScrollState()
    val horizontal = rememberScrollState()
    if (scroll != null) {
        DisposableEffect(vertical, horizontal, scroll) {
            val down: suspend (Float) -> Unit = { delta -> vertical.scrollBy(delta) }
            val pan: suspend (Float) -> Unit = { delta -> horizontal.scrollBy(delta) }
            scroll.scrollBy = down
            scroll.panBy = pan
            scroll.canScrollDown = { vertical.canScrollForward }
            scroll.canScrollUp = { vertical.canScrollBackward }
            onDispose {
                if (scroll.scrollBy === down) {
                    scroll.scrollBy = null
                    scroll.panBy = null
                    scroll.canScrollDown = null
                    scroll.canScrollUp = null
                }
            }
        }
    }
    val width = with(LocalDensity.current) { (viewportWidthPx * zoomPercent / ComicZoom.MIN).toDp() }
    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().verticalScroll(vertical).horizontalScroll(horizontal)) {
            Image(
                bitmap = image,
                contentDescription = description,
                contentScale = ContentScale.FillWidth,
                filterQuality = FilterQuality.Medium,
                modifier = Modifier.width(width),
            )
        }
        if (lens != null) {
            val pageWidth = (viewportWidthPx * zoomPercent / ComicZoom.MIN).toFloat()
            val pageHeight = pageWidth * image.height / image.width.coerceAtLeast(1)
            val left = -horizontal.value.toFloat()
            val top = -vertical.value.toFloat()
            lens(Rect(left, top, left + pageWidth, top + pageHeight))
        }
    }
}

@Composable
private fun Placeholder(index: Int, viewModel: ComicReaderViewModel, strip: Boolean) {
    val sizing = if (strip) {
        Modifier.fillMaxWidth().aspectRatio(viewModel.knownAspect(index) ?: DEFAULT_PAGE_ASPECT)
    } else {
        Modifier.fillMaxSize()
    }
    Box(sizing, contentAlignment = Alignment.Center) { CircularProgressIndicator() }
}

@Composable
private fun ProblemMessage(index: Int, problem: ComicPageProblem) {
    val reason = when (problem) {
        ComicPageProblem.TOO_LARGE -> "it is larger than Sable Reader will open"
        ComicPageProblem.COMPRESSION_BOMB -> "it looks unsafe (extreme compression)"
        ComicPageProblem.UNREADABLE -> "it could not be read"
        ComicPageProblem.ENCRYPTED -> "it is password protected"
        ComicPageProblem.UNDECODABLE -> "it is not a picture Android can decode"
        ComicPageProblem.TOO_MANY_PIXELS -> "its picture is unreasonably large"
        ComicPageProblem.OUT_OF_MEMORY -> "the device ran out of memory"
    }
    Box(Modifier.fillMaxWidth().padding(PROBLEM_PADDING), contentAlignment = Alignment.Center) {
        Text("Page ${index + 1} cannot be shown: $reason.", style = MaterialTheme.typography.bodyLarge)
    }
}
