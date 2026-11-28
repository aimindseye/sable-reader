package org.sableos.reader.comic

import org.junit.Assert.assertEquals
import org.junit.Test

class ComicZoomTest {
    @Test
    fun zoomingInClimbsTheLadderAndStopsAtTheTop() {
        assertEquals(150, ComicZoom.zoomIn(100))
        assertEquals(200, ComicZoom.zoomIn(150))
        assertEquals(200, ComicZoom.zoomIn(200))
    }

    @Test
    fun zoomingOutDescendsAndStopsAtTheBottom() {
        assertEquals(150, ComicZoom.zoomOut(200))
        assertEquals(100, ComicZoom.zoomOut(150))
        assertEquals(100, ComicZoom.zoomOut(100))
    }

    @Test
    fun oddValuesSnapToTheNextStep() {
        assertEquals(150, ComicZoom.zoomIn(120))
        assertEquals(100, ComicZoom.zoomOut(120))
    }

    @Test
    fun theLensStepsStayBetweenTwoAndFiveTimes() {
        assertEquals(4f, ComicZoom.lensIn(3f))
        assertEquals(5f, ComicZoom.lensIn(5f))
        assertEquals(2f, ComicZoom.lensOut(3f))
        assertEquals(2f, ComicZoom.lensOut(2f))
        assertEquals(3f, ComicZoom.LENS_DEFAULT)
    }

    @Test
    fun aPinchMovesOneStepOrJumpsAndSmallOnesChangeNothing() {
        assertEquals(150, ComicZoom.afterPinch(100, 1.3f))
        assertEquals(200, ComicZoom.afterPinch(100, 2f))
        assertEquals(100, ComicZoom.afterPinch(150, 0.7f))
        assertEquals(100, ComicZoom.afterPinch(200, 0.4f))
        assertEquals(150, ComicZoom.afterPinch(150, 1.1f))
        assertEquals(150, ComicZoom.afterPinch(150, 0.9f))
    }
}
