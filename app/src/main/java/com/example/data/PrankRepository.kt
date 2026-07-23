package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class PrankRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("bravu_prank_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<PrankSettings> = _settings.asStateFlow()

    private val _logs = MutableStateFlow(loadLogs())
    val logs: StateFlow<List<PrankLogEntry>> = _logs.asStateFlow()

    private val _prankCount = MutableStateFlow(prefs.getInt("total_pranks_triggered", 0))
    val prankCount: StateFlow<Int> = _prankCount.asStateFlow()

    fun updateSettings(newSettings: PrankSettings) {
        _settings.value = newSettings
        saveSettings(newSettings)
    }

    fun recordPrank(url: String) {
        val currentSettings = _settings.value
        val message = when (currentSettings.selectedMode) {
            PrankMode.CLASSIC -> currentSettings.customMessage
            PrankMode.VIRUS_ALERT -> "⚠️ CRITICAL SYSTEM WARNING: 99 Viruses Detected! ... Just Kidding! YOU ARE PRANKED!"
            PrankMode.ERROR_404 -> "HTTP 404 NOT FOUND: The requested webpage was replaced by a prank! YOU ARE PRANKED!"
            PrankMode.RICKROLL -> "🎶 Never gonna give you up, never gonna let you down! YOU ARE PRANKED!"
            PrankMode.CUSTOM -> currentSettings.customMessage
        }

        val entry = PrankLogEntry(
            attemptedUrl = url,
            prankMessage = message,
            modeName = currentSettings.selectedMode.displayName
        )

        val updatedLogs = listOf(entry) + _logs.value.take(49) // Keep last 50
        _logs.value = updatedLogs
        saveLogs(updatedLogs)

        val newCount = _prankCount.value + 1
        _prankCount.value = newCount
        prefs.edit().putInt("total_pranks_triggered", newCount).apply()
    }

    fun clearLogs() {
        _logs.value = emptyList()
        prefs.edit().remove("prank_logs_json").apply()
    }

    private fun loadSettings(): PrankSettings {
        val msg = prefs.getString("custom_message", "🤡 You are pranked") ?: "🤡 You are pranked"
        val modeStr = prefs.getString("selected_mode", PrankMode.CLASSIC.name) ?: PrankMode.CLASSIC.name
        val mode = try { PrankMode.valueOf(modeStr) } catch (e: Exception) { PrankMode.CLASSIC }
        val sound = prefs.getBoolean("sound_enabled", true)
        val vibration = prefs.getBoolean("vibration_enabled", true)
        val confetti = prefs.getBoolean("confetti_enabled", true)
        val loadingMs = prefs.getLong("loading_ms", 1000L)

        return PrankSettings(
            customMessage = msg,
            selectedMode = mode,
            soundEnabled = sound,
            vibrationEnabled = vibration,
            confettiEnabled = confetti,
            fakeLoadingMs = loadingMs
        )
    }

    private fun saveSettings(s: PrankSettings) {
        prefs.edit()
            .putString("custom_message", s.customMessage)
            .putString("selected_mode", s.selectedMode.name)
            .putBoolean("sound_enabled", s.soundEnabled)
            .putBoolean("vibration_enabled", s.vibrationEnabled)
            .putBoolean("confetti_enabled", s.confettiEnabled)
            .putLong("loading_ms", s.fakeLoadingMs)
            .apply()
    }

    private fun loadLogs(): List<PrankLogEntry> {
        val jsonStr = prefs.getString("prank_logs_json", null) ?: return emptyList()
        val list = mutableListOf<PrankLogEntry>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    PrankLogEntry(
                        id = obj.optString("id"),
                        timestamp = obj.optLong("timestamp"),
                        attemptedUrl = obj.optString("attemptedUrl"),
                        prankMessage = obj.optString("prankMessage"),
                        modeName = obj.optString("modeName")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun saveLogs(logs: List<PrankLogEntry>) {
        val jsonArray = JSONArray()
        for (log in logs) {
            val obj = JSONObject()
            obj.put("id", log.id)
            obj.put("timestamp", log.timestamp)
            obj.put("attemptedUrl", log.attemptedUrl)
            obj.put("prankMessage", log.prankMessage)
            obj.put("modeName", log.modeName)
            jsonArray.put(obj)
        }
        prefs.edit().putString("prank_logs_json", jsonArray.toString()).apply()
    }

    companion object {
        val DEFAULT_BOOKMARKS = listOf(
            BookmarkItem("1", "Google", "google.com", "https://google.com", "🔍", "Search"),
            BookmarkItem("2", "YouTube", "youtube.com", "https://youtube.com", "▶️", "Media"),
            BookmarkItem("3", "Wikipedia", "wikipedia.org", "https://wikipedia.org", "📚", "Knowledge"),
            BookmarkItem("4", "Amazon", "amazon.com", "https://amazon.com", "🛒", "Shopping"),
            BookmarkItem("5", "Reddit", "reddit.com", "https://reddit.com", "🤖", "Social"),
            BookmarkItem("6", "Instagram", "instagram.com", "https://instagram.com", "📸", "Social"),
            BookmarkItem("7", "Netflix", "netflix.com", "https://netflix.com", "🍿", "Media"),
            BookmarkItem("8", "Bravu Games", "bravugames.io", "https://bravugames.io", "🎮", "Fun")
        )

        val TRENDING_SEARCHES = listOf(
            TrendingSearch("How to browse faster without ads", "Tech", "2.4M searches"),
            TrendingSearch("Top movies streaming this week", "Entertainment", "1.8M searches"),
            TrendingSearch("Secret browser tricks & shortcuts", "Tips", "950K searches"),
            TrendingSearch("Cute cat memes 2026", "Viral", "3.1M searches")
        )

        val SAMPLE_NEWS = listOf(
            NewsArticle(
                id = "n1",
                title = "Breakthrough in Web Browsing Speed Record Set Today",
                source = "Bravu Tech Times",
                timeAgo = "12 min ago",
                category = "Technology",
                readTime = "2 min read",
                snippet = "Engineers unveil next-generation quantum routing engine promising zero latency loading across global servers."
            ),
            NewsArticle(
                id = "n2",
                title = "10 Essential Security Features You Should Turn On Immediately",
                source = "CyberGuard Digest",
                timeAgo = "1 hr ago",
                category = "Privacy",
                readTime = "4 min read",
                snippet = "Learn how to block cross-site tracking and keep your credentials protected against modern exploits."
            ),
            NewsArticle(
                id = "n3",
                title = "Mysterious Lights Spotted Over City Center Last Night",
                source = "Daily Chronicle",
                timeAgo = "3 hrs ago",
                category = "News",
                readTime = "3 min read",
                snippet = "Eye witnesses report colorful shimmering lights hovering above downtown skyline before vanishing."
            )
        )
    }
}
