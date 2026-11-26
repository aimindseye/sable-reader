package org.sableos.reader.setup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.readium.r2.navigator.preferences.FontFamily
import org.sableos.reader.appearance.AppearanceChoice
import org.sableos.reader.device.DeviceProfile
import org.vaachak.reader.core.data.local.ProfileDao
import org.vaachak.reader.core.data.repository.LibraryRepository
import org.vaachak.reader.core.data.repository.SettingsRepository
import org.vaachak.reader.core.data.repository.VaultRepository
import org.vaachak.reader.core.domain.model.ProfileEntity
import org.vaachak.reader.core.domain.model.ReaderPreferences
import org.vaachak.reader.core.utils.getCurrentTimeMillis
import javax.inject.Inject

/** What the wizard shows. Every choice is saved as soon as it is made, so leaving halfway loses nothing. */
data class SetupUiState(
    val step: SetupStep = SetupStep.WELCOME,
    val preset: AccessibilityPreset = AccessibilityPreset.STANDARD,
    val appearance: AppearanceChoice = AppearanceChoice.FOLLOW_PHONE,
    val detected: DeviceProfile = DeviceProfile.OTHER,
    val phone: DeviceProfile = DeviceProfile.OTHER,
    val multiUser: Boolean = false,
    val profileName: String = "",
    /** The profile made on the "Who reads here?" step; once it exists the choice is final (change it in Settings). */
    val createdProfileId: String? = null,
    val libraryMessage: String? = null,
    /** Started again from Settings: the people and privacy steps are skipped, and the profiles are left alone. */
    val rerun: Boolean = false,
) {
    val firstStep: SetupStep get() = if (rerun) SetupStep.ACCESSIBILITY else SetupStep.WELCOME
    val atFirstStep: Boolean get() = step == firstStep

    /** Next is allowed unless several people read here and the first has no name yet. */
    val canContinue: Boolean
        get() = step != SetupStep.PEOPLE || !multiUser || createdProfileId != null || profileName.isNotBlank()
}

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val vaultRepository: VaultRepository,
    private val profileDao: ProfileDao,
    private val libraryRepository: LibraryRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SetupUiState())
    val state: StateFlow<SetupUiState> = _state.asStateFlow()
    private var started = false

    /** Called once, with what Android says about the phone and the user's accessibility settings. */
    fun start(detected: DeviceProfile, suggested: AccessibilityPreset, rerun: Boolean) {
        if (started) return
        started = true
        _state.update {
            it.copy(
                detected = detected,
                phone = detected,
                preset = suggested,
                rerun = rerun,
                step = if (rerun) SetupStep.ACCESSIBILITY else SetupStep.WELCOME,
            )
        }
        viewModelScope.launch {
            if (rerun) _state.update { it.copy(multiUser = vaultRepository.isMultiUserMode.value) }
            val override = settingsRepository.deviceProfileOverride.first()
            val appearance = settingsRepository.appearanceChoice.first()
            _state.update { it.copy(phone = override ?: detected, appearance = appearance) }
        }
    }

    fun goNext() {
        val current = _state.value
        if (!current.canContinue) return
        if (current.step == SetupStep.PEOPLE) {
            createFirstProfileIfNeeded()
        }
        // The suggested preset counts as chosen once the user moves on from the step that shows it.
        if (current.step == SetupStep.ACCESSIBILITY) applyPreset(current.preset)
        current.step.next()?.let { next -> _state.update { it.copy(step = next) } }
    }

    fun goBack() {
        if (_state.value.atFirstStep) return
        _state.value.step.previous()?.let { previous -> _state.update { it.copy(step = previous) } }
    }

    fun setMultiUser(multi: Boolean) {
        if (_state.value.createdProfileId != null) return
        _state.update { it.copy(multiUser = multi) }
    }

    fun setProfileName(name: String) {
        if (_state.value.createdProfileId == null) _state.update { it.copy(profileName = name.take(MAX_NAME)) }
    }

    fun suggestName() = setProfileName(ProfileNames.random())

    fun choosePreset(preset: AccessibilityPreset) {
        _state.update { it.copy(preset = preset) }
        applyPreset(preset)
    }

    fun chooseAppearance(choice: AppearanceChoice) {
        _state.update { it.copy(appearance = choice) }
        viewModelScope.launch { settingsRepository.setAppearanceChoice(choice) }
    }

    fun choosePhone(profile: DeviceProfile) {
        _state.update { it.copy(phone = profile) }
        viewModelScope.launch {
            settingsRepository.setDeviceProfileOverride(profile.takeUnless { it == _state.value.detected })
        }
    }

    fun useReadingPage(theme: String) {
        viewModelScope.launch {
            settingsRepository.saveReaderPreferences(settingsRepository.readerPreferences.first().copy(theme = theme))
        }
    }

    fun importBook(uri: Uri) = import { libraryRepository.importBook(uri) }

    fun importFolder(tree: Uri) = import { libraryRepository.importFolder(tree) }

    fun finish(onDone: () -> Unit) {
        viewModelScope.launch {
            vaultRepository.completeOnboarding(_state.value.multiUser)
            onDone()
        }
    }

    private fun import(block: suspend () -> Result<String>) {
        viewModelScope.launch {
            val result = block()
            _state.update { it.copy(libraryMessage = result.getOrElse { e -> e.message ?: "Could not add that." }) }
        }
    }

    private fun createFirstProfileIfNeeded() {
        val current = _state.value
        if (!current.multiUser || current.createdProfileId != null || current.profileName.isBlank()) return
        val profile = ProfileEntity(name = current.profileName.trim(), avatarColorHex = "#333333", lastActiveTimestamp = getCurrentTimeMillis())
        _state.update { it.copy(createdProfileId = profile.profileId) }
        viewModelScope.launch {
            profileDao.insertProfile(profile)
            vaultRepository.setActiveVaultId(profile.profileId)
        }
    }

    private fun applyPreset(preset: AccessibilityPreset) {
        val look = AccessibilityPresets.look(preset)
        viewModelScope.launch {
            val base = ReaderPreferences()
            val customLayout = look.fontFamily != null || look.lineHeight != null
            settingsRepository.saveReaderPreferences(
                base.copy(
                    theme = look.pageTheme ?: base.theme,
                    fontSize = look.fontSize ?: base.fontSize,
                    fontWeight = look.fontWeight,
                    publisherStyles = !customLayout,
                    fontFamily = look.fontFamily?.let(::readiumFontName),
                    lineHeight = look.lineHeight,
                    letterSpacing = look.letterSpacing,
                    wordSpacing = look.wordSpacing,
                ),
            )
        }
    }

    private fun readiumFontName(key: String): String? =
        when (key) {
            "OPEN_DYSLEXIC" -> FontFamily.OPEN_DYSLEXIC.name
            "ACCESSIBLE_DFA" -> FontFamily.ACCESSIBLE_DFA.name
            else -> null
        }

    private companion object {
        const val MAX_NAME = 40
    }
}
