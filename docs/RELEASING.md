# Releasing Sable Reader

Releases are built and signed on the owner's computer, as for Sable Games. CI never gets the signing key: it builds unsigned APKs only, and those are not published.

The signing key is the owner's (the same one as Sable Games and TenSeven Launcher, if the owner chooses). Keep the keystore and its passwords in a password manager with an offline backup and never commit them. A new key makes the app a new app: phones cannot update from the old one.

The package name is `org.sableos.reader`. Gradle builds an unsigned release APK (`./gradlew :leisure:assembleSableRelease`) and `tools/check-apk.sh` checks its package and permissions. 
## Making a release

The version is never edited in a source file. You give it to the release script, which passes it to Gradle as `-Psable.versionName`; `versionCode` is derived from it (major x 10000 + minor x 100 + patch). `gradle/libs.versions.toml` holds only the default used for ordinary builds.

1. Write the release notes in `docs/releases/v<version>.md`, merge to `main` and pull it.
2. Run `bash tools/build-release.sh /path/to/key.jks KEY_ALIAS 1.0.1` and type the keystore password. The script builds and tests, signs the APK with `apksigner`, checks its version, package and permissions, and writes the APK, `SHA256SUMS.txt` and `SOURCE_COMMIT.txt` to `~/Downloads/sablereader-release/<version>/` (`SABLEREADER_RELEASE_DIR` overrides it).
3. Publish with the `gh release create` command the script prints.

Release notes go in `docs/releases/v<version>.md`.
