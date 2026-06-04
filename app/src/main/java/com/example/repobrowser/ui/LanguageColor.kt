package com.example.repobrowser.ui

import androidx.compose.ui.graphics.Color

/**
 * Maps a primary language to its GitHub "linguist" colour, used for the small
 * dot next to a language name. Falls back to a neutral grey for anything
 * unmapped or null.
 */
fun languageColor(language: String?): Color = when (language) {
    "Kotlin" -> Color(0xFFA97BFF)
    "Java" -> Color(0xFFB07219)
    "TypeScript" -> Color(0xFF3178C6)
    "JavaScript" -> Color(0xFFF1E05A)
    "Python" -> Color(0xFF3572A5)
    "Dart" -> Color(0xFF00B4AB)
    "HTML" -> Color(0xFFE34C26)
    "CSS" -> Color(0xFF563D7C)
    "C" -> Color(0xFF555555)
    "C++" -> Color(0xFFF34B7D)
    "C#" -> Color(0xFF178600)
    "Go" -> Color(0xFF00ADD8)
    "Rust" -> Color(0xFFDEA584)
    "Ruby" -> Color(0xFF701516)
    "Swift" -> Color(0xFFF05138)
    "PHP" -> Color(0xFF4F5D95)
    "Shell" -> Color(0xFF89E051)
    else -> Color(0xFF8B949E)
}
