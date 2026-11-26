# Device profiles

A device profile describes one phone to the Reader: how big its screen is, which keys it has, and the layout and key map that follow from that.

| Phone | Screen | Keys | Notes |
| --- | --- | --- | --- |
| Zinwa Q25 | 720 x 720 | Full keyboard, trackpad | The reference device; the SableOS 2.0 design was tested here |
| Unihertz Titan 2 | 1440 x 1440 square | Full keyboard, no arrows or trackpad | Moves with I J K L, as in Sable Games |
| Unihertz Titan 2 Elite | 1080 x 1200, 4.03" AMOLED, 120 Hz | QWERTY keyboard that is also touch-sensitive; no arrows or trackpad. Sym, Fn and a side Programmable key can be remapped | Not square (0.9 ratio); Android 16. Keyboard swipes act as scroll or cursor moves only when the user turns on Settings > Gestures > Keyboard Gestures, so the profile keeps I J K L as the reliable movement keys (user manual V1.0) |
| Zinwa P26 | 1440 x 1440, 4.5" square (BlackBerry Passport screen) | Full keyboard with touch-sensitive keys, no trackpad | Passport with a new board; to be confirmed on a device |
| Zinwa Q27 | 1080 x 1240, 4" AMOLED (reported) | Classic-style: navigation row with a trackpad key above a QWERTY keyboard | Resolution is from press coverage; to be confirmed on a device |

Any other phone gets a generic profile: touch plus arrow keys if present.

Profiles are chosen automatically from the phone's model and can be changed in the setup wizard and in Settings.

## How the Reader uses a profile

The profile is read once at start from the build's manufacturer, model and device names (matched loosely, so "Titan 2", "TITAN2" and "Titan_2" all work, and a longer model number such as SM-Q250 is not a Q25) and from the keyboard Android reports. Code: `DeviceProfile` in `reader-model`, `CurrentDeviceProfile.kt` in `leisure`. It is the only place that looks at the build's identity; a contract test keeps it that way.

| Profile | Moves with | Key list | Highlights / bookmarks |
| --- | --- | --- | --- |
| Zinwa Q25, Zinwa Q27, any phone with working arrows | Arrow keys or trackpad | K | L / Shift+L |
| Titan 2, Titan 2 Elite, Zinwa P26, other keyboard phones without arrows | I J K L (I up, J left, K down, L right; a held key repeats) | Q | G / Shift+G |

On a phone that moves with I J K L the letters K and L are taken by movement, so the key list moves to Q and highlights to G. The key list and the on-screen hints show the right keys for the phone.

The screen layout does not depend on the profile: it follows the window size, so the 720 x 720, 1440 x 1440, 1080 x 1200 and 1080 x 1240 screens all use the compact layout.
