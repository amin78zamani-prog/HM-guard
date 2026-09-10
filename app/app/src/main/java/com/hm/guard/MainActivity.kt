package com.hm.guard

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.guard.federated.FederatedClientManager
import com.hm.guard.url.SecurityLevel
import com.hm.guard.url.UrlSecurityAnalyzer

class MainActivity : AppCompatActivity() {

    private lateinit var analyzer: UrlSecurityAnalyzer
    private lateinit var federatedManager: FederatedClientManager
    
    private lateinit var urlInputBox: EditText
    private lateinit var checkButton: Button
    private lateinit var attackButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        analyzer = UrlSecurityAnalyzer()
        federatedManager = FederatedClientManager("Client_Node_01")

        urlInputBox = findViewById(R.id.urlInputBox)
        checkButton = findViewById(R.id.checkButton)
        attackButton = findViewById(R.id.attackButton)
        resultTextView = findViewById(R.id.resultTextView)

        checkButton.setOnClickListener {
            val urlText = urlInputBox.text.toString().trim()
            if (urlText.isNotEmpty()) {
                val securityLevel = analyzer.analyzeUrl(urlText)
                val weights = federatedManager.computeLocalWeights(securityLevel)
                displayResult(securityLevel, weights)
            } else {
                resultTextView.text = "لطفاً یک لینک معتبر وارد کنید."
                resultTextView.setTextColor(Color.YELLOW)
            }
        }

        attackButton.setOnClickListener {
            val dummyWeights = floatArrayOf(0.5f, -0.5f, 0.5f)
            val poisonedWeights = federatedManager.simulateModelPoisoning(dummyWeights)
            resultTextView.text = "هشدار: حمله مسموم‌سازی مدل (Model Poisoning) شبیه‌سازی و وزن‌های مخرب تولید شد:\n${poisonedWeights.contentToString()}"
            resultTextView.setTextColor(Color.parseColor("#FF5252"))
        }
    }

    private fun displayResult(level: SecurityLevel, weights: FloatArray) {
        val weightsStr = weights.contentToString()
        when (level) {
            SecurityLevel.SAFE -> {
                resultTextView.text = "نتیجه: امن (Safe)\nوزن‌های فدرال تولیدی: $weightsStr"
                resultTextView.setTextColor(Color.parseColor("#00E676"))
            }
            SecurityLevel.SUSPICIOUS -> {
                resultTextView.text = "نتیجه: مشکوک (Suspicious)\nوزن‌های فدرال تولیدی: $weightsStr"
                resultTextView.setTextColor(Color.parseColor("#FFAB00"))
            }
            SecurityLevel.UNSAFE -> {
                resultTextView.text = "نتیجه: ناامن (Unsafe - تهدید بدافزار)\nوزن‌های فدرال تولیدی: $weightsStr"
                resultTextView.setTextColor(Color.parseColor("#FF5252"))
            }
        }
    }
}
