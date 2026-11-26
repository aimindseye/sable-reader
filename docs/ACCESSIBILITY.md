# Accessibility

## What the Reader does

- Setup wizard (`SETUP_WIZARD.md`): keyboard-only, headings, labelled options, read-aloud, no timeouts, presets for low vision, dyslexia and listening.
- Reading look: font size, weight, spacing, Accessible DfA and OpenDyslexic fonts, yellow-on-black and white-on-black pages.
- Read aloud and audiobooks, with keyboard control.
- No internet permission, so nothing depends on a connection.

## Code pass (this release)

Read from the code, not tested with TalkBack:

- Toggle rows (settings toggles, cover options) use `toggleable` with Switch or Checkbox roles, so TalkBack announces the state itself instead of a hand-written "On/Off".
- Dictionary source rows use `selectable` with the RadioButton role.
- The previous/next arrows on the reader's appearance strip have labels ("Previous Font") and a Button role.
- Settings tiles and the shared rows announce as buttons.

## Not done yet

- A TalkBack walk through every screen on a device (focus order, reading order, the web page view of a book).
- Automated contrast checks of every theme pair (WCAG AA: 4.5:1 for text).
- A dedicated accessibility page in Settings: for now, *Run setup again* reapplies the presets.
- About 20 icons are marked decorative (`contentDescription = null`); each sits beside text, but this has not been confirmed on a device.
