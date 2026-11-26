package org.sableos.reader.appearance

/**
 * What the user chose for the Reader's own look (Settings > App Appearance and the setup wizard).
 *
 * [FOLLOW_PHONE] is the default: on SableOS the system appearance, on any other phone its Light or Dark setting.
 * [LIGHT] and [DARK] pin the Reader whatever the phone does.
 */
enum class AppearanceChoice(val stableValue: String) {
    FOLLOW_PHONE("follow-phone"),
    LIGHT("light"),
    DARK("dark"),
    ;

    companion object {
        fun fromStableValue(value: String?): AppearanceChoice =
            entries.firstOrNull { it.stableValue == value } ?: FOLLOW_PHONE
    }
}
