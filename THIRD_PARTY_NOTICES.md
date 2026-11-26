# Third-party notices

Sable Reader is Apache-2.0 (see LICENSE). These parts keep their own licences.

| Component | Used for | Licence |
| --- | --- | --- |
| Vaachak Mobile (https://github.com/vaachak-platform/vaachak-mobile), commit `5393503ec0695e87e0a9bc4567fec0fea110ea4d` | Starting point of the reading core and screens (`org.vaachak.*` packages) | MIT; the licence header stays at the top of every file that came from it |
| Readium Kotlin Toolkit 3.1.2 | EPUB parsing and rendering | BSD-3-Clause |
| AndroidX, Jetpack Compose, Kotlin, Hilt, Room, Media3 | App framework | Apache-2.0 |

The full dependency list is `gradle/libs.versions.toml`; each coordinate is pinned in `gradle/verification-metadata.xml`.

Fonts bundled in `leisure/src/main/res/font` keep their own licences; the list is to be completed before the first release.
