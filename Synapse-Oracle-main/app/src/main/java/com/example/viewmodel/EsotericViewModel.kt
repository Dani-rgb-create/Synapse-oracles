package com.example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.network.SynapseAiService
import com.example.network.CloudSyncService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EsotericViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EsotericRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = EsotericRepository(database.esotericDao())
        
        // Ensure initial profile exists
        viewModelScope.launch {
            repository.userProfile.firstOrNull()?.let { /* already exists */ } ?: run {
                repository.insertUserProfile(
                    UserProfileEntity(
                        name = "Buscador de la Esencia",
                        birthDate = "1994-05-20",
                        birthPlace = "Madrid, España",
                        birthTime = "11:11",
                        mainArchetype = "El Alquimista",
                        currentMood = "Contemplativo",
                        energyPoints = 100,
                        syncNumber = 7
                    )
                )
            }
        }
    }

    // --- State Flows from Database ---
    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val diaryEntries: StateFlow<List<UserDiaryEntity>> = repository.allDiaryEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dreamEntries: StateFlow<List<DreamJournalEntity>> = repository.allDreamEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val testResults: StateFlow<List<TestResultsEntity>> = repository.allTestResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val meditationLogs: StateFlow<List<MeditationLogEntity>> = repository.allMeditationLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tarotHistories: StateFlow<List<TarotHistoryEntity>> = repository.allTarotHistories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendulumLogs: StateFlow<List<PendulumLogEntity>> = repository.allPendulumLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shadowLogs: StateFlow<List<ShadowWorkJournalEntity>> = repository.allShadowWorkLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val numerologyReports: StateFlow<List<NumerologyReportsEntity>> = repository.allNumerologyReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val astrologyReports: StateFlow<List<AstrologyReportsEntity>> = repository.allAstrologyReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val geometricPatterns: StateFlow<List<GeometricPatternsEntity>> = repository.allGeometricPatterns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val akashicInsights: StateFlow<List<AkashicInsightsEntity>> = repository.allAkashicInsights
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val symbolEntries: StateFlow<List<SymbolJournalEntity>> = repository.allSymbolJournalEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userSignals: StateFlow<List<UserSignalsEntity>> = repository.allUserSignals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyMoods: StateFlow<List<DailyMoodEntity>> = repository.allDailyMoodLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val archetypeProgress: StateFlow<List<ArchetypeProgressEntity>> = repository.allArchetypeProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- State of Synapse Chat Screen ---
    private val _chatUiState = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("¡Saludos, alma despierta! Soy el oráculo de Synapse. Estoy aquí para acompañarte en la revelación de tu geometría más íntima y tus raíces celestiales. ¿Qué verdad buscas descubrir hoy?", true)
    ))
    val chatUiState: StateFlow<List<ChatMessage>> = _chatUiState.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // --- State of Active Screen Context/Operations ---
    private val _currentActiveSynapseContext = MutableStateFlow("")
    val currentActiveSynapseContext: StateFlow<String> = _currentActiveSynapseContext.asStateFlow()

    // --- Operations / Actions Database & AI ---

    fun updateProfile(name: String, birthDate: String, birthPlace: String, birthTime: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity(name = name)
            repository.insertUserProfile(
                current.copy(
                    name = name,
                    birthDate = birthDate,
                    birthPlace = birthPlace,
                    birthTime = birthTime
                )
            )
            updateSynapseContextFromProfile()
        }
    }

    fun updateUserMood(mood: String, moodScore: Int, energyScale: Int) {
        viewModelScope.launch {
            val currentPr = userProfile.value
            if (currentPr != null) {
                repository.insertUserProfile(currentPr.copy(currentMood = mood))
            }
            repository.insertDailyMood(
                DailyMoodEntity(
                    moodScale = moodScore,
                    physicalEnergy = energyScale,
                    notes = "Estado registrado: $mood"
                )
            )
            updateSynapseContextFromProfile()
        }
    }

    private fun updateSynapseContextFromProfile() {
        val currentPr = userProfile.value ?: return
        _currentActiveSynapseContext.value = """
            Nombre: ${currentPr.name}
            Ritmo Emocional: ${currentPr.currentMood}
            Frecuencia de Sincronicidad: ${currentPr.syncNumber}
            Arquetipo Principal: ${currentPr.mainArchetype}
            Fecha de Nacimiento: ${currentPr.birthDate} - Lugar: ${currentPr.birthPlace} a las ${currentPr.birthTime}
        """.trimIndent()
    }

    // 1. Synapse Chat Message Sender
    fun sendChatMessage(text: String) {
        if (text.trim().isEmpty()) return
        
        val userMsg = ChatMessage(text, isSynapse = false)
        _chatUiState.update { it + userMsg }
        _isChatLoading.value = true

        viewModelScope.launch {
            // Compile context summary from database stats
            val lastDiary = diaryEntries.value.firstOrNull()?.content ?: "Sin diarios registrados"
            val lastDream = dreamEntries.value.firstOrNull()?.dreamText ?: "Sin sueños registrados"
            val rawContext = """
                ${_currentActiveSynapseContext.value}
                Última reflexión del diario: $lastDiary
                Último sueño registrado: $lastDream
            """.trimIndent()

            val aiAnswer = SynapseAiService.generateSynapseResponse(text, rawContext)
            _chatUiState.update { it + ChatMessage(aiAnswer, isSynapse = true) }
            _isChatLoading.value = false
            
            // Add progress
            incrementEnergyPoints(5)
        }
    }

    fun runChatMessageAction(text: String, actionType: String) {
        // Formulated response enhancement buttons: "Profundizar", "Desafíame", "Simplifica"
        val prompt = when (actionType) {
            "DEEPEN" -> "Profundiza esotéricamente en tu respuesta anterior, conéctala con el principio de Vibración y sugiéreme un ritual de integración: $text"
            "CHALLENGE" -> "Retame con una pregunta de Sombra o un ejercicio hermético incómodo que me haga confrontar este patrón: $text"
            "SIMPLIFY" -> "Explícame esto de forma muy sencilla, comprensible y cotidiana sin perder la profundidad mágica: $text"
            else -> text
        }
        sendChatMessage(prompt)
    }

    // 2. Tarot Actions
    fun saveTarotReading(spread: String, prompt: String, cards: List<String>, interpretation: String, style: String) {
        viewModelScope.launch {
            repository.insertTarotHistory(
                TarotHistoryEntity(
                    spreadName = spread,
                    prompt = prompt,
                    cardsDrawn = cards.joinToString(","),
                    interpretation = interpretation,
                    deckStyle = style
                )
            )
            incrementEnergyPoints(15)
        }
    }

    // 3. Meditation Logging
    fun logMeditation(practice: String, seconds: Int, sounds: String, volumes: String) {
        viewModelScope.launch {
            repository.insertMeditationLog(
                MeditationLogEntity(
                    practiceName = practice,
                    durationSeconds = seconds,
                    soundsUsed = sounds,
                    volumeMix = volumes
                )
            )
            incrementEnergyPoints(10)
        }
    }

    // 4. Save Diary Entry
    fun saveDiaryEntry(content: String, emotion: String, runAiAnalysis: Boolean = true) {
        viewModelScope.launch {
            var analysisText = "Tu diario ha sido guardado en la memoria silenciosa de la Tierra."
            if (runAiAnalysis) {
                analysisText = SynapseAiService.generateSynapseResponse(
                    "He escrito una entrada en mi diario sobre: '$content'. Me siento '$emotion'. Dame una breve guía esotérica y una afirmación hermética.",
                    _currentActiveSynapseContext.value
                )
            }
            repository.insertDiaryEntry(
                UserDiaryEntity(
                    content = content,
                    emotion = emotion,
                    analysis = analysisText
                )
            )
            incrementEnergyPoints(8)
        }
    }

    // 5. Save Dream Entry
    fun saveDream(dreamText: String, emotionalCharge: String, symbols: String) {
        viewModelScope.launch {
            val analysisText = SynapseAiService.generateSynapseResponse(
                "He soñado esto: '$dreamText'. Carga emocional: $emotionalCharge. Símbolos identificados: '$symbols'. Interpreta estos símbolos en base a mis sueños del pasado, descifra lo oculto.",
                _currentActiveSynapseContext.value
            )
            repository.insertDreamEntry(
                DreamJournalEntity(
                    dreamText = dreamText,
                    symbolFound = symbols,
                    emotionalCharge = emotionalCharge,
                    analysis = analysisText
                )
            )
            incrementEnergyPoints(12)
        }
    }

    // 6. Test Results
    fun saveTestResult(testName: String, score: Int, categoryBreakdown: String, recommendation: String) {
        viewModelScope.launch {
            repository.insertTestResult(
                TestResultsEntity(
                    testName = testName,
                    score = score,
                    categoryBreakdown = categoryBreakdown,
                    recommendation = recommendation
                )
            )
            // Trigger archetype advancement
            if (testName.contains("Jung") || testName.contains("Eneagrama")) {
                val currentPr = userProfile.value
                val newArch = when {
                    score > 80 -> "El Sabio Gnostico"
                    score > 60 -> "El Mago Solitario"
                    score > 40 -> "El Alquimista Fiel"
                    else -> "El Buscador del Alba"
                }
                if (currentPr != null) {
                    repository.insertUserProfile(currentPr.copy(mainArchetype = newArch))
                }
                repository.insertArchetypeProgress(
                    ArchetypeProgressEntity(
                        archetypeTitle = newArch,
                        shadowType = "El Guardián del Umbral",
                        alignmentPercentage = score,
                        triggerPatterns = "Test $testName realizado"
                    )
                )
            }
            incrementEnergyPoints(20)
        }
    }

    // 7. Save Pendulum Query
    fun logPendulumQuery(question: String, answer: String, intensity: Float) {
        viewModelScope.launch {
            repository.insertPendulumLog(
                PendulumLogEntity(
                    question = question,
                    answerType = answer,
                    energyIntensity = intensity
                )
            )
            incrementEnergyPoints(5)
        }
    }

    // 8. Shadow Integration
    fun logShadowWork(prompt: String, response: String, archetypes: String, integrated: Boolean) {
        viewModelScope.launch {
            repository.insertShadowWorkLog(
                ShadowWorkJournalEntity(
                    shadowPrompt = prompt,
                    userResponse = response,
                    archetypesIdentified = archetypes,
                    integratedStatus = integrated
                )
            )
            incrementEnergyPoints(15)
        }
    }

    // 9. Save Universe Signal
    fun logUniverseSignal(desc: String, count: Int, context: String, interpreterResponse: String) {
        viewModelScope.launch {
            repository.insertUserSignal(
                UserSignalsEntity(
                    signalDescription = desc,
                    physicalCount = count,
                    contextObserved = context,
                    interpretation = interpreterResponse
                )
            )
            repository.insertSymbolJournalEntry(
                SymbolJournalEntity(
                    symbolFound = desc,
                    category = "Sincronicidades",
                    esotericMeaning = interpreterResponse,
                    personalReflection = context
                )
            )
            incrementEnergyPoints(10)
        }
    }

    // Helper to increment points & update mística sync numbers
    private suspend fun incrementEnergyPoints(amount: Int) {
        val currentPr = userProfile.value ?: return
        val newPoints = currentPr.energyPoints + amount
        val newSync = (currentPr.syncNumber + amount / 5) % 9 + 1
        repository.insertUserProfile(
            currentPr.copy(
                energyPoints = newPoints,
                syncNumber = newSync
            )
        )
    }

    // --- Cloud Synchronization State and Actions ---
    private val _syncing = MutableStateFlow(false)
    val syncing: StateFlow<Boolean> = _syncing.asStateFlow()

    private val _syncSuccess = MutableSharedFlow<Boolean>()
    val syncSuccess: SharedFlow<Boolean> = _syncSuccess.asSharedFlow()

    private val moshiInstance = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun createBackupJson(): String? {
        return try {
            val payload = BackupPayload(
                userProfile = userProfile.value,
                diaryEntries = diaryEntries.value,
                dreamEntries = dreamEntries.value,
                testResults = testResults.value,
                meditationLogs = meditationLogs.value,
                tarotHistories = tarotHistories.value,
                pendulumLogs = pendulumLogs.value,
                shadowLogs = shadowLogs.value,
                numerologyReports = numerologyReports.value,
                astrologyReports = astrologyReports.value,
                geometricPatterns = geometricPatterns.value,
                akashicInsights = akashicInsights.value,
                symbolEntries = symbolEntries.value,
                userSignals = userSignals.value,
                dailyMoods = dailyMoods.value,
                archetypeProgress = archetypeProgress.value
            )
            moshiInstance.adapter(BackupPayload::class.java).toJson(payload)
        } catch (e: Exception) {
            Log.e("EsotericViewModel", "Failed to compile backup JSON", e)
            null
        }
    }

    fun restoreBackupJson(jsonStr: String): Boolean {
        return try {
            val payload = moshiInstance.adapter(BackupPayload::class.java).fromJson(jsonStr) ?: return false
            viewModelScope.launch {
                repository.restoreDatabaseAll(
                    profile = payload.userProfile,
                    diaries = payload.diaryEntries,
                    dreams = payload.dreamEntries,
                    tests = payload.testResults,
                    meditations = payload.meditationLogs,
                    tarot = payload.tarotHistories,
                    pendulums = payload.pendulumLogs,
                    shadowWorks = payload.shadowLogs,
                    numerologies = payload.numerologyReports,
                    astrologies = payload.astrologyReports,
                    patterns = payload.geometricPatterns,
                    akashics = payload.akashicInsights,
                    symbols = payload.symbolEntries,
                    signals = payload.userSignals,
                    moods = payload.dailyMoods,
                    progresses = payload.archetypeProgress
                )
            }
            true
        } catch (e: Exception) {
            Log.e("EsotericViewModel", "Failed to parse and restore backup JSON", e)
            false
        }
    }

    fun uploadToCloud(projectId: String, syncKey: String) {
        viewModelScope.launch {
            _syncing.value = true
            val json = createBackupJson()
            if (json != null) {
                val ok = CloudSyncService.uploadBackup(getApplication(), projectId, syncKey, json)
                _syncSuccess.emit(ok)
            } else {
                _syncSuccess.emit(false)
            }
            _syncing.value = false
        }
    }

    fun downloadFromCloud(projectId: String, syncKey: String) {
        viewModelScope.launch {
            _syncing.value = true
            val json = CloudSyncService.downloadBackup(getApplication(), projectId, syncKey)
            if (json != null) {
                val ok = restoreBackupJson(json)
                _syncSuccess.emit(ok)
            } else {
                _syncSuccess.emit(false)
            }
            _syncing.value = false
        }
    }

    // --- Archetype Tracking State and Actions ---
    private val _archetypeAnalysisResult = MutableStateFlow<String>("")
    val archetypeAnalysisResult: StateFlow<String> = _archetypeAnalysisResult.asStateFlow()

    private val _isAnalyzingArchetypes = MutableStateFlow(false)
    val isAnalyzingArchetypes: StateFlow<Boolean> = _isAnalyzingArchetypes.asStateFlow()

    fun runArchetypeAnalysisWithAi() {
        viewModelScope.launch {
            _isAnalyzingArchetypes.value = true
            
            // Compile diaries, dreams, and test entries to analyze
            val diaries = diaryEntries.value.joinToString("\n") { "Entrada de diario (${it.emotion}): ${it.content}" }
            val dreams = dreamEntries.value.joinToString("\n") { "Estudio de sueño [símbolos: ${it.symbolFound}]: ${it.dreamText}" }
            val tests = testResults.value.joinToString("\n") { "Test [${it.testName}] puntuación: ${it.score}" }
            
            val prompt = """
                Analiza en profundidad y de forma esotérica gnóstica mis registros actuales para componer mi perfil de Arquetipos Dominantes:
                
                **MIS DIARIOS**:
                ${diaries.ifEmpty { "Sin diarios registrados." }}
                
                **MIS SUEÑOS EN EL ONIROMANTEION**:
                ${dreams.ifEmpty { "Sin sueños registrados." }}
                
                **RESULTADOS DE MIS TESTS**:
                ${tests.ifEmpty { "Sin pruebas realizadas." }}
                
                Compara estos misteriosos registros con el Tarot Junguiano y las corrientes esotéricas de SYNSPSE STUDIO.
                Genera tu respuesta en español, muy bien formateada en Markdown, cubriendo:
                1. **El Ego y El Héroe (Dominancia Activa)**: Describe cómo busca el conocimiento espiritual real.
                2. **La Sombra (Fuerzas Ocultas)**: Analiza mis miedos o saboteadores inconscientes reflejados en mis registros.
                3. **Anima / Animus (Fuerzas de Equilibrio)**: Explica el balance sagrado de mis energías receptivas y dinámicas.
                4. **Bespoke Ritual Integrador**: Brinda dos rituales esotéricos herméticos o preguntas de Sombra personalizadas basadas en mis debilidades o patrones actuales detectados.
            """.trimIndent()
            
            val result = SynapseAiService.generateSynapseResponse(prompt)
            _archetypeAnalysisResult.value = result
            _isAnalyzingArchetypes.value = false
            
            // Save an entry to progress
            repository.insertArchetypeProgress(
                ArchetypeProgressEntity(
                    archetypeTitle = "Análisis Sagrado Integrado",
                    shadowType = "Fuerzas Junguianas Analizadas",
                    alignmentPercentage = 95,
                    triggerPatterns = "Sintonización manual completada"
                )
            )
        }
    }
}

