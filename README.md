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

FitTracker is a feature-rich fitness application demonstrating modern Android development with Jetpack Compose. Built as part of my migration from Flutter to native Android development, this project showcases complex state management, real-time timers, health data integration, and custom UI components for workout tracking.

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

| Category | Technology | Flutter Analogy |
|----------|------------|-----------------|
| **Language** | Kotlin 1.9.0 | Dart |
| **UI Framework** | Jetpack Compose (No XML) | Flutter Widgets |
| **Architecture** | MVVM + Repository Pattern | BLoC / Provider |
| **Dependency Injection** | Hilt | get_it / Provider |
| **Database** | Room (Local SQLite) | sqflite / Drift |
| **Async** | Kotlinx Coroutines + Flows | Future / Stream |
| **State Management** | State / remember / ViewModel | ChangeNotifier / setState |
| **Timers** | kotlinx.coroutines.delay | Timer / Stream.periodic |
| **Health Data** | HealthRepository | health package / integration |
| **Date/Time** | kotlinx.datetime | intl |
| **Notifications** | NotificationManager | flutter_local_notifications |
| **Min SDK** | 24 (Android 7.0+) | iOS 11+, Android 5.0+ |
| **Target SDK** | 35 | Latest iOS/Android |

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

**Flutter Parallel:** Same layered architecture as BLoC pattern - UI → Cubits/BLoCs → Repositories → Data Sources

---

## 🔄 Flutter to Jetpack Compose: Key Concepts

### Real-time Timer with Coroutines

**Flutter (Timer.periodic):**
```dart
class WorkoutTimer extends StatefulWidget {
  @override
  _WorkoutTimerState createState() => _WorkoutTimerState();
}

class _WorkoutTimerState extends State<WorkoutTimer> {
  Timer? _timer;
  int _seconds = 0;
  bool _isRunning = false;

  void _startTimer() {
    _timer = Timer.periodic(Duration(seconds: 1), (timer) {
      setState(() {
        _seconds++;
      });
    });
    setState(() {
      _isRunning = true;
    });
  }

  void _pauseTimer() {
    _timer?.cancel();
    setState(() {
      _isRunning = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text('${_seconds ~/ 60}:${(_seconds % 60).toString().padLeft(2, '0')}'),
        ElevatedButton(
          onPressed: _isRunning ? _pauseTimer : _startTimer,
          child: Text(_isRunning ? 'Pause' : 'Start'),
        ),
      ],
    );
  }
}
```

**Jetpack Compose (Coroutines + LaunchedEffect):**
```kotlin
@Composable
fun WorkoutTimer(
    viewModel: WorkoutTimerViewModel = hiltViewModel()
) {
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = formatTime(elapsedTime),
            style = MaterialTheme.typography.displayLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { 
                    if (isRunning) viewModel.pause() else viewModel.start()
                }
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }
            
            Button(
                onClick = { viewModel.reset() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Reset")
            }
        }
    }
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "${minutes}:${secs.toString().padStart(2, '0')}"
}
```

### ViewModel with Timer State

**Flutter (BLoC with Timer):**
```dart
class WorkoutBloc extends Bloc<WorkoutEvent, WorkoutState> {
  Timer? _timer;
  int _elapsedSeconds = 0;

  WorkoutBloc() : super(WorkoutInitial()) {
    on<StartWorkout>(_onStartWorkout);
    on<PauseWorkout>(_onPauseWorkout);
    on<ResetWorkout>(_onResetWorkout);
  }

  Future<void> _onStartWorkout(
    StartWorkout event,
    Emitter<WorkoutState> emit
  ) async {
    _timer = Timer.periodic(Duration(seconds: 1), (timer) {
      _elapsedSeconds++;
      emit(WorkoutInProgress(_elapsedSeconds));
    });
  }

  Future<void> _onPauseWorkout(
    PauseWorkout event,
    Emitter<WorkoutState> emit
  ) async {
    _timer?.cancel();
    emit(WorkoutPaused(_elapsedSeconds));
  }

  @override
  Future<void> close() {
    _timer?.cancel();
    return super.close();
  }
}
```

