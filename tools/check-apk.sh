#!/usr/bin/env bash
# Checks the release APK under DIR: package org.sableos.reader, and none of the permissions the Reader
# promises never to ask for (internet, network state, camera, storage, location, contacts, microphone).
set -euo pipefail

dir="${1:?usage: tools/check-apk.sh APK_DIR}"
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

[[ "$fail" == 0 ]] && echo "PASS $package"
echo "Permissions asked for:"; grep "^uses-permission" <<< "$badging" || true
exit "$fail"
