package com.fittracker.data.dao

import androidx.room.*
import com.fittracker.data.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meals ORDER BY date DESC, time DESC")
    fun getAllMeals(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE date = :date ORDER BY time DESC")
    fun getMealsByDate(date: Long): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, time DESC")
    fun getMealsByDateRange(startDate: Long, endDate: Long): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE id = :id")
    fun getMealById(id: Int): Flow<MealEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)
}
