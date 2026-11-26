# Themes and the reading page

The e-ink theme is gone, so the Reader has two layers of look. Both work on stock firmware and on SableOS.

## 1. The app (library, settings, overlays)

Settings > App Appearance holds the choice:

| Choice | What you get |
| --- | --- |
| Follow phone (default) | SableOS: its Light/Dark and accent. Any other phone: the system Light or Dark setting and the default Sable Blue accent |
| Light, Dark | The Reader stays that way whatever the phone does |
| Accent: Follow phone, Sable Blue, Gold, Teal | The colour of buttons and selections |

An accent is kept only when you pick one; "Follow phone" removes it. Code: `AppearanceChoice` (`reader-model`), `resolveAppearance` and `ProductReaderTheme` (`leisure`).

## 2. The reading page

The EPUB page follows the app's Light or Dark, except where the book has its own look: Sepia, or White or Yellow on black (the Vision looks). Looks are kept per book; Settings sets the default for new books.

### Recommended page

Settings > App Appearance shows the page the Reader recommends for the current appearance and screen, with a button to use it:

| Appearance | Screen | Recommended | Why |
| --- | --- | --- | --- |
| Light | any | Sepia | Warm paper takes the glare out of a bright page |
| Dark | AMOLED (Titan 2 Elite, Zinwa Q27) | White on black | Pure black switches pixels off: sharper text, less battery |
| Dark | other or unknown | Light or dark page (soft dark grey) | Pure black on a backlit LCD glows grey and makes white text bloom |

Unknown screens count as not AMOLED. The Q27's AMOLED panel is from press coverage and has not been seen on a phone. Code: `PageRecommendation` and `DeviceProfile.amoled` in `reader-model`.

## Contrast fixes

Hard-coded greys that were hard to read in one of the two modes now use the theme: empty-state text, dividers, the loading spinner (black on a black page in Dark) and the checkbox tints in Library settings. What is left (cover badges, which sit on book covers, and the page previews in the look sheets, which show the page colours) is deliberate.

## Not done yet

Nothing has been looked at on a device: the layouts and colours are unverified on a real Q25 or Titan 2 screen.
