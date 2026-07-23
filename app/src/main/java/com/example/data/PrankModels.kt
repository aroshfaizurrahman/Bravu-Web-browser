package com.example.data

import androidx.compose.ui.graphics.vector.ImageVector

enum class PrankMode(val displayName: String, val description: String) {
    CLASSIC("Party Confetti", "Big animated reveal with cheerful confetti and sound effect!"),
    VIRUS_ALERT("Fake Security Alert", "Shows a fake dramatic virus scanner warning before revealing the prank!"),
    ERROR_404("Fake 404 Error", "Simulates a broken page error that transforms into 'You are pranked!'"),
    RICKROLL("Rickroll Surprise", "Plays the classic prank dance lyrics with party animation!"),
    CUSTOM("Custom Message", "Display your own personalized message when a link is opened!")
}

data class PrankSettings(
    val customMessage: String = "🤡 You are pranked",
    val selectedMode: PrankMode = PrankMode.CLASSIC,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val confettiEnabled: Boolean = true,
    val fakeLoadingMs: Long = 1000L,
    val stealthModeEnabled: Boolean = true
)

data class PrankLogEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val attemptedUrl: String,
    val prankMessage: String,
    val modeName: String
)

data class BookmarkItem(
    val id: String,
    val title: String,
    val domain: String,
    val url: String,
    val iconEmoji: String,
    val category: String = "Popular"
)

data class TrendingSearch(
    val query: String,
    val tag: String,
    val views: String
)

data class NewsArticle(
    val id: String,
    val title: String,
    val source: String,
    val timeAgo: String,
    val category: String,
    val readTime: String,
    val snippet: String
)
