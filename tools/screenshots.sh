#!/usr/bin/env bash
# Takes the Sable Reader screenshots for docs/USER_GUIDE.md from a phone connected over adb.
#
#   bash tools/screenshots.sh [--auto] [--out DIR] [--serial SERIAL] [SHOT ...]
#
# Sable Reader (org.sableos.reader) must already be installed on the phone, from a release made with
# tools/build-release.sh or `./gradlew :leisure:installSableDebug`. The script starts the app, then for each
# shot in the list below tells you what to put on the screen; you set it up on the phone and press Enter here,
# and it captures the screen. Type s and Enter to skip a shot. At the end it lists every shot skipped or failed,
# so none is missed by accident, and then lets you add shots of your own (type a short name, empty Enter ends).
#   --auto  captures just the app's start screen, with no questions (the quick way to refresh books.png).
# SHOT names one or more of the file names below without .png (books comics); with none, every shot is taken.
# Pictures go to docs/images/<name>.png (--out changes the folder), the names the User Guide uses. Nothing is
# uploaded anywhere. Plain arrays only: macOS ships bash 3.2, which has no associative arrays.
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
package="org.sableos.reader"

auto=no
out="$ROOT/docs/images"
serial=""
shots=()
while [[ $# -gt 0 ]]; do
    case "$1" in
        --auto) auto=yes ;;
        --out) out="${2:?--out needs a folder}"; shift ;;
        --serial) serial="${2:?--serial needs a device serial}"; shift ;;
        -h|--help) sed -n '2,15p' "${BASH_SOURCE[0]}" | sed 's/^# \{0,1\}//'; exit 0 ;;
        -*) echo "ERROR: unknown option $1" >&2; exit 2 ;;
        *) shots+=("$1") ;;
    esac
    shift
done

command -v adb >/dev/null 2>&1 || { echo "ERROR: adb not found (install Android platform-tools)" >&2; exit 2; }
adb_cmd=(adb)
[[ -n "$serial" ]] && adb_cmd=(adb -s "$serial")
devices="$("${adb_cmd[@]}" devices | sed -n '2,$p' | grep -c 'device$' || true)"
if [[ -z "$serial" && "$devices" != 1 ]]; then
    echo "ERROR: connect exactly one phone (found $devices), or pass --serial" >&2; exit 2
fi
"${adb_cmd[@]}" shell pm path "$package" >/dev/null 2>&1 || { echo "ERROR: $package is not installed on the phone" >&2; exit 2; }

# file name | what to put on the screen. Keep in step with the pictures docs/USER_GUIDE.md shows.
table=(
    "setup-welcome|Settings > App > Setup > Run the wizard again: the first wizard screen"
    "books|The Books tab with a few titles on the shelf"
    "books-search|The Books tab with search open (press /) and a word typed"
    "comics|The Comics tab"
    "audio|The Audio tab"
    "add-books|The Add button's file picker on the Books tab (only EPUB files show)"
    "add-comics|The Add button's file picker on the Comics tab (only CBZ and CBR show)"
    "add-audio|The Add button's file picker on the Audio tab (only M4B shows)"
    "settings|The Settings tab, App page"
    "settings-library|Settings > Library"
    "settings-reading|Settings > Reading"
    "settings-audio-comics|Settings > Audio & Comics"
    "theme|Settings > App > Theme"
    "speech-settings|Settings > Reading > Speech & Narration"
    "profiles|Settings > App > Reader Profiles"
    "dictionary|Settings > Reading > Dictionary Settings"
    "all-highlights|The All highlights screen (H on the library)"
    "book-page|An EPUB open on a page of text"
    "book-contents|An EPUB with the contents list open (T)"
    "book-appearance|An EPUB with the appearance sheet open (A)"
    "book-highlights|An EPUB with a highlight made (H) and the highlights list open (L)"
    "book-keys|An EPUB with the key list open (K)"
    "pdf-page|A PDF open on a page"
    "comic-page|A comic open on a page"
    "comic-zoomed|A comic page zoomed in (pinch, or Z)"
    "comic-contents|A comic with Contents and bookmarks open (T)"
    "comic-magnifier|A comic page with the magnifier on (G) over some lettering"
    "comic-settings|A comic with its reading mode and fit sheet open"
    "audio-player|An audiobook playing"
    "audio-playback|An audiobook with the playback sheet open (A)"
    "audio-chapters|An audiobook with its chapter list open"
    "backup|The backup and restore screen"
)

wanted() {
    [[ ${#shots[@]} -eq 0 ]] && return 0
    local s
    for s in "${shots[@]}"; do [[ "$s" == "$1" ]] && return 0; done
    return 1
}

# Saves the phone's screen to the file $1 as a PNG. It captures on the phone and pulls the file, because
# `adb exec-out screencap -p > file` can damage the picture (a redirect that rewrites bytes, or text the
# device prints before the PNG). The saved file's first eight bytes are then checked, and a file that is not
# a PNG is deleted and reported.
remote_shot=/data/local/tmp/sable-reader-shot.png
capture() {
    local file="$1" magic
    if ! "${adb_cmd[@]}" shell screencap -p "$remote_shot" >/dev/null 2>&1 \
        || ! "${adb_cmd[@]}" pull "$remote_shot" "$file" >/dev/null 2>&1; then
        echo "FAILED to capture $file" >&2; rm -f "$file"; return 1
    fi
    "${adb_cmd[@]}" shell rm -f "$remote_shot" >/dev/null 2>&1 || true
    magic="$(head -c 8 "$file" | od -An -tx1 | tr -d ' \n')"
    if [[ "$magic" != 89504e470d0a1a0a ]]; then
        echo "FAILED: $file is not a valid PNG (starts $magic)" >&2; rm -f "$file"; return 1
    fi
    echo "saved $file"
}

mkdir -p "$out"
"${adb_cmd[@]}" shell am force-stop "$package"
activity="$("${adb_cmd[@]}" shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.LAUNCHER "$package" | tr -d '\r' | tail -n 1)"
if [[ "$activity" != */* ]] || ! "${adb_cmd[@]}" shell am start -W -n "$activity" >/dev/null; then
    echo "ERROR: could not start $package" >&2; exit 2
fi

taken=0
missed=()
if [[ "$auto" == yes ]]; then
    sleep 4
    if capture "$out/books.png"; then taken=1; else missed+=("books.png (capture failed)"); fi
else
    for row in "${table[@]}"; do
        IFS='|' read -r name what <<< "$row"
        wanted "$name" || continue
        read -r -p "$name: put this on the phone, then press Enter (s skips): $what " answer
        if [[ "$answer" == s ]]; then echo "skipped $name"; missed+=("$name.png"); continue; fi
        if capture "$out/$name.png"; then taken=$((taken + 1)); else missed+=("$name.png (capture failed)"); fi
    done
    while [[ ${#shots[@]} -eq 0 ]]; do
        read -r -p "For another shot type a short name for it (e.g. dictionary), or Enter to finish: " extra
        extra="$(printf '%s' "$extra" | tr 'A-Z ' 'a-z-' | tr -cd 'a-z0-9-')"
        [[ -z "$extra" ]] && break
        if capture "$out/$extra.png"; then taken=$((taken + 1)); else missed+=("$extra.png (capture failed)"); fi
    done
fi
echo "$taken screenshot(s) in $out"
if [[ ${#missed[@]} -gt 0 ]]; then
    echo "skipped, still to take (run again with the shot's name to retake):"
    printf '  %s\n' "${missed[@]}"
fi
