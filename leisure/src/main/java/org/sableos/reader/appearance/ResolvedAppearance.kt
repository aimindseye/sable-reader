package org.sableos.reader.appearance

import androidx.compose.runtime.compositionLocalOf
import org.sableos.design.AccentPreset
import org.sableos.design.AppearanceMode
import org.sableos.design.SableAppearance

/**
 * Works out the look the Reader shows. [phone] is what the phone says (the SableOS appearance provider when there
 * is one, otherwise the system Light/Dark setting with the default accent). The user's [choice] and [accent] win
 * over it; [AppearanceChoice.FOLLOW_PHONE] and a null [accent] leave the phone's values alone.
 */
fun resolveAppearance(choice: AppearanceChoice, accent: String?, phone: SableAppearance): SableAppearance =
    phone.copy(
        mode = when (choice) {
            AppearanceChoice.FOLLOW_PHONE -> phone.mode
            AppearanceChoice.LIGHT -> AppearanceMode.Light
            AppearanceChoice.DARK -> AppearanceMode.Dark
        },
        accent = accent?.let { AccentPreset.fromStableValue(it) } ?: phone.accent,
    )

/** The appearance every screen and the EPUB page follow; provided once by the theme. */
val LocalResolvedAppearance = compositionLocalOf { SableAppearance() }