**Jetpack Compose (ViewModel + Coroutine Scope):**
```kotlin
@HiltViewModel
class WorkoutTimerViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()
    
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()
    
    private var timerJob: Job? = null
    
    fun start() {
        if (timerJob == null) {
            _isRunning.value = true
            timerJob = viewModelScope.launch {
                while (isActive) {
                    delay(1000L)
                    _elapsedTime.value++
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
    }
    
    fun completeWorkout() {
        viewModelScope.launch {
            workoutRepository.saveWorkout(
                Workout(
                    id = UUID.randomUUID().toString(),
                    duration = _elapsedTime.value,
                    completedAt = Clock.System.now()
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

### Exercise List with Swipe Actions

**Flutter (Dismissible):**
```dart
class ExerciseList extends StatelessWidget {
  final List<Exercise> exercises;
  final Function(Exercise) onRemove;
  
  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: exercises.length,
      itemBuilder: (context, index) {
        return Dismissible(
          key: Key(exercises[index].id),
          background: Container(color: Colors.red),
          onDismissed: (direction) {
            onRemove(exercises[index]);
          },
          child: ExerciseTile(exercises[index]),
        );
      },
    );
  }
}
```

**Jetpack Compose (SwipeToDismiss):**
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
                exit = shrinkVertically() + fadeOut()
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
                                tint = Color.White
                            )
                        }
                    },
                    dismissContent = {
                        ExerciseTile(exercise = exercise)
                    }
                )
            }
        }
    }
}
```

### Progress Charts

**Flutter (fl_chart):**
```dart
class ProgressChart extends StatelessWidget {
  final List<Workout> workouts;
  
  @override
  Widget build(BuildContext context) {
    final data = _groupWorkoutsByWeek(workouts);
    
    return LineChart(
      LineChartData(
        lineBarsData: [
          LineChartBarData(
            spots: data.map((point) {
              return FlSpot(point.key.toDouble(), point.value.toDouble());
            }).toList(),
            isCurved: true,
            color: Colors.blue,
          ),
        ],
      ),
    );
  }
}
```

**Jetpack Compose (Custom Canvas):**
```kotlin
@Composable
fun ProgressChart(
    workouts: List<Workout>,
    modifier: Modifier = Modifier
) {
    val weeklyData = remember(workouts) {
        workouts.groupByWeek()
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
        
        // Draw grid lines
        drawGrid(padding, width, height)
        
        // Draw chart line
        val path = Path().apply {
            weeklyData.entries.forEachIndexed { index, entry ->
                val x = padding + (index.toFloat() / (weeklyData.size - 1)) * width
                val y = size.height - padding - (entry.value / weeklyData.maxOrNull()!!) * height
                
                if (index == 0) moveTo(x, y) else lineTo(x, y)
                
                // Draw point
                drawCircle(
                    color = MaterialTheme.colorScheme.primary,
                    radius = 6.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
        
        drawPath(
            path = path,
            color = MaterialTheme.colorScheme.primary,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}
```

### Goal Progress Indicator

**Flutter (CircularProgressIndicator):**
```dart
class GoalProgress extends StatelessWidget {
  final double current;
  final double target;
  
  @override
  Widget build(BuildContext context) {
    final progress = current / target;
    
    return Column(
      children: [
        CircularProgressIndicator(
          value: progress,
          backgroundColor: Colors.grey[200],
        ),
        SizedBox(height: 8),
        Text('${(progress * 100).toInt()}%'),
      ],
    );
  }
}
```

**Jetpack Compose (Circular Progress):**
```kotlin
@Composable
fun GoalProgress(
    current: Double,
    target: Double,
    modifier: Modifier = Modifier
) {
    val progress by remember(current, target) {
        mutableStateOf((current / target).coerceIn(0.0, 1.0))
    }
    
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
                color = if (progress >= 1.0) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.secondary
            )
            
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "$current / $target workouts",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

### Health Data Integration

**Flutter (health package):**
```dart
class HealthService {
  final Health health = Health();

