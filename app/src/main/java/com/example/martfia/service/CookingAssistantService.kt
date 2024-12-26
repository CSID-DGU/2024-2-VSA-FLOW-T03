package com.example.martfia.service

import com.example.martfia.model.response.RecipeQueryResponse
import com.example.martfia.model.request.RecipeQueryRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CookingAssistantService {

    @Headers("Content-Type: application/json")
    @POST("api/recipe/query")
    fun queryRecipeStep(
        @Body request: RecipeQueryRequest
    ): Call<RecipeQueryResponse>
}
