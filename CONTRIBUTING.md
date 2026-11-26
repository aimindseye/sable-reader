# Contributing

This repository follows the same methodology as [Sable Games](https://github.com/aimindseye/sablegames).

## Standards

- Kotlin, Jetpack Compose, JDK 17, Android SDK 36, minSdk 30.
- ktlint (`ktlint_official`, android style, 120 columns; see `.editorconfig`) and detekt must be clean for code you touch.
- Every dependency comes from Google or Maven Central, is pinned in `gradle/libs.versions.toml` and covered by `gradle/verification-metadata.xml` and Gradle lockfiles. No JitPack.
- The app asks for no INTERNET, ACCESS_NETWORK_STATE or CAMERA permission, ever.
- User-facing text lives in string resources, not in code, so it can be translated.
- Keyboard first: every screen must be fully usable without touch.
- Add or update unit tests with each change.

## Pull requests

Keep them small and reviewable. Each PR runs:

| Check | Workflow | Blocks merge |
| --- | --- | --- |
| Build, unit tests, Android Lint, detekt, ktlint | `android.yml` | Yes |
| SonarCloud quality gate | `sonar.yml` | Yes |
| Gitleaks secret scan | `security.yml` | Yes |
| MobSF source scan | `security.yml` | Recorded, not failing |

Documentation-only changes skip the build and Sonar workflows; the security scan still runs.
