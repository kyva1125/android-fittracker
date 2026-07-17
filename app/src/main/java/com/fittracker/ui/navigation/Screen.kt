package com.fittracker.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Workouts : Screen("workouts")
    object WorkoutDetail : Screen("workout_detail/{workoutId}") {
        fun createRoute(workoutId: Int) = "workout_detail/$workoutId"
    }
    object CreateWorkout : Screen("create_workout")
    object ExerciseTracker : Screen("exercise_tracker/{workoutId}") {
        fun createRoute(workoutId: Int) = "exercise_tracker/$workoutId"
    }
    object RestTimer : Screen("rest_timer/{durationSeconds}") {
        fun createRoute(durationSeconds: Int) = "rest_timer/$durationSeconds"
    }
    object Nutrition : Screen("nutrition")
    object AddMeal : Screen("add_meal")
    object WaterTracker : Screen("water_tracker")
    object Progress : Screen("progress")
    object WeightChart : Screen("weight_chart")
    object Measurement : Screen("measurement")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object Settings : Screen("settings")
    object Achievements : Screen("achievements")
}
