package com.fittracker.network

import com.fittracker.network.models.ExerciseApiResponse
import com.fittracker.network.models.RecipeApiResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("exercises")
    suspend fun getExercises(
        @Query("limit") limit: Int = 50
    ): List<ExerciseApiResponse>

    @GET("exercises/search")
    suspend fun searchExercises(
        @Query("name") name: String
    ): List<ExerciseApiResponse>

    @GET("recipes/search")
    suspend fun searchRecipes(
        @Query("q") query: String,
        @Query("app_id") appId: String = "placeholder_id",
        @Query("app_key") appKey: String = "placeholder_key"
    ): RecipeSearchResponse
}

data class RecipeSearchResponse(
    @SerializedName("hits") val hits: List<RecipeHit>
)

data class RecipeHit(
    @SerializedName("recipe") val recipe: RecipeApiResponse
)
