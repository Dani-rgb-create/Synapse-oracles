package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EsotericDao {

    // 1. User Profile
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfileEntity)

    // 2. User Diary
    @Query("SELECT * FROM user_diary ORDER BY timestamp DESC")
    fun getAllDiaryEntries(): Flow<List<UserDiaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiaryEntry(entry: UserDiaryEntity)

    // 3. Dream Journal
    @Query("SELECT * FROM dream_journal ORDER BY timestamp DESC")
    fun getAllDreamEntries(): Flow<List<DreamJournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDreamEntry(entry: DreamJournalEntity)

    // 4. Test Results
    @Query("SELECT * FROM test_results ORDER BY timestamp DESC")
    fun getAllTestResults(): Flow<List<TestResultsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResultsEntity)

    // 5. Meditation Log
    @Query("SELECT * FROM meditation_log ORDER BY timestamp DESC")
    fun getAllMeditationLogs(): Flow<List<MeditationLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeditationLog(log: MeditationLogEntity)

    // 6. Tarot History
    @Query("SELECT * FROM tarot_history ORDER BY timestamp DESC")
    fun getAllTarotHistories(): Flow<List<TarotHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarotHistory(history: TarotHistoryEntity)

    // 7. Pendulum Log
    @Query("SELECT * FROM pendulum_log ORDER BY timestamp DESC")
    fun getAllPendulumLogs(): Flow<List<PendulumLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendulumLog(log: PendulumLogEntity)

    // 8. Shadow Work Journal
    @Query("SELECT * FROM shadow_work_journal ORDER BY timestamp DESC")
    fun getAllShadowWorkLogs(): Flow<List<ShadowWorkJournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShadowWorkLog(log: ShadowWorkJournalEntity)

    // 9. Numerology Reports
    @Query("SELECT * FROM numerology_reports ORDER BY timestamp DESC")
    fun getAllNumerologyReports(): Flow<List<NumerologyReportsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNumerologyReport(report: NumerologyReportsEntity)

    // 10. Astrology Reports
    @Query("SELECT * FROM astrology_reports ORDER BY timestamp DESC")
    fun getAllAstrologyReports(): Flow<List<AstrologyReportsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstrologyReport(report: AstrologyReportsEntity)

    // 11. Geometric Patterns
    @Query("SELECT * FROM geometric_patterns ORDER BY timestamp DESC")
    fun getAllGeometricPatterns(): Flow<List<GeometricPatternsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeometricPattern(pattern: GeometricPatternsEntity)

    // 12. Akashic Insights
    @Query("SELECT * FROM akashic_insights ORDER BY timestamp DESC")
    fun getAllAkashicInsights(): Flow<List<AkashicInsightsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAkashicInsight(insight: AkashicInsightsEntity)

    // 13. Symbol Journal
    @Query("SELECT * FROM symbol_journal ORDER BY timestamp DESC")
    fun getAllSymbolJournalEntries(): Flow<List<SymbolJournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbolJournalEntry(entry: SymbolJournalEntity)

    // 14. User Signals
    @Query("SELECT * FROM user_signals ORDER BY timestamp DESC")
    fun getAllUserSignals(): Flow<List<UserSignalsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSignal(signal: UserSignalsEntity)

    // 15. Daily Mood
    @Query("SELECT * FROM daily_mood ORDER BY timestamp DESC")
    fun getAllDailyMoodLogs(): Flow<List<DailyMoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyMood(mood: DailyMoodEntity)

    // 16. Archetype Progress
    @Query("SELECT * FROM archetype_progress ORDER BY timestamp DESC")
    fun getAllArchetypeProgress(): Flow<List<ArchetypeProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArchetypeProgress(progress: ArchetypeProgressEntity)

    // --- Database Clean & Sync Management ---

    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()

    @Query("DELETE FROM user_diary")
    suspend fun clearUserDiary()

    @Query("DELETE FROM dream_journal")
    suspend fun clearDreamJournal()

    @Query("DELETE FROM test_results")
    suspend fun clearTestResults()

    @Query("DELETE FROM meditation_log")
    suspend fun clearMeditationLog()

    @Query("DELETE FROM tarot_history")
    suspend fun clearTarotHistory()

    @Query("DELETE FROM pendulum_log")
    suspend fun clearPendulumLog()

    @Query("DELETE FROM shadow_work_journal")
    suspend fun clearShadowWorkJournal()

    @Query("DELETE FROM numerology_reports")
    suspend fun clearNumerologyReports()

    @Query("DELETE FROM astrology_reports")
    suspend fun clearAstrologyReports()

    @Query("DELETE FROM geometric_patterns")
    suspend fun clearGeometricPatterns()

    @Query("DELETE FROM akashic_insights")
    suspend fun clearAkashicInsights()

    @Query("DELETE FROM symbol_journal")
    suspend fun clearSymbolJournal()

    @Query("DELETE FROM user_signals")
    suspend fun clearUserSignals()

    @Query("DELETE FROM daily_mood")
    suspend fun clearDailyMood()

    @Query("DELETE FROM archetype_progress")
    suspend fun clearArchetypeProgress()

    @Transaction
    suspend fun restoreDatabaseAll(
        profile: UserProfileEntity?,
        diaries: List<UserDiaryEntity>,
        dreams: List<DreamJournalEntity>,
        tests: List<TestResultsEntity>,
        meditations: List<MeditationLogEntity>,
        tarot: List<TarotHistoryEntity>,
        pendulums: List<PendulumLogEntity>,
        shadowWorks: List<ShadowWorkJournalEntity>,
        numerologies: List<NumerologyReportsEntity>,
        astrologies: List<AstrologyReportsEntity>,
        patterns: List<GeometricPatternsEntity>,
        akashics: List<AkashicInsightsEntity>,
        symbols: List<SymbolJournalEntity>,
        signals: List<UserSignalsEntity>,
        moods: List<DailyMoodEntity>,
        progresses: List<ArchetypeProgressEntity>
    ) {
        clearUserProfile()
        clearUserDiary()
        clearDreamJournal()
        clearTestResults()
        clearMeditationLog()
        clearTarotHistory()
        clearPendulumLog()
        clearShadowWorkJournal()
        clearNumerologyReports()
        clearAstrologyReports()
        clearGeometricPatterns()
        clearAkashicInsights()
        clearSymbolJournal()
        clearUserSignals()
        clearDailyMood()
        clearArchetypeProgress()

        if (profile != null) {
            insertUserProfile(profile)
        }
        diaries.forEach { insertDiaryEntry(it) }
        dreams.forEach { insertDreamEntry(it) }
        tests.forEach { insertTestResult(it) }
        meditations.forEach { insertMeditationLog(it) }
        tarot.forEach { insertTarotHistory(it) }
        pendulums.forEach { insertPendulumLog(it) }
        shadowWorks.forEach { insertShadowWorkLog(it) }
        numerologies.forEach { insertNumerologyReport(it) }
        astrologies.forEach { insertAstrologyReport(it) }
        patterns.forEach { insertGeometricPattern(it) }
        akashics.forEach { insertAkashicInsight(it) }
        symbols.forEach { insertSymbolJournalEntry(it) }
        signals.forEach { insertUserSignal(it) }
        moods.forEach { insertDailyMood(it) }
        progresses.forEach { insertArchetypeProgress(it) }
    }
}
