package org.sableos.reader.setup

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.sableos.design.AppearanceMode
import org.sableos.reader.appearance.AppearanceChoice
import org.sableos.reader.appearance.LocalResolvedAppearance
import org.sableos.reader.appearance.PageRecommendation
import org.sableos.reader.device.DeviceProfile
import org.sableos.reader.device.LocalDeviceProfile
import org.sableos.reader.device.currentDeviceProfile
import org.sableos.reader.input.KeyInput
import org.sableos.reader.input.KeyTour
import org.sableos.reader.input.LocalReaderKeyDispatcher
import org.sableos.reader.input.ReaderKey
import java.util.Locale

/**
 * The first-run setup wizard, also run again from Settings. Every step works with the keyboard alone (Tab or arrows,
 * Enter, Back for the previous step), is laid out as headings and labelled options for TalkBack, has no timeouts, and
 * can be read aloud.
 */
@Composable
fun SetupWizardScreen(
    rerun: Boolean,
    onFinished: () -> Unit,
    viewModel: SetupViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val state by viewModel.state.collectAsState()
    val speaker = rememberSpeaker()
    val first = remember { FocusRequester() }
    val next = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        val screenReader = context.getSystemService(Context.ACCESSIBILITY_SERVICE)
            .let { it as? AccessibilityManager }?.isTouchExplorationEnabled == true
        viewModel.start(
            detected = currentDeviceProfile(configuration),
            suggested = AccessibilityPresets.suggest(screenReader, configuration.fontScale),
            rerun = rerun,
        )
    }

    val step = state.step
    val speaksSteps = AccessibilityPresets.look(state.preset).speaksSteps
    val spoken = spokenFor(state)
    LaunchedEffect(step) {
        if (speaksSteps) speaker.speak(spoken)
        runCatching { (if (step in CHOICE_STEPS || step in BUTTON_STEPS) first else next).requestFocus() }
    }

    BackHandler(enabled = !state.atFirstStep) { viewModel.goBack() }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(step.progress(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            step.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.semantics { heading() },
        )
        Column(
            Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            when (step) {
                SetupStep.WELCOME -> Body(
                    "Sable Reader reads books, comics and audiobooks, and works fully from the keyboard. " +
                        "These few steps set it up for you. You can change everything later in Settings.",
                )
                SetupStep.OFFLINE -> Body(
                    "Sable Reader works completely offline. It has no internet permission at all, so nothing you read, " +
                        "highlight or search for ever leaves this phone. Your library, bookmarks and settings stay here.",
                )
                SetupStep.PEOPLE -> PeopleStep(state, viewModel, first)
                SetupStep.ACCESSIBILITY -> AccessibilityStep(state, viewModel, first)
                SetupStep.PHONE -> PhoneStep(state, viewModel, first)
                SetupStep.LOOK -> LookStep(state, viewModel, first)
                SetupStep.LIBRARY -> LibraryStep(state, viewModel, first)
                SetupStep.KEYS -> KeysStep(state.phone, first, speaksSteps, speaker)
                SetupStep.DONE -> Body(
                    "Setup is finished. You can run it again, or change any choice, from Settings.\n\n" +
                        KeyTour.tryTheseKeys(state.phone),
                )
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (state.atFirstStep) {
                if (rerun) TextButton(onClick = onFinished) { Text("Cancel") }
            } else {
                OutlinedButton(onClick = viewModel::goBack, modifier = Modifier.heightIn(min = 48.dp)) { Text("Back") }
            }
            TextButton(onClick = { speaker.speak(spoken) }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Read aloud") }
            androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
            val last = step == SetupStep.DONE
            Button(
                onClick = { if (last) viewModel.finish(onFinished) else viewModel.goNext() },
                enabled = state.canContinue,
                modifier = Modifier.heightIn(min = 48.dp).let { if (step in CHOICE_STEPS || step in BUTTON_STEPS) it else it.focusRequester(next) },
            ) { Text(if (last) "Open my library" else "Next") }
        }
    }
}

private val CHOICE_STEPS = setOf(SetupStep.PEOPLE, SetupStep.ACCESSIBILITY, SetupStep.PHONE, SetupStep.LOOK)
private val BUTTON_STEPS = setOf(SetupStep.LIBRARY, SetupStep.KEYS)

@Composable
private fun Body(text: String) = Text(text, style = MaterialTheme.typography.bodyLarge)

@Composable
private fun Option(label: String, selected: Boolean, modifier: Modifier = Modifier, detail: String? = null, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Column(Modifier.padding(start = 8.dp)) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            if (detail != null) Text(detail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun PeopleStep(state: SetupUiState, vm: SetupViewModel, first: FocusRequester) {
    val locked = state.createdProfileId != null
    Option("Just me", !state.multiUser, Modifier.focusRequester(first), "One library and one set of settings.") { vm.setMultiUser(false) }
    Option(
        "Several people",
        state.multiUser,
        detail = "Each person gets their own library, bookmarks and settings. Add more people, and an optional PIN, in Settings.",
    ) { vm.setMultiUser(true) }
    if (state.multiUser) {
        Body("Name the first reader. Not sure what to call yourself? Choose Suggest a random name for a name like Quiet Owl; you can suggest again until you like one.")
        OutlinedTextField(
            value = state.profileName,
            onValueChange = vm::setProfileName,
            label = { Text("Reader name") },
            singleLine = true,
            enabled = !locked,
            modifier = Modifier.fillMaxWidth(),
        )
        if (!locked) OutlinedButton(onClick = vm::suggestName, modifier = Modifier.heightIn(min = 48.dp)) { Text("Suggest a random name") }
    }
    if (locked) Body("${state.profileName.trim()} has been created. Change people later in Settings.")
}

@Composable
private fun AccessibilityStep(state: SetupUiState, vm: SetupViewModel, first: FocusRequester) {
    Body("Pick the look that suits how you read. Each only sets text and page choices, and you can change them one by one later. Standard keeps the normal look.")
    AccessibilityPreset.entries.forEachIndexed { index, preset ->
        Option(
            preset.label,
            state.preset == preset,
            if (index == 0) Modifier.focusRequester(first) else Modifier,
            preset.summary,
        ) { vm.choosePreset(preset) }
    }
}

@Composable
private fun PhoneStep(state: SetupUiState, vm: SetupViewModel, first: FocusRequester) {
    Body("Sable Reader found: ${state.detected.label}. Pick your phone if that is wrong, so the keys fit your keyboard.")
    DeviceProfile.entries.forEachIndexed { index, profile ->
        Option(
            profile.label,
            state.phone == profile,
            if (index == 0) Modifier.focusRequester(first) else Modifier,
            if (profile.letterMoves) "No arrow keys: I J K L move." else "Arrow keys or trackpad move.",
        ) { vm.choosePhone(profile) }
    }
}

@Composable
private fun LookStep(state: SetupUiState, vm: SetupViewModel, first: FocusRequester) {
    val resolved = LocalResolvedAppearance.current
    val dark = when (resolved.mode) {
        AppearanceMode.Dark -> true
        AppearanceMode.Light -> false
        AppearanceMode.FollowSystem -> isSystemInDarkTheme()
    }
    val recommended = PageRecommendation.recommend(dark, LocalDeviceProfile.current.amoled || state.phone.amoled)
    Option("Follow phone", state.appearance == AppearanceChoice.FOLLOW_PHONE, Modifier.focusRequester(first)) {
        vm.chooseAppearance(AppearanceChoice.FOLLOW_PHONE)
    }
    Option("Light", state.appearance == AppearanceChoice.LIGHT) { vm.chooseAppearance(AppearanceChoice.LIGHT) }
    Option("Dark", state.appearance == AppearanceChoice.DARK) { vm.chooseAppearance(AppearanceChoice.DARK) }
    Body("Recommended reading page here: ${recommended.label}. ${recommended.reason}")
    OutlinedButton(
        onClick = { vm.useReadingPage(AccessibilityPresets.pageThemeFor(recommended)) },
        modifier = Modifier.heightIn(min = 48.dp),
    ) { Text("Use ${recommended.label}") }
}

@Composable
private fun LibraryStep(state: SetupUiState, vm: SetupViewModel, first: FocusRequester) {
    val bookPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> uri?.let(vm::importBook) }
    val folderPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri -> uri?.let(vm::importFolder) }
    Body("Add something to read now, or skip and add books later from the Books tab.")
    Button(
        onClick = { bookPicker.launch(arrayOf("*/*")) },
        modifier = Modifier.heightIn(min = 48.dp).focusRequester(first),
    ) { Text("Add a book") }
    OutlinedButton(onClick = { folderPicker.launch(null) }, modifier = Modifier.heightIn(min = 48.dp)) {
        Text("Add a comic or audiobook folder")
    }
    state.libraryMessage?.let { Text(it, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) }
}

@Composable
private fun KeysStep(phone: DeviceProfile, first: FocusRequester, speak: Boolean, speaker: Speaker) {
    val dispatcher = LocalReaderKeyDispatcher.current
    var listening by remember { mutableStateOf(false) }
    var heard by remember { mutableStateOf("") }
    DisposableEffect(listening, phone) {
        val unregister = if (listening) {
            dispatcher.register { input: KeyInput ->
                if (input.key == ReaderKey.ESCAPE || input.key == ReaderKey.BACK) {
                    listening = false
                    heard = "Stopped listening."
                } else {
                    heard = KeyTour.describe(input, phone) ?: "That key does nothing in a book."
                    if (speak) speaker.speak(heard)
                }
                true
            }
        } else {
            null
        }
        onDispose { unregister?.invoke() }
    }
    Body(KeyTour.tryTheseKeys(phone))
    Button(
        onClick = { listening = !listening; heard = if (listening) "Listening. Press any key." else "" },
        modifier = Modifier.heightIn(min = 48.dp).focusRequester(first),
    ) { Text(if (listening) "Stop listening (or press Escape)" else "Press keys to hear what they do") }
    if (heard.isNotEmpty()) {
        Text(heard, style = MaterialTheme.typography.titleMedium, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
    }
}

private fun spokenFor(state: SetupUiState): String {
    val step = state.step
    val body = when (step) {
        SetupStep.WELCOME -> "Sable Reader reads books, comics and audiobooks, and works from the keyboard. These few steps set it up for you."
        SetupStep.OFFLINE -> "Sable Reader works completely offline. It has no internet permission, so nothing you read ever leaves this phone."
        SetupStep.PEOPLE -> "Choose just me, or several people, who each get their own library. With several people, name the first reader, or choose Suggest a random name."
        SetupStep.ACCESSIBILITY -> "Choose how you read: Standard, Low vision, Dyslexia, or Listen mostly. Currently ${state.preset.label}."
        SetupStep.PHONE -> "Sable Reader found ${state.detected.label}. Choose your phone if that is wrong."
        SetupStep.LOOK -> "Choose Follow phone, Light or Dark, then use the recommended reading page if you like."
        SetupStep.LIBRARY -> "Add a book or a folder now, or skip this step."
        SetupStep.KEYS -> KeyTour.tryTheseKeys(state.phone)
        SetupStep.DONE -> "Setup is finished. ${KeyTour.tryTheseKeys(state.phone)}"
    }
    return "${step.title}. ${step.progress()}. $body"
}

/** A thin wrapper over Android's local text to speech, shut down when the screen leaves. */
class Speaker(private val engine: () -> TextToSpeech?) {
    fun speak(text: String) {
        engine()?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "setup")
    }
}

@Composable
private fun rememberSpeaker(): Speaker {
    val context = LocalContext.current
    var ready by remember { mutableStateOf(false) }
    val tts = remember {
        var created: TextToSpeech? = null
        created = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                created?.language = Locale.getDefault()
                ready = true
            }
        }
        created
    }
    DisposableEffect(tts) { onDispose { tts.stop(); tts.shutdown() } }
    return remember(tts) { Speaker { if (ready) tts else null } }
}
