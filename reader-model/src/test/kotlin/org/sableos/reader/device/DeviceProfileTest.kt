package org.sableos.reader.device

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceProfileTest {
    private fun detect(
        manufacturer: String = "",
        model: String = "",
        device: String = "",
        hasQwerty: Boolean = false,
        hasArrows: Boolean = false,
    ) = DeviceProfile.detect(manufacturer, model, device, hasQwerty, hasArrows)

    @Test
    fun knownPhonesByName() {
        assertEquals(DeviceProfile.TITAN_2, detect("Unihertz", "Titan 2", hasQwerty = true))
        assertEquals(DeviceProfile.TITAN_2, detect("Unihertz", "TITAN2"))
        assertEquals(DeviceProfile.TITAN_2, detect("Unihertz", "Titan_2"))
        assertEquals(DeviceProfile.TITAN_2, detect(device = "titan2", hasQwerty = true, hasArrows = true))
        assertEquals(DeviceProfile.Q25, detect("Zinwa", "Q25", hasQwerty = true, hasArrows = true))
        assertEquals(DeviceProfile.Q25, detect("ZINWA", "zinwa_q25"))
        assertEquals(DeviceProfile.Q27, detect("Zinwa", "Q27", hasQwerty = true, hasArrows = true))
        assertEquals(DeviceProfile.P26, detect("Zinwa", "P26", hasQwerty = true))
        assertEquals(DeviceProfile.P26, detect("ZINWA", "Passport 2026"))
        assertEquals(DeviceProfile.P26, detect(device = "p26"))
    }

    @Test
    fun titan2EliteIsNotTheTitan2() {
        assertEquals(DeviceProfile.TITAN_2_ELITE, detect("Unihertz", "Titan 2 Elite", hasQwerty = true))
        assertEquals(DeviceProfile.TITAN_2_ELITE, detect("Unihertz", "Titan2Elite"))
        assertEquals(DeviceProfile.TITAN_2_ELITE, detect(device = "titan_2_elite"))
    }

    @Test
    fun aModelNumberThatOnlyContainsTheLettersIsNotAKnownPhone() {
        assertEquals(DeviceProfile.OTHER, detect("Samsung", "SM-P260"))
        assertEquals(DeviceProfile.OTHER, detect("Samsung", "SM-Q250"))
    }

    @Test
    fun otherPhonesByTheirKeyboard() {
        assertEquals(DeviceProfile.KEYBOARD_NO_ARROWS, detect("Unihertz", "Titan Slim", hasQwerty = true))
        assertEquals(DeviceProfile.OTHER, detect("BlackBerry", "BBF100-1", hasQwerty = true, hasArrows = true))
        assertEquals(DeviceProfile.OTHER, detect("Google", "Pixel 9"))
        // The first Titan is not a Titan 2.
        assertEquals(DeviceProfile.OTHER, detect("Unihertz", "Titan"))
    }

    @Test
    fun onlyPhonesWithoutArrowsMoveWithLetters() {
        val movers = DeviceProfile.entries.filter { it.letterMoves }.toSet()
        assertEquals(
            setOf(
                DeviceProfile.TITAN_2,
                DeviceProfile.TITAN_2_ELITE,
                DeviceProfile.P26,
                DeviceProfile.KEYBOARD_NO_ARROWS,
            ),
            movers,
        )
        assertFalse(DeviceProfile.Q25.letterMoves)
        assertFalse(DeviceProfile.Q27.letterMoves)
        assertTrue(DeviceProfile.entries.all { it.label.isNotBlank() })
    }
}
