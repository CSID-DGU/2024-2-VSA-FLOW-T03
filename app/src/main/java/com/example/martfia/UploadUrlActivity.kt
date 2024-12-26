package com.example.martfia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.martfia.model.request.YouTubeRequest
import com.example.martfia.service.MartfiaRetrofitClient
import com.example.martfia.service.YouTubeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadUrlActivity : AppCompatActivity() {

    private lateinit var urlEditText: EditText
    private lateinit var detectUrlButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var backButton: ImageView

    private val youTubeService: YouTubeService by lazy {
        MartfiaRetrofitClient.createService(YouTubeService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_url)

        // UI 요소 초기화
        urlEditText = findViewById(R.id.urlEditText)
        detectUrlButton = findViewById(R.id.detectUrlButton)
        progressBar = findViewById(R.id.progressBar)
        backButton = findViewById(R.id.backButton)

        // 뒤로 가기 버튼 클릭 리스너
        backButton.setOnClickListener {
            navigateToChooseImageActivity()
        }

        // URL 인식 버튼 클릭 리스너
        detectUrlButton.setOnClickListener {
            val url = urlEditText.text.toString().trim()
            if (url.isEmpty()) {
                Toast.makeText(this, "URL을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = View.VISIBLE
                detectUrlButton.isEnabled = false // 버튼 비활성화
                uploadYouTubeUrl(url) // URL 업로드 API 호출
            }
        }
    }

    private fun uploadYouTubeUrl(url: String) {
        val request = YouTubeRequest(url)

        youTubeService.uploadYouTubeUrl(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                progressBar.visibility = View.GONE
                detectUrlButton.isEnabled = true
                if (response.isSuccessful) {
                    Toast.makeText(this@UploadUrlActivity, "URL 업로드 성공!", Toast.LENGTH_SHORT).show()
                    navigateToRecipeDetailActivityWithHardcodedData()
                } else {
                    Toast.makeText(this@UploadUrlActivity, "업로드 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBar.visibility = View.GONE
                detectUrlButton.isEnabled = true
                Toast.makeText(this@UploadUrlActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToRecipeDetailActivityWithHardcodedData() {
        // RecipeDetailActivity로 이동
        val intent = Intent(this, RecipeDetailActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToChooseImageActivity() {
        val intent = Intent(this, ChooseImageActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // 뒤로 가기 버튼 누르면 ChooseImageActivity로 이동
        navigateToChooseImageActivity()
    }
}
