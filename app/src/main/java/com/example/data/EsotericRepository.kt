package com.example.data

import kotlinx.coroutines.flow.Flow

class EsotericRepository(private val dao: EsotericDao) {

    // 1. User Profile
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    suspend fun insertUserProfile(profile: UserProfileEntity) = dao.insertUserProfile(profile)

    // 2. User Diary
    val allDiaryEntries: Flow<List<UserDiaryEntity>> = dao.getAllDiaryEntries()
    suspend fun insertDiaryEntry(entry: UserDiaryEntity) = dao.insertDiaryEntry(entry)

    // 3. Dream Journal
    val allDreamEntries: Flow<List<DreamJournalEntity>> = dao.getAllDreamEntries()
    suspend fun insertDreamEntry(entry: DreamJournalEntity) = dao.insertDreamEntry(entry)

    // 4. Test Results
    val allTestResults: Flow<List<TestResultsEntity>> = dao.getAllTestResults()
    suspend fun insertTestResult(result: TestResultsEntity) = dao.insertTestResult(result)

    // 5. Meditation Log
    val allMeditationLogs: Flow<List<MeditationLogEntity>> = dao.getAllMeditationLogs()
    suspend fun insertMeditationLog(log: MeditationLogEntity) = dao.insertMeditationLog(log)

    // 6. Tarot History
    val allTarotHistories: Flow<List<TarotHistoryEntity>> = dao.getAllTarotHistories()
    suspend fun insertTarotHistory(history: TarotHistoryEntity) = dao.insertTarotHistory(history)

    // 7. Pendulum Log
    val allPendulumLogs: Flow<List<PendulumLogEntity>> = dao.getAllPendulumLogs()
    suspend fun insertPendulumLog(log: PendulumLogEntity) = dao.insertPendulumLog(log)

    // 8. Shadow Work Journal
    val allShadowWorkLogs: Flow<List<ShadowWorkJournalEntity>> = dao.getAllShadowWorkLogs()
    suspend fun insertShadowWorkLog(log: ShadowWorkJournalEntity) = dao.insertShadowWorkLog(log)

    // 9. Numerology Reports
    val allNumerologyReports: Flow<List<NumerologyReportsEntity>> = dao.getAllNumerologyReports()
    suspend fun insertNumerologyReport(report: NumerologyReportsEntity) = dao.insertNumerologyReport(report)

    // 10. Astrology Reports
    val allAstrologyReports: Flow<List<AstrologyReportsEntity>> = dao.getAllAstrologyReports()
    suspend fun insertAstrologyReport(report: AstrologyReportsEntity) = dao.insertAstrologyReport(report)

    // 11. Geometric Patterns
    val allGeometricPatterns: Flow<List<GeometricPatternsEntity>> = dao.getAllGeometricPatterns()
    suspend fun insertGeometricPattern(pattern: GeometricPatternsEntity) = dao.insertGeometricPattern(pattern)

    // 12. Akashic Insights
    val allAkashicInsights: Flow<List<AkashicInsightsEntity>> = dao.getAllAkashicInsights()
    suspend fun insertAkashicInsight(insight: AkashicInsightsEntity) = dao.insertAkashicInsight(insight)

    // 13. Symbol Journal
    val allSymbolJournalEntries: Flow<List<SymbolJournalEntity>> = dao.getAllSymbolJournalEntries()
    suspend fun insertSymbolJournalEntry(entry: SymbolJournalEntity) = dao.insertSymbolJournalEntry(entry)

    // 14. User Signals
    val allUserSignals: Flow<List<UserSignalsEntity>> = dao.getAllUserSignals()
    suspend fun insertUserSignal(signal: UserSignalsEntity) = dao.insertUserSignal(signal)

    // 15. Daily Mood
    val allDailyMoodLogs: Flow<List<DailyMoodEntity>> = dao.getAllDailyMoodLogs()
    suspend fun insertDailyMood(mood: DailyMoodEntity) = dao.insertDailyMood(mood)

    // 16. Archetype Progress
    val allArchetypeProgress: Flow<List<ArchetypeProgressEntity>> = dao.getAllArchetypeProgress()
    suspend fun insertArchetypeProgress(progress: ArchetypeProgressEntity) = dao.insertArchetypeProgress(progress)

    // Sync functions
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
    ) = dao.restoreDatabaseAll(
        profile, diaries, dreams, tests, meditations, tarot, pendulums, shadowWorks,
        numerologies, astrologies, patterns, akashics, symbols, signals, moods, progresses
    )
}
