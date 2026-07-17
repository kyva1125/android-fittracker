package com.fittracker.di

import android.content.Context
import androidx.room.Room
import com.fittracker.data.FitTrackerDatabase
import com.fittracker.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): FitTrackerDatabase {
        return Room.databaseBuilder(
            context,
            FitTrackerDatabase::class.java,
            "fittracker_database"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun provideUserDao(db: FitTrackerDatabase): UserDao = db.userDao()

    @Provides
    fun provideWorkoutDao(db: FitTrackerDatabase): WorkoutDao = db.workoutDao()

    @Provides
    fun provideExerciseDao(db: FitTrackerDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    fun provideMealDao(db: FitTrackerDatabase): MealDao = db.mealDao()

    @Provides
    fun provideWaterDao(db: FitTrackerDatabase): WaterDao = db.waterDao()

    @Provides
    fun provideMeasurementDao(db: FitTrackerDatabase): MeasurementDao = db.measurementDao()

    @Provides
    fun provideAchievementDao(db: FitTrackerDatabase): AchievementDao = db.achievementDao()
}
