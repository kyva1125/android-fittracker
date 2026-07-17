package com.fittracker.di

import com.fittracker.data.dao.*
import com.fittracker.network.ApiService
import com.fittracker.repository.FitTrackerRepository
import com.fittracker.repository.HealthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFitTrackerRepository(
        userDao: UserDao,
        workoutDao: WorkoutDao,
        exerciseDao: ExerciseDao,
        mealDao: MealDao,
        waterDao: WaterDao,
        measurementDao: MeasurementDao,
        achievementDao: AchievementDao
    ): FitTrackerRepository {
        return FitTrackerRepository(
            userDao,
            workoutDao,
            exerciseDao,
            mealDao,
            waterDao,
            measurementDao,
            achievementDao
        )
    }

    @Provides
    @Singleton
    fun provideHealthRepository(
        apiService: ApiService
    ): HealthRepository {
        return HealthRepository(apiService)
    }
}
