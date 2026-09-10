package com.hm.guard.federated

import com.hm.guard.url.SecurityLevel

class FederatedClientManager(private val clientId: String) {

    // تولید بردار وزن‌های محلی بر اساس نتیجه تحلیل لینک (شبیه‌سازی یادگیری فدرال)
    fun computeLocalWeights(level: SecurityLevel): FloatArray {
        return when (level) {
            SecurityLevel.SAFE -> floatArrayOf(0.05f, 0.12f, -0.02f)
            SecurityLevel.SUSPICIOUS -> floatArrayOf(0.45f, -0.30f, 0.25f)
            SecurityLevel.UNSAFE -> floatArrayOf(0.95f, -0.88f, 0.75f)
        }
    }

    // شبیه‌سازی ریسک حمله مسموم‌سازی مدل (Model Poisoning Attack) برای ارزیابی امنیت شبکه
    fun simulateModelPoisoning(weights: FloatArray): FloatArray {
        val poisonedWeights = weights.clone()
        for (i in poisonedWeights.indices) {
            poisonedWeights[i] = poisonedWeights[i] * -10.0f // اعمال نویز مخرب جهت سنجش آسیب‌پذیری سرور مرکزی
        }
        return poisonedWeights
    }

    fun getClientId(): String {
        return clientId
    }
}
