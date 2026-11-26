package org.sableos.reader.device

import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.staticCompositionLocalOf

/** The profile of the phone the app runs on, read from the build names and the keyboard Android reports. */
fun currentDeviceProfile(configuration: Configuration): DeviceProfile =
    DeviceProfile.detect(
        manufacturer = Build.MANUFACTURER.orEmpty(),
        model = Build.MODEL.orEmpty(),
        device = Build.DEVICE.orEmpty(),
        hasQwerty = configuration.keyboard == Configuration.KEYBOARD_QWERTY,
        hasArrows =
            configuration.navigation == Configuration.NAVIGATION_DPAD ||
                configuration.navigation == Configuration.NAVIGATION_TRACKBALL,
    )

/** The profile of this phone; MainActivity provides it. */
val LocalDeviceProfile = staticCompositionLocalOf { DeviceProfile.OTHER }
