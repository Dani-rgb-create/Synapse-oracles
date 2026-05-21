package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.viewmodel.EsotericViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudSyncScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    val diaries by viewModel.diaryEntries.collectAsStateWithLifecycle()
    val dreams by viewModel.dreamEntries.collectAsStateWithLifecycle()
    val tests by viewModel.testResults.collectAsStateWithLifecycle()
    val meditations by viewModel.meditationLogs.collectAsStateWithLifecycle()
    val tarotReadings by viewModel.tarotHistories.collectAsStateWithLifecycle()
    val shadowLines by viewModel.shadowLogs.collectAsStateWithLifecycle()
    
    val isSyncing by viewModel.syncing.collectAsStateWithLifecycle()

    var archiveNameInput by remember { mutableStateOf("Cofre_Principal") }
    var syncKeyInput by remember { mutableStateOf("sello_sagrado_7") }
    
    var infoMessage by remember { mutableStateOf<String?>(null) }
    var isErrorType by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.syncSuccess.collectLatest { success ->
            if (success) {
                infoMessage = "¡Alineación local completada! Tus registros han sido resguardados en el archivo local de la app."
                isErrorType = false
            } else {
                infoMessage = "No se pudo sincronizar el archivo local. Verifica el nombre o el código del sello."
                isErrorType = true
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightCosmos)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate("home") },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("sync_back_button")
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = MysticGold)
                }
                Text(
                    text = "Alineación Celestial - Archivos",
                    color = MysticGold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                Box(
                    modifier = Modifier
                        .background(MossGreen.copy(0.3f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("LOCAL", color = MysticGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Informational introduction
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "El Archivo Akáshico Local (100% Offline)",
                        color = MysticGoldLight,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "La sabiduría del cosmos reside dentro de ti. Toda tu información espiritual (diario de sombras, sueños, numerología, cartas de tarot, y meditaciones) se almacena y resguarda localmente en tu terminal en archivos de texto aislados de alta integridad. No necesitas internet ni conexiones externas para respaldar tus múltiples perfiles.",
                        color = OffWhite,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Stat logs counters
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Registros Listos para Archivar",
                        color = MysticGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatCounterItem("Diario Gnóstico", diaries.size, Icons.Default.MenuBook)
                        StatCounterItem("Oniromanteion", dreams.size, Icons.Default.NightlightRound)
                        StatCounterItem("Tests del Alma", tests.size, Icons.Default.Psychology)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatCounterItem("Meditación", meditations.size, Icons.Default.SelfImprovement)
                        StatCounterItem("Tarot Sagrado", tarotReadings.size, Icons.Default.AutoAwesome)
                        StatCounterItem("Sombra Integrada", shadowLines.size, Icons.Default.VisibilityOff)
                    }
                }
            }
        }

        // Configuration Form Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Cámara del Sello y Cofre",
                        color = MysticGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Project ID Input -> Replaced with local category/drawer
                    Text("Nombre de tu Cofre de Consconsiencia:", color = OffWhite.copy(0.7f), fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = archiveNameInput,
                        onValueChange = { archiveNameInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sync_project_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MysticGold,
                            unfocusedBorderColor = CosmicSurfaceLight,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        placeholder = { Text("Eje: mi-cofre-esencia", color = OffWhite.copy(0.4f)) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sync Key Input
                    Text("Sello Código Secreto (Guardar/Restaurar):", color = OffWhite.copy(0.7f), fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = syncKeyInput,
                            onValueChange = { syncKeyInput = it },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sync_key_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MysticGold,
                                unfocusedBorderColor = CosmicSurfaceLight,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true,
                            placeholder = { Text("sello_sagrado_7", color = OffWhite.copy(0.4f)) }
                        )

                        // Key generator button
                        Button(
                            onClick = {
                                val generatedValue = "sello_" + Random.nextInt(100000, 999999).toString()
                                syncKeyInput = generatedValue
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("generate_key_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Generar")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sintoniza la misma Clave del Sello localmente para recuperar instantáneamente un estado de consciencia guardado.",
                        color = OffWhite.copy(0.5f),
                        fontSize = 10.sp
                    )
                }
            }
        }

        // Action Buttons with Status Feedback
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isSyncing) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MysticGold)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Backup up
                        Button(
                            onClick = {
                                infoMessage = null
                                viewModel.uploadToCloud(archiveNameInput, syncKeyInput)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("backup_button")
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = PureBlack)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Respaldar", color = PureBlack, fontWeight = FontWeight.Bold)
                        }

                        // Restore down
                        Button(
                            onClick = {
                                infoMessage = null
                                viewModel.downloadFromCloud(archiveNameInput, syncKeyInput)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("restore_button")
                        ) {
                            Icon(Icons.Default.CloudDownload, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Restaurar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Status Feedback Box
        if (infoMessage != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isErrorType) AstralRose.copy(0.2f) else MossGreen.copy(0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isErrorType) Icons.Default.Error else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isErrorType) AstralRose else MysticGold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = infoMessage!!,
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { infoMessage = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = OffWhite.copy(0.7f))
                        }
                    }
                }
            }
        }

        // Configuration Guidance section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight.copy(0.5f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tratamiento de Datos Ultra-Seguro",
                        color = MysticGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tus reflexiones, secretos, tarot y visiones de sombra nunca abandonan los circuitos de tu dispositivo. Este método conserva un 'Cofre' local en el sandbox seguro del sistema de archivos Android de forma 100% independiente de internet.",
                        color = OffWhite.copy(0.7f),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.StatCounterItem(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = MysticGold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = OffWhite.copy(0.6f), fontSize = 9.sp, textAlign = TextAlign.Center)
            Text("$count", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
