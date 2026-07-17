package com.fittracker.repository

import com.fittracker.network.ApiService
import com.fittracker.network.NetworkResult
import com.fittracker.network.RecipeSearchResponse
import com.fittracker.network.models.ExerciseApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getExercises(limit: Int = 50): Flow<NetworkResult<List<ExerciseApiResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = apiService.getExercises(limit)
            emit(NetworkResult.Success(response))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    fun searchExercises(name: String): Flow<NetworkResult<List<ExerciseApiResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = apiService.searchExercises(name)
            emit(NetworkResult.Success(response))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    fun searchRecipes(query: String): Flow<NetworkResult<RecipeSearchResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = apiService.searchRecipes(query)
            emit(NetworkResult.Success(response))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