// Struct representing a beautiful Chat Room bubble trace
data class ChatMessage(
    val text: String,
    val isSynapse: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// Wrapper payload representing complete exportable database state for Cloud Backup
data class BackupPayload(
    val userProfile: UserProfileEntity?,
    val diaryEntries: List<UserDiaryEntity> = emptyList(),
    val dreamEntries: List<DreamJournalEntity> = emptyList(),
    val testResults: List<TestResultsEntity> = emptyList(),
    val meditationLogs: List<MeditationLogEntity> = emptyList(),
    val tarotHistories: List<TarotHistoryEntity> = emptyList(),
    val pendulumLogs: List<PendulumLogEntity> = emptyList(),
    val shadowLogs: List<ShadowWorkJournalEntity> = emptyList(),
    val numerologyReports: List<NumerologyReportsEntity> = emptyList(),
    val astrologyReports: List<AstrologyReportsEntity> = emptyList(),
    val geometricPatterns: List<GeometricPatternsEntity> = emptyList(),
    val akashicInsights: List<AkashicInsightsEntity> = emptyList(),
    val symbolEntries: List<SymbolJournalEntity> = emptyList(),
    val userSignals: List<UserSignalsEntity> = emptyList(),
    val dailyMoods: List<DailyMoodEntity> = emptyList(),
    val archetypeProgress: List<ArchetypeProgressEntity> = emptyList()
)
