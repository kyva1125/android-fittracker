package com.fittracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fittracker.data.dao.*

@Database(
    entities = [
        UserEntity::class,
        WorkoutEntity::class,
        ExerciseEntity::class,
        WorkoutSetEntity::class,
        MealEntity::class,
        WaterIntakeEntity::class,
        BodyMeasurementEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FitTrackerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun mealDao(): MealDao
    abstract fun waterDao(): WaterDao
    abstract fun measurementDao(): MeasurementDao
    abstract fun achievementDao(): AchievementDao
}
