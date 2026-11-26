# Security policy

Report a security problem privately through GitHub: open the repository's **Security** tab and choose **Report a vulnerability**. Please don't open a public issue for it.

Things we treat as security bugs:

- The Reader reaching the network, or asking for the INTERNET, ACCESS_NETWORK_STATE or CAMERA permission. It is an offline reader and the merged manifest must never contain them.
- A crafted EPUB, CBZ, CBR, PDF or M4B that reads or writes files outside the Reader's own storage, runs code, or crashes the app on open (zip-slip, decompression bombs, oversized images and similar).
- A backup or vault file that opens without its passphrase or leaks the passphrase.
- A release APK whose SHA-256 doesn't match `SHA256SUMS.txt` on its release page, or that isn't signed with the Sable release key.