  Future<List<HealthDataPoint>> fetchSteps() async {
    final now = DateTime.now();
    final types = [HealthDataType.STEPS];
    
    final permission = await health.requestAuthorization(types);
    
    if (permission) {
      return await health.getHealthDataFromTypes(
        now.subtract(Duration(days: 7)),
        now,
        types,
      );
    }
    return [];
  }
}
```

**Jetpack Compose (HealthRepository):**
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
    
    private suspend fun queryHealthData(
        dataType: String,
        startTime: Instant,
        endTime: Instant
    ): List<StepData> {
        // Implement Health Connect API calls
        // This uses Android's Health Connect SDK
        return emptyList() // Placeholder
    }
    
    fun observeSteps(): Flow<StepData> = flow {
        // Real-time step count observation
    }
}
```

### Notification Scheduling

**Flutter (flutter_local_notifications):**
```dart
class NotificationService {
  final FlutterLocalNotificationsPlugin notifications = 
      FlutterLocalNotificationsPlugin();

  Future<void> initialize() async {
    const AndroidInitializationSettings androidSettings = 
        AndroidInitializationSettings('@mipmap/ic_launcher');
    
    const InitializationSettings settings = 
        InitializationSettings(android: androidSettings);
    
    await notifications.initialize(settings);
  }

  Future<void> scheduleWorkoutReminder() async {
    await notifications.zonedSchedule(
      0,
      'Workout Reminder',
      'Time to exercise!',
      nextInstanceOfMondayAt8am(),
      NotificationDetails(...),
      uiLocalNotificationDateInterpretation:
          UILocalNotificationDateInterpretation.absoluteTime,
      matchDateTimeComponents: DateTimeComponents.dayOfWeekAndTime,
    );
  }
}
```

**Jetpack Compose (NotificationManager):**
```kotlin
@Composable
fun rememberNotificationManager(): NotificationManager {
    val context = LocalContext.current
    return remember(context) {
        NotificationManagerCompat.from(context)
    }
}

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
    
    // Schedule for next Monday at specified time
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        if (before(Calendar.getInstance())) {
            add(Calendar.WEEK_OF_YEAR, 1)
        }
    }
    
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY * 7,
        pendingIntent
    )
}
```

### Workout Session State Machine

**Flutter (BLoC State):**
```dart
abstract class WorkoutState {}

class WorkoutInitial extends WorkoutState {}
class WorkoutInProgress extends WorkoutState {
  final int elapsedSeconds;
  WorkoutInProgress(this.elapsedSeconds);
}
class WorkoutPaused extends WorkoutState {
  final int elapsedSeconds;
  WorkoutPaused(this.elapsedSeconds);
}
class WorkoutCompleted extends WorkoutState {
  final Workout workout;
  WorkoutCompleted(this.workout);
}
```

**Jetpack Compose (Sealed Class + Flow):**
```kotlin
sealed class WorkoutState {
    data object Idle : WorkoutState()
    data class InProgress(
        val elapsedSeconds: Long,
        val currentExerciseIndex: Int
    ) : WorkoutState()
    data class Paused(
        val elapsedSeconds: Long,
        val currentExerciseIndex: Int
    ) : WorkoutState()
    data class Completed(
        val workout: Workout
    ) : WorkoutState()
}

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {
    
    private val _state = MutableStateFlow<WorkoutState>(WorkoutState.Idle)
    val state: StateFlow<WorkoutState> = _state.asStateFlow()
    
    fun startWorkout(workoutPlan: WorkoutPlan) {
        viewModelScope.launch {
            _state.value = WorkoutState.InProgress(0, 0)
        }
    }
    
    fun nextExercise() {
        when (val currentState = _state.value) {
            is WorkoutState.InProgress -> {
                val nextIndex = currentState.currentExerciseIndex + 1
                if (nextIndex < workoutPlan.exercises.size) {
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
                        completedAt = Clock.System.now()
                    )
                    workoutRepository.saveWorkout(workout)
                    _state.value = WorkoutState.Completed(workout)
                    sendCompletionNotification(workout)
                }
            }
            else -> {}
        }
    }
}
```

