package org.sableos.reader.appearance

import org.junit.Assert.assertEquals
import org.junit.Test
import org.sableos.design.AccentPreset
import org.sableos.design.AppearanceMode
import org.sableos.design.SableAppearance

class ResolvedAppearanceTest {
    private val sableOsDark = SableAppearance(mode = AppearanceMode.Dark, accent = AccentPreset.Teal)

    @Test
    fun followingThePhoneChangesNothing() {
        assertEquals(sableOsDark, resolveAppearance(AppearanceChoice.FOLLOW_PHONE, null, sableOsDark))
        assertEquals(SableAppearance(), resolveAppearance(AppearanceChoice.FOLLOW_PHONE, null, SableAppearance()))
    }

    @Test
    fun aChosenModeBeatsThePhone() {
        assertEquals(AppearanceMode.Light, resolveAppearance(AppearanceChoice.LIGHT, null, sableOsDark).mode)
        assertEquals(AppearanceMode.Dark, resolveAppearance(AppearanceChoice.DARK, null, SableAppearance()).mode)
    }

    @Test
    fun aChosenAccentBeatsThePhoneAndAnUnknownOneFallsBackToBlue() {
        assertEquals(AccentPreset.Gold, resolveAppearance(AppearanceChoice.FOLLOW_PHONE, "gold", sableOsDark).accent)
        assertEquals(AccentPreset.Blue, resolveAppearance(AppearanceChoice.FOLLOW_PHONE, "pink", sableOsDark).accent)
        assertEquals(AccentPreset.Teal, resolveAppearance(AppearanceChoice.LIGHT, null, sableOsDark).accent)
    }
}
