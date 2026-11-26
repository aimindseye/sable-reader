package org.sableos.reader.setup

import org.sableos.reader.appearance.RecommendedPage

/**
 * The starting looks the setup wizard offers (and Settings repeats). Each is only a set of reading preferences that
 * the user can change one by one afterwards.
 */
enum class AccessibilityPreset(val label: String, val summary: String) {
    STANDARD("Standard", "The Reader's normal look."),
    LOW_VISION(
        "Low vision",
        "Yellow text on black, bold, larger text, the Accessible DfA font, wider spacing.",
    ),
    DYSLEXIA("Dyslexia", "OpenDyslexic font, wider spacing and lines, warm Sepia page."),
    LISTEN("Listen mostly", "Read aloud and audiobooks first. Reads each setup step to you."),
}

/** The reading preferences a preset sets. A null value leaves that preference as it is. */
data class PresetLook(
    /** Stored page theme name: "light", "sepia", "contrast_white" or "contrast_yellow". */
    val pageTheme: String?,
    val fontSize: Double?,
    val fontWeight: Double?,
    /** A Readium font family name such as "OPEN_DYSLEXIC", or null for the book's own. */
    val fontFamily: String?,
    val lineHeight: Double?,
    val letterSpacing: Double?,
    val wordSpacing: Double?,
    /** The wizard reads its steps aloud on its own. */
    val speaksSteps: Boolean,
)

object AccessibilityPresets {
    const val PAGE_LIGHT = "light"
    const val PAGE_SEPIA = "sepia"
    const val PAGE_WHITE_ON_BLACK = "contrast_white"
    const val PAGE_YELLOW_ON_BLACK = "contrast_yellow"

    /** Font scale at or above which the phone's own text size says the user wants bigger text. */
    const val LARGE_FONT_SCALE = 1.3f

    fun look(preset: AccessibilityPreset): PresetLook =
        when (preset) {
            AccessibilityPreset.STANDARD -> PresetLook(null, null, null, null, null, null, null, speaksSteps = false)
            AccessibilityPreset.LOW_VISION ->
                PresetLook(
                    pageTheme = PAGE_YELLOW_ON_BLACK,
                    fontSize = 1.5,
                    fontWeight = 1.5,
                    fontFamily = "ACCESSIBLE_DFA",
                    lineHeight = 1.6,
                    letterSpacing = 0.1,
                    wordSpacing = 0.2,
                    speaksSteps = false,
                )
            AccessibilityPreset.DYSLEXIA ->
                PresetLook(
                    pageTheme = PAGE_SEPIA,
                    fontSize = 1.2,
                    fontWeight = null,
                    fontFamily = "OPEN_DYSLEXIC",
                    lineHeight = 1.7,
                    letterSpacing = 0.1,
                    wordSpacing = 0.3,
                    speaksSteps = false,
                )
            AccessibilityPreset.LISTEN ->
                PresetLook(null, null, null, null, null, null, null, speaksSteps = true)
        }

    /** The stored page theme name for a recommended reading page; the wizard and Settings both use it. */
    fun pageThemeFor(page: RecommendedPage): String =
        when (page) {
            RecommendedPage.AUTOMATIC -> PAGE_LIGHT
            RecommendedPage.SEPIA -> PAGE_SEPIA
            RecommendedPage.WHITE_ON_BLACK -> PAGE_WHITE_ON_BLACK
        }

    /**
     * Which preset to start on: a screen reader means the user listens; a large system font means low vision;
     * otherwise the normal look.
     */
    fun suggest(screenReaderOn: Boolean, fontScale: Float): AccessibilityPreset =
        when {
            screenReaderOn -> AccessibilityPreset.LISTEN
            fontScale >= LARGE_FONT_SCALE -> AccessibilityPreset.LOW_VISION
            else -> AccessibilityPreset.STANDARD
        }
}
