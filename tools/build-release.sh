#!/usr/bin/env bash
# Build, sign and package a Sable Reader release on the owner's computer (docs/RELEASING.md).
#
#   bash tools/build-release.sh KEYSTORE ALIAS [VERSION]
#
# VERSION (such as 1.0.1 or v1.0.1, or $SABLEREADER_VERSION) is the release version. It is passed to Gradle as
# -Psable.versionName, so a release needs no source edit; versionCode is derived from it. Without it the script
# uses the default in gradle/libs.versions.toml (app-versionName).
# KEYSTORE and ALIAS are required: as arguments, or as $SABLEREADER_KEYSTORE and $SABLEREADER_KEY_ALIAS.
# The repository names no keystore. Gradle builds an unsigned release APK, then apksigner signs it. The password
# is asked once and is never written to disk: it lives only in this script's environment while apksigner runs.
# CI never gets the key.
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

usage="usage: bash tools/build-release.sh KEYSTORE ALIAS [VERSION] (or set SABLEREADER_KEYSTORE, SABLEREADER_KEY_ALIAS and SABLEREADER_VERSION)"
keystore="${1:-${SABLEREADER_KEYSTORE:-}}"
alias_name="${2:-${SABLEREADER_KEY_ALIAS:-}}"
version="${3:-${SABLEREADER_VERSION:-}}"
version="${version#v}"
[[ -n "$keystore" && -n "$alias_name" ]] || { echo "ERROR: $usage" >&2; exit 2; }
if [[ -z "$version" ]]; then
    version="$(sed -n 's/^app-versionName = "\(.*\)"/\1/p' gradle/libs.versions.toml | head -n1)"
    [[ -n "$version" ]] || { echo "ERROR: no VERSION given and no app-versionName in gradle/libs.versions.toml" >&2; exit 2; }
    echo "No VERSION given; using the default, $version."
fi
[[ "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]] || { echo "ERROR: VERSION must look like 1.2.3, got $version" >&2; exit 2; }
[[ -f "$keystore" ]] || { echo "ERROR: keystore not found: $keystore" >&2; exit 2; }
if ! git diff --quiet || ! git diff --cached --quiet; then
    echo "ERROR: commit tracked source changes before packaging a release." >&2
    exit 2
fi

sdk="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$HOME/Library/Android/sdk}}"
bt="$(find "$sdk/build-tools" -mindepth 1 -maxdepth 1 -type d 2>/dev/null | sort -V | tail -n1)"
apksigner="$bt/apksigner"
[[ -x "$apksigner" ]] || { echo "ERROR: apksigner not found under $sdk/build-tools (set ANDROID_HOME)" >&2; exit 2; }

read -rs -p "Password for $keystore ($alias_name): " SABLEREADER_KS_PASS
echo
export SABLEREADER_KS_PASS
trap 'unset SABLEREADER_KS_PASS' EXIT

# Gradle builds an unsigned APK and never sees the password.
env -u SABLEREADER_KS_PASS ./gradlew --no-daemon "-Psable.versionName=$version" clean \
    :leisure:assembleSableRelease test

out="${SABLEREADER_RELEASE_DIR:-$HOME/Downloads/sablereader-release/$version}"
mkdir -p "$out"
rm -f "$out"/*.apk
unsigned=(leisure/build/outputs/apk/sable/release/*.apk)
[[ "${#unsigned[@]}" == 1 && -f "${unsigned[0]}" ]] || { echo "ERROR: expected one unsigned release APK" >&2; exit 1; }
signed="$out/sable-reader-$version.apk"
"$apksigner" sign --ks "$keystore" --ks-key-alias "$alias_name" \
    --ks-pass env:SABLEREADER_KS_PASS --key-pass env:SABLEREADER_KS_PASS \
    --out "$signed" "${unsigned[0]}"
rm -f "$out"/*.idsig

# The signed APK must carry the requested version and pass the package and permission check.
aapt2="$bt/aapt2"
built="$("$aapt2" dump badging "$signed" | sed -n "s/.*versionName='\([^']*\)'.*/\1/p" | head -n1)"
[[ "$built" == "$version" ]] || { echo "ERROR: built versionName $built, expected $version" >&2; exit 1; }
check="$(mktemp -d)"
trap 'unset SABLEREADER_KS_PASS; rm -rf "$check"' EXIT
cp "$signed" "$check/"
ANDROID_HOME="$sdk" bash tools/check-apk.sh --signed "$check"

git rev-parse HEAD > "$out/SOURCE_COMMIT.txt"
if command -v shasum >/dev/null 2>&1; then sum=(shasum -a 256); else sum=(sha256sum); fi
(cd "$out" && "${sum[@]}" ./*.apk > SHA256SUMS.txt)
printf 'Signed APK and SHA256SUMS.txt: %s\nSource commit: %s\n' "$out" "$(git rev-parse HEAD)"
notes="docs/releases/v$version.md"
if [[ -f "$notes" ]]; then notes_arg="--notes-file $notes"; else notes_arg="--generate-notes"; fi
echo "Publish with: gh release create v$version \"$out\"/*.apk \"$out\"/SHA256SUMS.txt --title \"Sable Reader $version\" $notes_arg"
