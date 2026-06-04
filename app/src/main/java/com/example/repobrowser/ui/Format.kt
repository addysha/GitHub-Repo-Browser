package com.example.repobrowser.ui

/**
 * GitHub returns timestamps as ISO-8601, e.g. "2024-12-01T10:20:30Z". For the
 * UI we only need the calendar date, so we take the leading date portion. This
 * avoids pulling in java.time/desugaring for a minSdk-24 build.
 */
fun String.toDisplayDate(): String =
    if (length >= 10) substring(0, 10) else this
