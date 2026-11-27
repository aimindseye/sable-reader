#!/usr/bin/env bash
# Checks the release APK under DIR: package org.sableos.reader, and none of the permissions the Reader
# promises never to ask for (internet, network state, camera, storage, location, contacts, microphone).
# With --signed (tools/build-release.sh) the APK must also be signed with the registered key, the one Sable
# Games and TenSeven Launcher use; SABLEREADER_EXPECTED_CERT overrides the expected SHA-256 certificate digest.
set -euo pipefail

signed=no
expected_cert="${SABLEREADER_EXPECTED_CERT:-9b2047390e5aa77537b8e8875cdfd3c2c72d0a0715e172a7f77457fc4300f292}"
if [[ "${1:-}" == "--signed" ]]; then signed=yes; shift; fi
dir="${1:?usage: tools/check-apk.sh [--signed] APK_DIR}"
sdk="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
aapt2="$(find "$sdk/build-tools" -maxdepth 2 -name aapt2 -type f 2>/dev/null | LC_ALL=C sort | tail -n1)"
[[ -x "$aapt2" ]] || { echo "aapt2 not found under $sdk/build-tools"; exit 1; }

apks=()
for apk in "$dir"/*.apk; do [[ -f "$apk" ]] && apks+=("$apk"); done
[[ "${#apks[@]}" == 1 ]] || { echo "FAIL expected one APK in $dir, found ${#apks[@]}"; exit 1; }

badging="$("$aapt2" dump badging "${apks[0]}")"
package="$(sed -n "s/^package: name='\([^']*\)'.*/\1/p" <<< "$badging")"
want="org.sableos.reader"
fail=0
if [[ "$package" != "$want" ]]; then echo "FAIL package $package, expected $want"; fail=1; fi

forbidden='INTERNET|ACCESS_NETWORK_STATE|CAMERA|READ_EXTERNAL_STORAGE|WRITE_EXTERNAL_STORAGE|MANAGE_EXTERNAL_STORAGE|LOCATION|CONTACTS|RECORD_AUDIO'
found="$(grep "^uses-permission" <<< "$badging" | grep -E "name='android.permission.($forbidden)" || true)"
if [[ -n "$found" ]]; then echo "FAIL forbidden permissions:"; echo "$found"; fail=1; fi

if [[ "$signed" == yes ]]; then
    bt="$(dirname "$aapt2")"
    cert="$("$bt/apksigner" verify --print-certs "${apks[0]}" 2>/dev/null | sed -n 's/.*certificate SHA-256 digest: //p' | head -n1)"
    if [[ -z "$cert" ]]; then echo "FAIL the APK is not signed"; fail=1
    elif [[ "$cert" != "$expected_cert" ]]; then echo "FAIL signed with $cert, not the registered certificate $expected_cert"; fail=1
    else echo "PASS signed with the registered certificate $cert"; fi
fi

[[ "$fail" == 0 ]] && echo "PASS $package"
echo "Permissions asked for:"; grep "^uses-permission" <<< "$badging" || true
exit "$fail"
