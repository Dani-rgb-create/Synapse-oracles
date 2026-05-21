package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfileEntity::class,
        UserDiaryEntity::class,
        DreamJournalEntity::class,
        TestResultsEntity::class,
        MeditationLogEntity::class,
        TarotHistoryEntity::class,
        PendulumLogEntity::class,
        ShadowWorkJournalEntity::class,
        NumerologyReportsEntity::class,
        AstrologyReportsEntity::class,
        GeometricPatternsEntity::class,
        AkashicInsightsEntity::class,
        SymbolJournalEntity::class,
        UserSignalsEntity::class,
        DailyMoodEntity::class,
        ArchetypeProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun esotericDao(): EsotericDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nucleo_esencia_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
