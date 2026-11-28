# Sable Reader: User Guide

How to install and use Sable Reader on a keyboard phone such as the Zinwa Q25 or the Unihertz Titan 2. The Reader opens books (EPUB), PDFs, comics (CBZ and CBR) and audiobooks (M4B). It is an offline app: it asks for no internet, no camera and no storage permission, and your library never leaves the phone.

The pictures below are made with `bash tools/screenshots.sh` (see [Taking the screenshots](#taking-the-screenshots)); until a picture is added to `docs/images/` its link is empty.

## Contents

1. [Install](#install)
2. [First start: the setup wizard](#first-start-the-setup-wizard)
3. [The library](#the-library)
4. [Adding books, comics and audiobooks](#adding-books-comics-and-audiobooks)
5. [Reading a book](#reading-a-book)
6. [Reading a comic](#reading-a-comic)
7. [Listening to an audiobook](#listening-to-an-audiobook)
8. [Settings](#settings)
9. [Profiles](#profiles)
10. [Backup and restore](#backup-and-restore)
11. [Keys at a glance](#keys-at-a-glance)
12. [Taking the screenshots](#taking-the-screenshots)

## Install

Download `sable-reader-<version>.apk` from [Releases](https://github.com/aimindseye/sable-reader/releases). On the phone, open it and allow your file manager or browser to install apps when Android asks. From a computer, `adb install sable-reader-1.0.1.apk` does the same. `SHA256SUMS.txt` on the release page lists the file's SHA-256, so you can check a download with `sha256sum -c SHA256SUMS.txt`.

Sable Reader needs Android 11 or later. The app id is `org.sableos.reader`.

## First start: the setup wizard

The first time you open the Reader, a short wizard asks how you read: who reads on this phone, an accessibility preset (Standard, Low vision, Dyslexia, Listen mostly), your phone model, the look (follow the phone, light or dark) and an optional first library folder, then lets you try the keys. Every choice can be changed later in Settings. Run it again from Settings > App > Setup. The steps are described in [SETUP_WIZARD.md](SETUP_WIZARD.md).

![The wizard's first screen](images/setup-welcome.png)

## The library

The top bar has four tabs: Books, Comics, Audio and Settings. Books shows the whole library; Comics and Audio show the same shelf narrowed to one kind. Below the bar are the history and folder buttons, then search, selection and sort. The chips (All, Continue, Books, Comics, Audiobooks and one per language) filter the shelf. Each cover shows its kind and how far you have read; the bin on a cover removes it from the library.

![The Books tab](images/books.png)

| Key | Does |
| --- | --- |
| S | Change the sort order |
| F | Next filter chip |
| C | Continue the book you were reading |
| / | Search (type to filter, Escape to close) |
| H | Open all highlights |
| Back | Leaves a stack or search, then the Comics and Audio tabs return to Books |

![Search on the Books tab](images/books-search.png)

![The Comics tab](images/comics.png)

![The Audio tab](images/audio.png)

## Adding books, comics and audiobooks

Press the **+** button at the bottom right. Android's file picker opens and shows only the files the current tab can read:

| Tab | The picker shows |
| --- | --- |
| Books | EPUB files |
| Comics | CBZ and CBR files |
| Audio | M4B files |

![Adding a book](images/add-books.png)

![Adding a comic](images/add-comics.png)

![Adding an audiobook](images/add-audio.png)

Some phones' file apps label a comic archive or an audiobook as a generic archive or binary file, so those can still appear in the Comics and Audio pickers; the Reader checks the file name and tells you if it cannot use a file. The folder button next to **+** adds a whole folder at once: the Reader imports every book, comic and audiobook it finds there.

## Reading a book

Tap a cover to open it. Tap the middle of the page, or press Enter or Menu, to show or hide the controls.

![An open book](images/book-page.png)

| Key | Does |
| --- | --- |
| Left / Right, Space, Page Up / Page Down | Turn the page |
| Up / Down | Scroll, in scrolling mode |
| T | Contents |
| A | Appearance: text size, font, spacing, theme |
| S / Shift+S | Larger / smaller text |
| M | Next reading theme |
| B | Bookmark this page |
| F | Find in the book; N / Shift+N for the next and previous match |
| H | Highlight the selection |
| L / Shift+L | Highlights / bookmarks |
| D | Look a word up in the dictionary |
| V | Read aloud; `,` and `.` skip a sentence, `-` and `+` change the speed |
| K | Show the keys that work on this screen |
| Escape or Back | Close what is open, then leave the book |

On a phone with no arrow keys (the Titan 2) I J K L move instead of the arrows.

![Contents](images/book-contents.png)

![Appearance](images/book-appearance.png)

![Highlights](images/book-highlights.png)

![The key list](images/book-keys.png)

PDFs open the same way and turn pages with the same keys.

![A PDF](images/pdf-page.png)

## Reading a comic

![A comic page](images/comic-page.png)

**Reading mode** (in the comic's settings sheet): paged left to right, paged right to left (manga), vertical pager, continuous scroll or webtoon. **Fit**: the whole page, or the page width.

**Zoom.** Spread two fingers on the screen to zoom in a step (100%, 150%, 200%) and bring them together to zoom out; a long spread or pinch jumps two steps. On the keyboard, Z or `+` zooms in, X or `-` zooms out and 0 goes back to the whole page. When a page is bigger than the screen, Up / Down / Space scroll it and Left / Right pan; Space turns the page once you reach the end of it. Zoom is not saved: each comic opens at 100%. Continuous and webtoon strips always fit the width and do not zoom.

![A zoomed comic page](images/comic-zoomed.png)

| Key | Does |
| --- | --- |
| Left / Right (or Right / Left in manga mode) | Turn the page |
| Z / `+`, X / `-`, 0 | Zoom in, out, reset |
| T | Contents |
| A | Reading mode and fit |
| B | Bookmark this page |
| Escape or Back | Close what is open, then leave the comic |

![Comic settings](images/comic-settings.png)

## Listening to an audiobook

Open an M4B to start the player. Playback continues with the screen off, and the Now Playing bar stays on the shelf.

![The audiobook player](images/audio-player.png)

| Key | Does |
| --- | --- |
| Space | Play or pause |
| Left / Right | Seek back / forward |
| N / P, Page Down / Page Up | Next / previous chapter |
| Escape or Back | Leave the player (the audio keeps playing) |

![Chapters](images/audio-chapters.png)

## Settings

Settings has four pages: **App**, **Library**, **Reading** and **Audio & Comics**.

![Settings, App](images/settings.png)

- **App** holds Profiles, Multi-user, Theme (Follow phone, Light, Dark, accent), Setup (run the wizard again) and About (version and privacy).
- **Library** holds Library Settings (cover styles, Smart Stacks) and Library backup.
- **Reading** holds Default Book Appearance (fonts, margins, page colour), Speech & Narration and Dictionary Settings.
- **Audio & Comics** holds the comic reading mode and fit, and the audiobook playback defaults (speed and skip lengths).

![Library](images/settings-library.png)

![Reading](images/settings-reading.png)

![Audio and comics](images/settings-audio-comics.png)

![Theme](images/theme.png)

See [THEMES.md](THEMES.md), [DEVICE_PROFILES.md](DEVICE_PROFILES.md) and [ACCESSIBILITY.md](ACCESSIBILITY.md) for the details.

## Profiles

Profiles give each reader their own library, progress and settings on one phone. Turn on **Multi-user** in Settings > App to add readers and switch between them from the top bar. Off, the Reader has one library and no profile switcher.

## Backup and restore

Settings > Library > Library backup (Create backup, Restore from backup) writes your library list, reading positions, bookmarks, highlights, collections and per-title settings to a file you choose, and reads one back. A restored title has no file yet: tap it and choose its file (or folder) again to link it.

![Backup](images/backup.png)

## Keys at a glance

| Where | Keys |
| --- | --- |
| Library | S sort, F filter, C continue, / search, H highlights |
| Book | Arrows or Space page, T contents, A appearance, S text size, B bookmark, F find, H highlight, L highlights, D dictionary, V read aloud, K keys |
| Comic | Arrows page, Z / X / 0 zoom, A mode and fit, B bookmark |
| Audiobook | Space play/pause, arrows seek, N / P chapter |
| Anywhere | Escape or Back closes, then leaves |

## Taking the screenshots

`tools/screenshots.sh` captures the pictures this guide uses from a phone connected over adb. Install the Reader on the phone, then:

```
bash tools/screenshots.sh                 # every shot in turn: set it up on the phone, press Enter
bash tools/screenshots.sh books comics    # only those shots
bash tools/screenshots.sh --auto          # only books.png, with no questions
```

For each shot the script prints what to put on the screen, waits for Enter (type s to skip), and saves `docs/images/<name>.png`. At the end it lists skipped or failed shots, so none is lost. Run `bash tools/screenshots.sh --help` for the options (`--out`, `--serial`).
