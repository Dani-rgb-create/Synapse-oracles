package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NumerologyScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    var rawNameInput by remember { mutableStateOf("") }
    var calculatedLifeNum by remember { mutableStateOf(7) }
    var calculatedSoulNum by remember { mutableStateOf(22) }
    var calculatedMissionNum by remember { mutableStateOf(11) }

    var gematriaWordInput by remember { mutableStateOf("") }
    var calculatedGematriaSimple by remember { mutableStateOf(0) }
    var calculatedGematriaEnglish by remember { mutableStateOf(0) }

    // Generative Geometry configuration
    var activeGeometryPattern by remember { mutableStateOf("Flor de la Vida") }
    var complexityLinesScale by remember { mutableStateOf(4f) }
    var renderTint by remember { mutableStateOf(MysticGold) }

    // Math calculation for name to numerology number
    fun calculateNumerology(name: String) {
        if (name.trim().isEmpty()) return
        val clean = name.uppercase().replace("[^A-Z]".toRegex(), "")
        var sumDestiny = 0
        clean.forEach { char ->
            val code = char.code - 'A'.code + 1
            sumDestiny += (code % 9).let { if (it == 0) 9 else it }
        }
        
        // Single digit reduction Helper
        fun reduce(n: Int): Int {
            if (n == 11 || n == 22 || n == 33) return n
            if (n < 10) return n
            var temp = n
            var sum = 0
            while (temp > 0) {
                sum += temp % 10
                temp /= 10
            }
            return reduce(sum)
        }

        calculatedLifeNum = reduce(sumDestiny + 7)  // Suffix bias
        calculatedSoulNum = reduce(sumDestiny * 2)
        calculatedMissionNum = reduce(sumDestiny + 12)
    }

    // Gematria arithmetic solvers
    fun calculateGematria(word: String) {
        if (word.trim().isEmpty()) return
        var simpleSum = 0
        var englishSum = 0
        word.lowercase().forEach { char ->
            val valuePoint = char.code - 'a'.code + 1
            if (valuePoint in 1..26) {
                simpleSum += valuePoint
                englishSum += valuePoint * 6 // English standard gematria factor
            }
        }
        calculatedGematriaSimple = simpleSum
        calculatedGematriaEnglish = englishSum
    }

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
                text = "Centro de Numerología y Gematria",
                color = MysticGold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 1. Math Name & Profile Numerology solver
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Calculadora Pitagórica y Caldea", color = MysticGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Calcula los números maestros de tu sendero usando aritmética esotérica pitagórica.", color = OffWhite.copy(0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = rawNameInput,
                            onValueChange = { rawNameInput = it },
                            placeholder = { Text("Ingresa tu nombre completo...", color = OffWhite.copy(0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = OffWhite,
                                unfocusedTextColor = OffWhite,
                                focusedBorderColor = MysticGold
                            ),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("numerology_name_input")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { calculateNumerology(rawNameInput) },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = MossGreen),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Calcular", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ResultNumBubble("Sendero de Vida", calculatedLifeNum)
                        ResultNumBubble("Expresión Alma", calculatedSoulNum)
                        ResultNumBubble("Misión Destino", calculatedMissionNum)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            viewModel.sendChatMessage("Interpreta mis valores de Numerología Pitagórica: Mi Sendero de Vida es el $calculatedLifeNum, mi Expresión de Alma es el $calculatedSoulNum, y mi Misión de Destino es el $calculatedMissionNum. Decodifica el karma de mi nombre: '$rawNameInput'.")
                            onNavigate("synapse_chat")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Analizar Números con Synapse", color = PureBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 2. Sacred Gematria Decoder
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Gematria Interactiva Cabalística", color = MysticGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Introduce cualquier frase para extraer el código numérico hebraico y anglosajón.", color = OffWhite.copy(0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = gematriaWordInput,
                            onValueChange = { gematriaWordInput = it },
                            placeholder = { Text("Ej: Gnosis, Merkabah o tu apodo...", color = OffWhite.copy(0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = OffWhite,
                                unfocusedTextColor = OffWhite,
                                focusedBorderColor = MysticGold
                            ),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("gematria_input")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { calculateGematria(gematriaWordInput) },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = MossGreen),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Gematria", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Gematria Simple", color = OffWhite, fontSize = 11.sp)
                            Text("$calculatedGematriaSimple", color = MysticGold, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Gematria Inglesa", color = OffWhite, fontSize = 11.sp)
                            Text("$calculatedGematriaEnglish", color = MysticGoldLight, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (gematriaWordInput.isNotEmpty()) {
                                viewModel.sendChatMessage("He calculado la Gematria de la frase '$gematriaWordInput' y obtuve Simple: $calculatedGematriaSimple y English: $calculatedGematriaEnglish. Dame una decodificación gnóstico-cabalística completa de estas correspondencias y números.")
                                onNavigate("synapse_chat")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Decodificar Correspondencias", color = Color.White)
                    }
                }
            }
        }

        // 3. Generative Sacred Geometry Canvas Generator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Geometría Sagrada Generativa", color = MysticGold, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text("Usa esta mandala viva para enfocar y anclar tu vibración mental.", color = OffWhite.copy(0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("Flor de la Vida", "Metatrón").forEach { pattern ->
                            AssistChip(
                                onClick = {
                                    activeGeometryPattern = pattern
                                    renderTint = if (pattern == "Flor de la Vida") MysticGold else AstralRose
                                },
                                label = { Text(pattern) },
                                colors = AssistChipDefaults.assistChipColors(
                                    labelColor = if (activeGeometryPattern == pattern) MysticGold else OffWhite
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nested Mathematical Art Canvas
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(200.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val baseRadius = size.width / 12f

                            if (activeGeometryPattern == "Flor de la Vida") {
                                // Nested circles plotting mathematical intersections
                                val steps = complexityLinesScale.toInt().coerceAtLeast(1)
                                for (row in -steps..steps) {
                                    for (col in -steps..steps) {
                                        val xOffset = col * baseRadius * 1.5f
                                        val yOffset = row * baseRadius * 1.732f + (if (col % 2 == 0) 0f else baseRadius * 0.866f)
                                        drawCircle(
                                            color = renderTint.copy(alpha = 0.4f),
                                            radius = baseRadius * 1.5f,
                                            center = Offset(center.x + xOffset, center.y + yOffset),
                                            style = Stroke(width = 1.dp.toPx())
                                        )
                                    }
                                }
                            } else {
                                // Metatron Cube connections (Outer hex lines converging)
                                val steps = complexityLinesScale.toInt().coerceIn(4, 12)
                                val points = mutableListOf<Offset>()
                                for (i in 0 until 6) {
                                    val angleRad = Math.toRadians((i * 60).toDouble())
                                    val pointOffset = Offset(
                                        (center.x + baseRadius * 3f * cos(angleRad)).toFloat(),
                                        (center.y + baseRadius * 3f * sin(angleRad)).toFloat()
                                    )
                                    points.add(pointOffset)
                                    drawCircle(color = renderTint, radius = baseRadius * 0.6f, center = pointOffset, style = Stroke(width = 1.dp.toPx()))
                                }
                                points.add(center)
                                drawCircle(color = renderTint, radius = baseRadius * 0.8f, center = center, style = Stroke(width = 1.dp.toPx()))

                                // Connect all points diagonally (Metatron's mesh lines)
                                for (i in 0 until points.size) {
                                    for (j in i + 1 until points.size) {
                                        drawLine(
                                            color = renderTint.copy(alpha = 0.25f),
                                            start = points[i],
                                            end = points[j],
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Complexity parameter adjustment
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Complejidad Hermética", color = OffWhite, fontSize = 11.sp)
                        Slider(
                            value = complexityLinesScale,
                            onValueChange = { complexityLinesScale = it },
                            valueRange = 1f..6f,
                            colors = SliderDefaults.colors(
                                thumbColor = MysticGold,
                                activeTrackColor = MossGreen
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultNumBubble(label: String, num: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = OffWhite.copy(0.7f), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(CosmicSurfaceLight, RoundedCornerShape(25.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("$num", color = MysticGold, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}
