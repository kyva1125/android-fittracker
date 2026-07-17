package com.fittracker.data.dao

import androidx.room.*
import com.fittracker.data.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE id = :id")
    fun getWorkoutById(id: Int): Flow<WorkoutEntity?>

    @Query("SELECT * FROM workouts WHERE type = :type ORDER BY date DESC")
    fun getWorkoutsByType(type: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE name LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchWorkouts(query: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE date BETWEEN :startOfDay AND :endOfDay ORDER BY date DESC")
    fun getWorkoutsByDateRange(startOfDay: Long, endOfDay: Long): Flow<List<WorkoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)
}
