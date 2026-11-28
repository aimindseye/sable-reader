# Sable Reader

[![Build and test](https://github.com/aimindseye/sable-reader/actions/workflows/android.yml/badge.svg)](https://github.com/aimindseye/sable-reader/actions/workflows/android.yml)
[![Security](https://github.com/aimindseye/sable-reader/actions/workflows/security.yml/badge.svg)](https://github.com/aimindseye/sable-reader/actions/workflows/security.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aimindseye_sable-reader&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=aimindseye_sable-reader)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Android 11+](https://img.shields.io/badge/Android-11%2B-3DDC84.svg)](docs/USER_GUIDE.md)

A keyboard-first reader for BlackBerry-style Android phones. It opens **EPUB** books, **PDFs**, **comics** (CBZ and CBR) and **audiobooks** (M4B), and is driven from the physical keys and the trackpad as well as by touch. It was first written for [SableOS](https://github.com/aimindseye/zinwaq25-sableos) and lives here so it installs on stock firmware too.

It is an offline app: no internet, no camera, no account, no storage permission. Your library, progress, bookmarks and highlights stay on the phone.

## What it does

- **Library**: one shelf for books, comics and audiobooks, with Books, Comics and Audio tabs, search, filters, sorting, Smart Stacks by author or series, and a cover for every title. The **+** button only offers the files the tab can open.
- **Books**: contents, find, bookmarks, tagged highlights (exportable to Markdown), a dictionary (built-in or StarDict), read-aloud with Android's own text-to-speech, and full control of fonts, spacing and margins per book.
- **PDFs**: page view with a page list, go-to-page and bookmarks.
- **Comics**: paged left to right or right to left (manga), vertical pager, continuous scroll and webtoon; whole-page or page-width fit; pinch and keyboard zoom.
- **Audiobooks**: chapters, bookmarks, speed from 0.5x to 3x, skip lengths, a sleep timer, and playback with the screen off.
- **Keyboard first**: every screen works from the keys; K shows the keys of the screen you are on. Phones without arrow keys move with I J K L.
- **Looks**: Follow phone, Light or Dark with accents and a recommended reading page, in the Sable style.
- **People**: optional local profiles with their own libraries and PINs, a setup wizard, accessibility presets, and backup and restore of your library list and progress.

See the [user guide](docs/USER_GUIDE.md) for all of it.

## Install

Download `sable-reader-<version>.apk` from [Releases](https://github.com/aimindseye/sable-reader/releases) and open it on the phone (Android 11 or later). Check the download against `SHA256SUMS.txt` on the release page. The app id is `org.sableos.reader`.

## Devices

The Reader reads which phone it runs on and fits its layout and keys to it through a [device profile](docs/DEVICE_PROFILES.md).

| Phone | Screen | Status |
| --- | --- | --- |
| Zinwa Q25 | 720 x 720 | Designed and tested (from SableOS 2.0) |
| Unihertz Titan 2 | 1440 x 1440 | Profile ready, untested on the phone |
| Unihertz Titan 2 Elite | 1080 x 1200 | Profile ready, untested on the phone |
| Zinwa P26 | 1440 x 1440 square | Profile ready, untested on the phone |
| Zinwa Q27 | 1080 x 1240 | Profile ready, untested on the phone |

## Status

The SableOS 2.0 Reader is ported (EPUB, PDF, comics, audiobooks, library keys), with device profiles, themes, the setup wizard and an accessibility pass in. The first release is not out yet. TalkBack and the other phones' profiles still need checking on real devices; see the [roadmap](docs/ROADMAP.md).

## Documentation

- [User guide](docs/USER_GUIDE.md): install, library, reading, comics, audiobooks, settings and the screenshot script.
- [Architecture](docs/ARCHITECTURE.md): modules and how the source is organised.
- [Device profiles](docs/DEVICE_PROFILES.md): what a profile holds and how to add a phone.
- [Themes](docs/THEMES.md): Follow phone, Light, Dark, accents and the recommended reading page.
- [Setup wizard](docs/SETUP_WIZARD.md) and [Accessibility](docs/ACCESSIBILITY.md).
- [Roadmap](docs/ROADMAP.md): what is done and what is next.
- [Contributing](CONTRIBUTING.md): coding standards and the CI checks.
- [Releasing](docs/RELEASING.md): signing on the owner's computer and publishing; notes for each release are in [docs/releases](docs/releases).

## Build

You need JDK 17 and the Android SDK (platform 36). Then:

```
./gradlew :leisure:assembleSableRelease        # unsigned release APK
./gradlew test :leisure:lintSableDebug detekt ktlintCheck
bash tools/build-release.sh KEYSTORE ALIAS     # signed release, on the owner's computer
bash tools/screenshots.sh                      # screenshots for the user guide, over adb
```

The app id is `org.sableos.reader`. Dependencies are pinned in `gradle/libs.versions.toml` and `gradle/verification-metadata.xml`.

## Licence

Apache-2.0 (see [LICENSE](LICENSE) and [NOTICE](NOTICE)). Files descended from Vaachak Mobile keep their MIT headers; see [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
