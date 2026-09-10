package com.hm.guard

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.guard.url.SecurityLevel
import com.hm.guard.url.UrlSecurityAnalyzer

class MainActivity : AppCompatActivity() {

    private lateinit var analyzer: UrlSecurityAnalyzer
    private lateinit var urlInputBox: EditText
    private lateinit var checkButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        analyzer = UrlSecurityAnalyzer()

        urlInputBox = findViewById(R.id.urlInputBox)
        checkButton = findViewById(R.id.checkButton)
        resultTextView = findViewById(R.id.resultTextView)

        checkButton.setOnClickListener {
            val urlText = urlInputBox.text.toString().trim()
            if (urlText.isNotEmpty()) {
                val securityLevel = analyzer.analyzeUrl(urlText)
                displayResult(securityLevel)
            } else {
                resultTextView.text = "لطفاً یک لینک معتبر وارد کنید."
                resultTextView.setTextColor(Color.YELLOW)
            }
        }
    }

    private fun displayResult(level: SecurityLevel) {
        when (level) {
            SecurityLevel.SAFE -> {
                resultTextView.text = "نتیجه تحلیل: امن (Safe)\nاین لینک فاقد نشانه‌های خطر است."
                resultTextView.setTextColor(Color.parseColor("#00E676"))
            }
            SecurityLevel.SUSPICIOUS -> {
                resultTextView.text = "نتیجه تحلیل: مشکوک (Suspicious)\nدارای الگوهای نیازمند بررسی بیشتر."
                resultTextView.setTextColor(Color.parseColor("#FFAB00"))
            }
            SecurityLevel.UNSAFE -> {
                resultTextView.text = "نتیجه تحلیل: ناامن (Unsafe)\nخطر بدافزار یا کلاهبرداری شناسایی شد!"
                resultTextView.setTextColor(Color.parseColor("#FF5252"))
            }
        }
    }
}
