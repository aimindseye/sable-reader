# Architecture

Sable Reader is one Android app built from several Gradle modules. The layout below is what the SableOS 2.0 Reader uses; the port keeps it.

| Module | Holds |
| --- | --- |
| `leisure` | The app: screens (bookshelf, EPUB, comic, audiobook, PDF, settings, vault, backup), navigation and key handling |
| `core` | Library database, repositories, settings, importers, dictionary and sync-free data code |
| `reader-model` | Plain data types shared by the modules |
| `reader-engine-comic` | CBZ and CBR page sources |
| `reader-engine-audio` | M4B chapters and playback model |
| `reader-engine-pdf` | PDF page source |

New code goes in the `org.sableos.reader` package. Code that came from Vaachak Mobile stays in `org.vaachak.*` until it is rewritten or moved.

Two seams are added on top of the port, each in its own pull request:

- **Device profiles** (`docs/DEVICE_PROFILES.md`): one place that knows each phone's screen and keys.
- **Setup wizard**: a first-run flow that chooses the profile, theme and library folder.
