# Releasing Sable Reader

Releases are built and signed on the owner's computer, as for Sable Games. CI never gets the signing key: it builds unsigned APKs only, and those are not published.

The signing key is the owner's (the same one as Sable Games and TenSeven Launcher, if the owner chooses). Keep the keystore and its passwords in a password manager with an offline backup and never commit them. A new key makes the app a new app: phones cannot update from the old one.

The package name is `org.sableos.reader`. Gradle builds an unsigned release APK (`./gradlew :leisure:assembleSableRelease`) and `tools/check-apk.sh` checks its package and permissions. The signing script, modelled on Sable Games' `tools/build-release.sh`, is still to be added before the first release.

Release notes go in `docs/releases/v<version>.md`.
