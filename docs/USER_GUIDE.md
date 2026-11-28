# Sable Reader: User Guide

How to install and use Sable Reader on a keyboard phone such as the Zinwa Q25 or the Unihertz Titan 2. The Reader opens books (EPUB), PDFs, comics (CBZ and CBR) and audiobooks (M4B). It is an offline app: it asks for no internet, no camera and no storage permission, and your library never leaves the phone.

This guide follows the layout of the [Vaachak Mobile guide](https://github.com/vaachak-platform/vaachak-mobile/blob/main/docs/USER_GUIDE.md) that the Reader grew out of, without its AI and sync chapters: Sable Reader has no AI features, no account and no network. The pictures below are made with `bash tools/screenshots.sh` (see [Taking the screenshots](#taking-the-screenshots)); until a picture is added to `docs/images/` its link is empty.

## Contents

1. [Install](#install)
2. [First start: the setup wizard](#first-start-the-setup-wizard)
3. [The library](#the-library)
4. [Adding books, comics and audiobooks](#adding-books-comics-and-audiobooks)
5. [Reading a book](#reading-a-book)
6. [Reading a comic](#reading-a-comic)
7. [Listening to an audiobook](#listening-to-an-audiobook)
8. [Settings](#settings)
9. [Dictionary](#dictionary)
10. [All highlights](#all-highlights)
11. [Profiles](#profiles)
12. [Backup and restore](#backup-and-restore)
13. [Keys at a glance](#keys-at-a-glance)
14. [Taking the screenshots](#taking-the-screenshots)

## Install

Download `sable-reader-<version>.apk` from [Releases](https://github.com/aimindseye/sable-reader/releases). On the phone, open it and allow your file manager or browser to install apps when Android asks. From a computer, `adb install sable-reader-1.0.1.apk` does the same. `SHA256SUMS.txt` on the release page lists the file's SHA-256, so you can check a download with `sha256sum -c SHA256SUMS.txt`.

Sable Reader needs Android 11 or later. The app id is `org.sableos.reader`.

## First start: the setup wizard

The first time you open the Reader, a short wizard asks how you read: who reads on this phone, an accessibility preset (Standard, Low vision, Dyslexia, Listen mostly), your phone model, the look (follow the phone, light or dark) and an optional first library folder, then lets you try the keys. Every choice can be changed later in Settings. Run it again from Settings > App > Setup. The steps are described in [SETUP_WIZARD.md](SETUP_WIZARD.md).

![The wizard's first screen](images/setup-welcome.png)

## The library

The top bar has four tabs: Books, Comics, Audio and Settings. Books shows the whole library; Comics and Audio show the same shelf narrowed to one kind. Below the bar are the history and folder buttons, then search, selection and sort. The chips (All, Continue, Books, Comics, Audiobooks and one per language) filter the shelf. Each cover shows its kind and how far you have read, and a bookmark mark if it has bookmarks; the bin on a cover removes it from the library. With Smart Stacks on (Settings > Library), books of one series or author are grouped into a stack: open it to see its books, Back to return.

![The Books tab](images/books.png)

| Key | Does |
| --- | --- |
| S | Change the sort order: Progress, Recently added, Title, Author (the sort button's menu also lists them) |
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

### Highlights, tags and bookmarks

Select text (long-press, or move the selection with the keys), then press H. The Reader asks for a tag: General, Research, Quotes or Characters; pressing the tag's first letter (G, R, Q or C) saves the highlight with it. L opens the book's highlights; Shift+L its bookmarks. Bookmarks (B) are saved per page.

### Reading aloud

![Speech settings](images/speech-settings.png)

Press V to have the book read aloud with Android's own text-to-speech (no network). `,` and `.` skip a sentence and `-` and `+` change the speed. The speech sheet sets speed, pitch, the highlight style and Auto-Turn Pages; the language and voice are Android's, with a shortcut to Android's text-to-speech settings. A sleep timer is in Settings > Reading > Speech & Narration.

### Appearance of one book

Press A (or Settings in the book's top bar) for the book's own look. **Display**: theme (Follow SableOS, Light, Dark, or Sepia as a reading-only override), font and base font size. **Layout**: Publisher Styles on keeps the book's own layout; turn it off to set Line Height, Paragraph Gap, Letter Spacing, Side Margins, Word Spacing, Paragraph Indent, alignment (for example Justify), Hyphenation and Ligatures, with a live preview. Reset Layout to Defaults undoes your changes. Defaults for every book are in Settings > Reading > Default Book Appearance.

### Top bar

Back, Contents, Bookmarks, Highlights, Search and Settings (under More options).

### PDFs

A PDF opens the same way but has only page features: turn pages with the arrows, Space or Page Up / Page Down, **T** for the page list and bookmarks, **Go to page** to jump, **B** to bookmark, **K** for the keys. PDFs have no text search, highlights, dictionary or appearance settings.

![A PDF](images/pdf-page.png)

## Reading a comic

![A comic page](images/comic-page.png)

**Reading mode** (press A, or the Reading mode button): Pages left to right, Pages right to left (manga), Pages top to bottom, Continuous scroll, or Webtoon (seamless strip). **Fit**: Whole page or Page width. These are saved per comic; the defaults for new comics are in Settings > Audio & Comics > Reading mode and fit. T opens Contents (chapters, pages) and Bookmarks (B bookmarks the current page).

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

![Comic contents and bookmarks](images/comic-contents.png)

## Listening to an audiobook

Open an M4B to start the player. Playback continues with the screen off, and a Now Playing bar stays on the shelf. The player shows the chapter ("Chapter 3 of 20") and the time, with previous chapter, back, play/pause, forward and next chapter buttons.

![The audiobook player](images/audio-player.png)

![The playback sheet](images/audio-playback.png)

| Key | Does |
| --- | --- |
| Space | Play or pause |
| Left / Right | Skip back / forward by your skip lengths |
| N / P, Page Down / Page Up | Next / previous chapter |
| T | Chapters |
| B | Bookmark this position |
| A | Playback: speed, skip lengths, sleep timer |
| K | The key list |
| Escape or Back | Leave the player (the audio keeps playing) |

**Playback sheet (A).** Speed: 0.5x, 0.75x, 1x, 1.1x, 1.25x, 1.5x, 1.75x, 2x, 2.5x or 3x. Skip back: 5, 10, 15 or 30 seconds. Skip forward: 10, 15, 30 or 60 seconds. Sleep timer: Off, 5, 10, 15, 30, 45 or 60 minutes, or End of chapter; Cancel sleep timer stops it. Defaults for new audiobooks are in Settings > Audio & Comics > Playback defaults.

![Chapters](images/audio-chapters.png)

## Settings

Settings has four pages: **App**, **Library**, **Reading** and **Audio & Comics**.

![Settings, App](images/settings.png)

- **App**
  - *Profiles > Reader Profiles*: add, rename and set an optional PIN for local readers (leave the new PIN blank to remove one).
  - *Local Multi-User Mode*: separate libraries for different readers; the top bar then shows the active profile.
  - *Appearance > App-wide Theme*: Follow phone (Light or Dark from SableOS), Light or Dark; the accent colour; and the Reading page (a recommended page colour for your theme).
  - *Run setup again*: reading look, phone, keys and a library check.
  - *About Sable Reader*: version, privacy (no account, no analytics, no network) and open-source notices.
- **Library**
  - *Library Settings (Visuals)*: cover style (Original ratio or Uniform ratio), cover element, bookshelf settings and Smart Stacks (group by author or series).
  - *Library backup*: see [Backup and restore](#backup-and-restore).
- **Reading**
  - *Default Book Appearance*: Publisher Styles, font, base font size, line height, margins and page colour for every book.
  - *Speech & Narration*: reading speed, voice pitch, Auto-Turn Pages, a sleep timer, and a shortcut to Android's text-to-speech language and voice settings.
  - *Dictionary Settings*: see [Dictionary](#dictionary).
- **Audio & Comics**
  - *Comics > Reading mode and fit*: the default for a comic you open for the first time. Press A while reading to change one comic.
  - *Audiobooks > Playback defaults*: default speed and skip lengths for an audiobook you start for the first time. Press A while listening to change one.

![Reader profiles](images/profiles.png)

![Library](images/settings-library.png)

![Reading](images/settings-reading.png)

![Audio and comics](images/settings-audio-comics.png)

![Theme](images/theme.png)

See [THEMES.md](THEMES.md), [DEVICE_PROFILES.md](DEVICE_PROFILES.md) and [ACCESSIBILITY.md](ACCESSIBILITY.md) for the details.

## Dictionary

Select a word in a book and press D (or use the lookup button) to see its definition. Choose the source in Settings > Reading > Dictionary Settings:

1. **Built-in English Dictionary**: simple offline definitions, nothing to set up.
2. **Custom Dictionary (StarDict)**: pick the folder that holds your StarDict files (`.ifo`, `.idx` and `.dict`). The Reader checks the folder straight away and says if it finds no usable dictionary.

![Dictionary settings](images/dictionary.png)

## All highlights

Press H on the library (or use the highlights button) to see every highlight from every book, grouped by book. Use the tag chips to filter, tap a highlight to jump to its page in the book, and delete the ones you no longer want. **Export** saves all highlights as a Markdown file (`sable-reader-highlights.md`): one heading per book, each highlight as a quote with its tag. Inside a book, L opens that book's highlights and Shift+L its bookmarks.

![All highlights](images/all-highlights.png)

## Profiles

Profiles give each reader their own library, progress and settings on one phone. Turn on **Local Multi-User Mode** in Settings > App to add readers and switch between them from the top bar. Off, the Reader has one library and no profile switcher.

## Backup and restore

Settings > Library > Library backup (Create backup, Restore from backup) writes your library list, reading positions, bookmarks, highlights, collections and per-title settings to a file you choose, and reads one back. A restored title has no file yet: tap it and choose its file (or folder) again to link it.

![Backup](images/backup.png)

## Keys at a glance

| Where | Keys |
| --- | --- |
| Library | S sort, F filter, C continue, / search, H highlights |
| Book | Arrows or Space page, T contents, A appearance, S text size, B bookmark, F find, H highlight, L highlights, D dictionary, V read aloud, K keys |
| Comic | Arrows page, Z / X / 0 zoom, A mode and fit, B bookmark |
| Audiobook | Space play/pause, arrows skip, N / P chapter, T chapters, B bookmark, A playback |
| PDF | Arrows or Space page, T pages, B bookmark |
| Anywhere | Escape or Back closes, then leaves |

## Taking the screenshots

`tools/screenshots.sh` captures the pictures this guide uses from a phone connected over adb. Install the Reader on the phone, then:

```
bash tools/screenshots.sh                 # every shot in turn: set it up on the phone, press Enter
bash tools/screenshots.sh books comics    # only those shots
bash tools/screenshots.sh --auto          # only books.png, with no questions
```

For each shot the script prints what to put on the screen, waits for Enter (type s to skip), and saves `docs/images/<name>.png`. At the end it lists skipped or failed shots, so none is lost. Run `bash tools/screenshots.sh --help` for the options (`--out`, `--serial`).
