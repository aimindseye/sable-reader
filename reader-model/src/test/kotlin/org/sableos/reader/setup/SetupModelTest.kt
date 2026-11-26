package org.sableos.reader.setup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.sableos.reader.device.DeviceProfile
import org.sableos.reader.input.KeyInput
import org.sableos.reader.input.KeyTour
import org.sableos.reader.input.ReaderAction
import org.sableos.reader.input.ReaderKey

class SetupModelTest {
    private fun letter(c: Char, shift: Boolean = false) = KeyInput(ReaderKey.CHARACTER, character = c, shift = shift)

    @Test
    fun aScreenReaderMeansListenAndALargeFontMeansLowVision() {
        assertEquals(AccessibilityPreset.LISTEN, AccessibilityPresets.suggest(screenReaderOn = true, fontScale = 1f))
        assertEquals(AccessibilityPreset.LISTEN, AccessibilityPresets.suggest(screenReaderOn = true, fontScale = 2f))
        assertEquals(AccessibilityPreset.LOW_VISION, AccessibilityPresets.suggest(false, 1.3f))
        assertEquals(AccessibilityPreset.STANDARD, AccessibilityPresets.suggest(false, 1f))
        assertEquals(AccessibilityPreset.STANDARD, AccessibilityPresets.suggest(false, 1.15f))
    }

    @Test
    fun standardChangesNothingAndListenOnlySpeaks() {
        val standard = AccessibilityPresets.look(AccessibilityPreset.STANDARD)
        assertNull(standard.pageTheme)
        assertNull(standard.fontSize)
        assertEquals(false, standard.speaksSteps)
        val listen = AccessibilityPresets.look(AccessibilityPreset.LISTEN)
        assertNull(listen.pageTheme)
        assertEquals(true, listen.speaksSteps)
    }

    @Test
    fun lowVisionIsYellowOnBlackBoldAndBig() {
        val look = AccessibilityPresets.look(AccessibilityPreset.LOW_VISION)
        assertEquals("contrast_yellow", look.pageTheme)
        assertEquals("ACCESSIBLE_DFA", look.fontFamily)
        assertTrue(look.fontSize!! >= 1.4)
        assertTrue(look.fontWeight!! > 1.0)
    }

    @Test
    fun dyslexiaUsesTheDyslexicFontOnSepia() {
        val look = AccessibilityPresets.look(AccessibilityPreset.DYSLEXIA)
        assertEquals("sepia", look.pageTheme)
        assertEquals("OPEN_DYSLEXIC", look.fontFamily)
    }

    @Test
    fun theKeyTourSaysWhatTheKeyDoesOnThePhone() {
        val q25 = DeviceProfile.Q25
        val titan = DeviceProfile.TITAN_2
        assertEquals("Next page", KeyTour.describe(KeyInput(ReaderKey.RIGHT), q25))
        assertEquals("Next page", KeyTour.describe(letter('l'), titan))
        // L on a phone with arrows is highlights; on the Titan it moves.
        assertEquals("List of highlights", KeyTour.describe(letter('l'), q25))
        assertEquals("List of highlights", KeyTour.describe(letter('g'), titan))
        assertEquals("List of keys", KeyTour.describe(letter('q'), titan))
        assertNull(KeyTour.describe(letter('z'), q25))
    }

    @Test
    fun everyActionHasAPhrase() {
        ReaderAction.entries.forEach { assertNotNull(KeyTour.phrase(it)) }
        assertTrue(KeyTour.tryTheseKeys(DeviceProfile.TITAN_2).contains("J or L"))
        assertTrue(KeyTour.tryTheseKeys(DeviceProfile.Q25).contains("arrow"))
    }
}

class ProfileNamesAndStepsTest {
    @org.junit.Test
    fun randomNameHasTwoWordsFromTheLists() {
        repeat(50) {
            val (a, n) = ProfileNames.random().split(" ")
            org.junit.Assert.assertTrue(a in ProfileNames.adjectives && n in ProfileNames.nouns)
        }
    }

    @org.junit.Test
    fun stepsRunInOrderAndEndAtDone() {
        org.junit.Assert.assertNull(SetupStep.WELCOME.previous())
        org.junit.Assert.assertEquals(SetupStep.OFFLINE, SetupStep.WELCOME.next())
        org.junit.Assert.assertNull(SetupStep.DONE.next())
        org.junit.Assert.assertEquals("Step 1 of 9", SetupStep.WELCOME.progress())
    }
}
