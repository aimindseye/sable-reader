# Setup wizard

The wizard runs on first start and replaces the old welcome and first-run profile screens. Run it again from
Settings > Appearance > Run setup again (which skips the people steps and leaves profiles alone).

## Steps

1. **Welcome**
2. **Private and offline**: the app has no internet permission (checked in CI by `tools/check-apk.sh`), so nothing leaves the phone. There is no offline switch because there is no online mode.
3. **Who reads here?** Just me, or several people. For several, name the first reader, or press *Suggest a random name* (for example "Quiet Owl"; press again for another). The profile is created and selected right away because every profile keeps its own library and settings. More people and PINs are added in Settings.
4. **How do you read?** Presets: Standard, Low vision, Dyslexia, Listen mostly. Suggested from TalkBack and the phone's font size. A preset only sets reading preferences and replaces the current reading look; every value can be changed afterwards. Standard restores the defaults.
5. **Your phone**: the detected device profile, changeable if detection is wrong. Stored for the whole device.
6. **Look**: Follow phone / Light / Dark and the recommended reading page (see `THEMES.md`).
7. **Your books**: add a book, or a comic or audiobook folder. Optional.
8. **Try the keys**: press keys and hear or read what each does in a book, for this phone's keyboard. Escape or Back stops listening, so the keyboard is never trapped.
9. **All set**

## Accessibility

- Every step works from the keyboard alone: Tab or arrows, Enter, Back for the previous step. The first control of each step takes focus.
- Titles are headings; options are labelled radio buttons; results are announced as polite live regions; controls are at least 48 dp high; nothing times out.
- *Read aloud* reads the current step with Android's own text to speech (no network). The *Listen mostly* preset, or a TalkBack suggestion, reads each step automatically.
- Choices are saved as they are made, so leaving halfway loses nothing.

## Not yet verified

No on-device or TalkBack check has been done; a TalkBack pass over every screen is the next accessibility step.
