package com.hm.guard.url

enum class SecurityLevel {
    SAFE, SUSPICIOUS, UNSAFE
}

class UrlSecurityAnalyzer {

    fun analyzeUrl(url: String): SecurityLevel {
        val lowercaseUrl = url.lowercase()

        val unsafeKeywords = listOf("login-verify", "update-account", "free-gift", "malware", "phishing")
        val suspiciousKeywords = listOf("free", "win", "verify", "support", "auth", "login")

        return when {
            unsafeKeywords.any { lowercaseUrl.contains(it) } -> SecurityLevel.UNSAFE
            suspiciousKeywords.any { lowercaseUrl.contains(it) } || url.length > 70 -> SecurityLevel.SUSPICIOUS
            else -> SecurityLevel.SAFE
        }
    }
}