---

## 🚀 Migrando de Flutter

### Conceptos Equivalentes

| Flutter | Jetpack Compose | Notes |
|---------|-----------------|-------|
| `Timer.periodic` | `kotlinx.coroutines.delay` | Coroutine-based timers |
| `Stream.periodic` | `flow + delay` | Reactive time-based streams |
| `Dismissible` | `SwipeToDismiss` | Swipe-to-dismiss gestures |
| `CircularProgressIndicator` | `CircularProgressIndicator` | Same API, Compose version |
| `fl_chart` | Custom `Canvas` | More control, built-in |
| `health` package | Health Connect SDK | Native Android health API |
| `flutter_local_notifications` | `NotificationManager` | Better integration |
| `background_fetch` | `WorkManager` | Reliable background tasks |
| `shared_preferences` | `DataStore` | Type-safe, coroutine-based |
| `path_provider` | `context.filesDir` | Direct API access |

### Tips de Migración

#### 1. **Timer Management with Coroutines**
Use coroutines for timers instead of Timer class:

```kotlin
@Composable
fun CountdownTimer(
    duration: Long,
    onComplete: () -> Unit
) {
    var remainingTime by remember { mutableStateOf(duration) }
    
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

#### 2. **Swipe Actions**
SwipeToDismiss gives you more control than Dismissible:

```kotlin
SwipeToDismiss(
    state = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    // Swipe right action
                    true
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    // Swipe left action
                    true
                }
                else -> false
            }
        }
    ),
    background = {
        // Background UI
    },
    dismissContent = {
        // Main content
    }
)
```

#### 3. **Health Connect Integration**
Use Android's Health Connect SDK for health data:

```kotlin
suspend fun readSteps(
    healthConnectClient: HealthConnectClient,
    startTime: Instant,
    endTime: Instant
): List<StepsRecord> {
    val response = healthConnectClient.readRecords(
        ReadRecordsRequest(
            StepsRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
        )
    )
    return response.records
}
```

#### 4. **WorkManager for Background Tasks**
Use WorkManager for reliable background work:

```kotlin
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
}

// Schedule periodic sync
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()

val syncRequest = PeriodicWorkRequestBuilder<SyncWorkoutWorker>(
    1, TimeUnit.DAYS
)
    .setConstraints(constraints)
    .build()

WorkManager.getInstance(context).enqueue(syncRequest)
```

#### 5. **DataStore for Preferences**
Replace SharedPreferences with DataStore:

```kotlin
object PreferencesKeys {
    val WORKOUT_REMINDER_TIME = stringPreferencesKey("workout_reminder_time")
    val DAILY_GOAL = intPreferencesKey("daily_goal")
}

class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("preferences")
    
    val workoutReminderTime: Flow<String> = context.dataStore.data
        .map { it[PreferencesKeys.WORKOUT_REMINDER_TIME] ?: "08:00" }
    
    suspend fun setReminderTime(time: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WORKOUT_REMINDER_TIME] = time
        }
    }
}
```

#### 6. **Notification Channels**
Create notification channels for better control:

```kotlin
fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "workout_reminders",
        "Workout Reminders",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Notifications for workout reminders"
        enableLights(true)
        enableVibration(true)
    }
    
    val manager = NotificationManagerCompat.from(context)
    manager.createNotificationChannel(channel)
}
```

#### 7. **Custom Chart Drawing**
Use Canvas for custom visualizations:

```kotlin
@Composable
fun WeeklyProgressChart(
    data: List<Int>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.height(200.dp)) {
        val barWidth = size.width / (data.size * 2)
        val maxValue = data.maxOrNull() ?: 1
        
        data.forEachIndexed { index, value ->
            val barHeight = (value.toFloat() / maxValue) * size.height
            val x = index * barWidth * 2 + barWidth / 2
            val y = size.height - barHeight
            
            drawRoundRect(
                color = MaterialTheme.colorScheme.primary,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
        }
    }
}
```

#### 8. **State Hoisting for Timer**
Hoist timer state for better control:

```kotlin
@Composable
fun rememberWorkoutTimerState(
    onComplete: () -> Unit
): WorkoutTimerState {
    return remember { WorkoutTimerState(onComplete) }
}

