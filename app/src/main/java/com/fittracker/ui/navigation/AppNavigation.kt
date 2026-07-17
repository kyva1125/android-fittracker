package com.fittracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.fittracker.ui.home.HomeScreen
import com.fittracker.ui.nutrition.*
import com.fittracker.ui.profile.*
import com.fittracker.ui.progress.*
import com.fittracker.ui.workout.*

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home)
    object Workouts : BottomNavItem(Screen.Workouts.route, "Workouts", Icons.Default.FitnessCenter)
    object Nutrition : BottomNavItem(Screen.Nutrition.route, "Nutrition", Icons.Default.Restaurant)
    object Progress : BottomNavItem(Screen.Progress.route, "Progress", Icons.Default.ShowChart)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Workouts,
        BottomNavItem.Nutrition,
        BottomNavItem.Progress,
        BottomNavItem.Profile
    )

    // Determine if the current route is a root bottom navigation destination
    val showBottomBar = bottomItems.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Home Screen
            composable(Screen.Home.route) {
                HomeScreen(
                    onQuickAction = { action ->
                        when (action) {
                            "workout" -> navController.navigate(Screen.CreateWorkout.route)
                            "meal" -> navController.navigate(Screen.AddMeal.route)
                            "water" -> navController.navigate(Screen.WaterTracker.route)
                            "progress" -> navController.navigate(Screen.Measurement.route)
                        }
                    }
                )
            }

            // Workouts Screen
            composable(Screen.Workouts.route) {
                WorkoutListScreen(
                    onWorkoutClick = { id ->
                        navController.navigate(Screen.WorkoutDetail.createRoute(id))
                    },
                    onCreateWorkoutClick = {
                        navController.navigate(Screen.CreateWorkout.route)
                    }
                )
            }

            // Workout Detail Screen
            composable(
                route = Screen.WorkoutDetail.route,
                arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
            ) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
                WorkoutDetailScreen(
                    workoutId = workoutId,
                    onBack = { navController.popBackStack() },
                    onStartWorkout = { id ->
                        navController.navigate(Screen.ExerciseTracker.createRoute(id))
                    }
                )
            }

            // Create Workout Screen
            composable(Screen.CreateWorkout.route) {
                CreateWorkoutScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Exercise Tracker Screen
            composable(
                route = Screen.ExerciseTracker.route,
                arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
            ) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
                ExerciseTrackerScreen(
                    workoutId = workoutId,
                    onBack = { navController.popBackStack() },
                    onRestTimer = { duration ->
                        navController.navigate(Screen.RestTimer.createRoute(duration))
                    }
                )
            }

            // Rest Timer Screen
            composable(
                route = Screen.RestTimer.route,
                arguments = listOf(navArgument("durationSeconds") { type = NavType.IntType })
            ) { backStackEntry ->
                val durationSeconds = backStackEntry.arguments?.getInt("durationSeconds") ?: 60
                RestTimerScreen(
                    durationSeconds = durationSeconds,
                    onTimerFinished = { navController.popBackStack() },
                    onSkip = { navController.popBackStack() }
                )
            }

            // Nutrition Screen
            composable(Screen.Nutrition.route) {
                NutritionScreen(
                    onAddMealClick = { navController.navigate(Screen.AddMeal.route) },
                    onWaterClick = { navController.navigate(Screen.WaterTracker.route) }
                )
            }

            // Add Meal Screen
            composable(Screen.AddMeal.route) {
                AddMealScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Water Tracker Screen
            composable(Screen.WaterTracker.route) {
                WaterTrackerScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Progress Screen
            composable(Screen.Progress.route) {
                ProgressScreen(
                    onViewWeightChart = { navController.navigate(Screen.WeightChart.route) },
                    onLogMeasurements = { navController.navigate(Screen.Measurement.route) }
                )
            }

            // Weight Chart Screen
            composable(Screen.WeightChart.route) {
                WeightChartScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Measurement Screen
            composable(Screen.Measurement.route) {
                MeasurementScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Profile Screen
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onSettings = { navController.navigate(Screen.Settings.route) },
                    onAchievements = { navController.navigate(Screen.Achievements.route) }
                )
            }

            // Edit Profile Screen
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Settings Screen
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Achievements Screen
            composable(Screen.Achievements.route) {
                AchievementsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
