package com.example.martfia.model

data class YouTubeRecipe(
    val foodName: String,               // 음식 이름
    val cookingTime: String,            // 조리 시간
    val image: String,                  // 이미지 URL
    val instructions: Map<String, String> // 조리 단계 (Key: 단계 번호, Value: 설명)
)
