package org.sableos.reader.appearance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.sableos.reader.device.DeviceProfile

class AppearanceTest {
    @Test
    fun choicesRoundTripAndUnknownFollowsThePhone() {
        AppearanceChoice.entries.forEach {
            assertEquals(it, AppearanceChoice.fromStableValue(it.stableValue))
        }
        assertEquals(AppearanceChoice.FOLLOW_PHONE, AppearanceChoice.fromStableValue(null))
        assertEquals(AppearanceChoice.FOLLOW_PHONE, AppearanceChoice.fromStableValue("E_INK"))
    }

    @Test
    fun lightGetsWarmPaperWhateverTheScreen() {
        assertEquals(RecommendedPage.SEPIA, PageRecommendation.recommend(dark = false, amoled = true))
        assertEquals(RecommendedPage.SEPIA, PageRecommendation.recommend(dark = false, amoled = false))
    }

    @Test
    fun darkGetsPureBlackOnlyOnAmoled() {
        assertEquals(RecommendedPage.WHITE_ON_BLACK, PageRecommendation.recommend(dark = true, amoled = true))
        assertEquals(RecommendedPage.AUTOMATIC, PageRecommendation.recommend(dark = true, amoled = false))
    }

    @Test
    fun onlyKnownAmoledPhonesAreAmoled() {
        val amoled = DeviceProfile.entries.filter { it.amoled }.toSet()
        assertEquals(setOf(DeviceProfile.Q27, DeviceProfile.TITAN_2_ELITE), amoled)
    }

    @Test
    fun everyRecommendationExplainsItself() {
        assertTrue(RecommendedPage.entries.all { it.label.isNotBlank() && it.reason.isNotBlank() })
    }
}
