package com.example.network

import android.content.Context
import android.util.Log
import java.io.File

object CloudSyncService {
    private const val TAG = "CloudSyncService"

    private fun getStorageDirectory(context: Context): File {
        val dir = File(context.filesDir, "esoteric_local_backups")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Guarda el respaldo serializado localmente de forma segura en el almacenamiento local de la app.
     * Esto permite una experiencia completamente offline y libre de servidores externos.
     */
    suspend fun uploadBackup(context: Context, projectId: String, syncKey: String, payloadJson: String): Boolean {
        return try {
            val cleanKey = syncKey.trim().ifEmpty { "anonymous_seeker" }
            val file = File(getStorageDirectory(context), "backup_${cleanKey}.json")
            Log.i(TAG, "Guardando respaldo local offline en: ${file.absolutePath}")
            file.writeText(payloadJson, Charsets.UTF_8)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error guardando respaldo local offline", e)
            false
        }
    }

    /**
     * Recupera el respaldo serializado localmente de forma segura desde el almacenamiento local de la app.
     */
    suspend fun downloadBackup(context: Context, projectId: String, syncKey: String): String? {
        return try {
            val cleanKey = syncKey.trim().ifEmpty { "anonymous_seeker" }
            val file = File(getStorageDirectory(context), "backup_${cleanKey}.json")
            if (file.exists()) {
                Log.i(TAG, "Cargando respaldo local offline desde: ${file.absolutePath}")
                file.readText(Charsets.UTF_8)
            } else {
                Log.w(TAG, "El archivo de respaldo local no existe: ${file.absolutePath}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cargando respaldo local offline", e)
            null
        }
    }
}
