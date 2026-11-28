package org.sableos.reader.comic

/** Zoom steps for a comic page, in percent of the page-width fit. Pure so the ladder is JVM-tested. */
object ComicZoom {
    const val MIN: Int = 100
    val steps: List<Int> = listOf(MIN, 150, 200)
    val MAX: Int = steps.last()

    fun zoomIn(current: Int): Int = steps.firstOrNull { it > current } ?: MAX

    fun zoomOut(current: Int): Int = steps.lastOrNull { it < current } ?: MIN

    /** The magnifier lens strengths, as times the on-screen size of the page. */
    val lensSteps: List<Float> = listOf(2f, 3f, 4f, 5f)
    const val LENS_DEFAULT: Float = 3f

    fun lensIn(current: Float): Float = lensSteps.firstOrNull { it > current } ?: lensSteps.last()

    fun lensOut(current: Float): Float = lensSteps.lastOrNull { it < current } ?: lensSteps.first()

    /** A pinch that spreads the fingers by this factor or more zooms in a step; one that closes them by 1/this zooms out. */
    const val PINCH_STEP: Float = 1.25f

    /** The zoom a finished pinch of the given spread ratio (end distance over start distance) leads to. */
    fun afterPinch(current: Int, ratio: Float): Int = when {
        ratio >= PINCH_STEP * PINCH_STEP -> MAX
        ratio >= PINCH_STEP -> zoomIn(current)
        ratio <= 1f / (PINCH_STEP * PINCH_STEP) -> MIN
        ratio <= 1f / PINCH_STEP -> zoomOut(current)
        else -> current
    }
}
