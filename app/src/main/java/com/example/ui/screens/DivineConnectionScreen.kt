package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.EsotericViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DivineConnectionScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Pendulum State
    var pendulumQuestion by remember { mutableStateOf("") }
    var pendulumAnswerValue by remember { mutableStateOf("CONSULTAR EL VACÍO") }
    var pendulumAngleOffset by remember { mutableStateOf(0f) }
    var isPendulumSwinging by remember { mutableStateOf(false) }

    // OniroManteion dream journal State
    var dreamTextInput by remember { mutableStateOf("") }
    var emotionalChargeInput by remember { mutableStateOf("") }
    var symbolsFoundInput by remember { mutableStateOf("") }
    var dreamSavedFinished by remember { mutableStateOf(false) }

    // Dampening swing coroutine logic
    fun startPendulumSwing() {
        if (pendulumQuestion.trim().isEmpty()) return
        isPendulumSwinging = true
        coroutineScope.launch {
            var time = 0.0
            var amplitude = 50.0f
            while (amplitude > 0.5f) {
                // Harmonic oscillator with decay
                pendulumAngleOffset = amplitude * sin(time).toFloat()
                amplitude *= 0.96f // decay factor
                time += 0.35
                delay(30)
            }
            pendulumAngleOffset = 0f
            isPendulumSwinging = false
            
            // Resolve answer procedurally based on letters
            val len = pendulumQuestion.trim().length
            pendulumAnswerValue = when {
                len % 3 == 0 -> "SÍ (Abraza esta vereda)"
                len % 3 == 1 -> "NO (El sendero está truncado)"
                else -> "DUDOSO (La Gnosis aún se está decantando, medita más)"
            }
            
            viewModel.logPendulumQuery(
                question = pendulumQuestion,
                answer = pendulumAnswerValue,
                intensity = 0.8f
            )
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
        // Module Title
        item {
            Text(
                text = "Conexión Divina - Péndulo y Sueños",
                color = MysticGold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Section Tabs title
        item {
            Text(
                text = "Péndulo Virtual Energético",
                color = MysticGoldLight,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 1. Interactive Pendulum Physics Canvas Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Introduce tu duda de Sí o No", color = OffWhite, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = pendulumQuestion,
                        onValueChange = { pendulumQuestion = it },
                        placeholder = { Text("Ej: ¿Encontraré mi verdad en esta revelación?", color = OffWhite.copy(0.4f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedBorderColor = MysticGold
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("pendulum_input_question")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated pendulum drawing
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(180.dp)
                            .testTag("pendulum_canvas_box")
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val startOffset = Offset(size.width / 2f, 15.dp.toPx())
                            val length = size.height / 1.6f
                            val angleRad = Math.toRadians(pendulumAngleOffset.toDouble())

                            val endOffset = Offset(
                                (startOffset.x + length * sin(angleRad)).toFloat(),
                                (startOffset.y + length * cos(angleRad)).toFloat()
                            )

                            // Cord string
                            drawLine(
                                color = OffWhite.copy(alpha = 0.5f),
                                start = startOffset,
                                end = endOffset,
                                strokeWidth = 2.dp.toPx()
                            )

                            // Celestial support point circle ring
                            drawCircle(color = MossGreen, radius = 5.dp.toPx(), center = startOffset)

                            // Golden crystal weight pointing down
                            drawCircle(color = MysticGold, radius = 14.dp.toPx(), center = endOffset)
                            drawCircle(color = MysticGoldLight, radius = 8.dp.toPx(), center = endOffset, style = Stroke(width = 1.dp.toPx()))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { startPendulumSwing() },
                        colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isPendulumSwinging
                    ) {
                        Text("Vibrar Péndulo en el Vacío", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Veredicto del Oráculo:",
                        color = MysticGoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = pendulumAnswerValue,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 2. OniroManteion Dream Journal segment
        item {
            Text(
                text = "OniroManteion - Intérprete de Sueños",
                color = MysticGoldLight,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Registra tu Actividad Onírica", color = MysticGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Los sueños albergan las llaves de tu crisol interior. Permite que Synapse analice sus símbolos.", color = OffWhite.copy(0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("¿Qué has soñado?", color = OffWhite, fontSize = 12.sp)
                    OutlinedTextField(
                        value = dreamTextInput,
                        onValueChange = { dreamTextInput = it },
                        placeholder = { Text("Describe las imágenes, sentimientos, colores y voces que recuerdes...", color = OffWhite.copy(0.4f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedBorderColor = MysticGold
                        ),
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("dream_desc_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Símbolos o arquetipos notables (ej: agua, puerta, fuego, lobo)", color = OffWhite, fontSize = 11.sp)
                    OutlinedTextField(
                        value = symbolsFoundInput,
                        onValueChange = { symbolsFoundInput = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedBorderColor = MysticGold
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("dream_symbols_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Carga emocional (ej: Angustia, Éxtasis, Calma)", color = OffWhite, fontSize = 11.sp)
                    OutlinedTextField(
                        value = emotionalChargeInput,
                        onValueChange = { emotionalChargeInput = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedBorderColor = MysticGold
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (dreamTextInput.isNotEmpty()) {
                                viewModel.saveDream(
                                    dreamText = dreamTextInput,
                                    emotionalCharge = emotionalChargeInput,
                                    symbols = symbolsFoundInput
                                )
                                dreamSavedFinished = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Consolidar Sueño en el Alma", color = PureBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (dreamSavedFinished) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MossGreen.copy(0.3f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sueño guardado con éxito y sellado.", color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.sendChatMessage("He registrado este sueño: '$dreamTextInput'. Los símbolos que identifiqué son: '$symbolsFoundInput', y me sentí '$emotionalChargeInput'. Ayúdame a decodificar su arquetipo de sombra.")
                                onNavigate("synapse_chat")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MysticGold)
                        ) {
                            Text("Interpretar Revelación Astral Completa", color = PureBlack)
                        }
                    }
                }
            }
        }
    }
}
