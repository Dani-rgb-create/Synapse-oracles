package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val birthDate: String = "",
    val birthPlace: String = "",
    val birthTime: String = "",
    val mainArchetype: String = "El Buscador",
    val currentMood: String = "Centrado",
    val energyPoints: Int = 100,
    val syncNumber: Int = 7
)

@Entity(tableName = "user_diary")
data class UserDiaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val emotion: String,
    val analysis: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "dream_journal")
data class DreamJournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dreamText: String,
    val symbolFound: String,
    val emotionalCharge: String,
    val analysis: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_results")
data class TestResultsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testName: String,
    val score: Int,
    val categoryBreakdown: String, // Comma separated scores or json
    val recommendation: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "meditation_log")
data class MeditationLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val practiceName: String,
    val durationSeconds: Int,
    val soundsUsed: String,
    val volumeMix: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tarot_history")
data class TarotHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val spreadName: String,
    val prompt: String,
    val cardsDrawn: String, // Comma separated ids
    val interpretation: String,
    val deckStyle: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "pendulum_log")
data class PendulumLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answerType: String, // SI, NO, DUDOSO
    val energyIntensity: Float,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "shadow_work_journal")
data class ShadowWorkJournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shadowPrompt: String,
    val userResponse: String,
    val archetypesIdentified: String,
    val integratedStatus: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "numerology_reports")
data class NumerologyReportsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val queryName: String,
    val birthNumber: Int,
    val expressionNumber: Int,
    val soulUrgeNumber: Int,
    val personalityNumber: Int,
    val fullAnalysis: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "astrology_reports")
data class AstrologyReportsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileName: String,
    val planetsPositions: String, // Description of planets in signs/houses
    val housesPositions: String,
    val aspectsList: String,
    val overallInterpretation: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "geometric_patterns")
data class GeometricPatternsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patternName: String,
    val complexity: Float,
    val colorPalette: String,
    val durationFocused: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "akashic_insights")
data class AkashicInsightsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val invocationTopic: String,
    val channeledMessage: String,
    val guidanceKey: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "symbol_journal")
data class SymbolJournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val symbolFound: String,
    val category: String,
    val esotericMeaning: String,
    val personalReflection: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_signals")
data class UserSignalsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val signalDescription: String,
    val physicalCount: Int,
    val contextObserved: String,
    val interpretation: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_mood")
data class DailyMoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val moodScale: Int, // 1 to 10
    val physicalEnergy: Int, // 1 to 10
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "archetype_progress")
data class ArchetypeProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val archetypeTitle: String,
    val shadowType: String,
    val alignmentPercentage: Int,
    val triggerPatterns: String,
    val timestamp: Long = System.currentTimeMillis()
)
