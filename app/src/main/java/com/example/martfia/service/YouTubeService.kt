package com.example.martfia.service

import com.example.martfia.model.request.YouTubeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface YouTubeService {
    @Headers("Content-Type: application/json")
    @POST("api/youtube")
    fun uploadYouTubeUrl(
        @Body request: YouTubeRequest
    ): Call<Void>
}
