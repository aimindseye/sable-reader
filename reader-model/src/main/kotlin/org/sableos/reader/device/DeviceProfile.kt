package org.sableos.reader.device

/**
 * The phone the Reader runs on, worked out once at start so the keys fit its keyboard. The screen needs no profile:
 * the layout follows the window ([org.sableos.reader.layout.ReaderLayoutProfile]), so every square or near-square
 * keyboard phone gets the compact reading layout.
 *
 * Pure Kotlin, so detection is unit tested off-device. Same rules as Sable Games' device profile.
 *
 * - [Q25]: Zinwa Q25, 720 x 720. The trackpad gives arrow keys.
 * - [Q27]: Zinwa Q27, about 1080 x 1240. A Classic-style navigation row with a trackpad key, so arrows work.
 * - [TITAN_2]: Unihertz Titan 2, 1440 x 1440. No arrows or trackpad, so I J K L move.
 * - [TITAN_2_ELITE]: Unihertz Titan 2 Elite, 1080 x 1200. A touch-sensitive keyboard but no arrow keys: I J K L move.
 * - [P26]: Zinwa P26 (a BlackBerry Passport with a new board), 1440 x 1440. No arrows or trackpad: I J K L move.
 * - [KEYBOARD_NO_ARROWS]: another phone with a hardware keyboard and no arrows or trackpad: I J K L move.
 * - [OTHER]: a touch phone, or one whose arrows work. Nothing changes.
 */
enum class DeviceProfile(
    /** The name shown in Settings and the key list. */
    val label: String,
    /** I J K L move the cursor (I up, J left, K down, L right), because the phone has no arrow keys. */
    val letterMoves: Boolean,
) {
    Q25("Zinwa Q25", letterMoves = false),
    Q27("Zinwa Q27", letterMoves = false),
    TITAN_2("Unihertz Titan 2", letterMoves = true),
    TITAN_2_ELITE("Unihertz Titan 2 Elite", letterMoves = true),
    P26("Zinwa P26", letterMoves = true),
    KEYBOARD_NO_ARROWS("Keyboard phone", letterMoves = true),
    OTHER("This phone", letterMoves = false),
    ;

    companion object {
        /**
         * Works the profile out from the build's names and the keyboard Android reports. Model names are matched
         * loosely ("Titan 2", "TITAN2", "Titan_2"), and an unknown keyboard phone falls back on its configuration.
         */
        fun detect(
            manufacturer: String,
            model: String,
            device: String,
            hasQwerty: Boolean,
            hasArrows: Boolean,
        ): DeviceProfile {
            val names = listOf(manufacturer + model, device).map { it.lowercase().filter(Char::isLetterOrDigit) }
            // "q25" must not match a longer model number such as SM-Q250.
            fun named(pattern: String) = Regex(pattern).let { rx -> names.any { rx.containsMatchIn(it) } }
            return when {
                named("titan2elite") -> TITAN_2_ELITE
                named("titan2(?!\\d)") -> TITAN_2
                named("q25(?!\\d)") -> Q25
                named("q27(?!\\d)") -> Q27
                named("zinwap26(?!\\d)|^p26$|passport") -> P26
                hasQwerty && !hasArrows -> KEYBOARD_NO_ARROWS
                else -> OTHER
            }
        }
    }
}
