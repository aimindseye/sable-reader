package org.vaachak.reader.leisure.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.sableos.design.SableTheme
import org.sableos.design.rememberGlobalSableAppearance
import org.sableos.reader.appearance.AppearanceChoice
import org.sableos.reader.appearance.LocalResolvedAppearance
import org.sableos.reader.appearance.resolveAppearance

/**
 * The Reader's look: the phone's (SableOS appearance provider, else the system Light/Dark) unless the user chose
 * Light, Dark or an accent in Settings > App Appearance.
 */
@Composable
fun ProductReaderTheme(
    choice: AppearanceChoice = AppearanceChoice.FOLLOW_PHONE,
    accent: String? = null,
    content: @Composable () -> Unit,
) {
    val appearance = resolveAppearance(choice, accent, rememberGlobalSableAppearance())
    CompositionLocalProvider(LocalResolvedAppearance provides appearance) {
        SableTheme(appearance = appearance, content = content)
    }
}
