# Sable Reader

[![Security](https://github.com/aimindseye/sable-reader/actions/workflows/security.yml/badge.svg)](https://github.com/aimindseye/sable-reader/actions/workflows/security.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aimindseye_sable-reader&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=aimindseye_sable-reader)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Android 11+](https://img.shields.io/badge/Android-11%2B-3DDC84.svg)](docs/USER_GUIDE.md)

A keyboard-first reader for BlackBerry-style Android phones. It opens **EPUB** books, **comics** (CBZ and CBR) and **audiobooks** (M4B), and is driven from the physical keys and the trackpad as well as by touch. It was first written for [SableOS](https://github.com/aimindseye/zinwaq25-sableos) and lives here so it installs on stock firmware too.

It is an offline app: no internet, no camera, no account.

## Devices

The Reader reads which phone it runs on and fits its layout and keys to it through a [device profile](docs/DEVICE_PROFILES.md).

| Phone | Screen | Status |
| --- | --- | --- |
| Zinwa Q25 | 720 x 720 | Designed and tested (from SableOS 2.0) |
| Unihertz Titan 2 | 1440 x 1440 | Profile planned |
| Unihertz Titan 2 Elite | 1080 x 1200 | Profile planned |
| Zinwa P26 | 1440 x 1440 square | Profile planned |
| Zinwa Q27 | 1080 x 1240 | Profile planned |

## Status

The repository is being set up in steps; see the [roadmap](docs/ROADMAP.md).

## Documentation

- [Architecture](docs/ARCHITECTURE.md): modules and how the source is organised.
- [Device profiles](docs/DEVICE_PROFILES.md): what a profile holds and how to add a phone.
- [Roadmap](docs/ROADMAP.md): the port, device profiles, themes and the setup wizard.
- [Contributing](CONTRIBUTING.md): coding standards and the CI checks.
- [Releasing](docs/RELEASING.md): signing on the owner's computer and publishing.

## Build

You need JDK 17 and the Android SDK (platform 36). Build instructions arrive with the source port.

## Licence

Apache-2.0 (see [LICENSE](LICENSE) and [NOTICE](NOTICE)). Files descended from Vaachak Mobile keep their MIT headers; see [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
