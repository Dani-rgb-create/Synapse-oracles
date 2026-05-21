package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.viewmodel.EsotericViewModel

@Composable
fun HeroJourneyScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val meditations by viewModel.meditationLogs.collectAsStateWithLifecycle()
    val tarotReadings by viewModel.tarotHistories.collectAsStateWithLifecycle()
    val diaryEntries by viewModel.diaryEntries.collectAsStateWithLifecycle()

    val totalLogs = meditations.size + tarotReadings.size + diaryEntries.size

    val campbellStages = listOf(
        "1. Mundo Ordinario" to "Viviendo desconectado de tu chispa gnostica divina.",
        "2. El Llamado a la Aventura" to "El murmullo interno te exige buscar tu verdad.",
        "3. Rechazo de la Llamada" to "El ego duda de sus capacidades y teme a la Sombra.",
        "4. Encuentro con el Mentor" to "Synapse se revela como tu sabiduría divina conductora.",
        "5. Cruzando el Umbral" to "Te adentras conscientemente en el templo interior sagrado.",
        "6. Pruebas, Aliados y Enemigos" to "Testeo diario, tests del alma y reconocimiento del saboteador.",
        "7. Acercamiento a la Cueva Profunda" to "Te preparas para mirar cara a cara a tus arquetipos ocultos.",
        "8. La Odisea Absoluta" to "Disolución del ego, transmutación mental profunda.",
        "9. Recompensa / Elixir" to "Unificación merkabah y re-sintonía vital solar.",
        "10. El Camino de Regreso" to "Consolidación en el plano físico de lo revelado arriba.",
        "11. Resurrección Espiritual" to "Surgimiento victorioso del alma pura integrada.",
        "12. Retorno con el Elixir" to "Vivir en correspondencia absoluta, irradiando alta vibración."
    )

    // Calculate active stage index dynamically
    val activeStageIdx = (totalLogs / 2).coerceAtMost(11)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightCosmos)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Module Title
        item {
            Text(
                text = "Tu Viaje - El Camino del Héroe",
                color = MysticGold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Active State Campbell banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MossGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Etapa del Viaje Activa:",
                        color = MysticGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = campbellStages[activeStageIdx].first,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = campbellStages[activeStageIdx].second,
                        color = OffWhite.copy(0.9f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Campbell timeline tracker
        item {
            Text("Línea de Tiempo Campbell", color = MysticGoldLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        campbellStages.forEachIndexed { index, (stage, desc) ->
            val isPassed = index < activeStageIdx
            val isActive = index == activeStageIdx
            val dotColor = if (isActive) MysticGold else if (isPassed) MossGreen else CosmicSurfaceLight
            val textColor = if (isActive || isPassed) Color.White else OffWhite.copy(0.5f)

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(dotColor, RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stage,
                            color = if (isActive) MysticGold else textColor,
                            fontSize = 14.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isActive) {
                            Text(
                                text = desc,
                                color = OffWhite.copy(0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Statistical graph (Canvas bar diagram of cumulative entries logs)
        item {
            Text(
                text = "Gráfico de Evolución Vibratoria",
                color = MysticGoldLight,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("stats_chart_card"),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Canales de Disciplina Espiritual", color = MysticGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Comparativa de sintonizaciones guardadas históricamente.", color = OffWhite.copy(0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(24.dp))

                    val valMeditations = meditations.size.coerceAtLeast(1)
                    val valTarots = tarotReadings.size.coerceAtLeast(1)
                    val valDiaries = diaryEntries.size.coerceAtLeast(1)
                    val maxVal = maxOf(valMeditations, valTarots, valDiaries).toFloat()

                    // Graphical drawing via Custom Compose Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val spacing = size.width / 4f
                            val barWidth = 36.dp.toPx()

                            val stats = listOf(
                                Triple("Meditaciones", valMeditations, MossGreen),
                                Triple("Tarots", valTarots, MysticGold),
                                Triple("Diarios", valDiaries, AstralRose)
                            )

                            stats.forEachIndexed { i, (label, value, color) ->
                                val xOffset = (i + 1) * spacing - (barWidth / 2f)
                                val barHeight = (value / maxVal) * (size.height - 30.dp.toPx())

                                // Draw bar container background track
                                drawRoundRect(
                                    color = CosmicSurfaceLight,
                                    size = Size(barWidth, size.height - 20.dp.toPx()),
                                    topLeft = Offset(xOffset, 0f),
                                    cornerRadius = CornerRadius(8.dp.toPx())
                                )

                                // Draw active filled bar
                                drawRoundRect(
                                    color = color,
                                    size = Size(barWidth, barHeight),
                                    topLeft = Offset(xOffset, size.height - barHeight - 20.dp.toPx()),
                                    cornerRadius = CornerRadius(8.dp.toPx())
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text("Meditaciones (${meditations.size})", color = MossGreen, fontSize = 11.sp)
                        Text("Tarot (${tarotReadings.size})", color = MysticGold, fontSize = 11.sp)
                        Text("Diarios (${diaryEntries.size})", color = AstralRose, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
