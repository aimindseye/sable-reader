/*
 *  Copyright (c) 2026 Piyush Daiya
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 */

package org.vaachak.reader.leisure.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role
import org.sableos.design.AccentPreset
import org.sableos.design.AppearanceMode
import org.sableos.reader.appearance.AppearanceChoice
import org.sableos.reader.appearance.LocalResolvedAppearance
import org.sableos.reader.appearance.PageRecommendation
import org.sableos.reader.device.LocalDeviceProfile
import org.vaachak.reader.leisure.ui.testability.Tid
import org.vaachak.reader.leisure.ui.testability.TidScreen
import org.vaachak.reader.leisure.ui.testability.tid

@Composable
private fun ChoiceRow(label: String, selected: Boolean, tag: String, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, role = Role.RadioButton, onClick = onSelect)
            .tid(tag),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(label, modifier = Modifier.padding(start = 12.dp), style = MaterialTheme.typography.bodyLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppAppearanceScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    TidScreen(Tid.Screen.appAppearance) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Appearance") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.tid("app_appearance_back")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val choice by viewModel.appearanceChoice.collectAsState()
            val accent by viewModel.accentChoice.collectAsState()
            val profile = LocalDeviceProfile.current
            val resolved = LocalResolvedAppearance.current
            val dark = when (resolved.mode) {
                AppearanceMode.Dark -> true
                AppearanceMode.Light -> false
                AppearanceMode.FollowSystem -> isSystemInDarkTheme()
            }
            val recommended = PageRecommendation.recommend(dark = dark, amoled = profile.amoled)

            Text("INTERFACE THEME", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Text(
                "Follow phone uses the Light or Dark setting of ${profile.label}. Light and Dark keep the Reader that way.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ChoiceRow("Follow phone", choice == AppearanceChoice.FOLLOW_PHONE, "appearance_follow") {
                viewModel.setAppearanceChoice(AppearanceChoice.FOLLOW_PHONE)
            }
            ChoiceRow("Light", choice == AppearanceChoice.LIGHT, "appearance_light") {
                viewModel.setAppearanceChoice(AppearanceChoice.LIGHT)
            }
            ChoiceRow("Dark", choice == AppearanceChoice.DARK, "appearance_dark") {
                viewModel.setAppearanceChoice(AppearanceChoice.DARK)
            }

            Text("ACCENT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            ChoiceRow("Follow phone", accent == null, "accent_follow") { viewModel.setAccentChoice(null) }
            AccentPreset.entries.forEach { preset ->
                ChoiceRow(preset.displayName, accent == preset.stableValue, "accent_${preset.stableValue}") {
                    viewModel.setAccentChoice(preset.stableValue)
                }
            }

            Text("READING PAGE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Text(
                "Recommended here: ${recommended.label}. ${recommended.reason}",
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(
                onClick = { viewModel.useRecommendedPage(recommended) },
                modifier = Modifier.tid("use_recommended_page"),
            ) { Text("Use ${recommended.label}") }
        }
    }
    }
}
