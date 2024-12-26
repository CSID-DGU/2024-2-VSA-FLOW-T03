package com.example.martfia

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.martfia.adapter.CookingStepAdapter
import com.example.martfia.model.YouTubeRecipe
import com.example.martfia.model.response.YoutubeAssistantStartResponse
import com.example.martfia.service.MartfiaRetrofitClient
import com.example.martfia.service.YoutubeAssistantService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // UI 요소 초기화
        val backButton = findViewById<ImageView>(R.id.backButton)
        val recipeImageView = findViewById<ImageView>(R.id.recipeImageView)
        val recipeNameTextView = findViewById<TextView>(R.id.recipeNameTextView)
        val recipeTimeTextView = findViewById<TextView>(R.id.recipeTimeTextView)
        val recipeStepsRecyclerView = findViewById<RecyclerView>(R.id.recipeStepsRecyclerView)
        val assistantButton = findViewById<LinearLayout>(R.id.assistantButton)

        // 뒤로 가기 버튼 클릭 리스너
        backButton.setOnClickListener {
            finish() // 이전 화면으로 이동
        }

        // 하드코딩된 YouTubeRecipe 데이터 생성
        val sampleRecipe = YouTubeRecipe(
            foodName = "성공률 100% 돼지고기 김치찌개",
            cookingTime = "20-30분",
            image = "https://i.ytimg.com/vi_webp/qWbHSOplcvY/maxresdefault.webp",
            instructions = mapOf(
                "1" to "먼저, 돼지고기는 먹기 편한 크기로 잘라 냄비에 넣습니다. 원할 경우 삼겹살이나 다른 부위를 사용해도 됩니다.",
                "2" to "물과 대추를 넣고 중약 불에서 20~30분 동안 고기가 충분히 익도록 끓여 줍니다.",
                "3" to "20~30분 후, 물이 충분히 줄어든 상태에서 김치를 3컵 정도 넣어줍니다.",
                "4" to "관리하면서 오래 끓이면 더욱 맛있어진다는 포인트를 잊지 마세요. 만약 물이 너무 많이 줄어든다면 추가로 물을 보충해서 끓여줍니다.",
                "5" to "다음으로 국간장을 준비하고, 맛을 보며 적당한 양을 넣어줍니다. 조금씩 넣어 가며 김치찌개의 간을 맞춰줍니다.",
                "6" to "기호에 따라 파, 청양고추, 양념을 추가하며 끓여 갑니다. 참고로 간장은 향을 위한 용도로 주 사용됩니다.",
                "7" to "마지막으로 청양고추와 대파를 올리고 다시 끓여줍니다. 그리고 김치찌개를 상에 올려서 바로 선다면 더욱 신선하고 맛있게 느낄 수 있습니다.",
                "8" to "분량은 약간씩 조절하며 여러번 시도하면서 본인이 좋아하는 맛으로 맞추면 됩니다.",
                "9" to "대부분의 재료는 대충 가늠하면 되지만, 신김치는 반드시 잘 익혀진 것을 사용해야 합니다.",
                "10" to "약 4분 정도 더 끓여 준 후, 불을 끄고 차분히 뜨거운 김치찌개를 즐겨보세요."
            )
        )

        // UI 업데이트
        recipeNameTextView.text = sampleRecipe.foodName
        recipeTimeTextView.text = sampleRecipe.cookingTime

        Glide.with(this)
            .load(sampleRecipe.image)
            .centerCrop()
            .placeholder(R.drawable.img_cooking)
            .into(recipeImageView)

        // RecyclerView 설정
        recipeStepsRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeStepsRecyclerView.adapter = CookingStepAdapter(sampleRecipe.instructions)

        // 조리 Assistant 버튼 동작 연결
        assistantButton.setOnClickListener {
            startCookingAssistantSession() // API 호출 후 이동
        }
    }

    private fun startCookingAssistantSession() {
        val service = MartfiaRetrofitClient.createService(YoutubeAssistantService::class.java)

        service.startCookingAssistant().enqueue(object : Callback<YoutubeAssistantStartResponse> {
            override fun onResponse(call: Call<YoutubeAssistantStartResponse>, response: Response<YoutubeAssistantStartResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val message = response.body()?.message ?: "조리 어시스턴트를 시작할 수 없습니다."

                    // CookingAssistantActivity로 전환
                    val intent = Intent(this@RecipeDetailActivity, CookingAssistantActivity::class.java)
                    intent.putExtra("assistant_message", message)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@RecipeDetailActivity, "조리 어시스턴트를 시작할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<YoutubeAssistantStartResponse>, t: Throwable) {
                Toast.makeText(this@RecipeDetailActivity, "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
