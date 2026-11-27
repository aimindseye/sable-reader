# Releasing Sable Reader

Releases are built and signed on the owner's computer, as for Sable Games. CI never gets the signing key: it builds unsigned APKs only, and those are not published.

The signing key is the same one as Sable Games and TenSeven Launcher, whose certificate (SHA-256 `9b2047390e5aa77537b8e8875cdfd3c2c72d0a0715e172a7f77457fc4300f292`) is registered in the Android Developer Console. `tools/check-apk.sh --signed` refuses an APK signed with any other key (`SABLEREADER_EXPECTED_CERT` overrides the digest if the Reader gets its own key). Keep the keystore and its passwords in a password manager with an offline backup and never commit them. A new key makes the app a new app: phones cannot update from the old one.

## Package name

The one name to register in the Android Developer Console:

```
org.sableos.reader
```

It is the `applicationId` in `leisure/build.gradle.kts`. The flavor and the release build type add no suffix, so the release APK's package is exactly that (the debug build also keeps it, with only `-debug` on the version name). `tools/check-apk.sh` fails the build if it differs. The Kotlin namespace (`org.vaachak.reader.leisure`) is source-only and is not the package name.

Gradle builds an unsigned release APK (`./gradlew :leisure:assembleSableRelease`), as CI does, and `tools/check-apk.sh` checks its package and permissions.

## Making a release

The version is never edited in a source file. You give it to the release script, which passes it to Gradle as `-Psable.versionName`; `versionCode` is derived from it (major x 10000 + minor x 100 + patch). `gradle/libs.versions.toml` holds only the default used for ordinary builds.

1. Write the release notes in `docs/releases/v<version>.md`, merge to `main` and pull it.
2. Run `bash tools/build-release.sh /path/to/key.jks KEY_ALIAS 1.0.1` and type the keystore password. The version is optional (a leading `v` is fine; `SABLEREADER_VERSION` works too); without it the script uses the default. The script builds and tests, signs the APK with `apksigner`, checks its version, package, permissions and signing certificate (`tools/check-apk.sh --signed`), and writes the APK, `SHA256SUMS.txt` and `SOURCE_COMMIT.txt` to `~/Downloads/sablereader-release/<version>/` (`SABLEREADER_RELEASE_DIR` overrides it).
3. Publish with the `gh release create` command the script prints.

Release notes go in `docs/releases/v<version>.md`.
