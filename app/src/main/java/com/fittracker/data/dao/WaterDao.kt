package com.fittracker.data.dao

import androidx.room.*
import com.fittracker.data.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_intakes ORDER BY date DESC")
    fun getAllWaterLogs(): Flow<List<WaterIntakeEntity>>

    @Query("SELECT * FROM water_intakes WHERE date = :date")
    fun getWaterLogsByDate(date: Long): Flow<List<WaterIntakeEntity>>

    @Query("SELECT SUM(amountMl) FROM water_intakes WHERE date = :date")
    fun getDailyWaterTotal(date: Long): Flow<Int?>

    @Query("SELECT * FROM water_intakes WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getWaterLogsByDateRange(startDate: Long, endDate: Long): Flow<List<WaterIntakeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaterLog(waterIntake: WaterIntakeEntity): Long

    @Update
    suspend fun updateWaterLog(waterIntake: WaterIntakeEntity)

    @Delete
    suspend fun deleteWaterLog(waterIntake: WaterIntakeEntity)

    @Query("DELETE FROM water_intakes WHERE date = :date")
    suspend fun clearWaterLogsForDate(date: Long)
}
