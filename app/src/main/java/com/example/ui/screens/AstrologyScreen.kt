package com.example.ui.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.viewmodel.EsotericViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AstrologyScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()

    var activeSectorInfo by remember { mutableStateOf("Toca cualquier sección de la Rueda Astral para interpretar su influjo divino.") }
    var rawSynastryNameInput by remember { mutableStateOf("") }
    var selectedHouseNum by remember { mutableStateOf(1) }

    val houseDecodings = mapOf(
        1 to "Casa I (Ascendente): Tu yo externo, personalidad física y portal de encarnación inicial.",
        2 to "Casa II: Finanzas, recursos, valoración propia e integración de la abundancia mundana.",
        3 to "Casa III: Comunicación, intelecto práctico, viajes cortos y entendimiento mental hermético.",
        4 to "Casa IV (Medio Cielo): Tus raíces familiares ocultas, herencia interna transgeneracional.",
        5 to "Casa V: Expresión creadora, pasiones amorosas, juego infantil y brillo del yo.",
        6 to "Casa VI: Salud biológica, servicio altruista, trabajo diario y conexión somática.",
        7 to "Casa VII: Relaciones duales de espejo, matrimonio y contratos del alma karmáticos.",
        8 to "Casa VIII: Muerte iniciática, sexo tántrico, transmutación y recursos compartidos.",
        9 to "Casa IX: Viajes interestelares, alta filosofía gnóstica, religión y sabiduría cabalística.",
        10 to "Casa X: Destino, profesión soberana, reputación pública y poder de manifestación.",
        11 to "Casa XI: Comunidad de egregoras afines, redes estelares y misiones colectivas.",
        12 to "Casa XII: El inconsciente colectivo, dolores no resueltos, el templo del retiro final del ego."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightCosmos)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Block
        item {
            Text(
                text = "Cosmobiología Astral - Carta Natal",
                color = MysticGold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Concentric Graphical Astronomy Natal Wheel Canvas
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Rueda Natal de ${profile?.name ?: "Buscador"}",
                        color = MysticGoldLight,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(240.dp)
                            .testTag("astronomy_canvas_box")
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val radius = size.width / 2.3f

                            // Concentric circle rings
                            drawCircle(color = MossGreen, radius = radius, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
                            drawCircle(color = MossGreen.copy(0.4f), radius = radius * 0.7f, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()))
                            drawCircle(color = MysticGold.copy(0.3f), radius = radius * 0.4f, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()))

                            // Draw House Spoke Sectors (12 sectors)
                            for (i in 0 until 12) {
                                val angleDeg = i * 30.0
                                val angleRad = Math.toRadians(angleDeg)
                                val outerPoint = Offset(
                                    (center.x + radius * cos(angleRad)).toFloat(),
                                    (center.y + radius * sin(angleRad)).toFloat()
                                )
                                drawLine(
                                    color = MossGreen.copy(alpha = 0.5f),
                                    start = center,
                                    end = outerPoint,
                                    strokeWidth = 1.dp.toPx()
                                )

                                // Draw Zodiac symbol numbers text natively
                                drawContext.canvas.nativeCanvas.apply {
                                    val textAngle = Math.toRadians(angleDeg + 15)
                                    val txX = (center.x + (radius * 0.85f) * cos(textAngle)).toFloat()
                                    val txY = (center.y + (radius * 0.85f) * sin(textAngle)).toFloat()

                                    val paint = Paint().apply {
                                        color = MysticGold.toArgb()
                                        textSize = 24f
                                        isAntiAlias = true
                                        textAlign = Paint.Align.CENTER
                                    }
                                    val zodiacSymbols = listOf("♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓")
                                    drawText(zodiacSymbols[i], txX, txY + 8f, paint)
                                }
                            }

                            // Aspect Lines (Lines of force connecting planets across the wheel in triangles/aspects)
                            val aspects = listOf(
                                Pair(0, 4),   // Trino (120 deg)
                                Pair(2, 6),   // Oposición (180 deg)
                                Pair(3, 9),   // Trino
                                Pair(1, 4)    // Cuadratura (90 deg)
                            )
                            aspects.forEach { (p1, p2) ->
                                val r1Rad = Math.toRadians(p1 * 30.0 + 15)
                                val r2Rad = Math.toRadians(p2 * 30.0 + 15)
                                val startOffset = Offset(
                                    (center.x + (radius * 0.65f) * cos(r1Rad)).toFloat(),
                                    (center.y + (radius * 0.65f) * sin(r1Rad)).toFloat()
                                )
                                val endOffset = Offset(
                                    (center.x + (radius * 0.65f) * cos(r2Rad)).toFloat(),
                                    (center.y + (radius * 0.65f) * sin(r2Rad)).toFloat()
                                )
                                drawLine(
                                    color = if (Math.abs(p1 - p2) == 4) MysticGold else AstralRose,
                                    start = startOffset,
                                    end = endOffset,
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Vibración de Tránsitos Planetarios Activa",
                        color = MysticGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // 3. Interactive Interpretation Panels
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Explorar las 12 Horas del Zodíaco", color = MysticGoldLight, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        (1..6).forEach { num ->
                            AssistChip(
                                onClick = {
                                    selectedHouseNum = num
                                    activeSectorInfo = houseDecodings[num] ?: ""
                                },
                                label = { Text("H$num") },
                                colors = AssistChipDefaults.assistChipColors(
                                    labelColor = if (selectedHouseNum == num) MysticGold else OffWhite
                                )
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        (7..12).forEach { num ->
                            AssistChip(
                                onClick = {
                                    selectedHouseNum = num
                                    activeSectorInfo = houseDecodings[num] ?: ""
                                },
                                label = { Text("H$num") },
                                colors = AssistChipDefaults.assistChipColors(
                                    labelColor = if (selectedHouseNum == num) MysticGold else OffWhite
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = MysticGold)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = activeSectorInfo,
                            color = OffWhite,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // 4. Cosmological Sinastry Compatibilidad Calculator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Sinastría de Almas & Compatibilidad (IA)", color = MysticGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Somete la carta natal de otra persona al influjo orbital para descubrir compatibilidad karmática.", color = OffWhite.copy(0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rawSynastryNameInput,
                        onValueChange = { rawSynastryNameInput = it },
                        placeholder = { Text("Ej: María (24 de Octubre 1995, 23:40, París)...", color = OffWhite.copy(0.4f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedBorderColor = MysticGold
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("synastry_input_field")
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (rawSynastryNameInput.isNotEmpty()) {
                                viewModel.sendChatMessage("Realiza un análisis profundo de Sinastría y Compatibilidad amorosa/espiritual hermética entre yo (nacido el ${profile?.birthDate} en ${profile?.birthPlace}) y la persona: $rawSynastryNameInput. Explícame el karma mutuo y los tránsitos cruzados.")
                                onNavigate("synapse_chat")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cruzar Cartas Astrales", color = PureBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
