package com.fittracker.repository

import com.fittracker.data.*
import com.fittracker.data.dao.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FitTrackerRepository @Inject constructor(
    private val userDao: UserDao,
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val mealDao: MealDao,
    private val waterDao: WaterDao,
    private val measurementDao: MeasurementDao,
    private val achievementDao: AchievementDao
) {
    // User profile queries
    fun getUser(): Flow<UserEntity?> = userDao.getUser()
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    // Workout queries
    fun getAllWorkouts(): Flow<List<WorkoutEntity>> = workoutDao.getAllWorkouts()
    fun getWorkoutById(id: Int): Flow<WorkoutEntity?> = workoutDao.getWorkoutById(id)
    fun getWorkoutsByType(type: String): Flow<List<WorkoutEntity>> = workoutDao.getWorkoutsByType(type)
    fun searchWorkouts(query: String): Flow<List<WorkoutEntity>> = workoutDao.searchWorkouts(query)
    fun getWorkoutsByDateRange(start: Long, end: Long): Flow<List<WorkoutEntity>> =
        workoutDao.getWorkoutsByDateRange(start, end)
    suspend fun insertWorkout(workout: WorkoutEntity): Long = workoutDao.insertWorkout(workout)
    suspend fun updateWorkout(workout: WorkoutEntity) = workoutDao.updateWorkout(workout)
    suspend fun deleteWorkout(workout: WorkoutEntity) = workoutDao.deleteWorkout(workout)

    // Exercise & Set queries
    fun getExercisesForWorkout(workoutId: Int): Flow<List<ExerciseEntity>> = exerciseDao.getExercisesForWorkout(workoutId)
    fun getExerciseById(id: Int): Flow<ExerciseEntity?> = exerciseDao.getExerciseById(id)
    suspend fun insertExercise(exercise: ExerciseEntity): Long = exerciseDao.insertExercise(exercise)
    suspend fun updateExercise(exercise: ExerciseEntity) = exerciseDao.updateExercise(exercise)
    suspend fun deleteExercise(exercise: ExerciseEntity) = exerciseDao.deleteExercise(exercise)

    fun getSetsForExercise(exerciseId: Int): Flow<List<WorkoutSetEntity>> = exerciseDao.getSetsForExercise(exerciseId)
    suspend fun insertSet(set: WorkoutSetEntity): Long = exerciseDao.insertSet(set)
    suspend fun updateSet(set: WorkoutSetEntity) = exerciseDao.updateSet(set)
    suspend fun deleteSet(set: WorkoutSetEntity) = exerciseDao.deleteSet(set)
    suspend fun deleteSetsForExercise(exerciseId: Int) = exerciseDao.deleteSetsForExercise(exerciseId)

    // Meal queries
    fun getAllMeals(): Flow<List<MealEntity>> = mealDao.getAllMeals()
    fun getMealsByDate(date: Long): Flow<List<MealEntity>> = mealDao.getMealsByDate(date)
    fun getMealsByDateRange(start: Long, end: Long): Flow<List<MealEntity>> = mealDao.getMealsByDateRange(start, end)
    fun getMealById(id: Int): Flow<MealEntity?> = mealDao.getMealById(id)
    suspend fun insertMeal(meal: MealEntity): Long = mealDao.insertMeal(meal)
    suspend fun updateMeal(meal: MealEntity) = mealDao.updateMeal(meal)
    suspend fun deleteMeal(meal: MealEntity) = mealDao.deleteMeal(meal)

    // Water Intake queries
    fun getAllWaterLogs(): Flow<List<WaterIntakeEntity>> = waterDao.getAllWaterLogs()
    fun getWaterLogsByDate(date: Long): Flow<List<WaterIntakeEntity>> = waterDao.getWaterLogsByDate(date)
    fun getWaterLogsByDateRange(start: Long, end: Long): Flow<List<WaterIntakeEntity>> = waterDao.getWaterLogsByDateRange(start, end)
    fun getDailyWaterTotal(date: Long): Flow<Int?> = waterDao.getDailyWaterTotal(date)
    suspend fun insertWaterLog(waterIntake: WaterIntakeEntity): Long = waterDao.insertWaterLog(waterIntake)
    suspend fun deleteWaterLog(waterIntake: WaterIntakeEntity) = waterDao.deleteWaterLog(waterIntake)
    suspend fun clearWaterLogsForDate(date: Long) = waterDao.clearWaterLogsForDate(date)

    // Body Measurement queries
    fun getAllMeasurements(): Flow<List<BodyMeasurementEntity>> = measurementDao.getAllMeasurements()
    fun getLatestMeasurement(): Flow<BodyMeasurementEntity?> = measurementDao.getLatestMeasurement()
    fun getMeasurementById(id: Int): Flow<BodyMeasurementEntity?> = measurementDao.getMeasurementById(id)
    suspend fun insertMeasurement(measurement: BodyMeasurementEntity): Long = measurementDao.insertMeasurement(measurement)
    suspend fun updateMeasurement(measurement: BodyMeasurementEntity) = measurementDao.updateMeasurement(measurement)
    suspend fun deleteMeasurement(measurement: BodyMeasurementEntity) = measurementDao.deleteMeasurement(measurement)

    // Achievements queries
    fun getAllAchievements(): Flow<List<AchievementEntity>> = achievementDao.getAllAchievements()
    fun getUnlockedAchievements(): Flow<List<AchievementEntity>> = achievementDao.getUnlockedAchievements()
    fun getLockedAchievements(): Flow<List<AchievementEntity>> = achievementDao.getLockedAchievements()
    fun getTotalXpPoints(): Flow<Int?> = achievementDao.getTotalXpPoints()
    fun getUnlockedCount(): Flow<Int> = achievementDao.getUnlockedCount()
    suspend fun insertAchievement(achievement: AchievementEntity) = achievementDao.insertAchievement(achievement)
    suspend fun insertAchievements(achievements: List<AchievementEntity>) = achievementDao.insertAchievements(achievements)
    suspend fun updateAchievement(achievement: AchievementEntity) = achievementDao.updateAchievement(achievement)
    suspend fun unlockAchievement(id: Int, date: Long = System.currentTimeMillis()) = achievementDao.unlockAchievement(id, date)
}