class WorkoutTimerState(
    private val onComplete: () -> Unit
) {
    var elapsedSeconds by mutableLongStateOf(0L)
        private set
    
    var isRunning by mutableStateOf(false)
        private set
    
    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    fun start() {
        if (!isRunning) {
            isRunning = true
            scope.launch {
                while (isActive) {
                    delay(1000L)
                    elapsedSeconds++
                }
            }
        }
    }
    
    fun pause() {
        isRunning = false
        scope.coroutineContext.cancelChildren()
    }
    
    fun reset() {
        pause()
        elapsedSeconds = 0L
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }
}
```

### Common Mistakes to Avoid

1. **❌ Don't forget to cancel coroutines**
   ```kotlin
   // Bad - memory leak
   fun start() {
       viewModelScope.launch {
           while (true) {
               delay(1000)
               update()
           }
       }
   }
   
   // Good - proper cancellation
   private var timerJob: Job? = null
   
   fun start() {
       timerJob = viewModelScope.launch {
           while (isActive) {
               delay(1000)
               update()
           }
       }
   }
   
   override fun onCleared() {
       timerJob?.cancel()
   }
   ```

2. **❌ Don't use blocking delay on main thread**
   ```kotlin
   // Bad - blocks UI
   fun waitFiveSeconds() {
       Thread.sleep(5000)
   }
   
   // Good - suspending delay
   suspend fun waitFiveSeconds() {
       delay(5000L)
   }
   ```

3. **❌ Don't ignore notification permissions**
   ```kotlin
   // Always check and request notification permissions
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
       requestPermission(Manifest.permission.POST_NOTIFICATIONS)
   }
   ```

4. **❌ Don't create multiple notification channels**
   ```kotlin
   // Only create channel once (e.g., in Application class)
   if (manager.getNotificationChannel(channelId) == null) {
       manager.createNotificationChannel(channel)
   }
   ```

---

## 📦 Installation

```bash
git clone https://github.com/kyva1125/android-fittracker.git
cd android-fittracker
./gradlew assembleDebug
```

### Requirements

- Android Studio Hedgehog or later
- JDK 17
- Android SDK 35
- Gradle 8.0+

---

## 🔑 Environment Variables

No external API keys required - fully offline capable.

---

## 🧪 Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# UI tests
./gradlew connectedDebugAndroidTest
```

---

## 📸 Screenshots

> **Coming Soon** - Screenshots demonstrating workout timer and progress tracking

---

## 🎓 Learning Resources

- [Jetpack Compose Basics](https://developer.android.com/courses/jetpack-compose/course)
- [Compose for Flutter Developers](https://developer.android.com/jetpack/compose/mental-model)
- [State in Compose](https://developer.android.com/jetpack/compose/state)
- [Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Health Connect](https://developer.android.com/health-and-fitness)

---

## 📄 License

MIT License - see [LICENSE](LICENSE) for details

---

## 👤 Author

**Nick Ledesma**  
- 🐙 [GitHub](https://github.com/kyva1125)  
- 📧 Contact: [GitHub Issues](https://github.com/kyva1125/android-fittracker/issues)

---

## 🙏 Acknowledgments

Built with modern Android best practices, transitioning from Flutter to Jetpack Compose. Demonstrates expertise in:
- Real-time timer implementations with coroutines
- Complex state management and state machines
- Health data integration (Health Connect)
- Custom data visualization
- Notification scheduling
- Background task management (WorkManager)
- Clean Architecture principles
- Offline-first data strategies

---

<div align="center">

**Built with ❤️ using Kotlin & Jetpack Compose**

</div>