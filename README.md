<div align="center">

  <h1>🏋️ FitTracker</h1>
  
  <p><strong>Fitness & Workout Tracking App</strong> • Kotlin • Jetpack Compose</p>
  
  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue?logo=kotlin)](https://kotlinlang.org)
  [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.8-blue?logo=jetpack)](https://developer.android.com/jetpack/compose)
  [![Android API](https://img.shields.io/badge/Android%20API-24%2B-green)](https://developer.android.com)
  [![Material3](https://img.shields.io/badge/Material%203-1.1.1-blue)](https://m3.material.io)
  [![Hilt](https://img.shields.io/badge/Hilt-DI-blue)](https://dagger.dev/hilt/)
  [![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

  <p>A comprehensive fitness tracking app built with <strong>Jetpack Compose</strong> featuring workout timer, progress tracking, and health metrics integration.</p>

</div>

---

## 🎯 About

FitTracker is a feature-rich fitness application demonstrating modern Android development with Jetpack Compose. This project showcases complex state management, real-time timers with coroutines, health data integration, custom UI components for workout tracking, and advanced animation patterns.

---

## ✨ Features

- **⏱️ Workout Timer** - Real-time workout session tracking with pause/resume
- **📊 Progress Monitoring** - Track fitness goals and improvements over time
- **🏋️ Exercise Database** - Pre-built exercise library with instructions
- **💪 Work History** - Complete log of completed workouts
- **📈 Statistics & Charts** - Visual progress tracking with custom charts
- **🎯 Goal Setting** - Set and track fitness goals with reminders
- **🎨 Material3 Design** - Modern, accessible UI with Dark Mode
- **💾 Local Storage** - Room database for workout persistence
- **⚡ Instant Updates** - State-driven UI with Jetpack Compose
- **🔗 Health Integration** - Connect with health data repositories
- **🔔 Notifications** - Workout reminders and progress alerts
- **📊 Weekly/Monthly Reports** - Detailed progress summaries

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin 1.9.0 |
| **UI Framework** | Jetpack Compose (No XML) |
| **Architecture** | MVVM + Repository Pattern |
| **Dependency Injection** | Hilt |
| **Database** | Room (Local SQLite) |
| **Async** | Kotlinx Coroutines + Flows |
| **State Management** | State / remember / ViewModel |
| **Timers** | kotlinx.coroutines.delay |
| **Health Data** | HealthRepository |
| **Date/Time** | kotlinx.datetime |
| **Notifications** | NotificationManager |
| **Background Tasks** | WorkManager |
| **Preferences** | DataStore |
| **Min SDK** | 24 (Android 7.0+) |
| **Target SDK** | 35 |

---

## 📱 Screens

- **🏠 Home** - Dashboard with daily goals & quick actions
- **⏱️ Active Workout** - Live workout timer with exercises
- **📋 Exercise Library** - Browse and add exercises
- **📊 Progress** - Charts and statistics
- **📜 History** - Completed workout log
- **🎯 Goals** - Set and track fitness goals
- **⚙️ Settings** - Preferences and notifications

---

## 🚀 Architecture

```
┌─────────────────────────────────────┐
│          UI Layer (Compose)          │
│  - Screens, ViewModels, Components  │
│  - Timer Components, Chart Widgets  │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│      Repository Layer (Data)        │
│  - WorkoutRepository                │
│  - ExerciseRepository               │
│  - HealthRepository                 │
│  - Room Database (Local Storage)     │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│       Domain Layer (Business)       │
│  - WorkoutTimer, Entities, Utils     │
│  - Calculations, Aggregations       │
└─────────────────────────────────────┘
```

---

## 🎯 Jetpack Compose Expertise

### Advanced Compose Patterns

#### 1. Real-time Timer with Coroutines and LaunchedEffect

```kotlin
@Composable
fun WorkoutTimer(
    viewModel: WorkoutTimerViewModel = hiltViewModel()
) {
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    
    // Timer animation
    val animatedProgress by animateFloatAsState(
        targetValue = (elapsedTime % 60) / 60f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "timerProgress"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        // Circular progress indicator
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 8.dp,
                color = if (isRunning) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.surfaceVariant
            )
            
            Text(
                text = formatTime(elapsedTime),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Timer controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { 
                    if (isRunning) viewModel.pause() else viewModel.start()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) 
                        MaterialTheme.colorScheme.secondary 
                    else 
                        MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Start"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isRunning) "Pause" else "Start")
            }
            
            Button(
                onClick = { viewModel.reset() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset")
            }
        }
    }
}
```

#### 2. ViewModel with Coroutine Timer Management

```kotlin
@HiltViewModel
class WorkoutTimerViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()
    
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()
    
    private val _currentExercise = MutableStateFlow(Exercise.default())
    val currentExercise: StateFlow<Exercise> = _currentExercise.asStateFlow()
    
    private var timerJob: Job? = null
    
    fun start() {
        if (timerJob == null) {
            _isRunning.value = true
            timerJob = viewModelScope.launch {
                while (isActive) {
                    delay(1000L)
                    _elapsedTime.value++
                    
                    // Update exercise progress
                    val exercise = _currentExercise.value
                    if (_elapsedTime.value >= exercise.duration) {
                        nextExercise()
                    }
                }
            }
        }
    }
    
    fun pause() {
        timerJob?.cancel()
        timerJob = null
        _isRunning.value = false
    }
    
    fun reset() {
        pause()
        _elapsedTime.value = 0L
        _currentExercise.value = Exercise.default()
    }
    
    private fun nextExercise() {
        viewModelScope.launch {
            _currentExercise.value = workoutRepository.getNextExercise()
            _elapsedTime.value = 0L
        }
    }
    
    fun completeWorkout() {
        viewModelScope.launch {
            workoutRepository.saveWorkout(
                Workout(
                    id = UUID.randomUUID().toString(),
                    duration = _elapsedTime.value,
                    completedAt = Clock.System.now(),
                    exercises = listOf(_currentExercise.value)
                )
            )
            reset()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
```

#### 3. Swipe-to-Delete with AnimatedVisibility

```kotlin
@Composable
fun ExerciseList(
    exercises: List<Exercise>,
    onRemove: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = exercises,
            key = { it.id }
        ) { exercise ->
            var dismissed by remember { mutableStateOf(false) }
            
            AnimatedVisibility(
                visible = !dismissed,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeOut()
            ) {
                SwipeToDismiss(
                    state = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                dismissed = true
                                onRemove(exercise)
                                true
                            } else {
                                false
                            }
                        },
                        positionalThreshold = { 150.dp.toPx() }
                    ),
                    background = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    dismissContent = {
                        ExerciseItem(exercise = exercise)
                    }
                )
            }
        }
    }
}
```

#### 4. Custom Progress Chart with Canvas

```kotlin
@Composable
fun ProgressChart(
    workouts: List<Workout>,
    modifier: Modifier = Modifier
) {
    val weeklyData by remember(workouts) {
        derivedStateOf {
            workouts
                .groupBy { it.completedAt.toLocalDateTime(TimeZone.UTC).date.dayOfWeek }
                .mapValues { (_, items) -> 
                    items.sumOf { it.duration } 
                }
                .toList()
                .sortedBy { it.first.value }
        }
    }
    
    val maxValue by remember(weeklyData) {
        derivedStateOf { weeklyData.maxOfOrNull { it.second } ?: 1L }
    }
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    ) {
        val padding = 40.dp.toPx()
        val width = size.width - padding * 2
        val height = size.height - padding * 2
        val barWidth = width / weeklyData.size * 0.6f
        val gap = width / weeklyData.size * 0.4f
        
        weeklyData.forEachIndexed { index, (dayOfWeek, duration) ->
            val barHeight = (duration.toFloat() / maxValue) * height
            val x = padding + index * (barWidth + gap) + gap / 2
            val y = size.height - padding - barHeight
            
            // Draw bar with gradient
            drawRoundRect(
                color = MaterialTheme.colorScheme.primary,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
            
            // Draw day label
            val dayText = dayOfWeek.name.take(3)
            drawText(
                textMeasurer = TextMeasurer(),
                text = AnnotatedString(dayText),
                topLeft = Offset(
                    x + barWidth / 2 - 10.dp.toPx(),
                    size.height - padding + 8.dp.toPx()
                ),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp
                )
            )
            
            // Draw duration label
            drawText(
                textMeasurer = TextMeasurer(),
                text = AnnotatedString("${duration}s"),
                topLeft = Offset(
                    x + barWidth / 2 - 15.dp.toPx(),
                    y - 20.dp.toPx()
                ),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
```

#### 5. Goal Progress with Animated Values

```kotlin
@Composable
fun GoalProgress(
    current: Double,
    target: Double,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = (current / target).coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "goalProgress"
    )
    
    val isComplete by remember(current, target) {
        derivedStateOf { current >= target }
    }
    
    val progressColor by animateColorAsState(
        targetValue = when {
            isComplete -> Color(0xFF4CAF50)
            progress > 0.8f -> Color(0xFFFF9800)
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 500),
        label = "progressColor"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp)
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 8.dp,
                color = progressColor
            )
            
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "$current / $target workouts",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (isComplete) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🎉 Goal achieved!",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
```

#### 6. Health Data Integration with Flows

```kotlin
class HealthRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    fun fetchSteps(
        startDate: Instant,
        endDate: Instant
    ): Flow<List<StepData>> = flow {
        // Health Connect integration
        val steps = queryHealthData(
            dataType = HealthDataTypes.Steps,
            startTime = startDate,
            endTime = endDate
        )
        emit(steps)
    }
    
    fun observeSteps(): Flow<StepData> = flow {
        // Real-time step count observation
        emitAll(
            callbackFlow {
                val callback = object : HealthDataObserver {
                    override fun onDataReceived(data: StepData) {
                        trySend(data)
                    }
                }
                
                registerObserver(callback)
                awaitClose { unregisterObserver(callback) }
            }
        )
    }
    
    private suspend fun queryHealthData(
        dataType: String,
        startTime: Instant,
        endTime: Instant
    ): List<StepData> {
        // Implement Health Connect API calls
        return emptyList()
    }
}
```

#### 7. Workout Session State Machine

```kotlin
sealed class WorkoutState {
    data object Idle : WorkoutState()
    data class InProgress(
        val elapsedSeconds: Long,
        val currentExerciseIndex: Int,
        val exercises: List<Exercise>
    ) : WorkoutState()
    data class Paused(
        val elapsedSeconds: Long,
        val currentExerciseIndex: Int,
        val exercises: List<Exercise>
    ) : WorkoutState()
    data class Completed(
        val workout: Workout
    ) : WorkoutState()
    data class Error(val message: String) : WorkoutState()
}

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow<WorkoutState>(WorkoutState.Idle)
    val state: StateFlow<WorkoutState> = _state.asStateFlow()
    
    fun startWorkout(workoutPlan: WorkoutPlan) {
        viewModelScope.launch {
            _state.value = WorkoutState.InProgress(
                elapsedSeconds = 0,
                currentExerciseIndex = 0,
                exercises = workoutPlan.exercises
            )
        }
    }
    
    fun nextExercise() {
        when (val currentState = _state.value) {
            is WorkoutState.InProgress -> {
                val nextIndex = currentState.currentExerciseIndex + 1
                if (nextIndex < currentState.exercises.size) {
                    _state.value = currentState.copy(currentExerciseIndex = nextIndex)
                } else {
                    completeWorkout()
                }
            }
            else -> {}
        }
    }
    
    fun completeWorkout() {
        when (val currentState = _state.value) {
            is WorkoutState.InProgress -> {
                viewModelScope.launch {
                    val workout = Workout(
                        id = UUID.randomUUID().toString(),
                        duration = currentState.elapsedSeconds,
                        completedAt = Clock.System.now(),
                        exercises = currentState.exercises
                    )
                    workoutRepository.saveWorkout(workout)
                    _state.value = WorkoutState.Completed(workout)
                }
            }
            else -> {}
        }
    }
}
```

#### 8. Notification Scheduling with WorkManager

```kotlin
fun scheduleWorkoutReminder(
    context: Context,
    hour: Int = 8,
    minute: Int = 0
) {
    val notificationManager = NotificationManagerCompat.from(context)
    
    // Create notification channel
    createNotificationChannel(context)
    
    // Schedule alarm
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, WorkoutReminderReceiver::class.java).apply {
        putExtra("message", "Time to exercise!")
    }
    
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    
    // Schedule for daily reminder
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        if (before(Calendar.getInstance())) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }
    
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}

class SyncWorkoutWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // Sync workout data
            syncWorkouts()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    private suspend fun syncWorkouts() {
        // Implementation
    }
}
```

### State Management Best Practices

#### remember vs rememberSaveable

```kotlin
@Composable
fun WorkoutForm(
    onSubmit: (Workout) -> Unit
) {
    // State that survives configuration changes
    var workoutName by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    
    // State that resets on recomposition (ephemeral)
    var showNameError by remember { mutableStateOf(false) }
    
    // Derived state
    val isValid by remember(workoutName, duration) {
        derivedStateOf {
            workoutName.isNotBlank() && 
            duration.isNotBlank() && 
            duration.toIntOrNull() != null &&
            duration.toIntOrNull()!! > 0
        }
    }
    
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = workoutName,
            onValueChange = { 
                workoutName = it
                showNameError = false
            },
            label = { Text("Workout Name") },
            isError = showNameError && workoutName.isBlank(),
            supportingText = if (showNameError && workoutName.isBlank()) {
                { Text("Please enter a workout name") }
            } else null
        )
        
        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (seconds)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
        Button(
            onClick = {
                if (workoutName.isBlank()) {
                    showNameError = true
                } else {
                    onSubmit(
                        Workout(
                            name = workoutName,
                            duration = duration.toInt()
                        )
                    )
                }
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Workout")
        }
    }
}
```

### Performance Optimization Techniques

#### Stable Data Classes

```kotlin
@Immutable
data class Exercise(
    val id: String,
    val name: String,
    val duration: Long,
    val instructions: List<String>,
    val category: ExerciseCategory
)

@Stable
enum class ExerciseCategory {
    CARDIO,
    STRENGTH,
    FLEXIBILITY,
    BALANCE
}

@Immutable
data class Workout(
    val id: String,
    val name: String,
    val duration: Long,
    val completedAt: Instant,
    val exercises: List<Exercise>
)
```

#### Efficient List Rendering with Keys

```kotlin
@Composable
fun OptimizedExerciseList(
    exercises: List<Exercise>,
    onExerciseClick: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = exercises,
            key = { it.id }  // Critical for performance
        ) { exercise ->
            ExerciseListItem(
                exercise = exercise,
                onClick = onExerciseClick
            )
        }
    }
}
```

### Recomposition Strategies

#### Minimizing Recomposition Scope

```kotlin
@Composable
fun WorkoutListItem(
    exercise: Exercise,
    onClick: () -> Unit
) {
    // Only recomposes when exercise changes
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Icon(
                    imageVector = if (isExpanded) 
                        Icons.Default.ExpandLess 
                    else 
                        Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
            
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Duration: ${exercise.duration}s",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    exercise.instructions.forEach { instruction ->
                        Text(
                            text = "• $instruction",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
```

### Side Effects Handling

#### LaunchedEffect for Timers and One-time Events

```kotlin
@Composable
fun WorkoutTimer(
    duration: Long,
    onComplete: () -> Unit
) {
    var remainingTime by remember { mutableStateOf(duration) }
    
    // Runs when duration changes
    LaunchedEffect(duration) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime--
        }
        onComplete()
    }
    
    Text(
        text = formatTime(remainingTime),
        style = MaterialTheme.typography.displayLarge
    )
}
```

#### DisposableEffect for Resource Cleanup

```kotlin
@Composable
fun StepCounterScreen(
    viewModel: StepCounterViewModel = hiltViewModel()
) {
    DisposableEffect(Unit) {
        // Register sensor listener
        val sensorManager = viewModel.registerSensorListener()
        
        onDispose {
            // Cleanup when leaving composition
            sensorManager.unregisterListener()
        }
    }
    
    val steps by viewModel.steps.collectAsState()
    Text("Steps: $steps")
}
```

#### produceState for Non-Compose Data

```kotlin
@Composable
fun rememberHealthData(
    startDate: LocalDate,
    endDate: LocalDate
): State<HealthData?> {
    val viewModel: HealthViewModel = hiltViewModel()
    
    return produceState<HealthData?>(initialValue = null, startDate, endDate) {
        value = viewModel.fetchHealthData(startDate, endDate)
    }
}
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17
- Android SDK 24+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/kyva1125/android-fittracker.git
cd android-fittracker
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run on emulator or physical device

### Build

```bash
./gradlew assembleDebug
```

### Run Tests

```bash
./gradlew test
./gradlew connectedAndroidTest
```

---

## 📖 Key Compose Concepts Used

- **Declarative UI** - UI is a function of state
- **Composition** - Describe the UI once, Compose handles updates
- **Recomposition** - Smart recomposition only updates what changed
- **State Hoisting** - State managed at the lowest common parent
- **Side Effects** - Controlled execution of non-compose code
- **Immutable Data** - State objects are immutable for thread safety
- **Stability** - Compose compiler optimizations for performance
- **Custom Canvas** - Drawing custom charts and visualizations
- **Animation API** - Smooth transitions and animations
- **Coroutine Timers** - Efficient time-based operations
- **State Machines** - Complex state management patterns

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👨‍💻 Author

**Nick Ledesma** - Jetpack Compose Expert

- GitHub: [@kyva1125](https://github.com/kyva1125)

---

## 🌟 Showcasing Advanced Compose Expertise

This project demonstrates deep knowledge of Jetpack Compose including:
- Real-time timer implementations with coroutines and LaunchedEffect
- Complex state management and state machines with sealed classes
- Custom data visualization with Canvas API
- Swipe-to-delete gestures with AnimatedVisibility
- Notification scheduling with WorkManager and AlarmManager
- Health data integration with Flow and callbackFlow
- Background task management with WorkManager
- Advanced animation techniques (animateFloatAsState, animateColorAsState)
- Material3 design system integration
- Modern Android architecture (MVVM, Clean Architecture)
- Reactive programming with Kotlin Flow
- Dependency injection with Hilt
- Offline-first data persistence with Room
- Performance optimization with stable types and derived state

Built with ❤️ using Jetpack Compose