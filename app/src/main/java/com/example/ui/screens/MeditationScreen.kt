package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

data class BreathingTechnique(
    val name: String,
    val description: String,
    val inhale: Int,
    val holdFull: Int,
    val exhale: Int,
    val holdEmpty: Int
)

@Composable
fun MeditationScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    var selectedPractice by remember { mutableStateOf("Voz del Vacío (Presencia)") }
    var emotionalFocusInput by remember { mutableStateOf("") }
    var isMeditationPlaying by remember { mutableStateOf(false) }
    var soundMixerState = remember {
        mutableStateListOf(
            SoundChannel("Cuencos Tibetanos", 0.5f),
            SoundChannel("Lluvia Sagrada", 0.4f),
            SoundChannel("Fuego Divino", 0.0f),
            SoundChannel("Viento Celestial", 0.0f),
            SoundChannel("Latido del Corazón", 0.2f),
            SoundChannel("Cuerdas Drone", 0.3f),
            SoundChannel("Campanas del Templo", 0.1f),
            SoundChannel("Murmullo del Río", 0.0f),
            SoundChannel("Grillos del Bosque", 0.0f),
            SoundChannel("Ondas Alfa", 0.5f),
            SoundChannel("Canto Cómico OM", 0.0f),
            SoundChannel("Resonancia Solar", 0.1f)
        )
    }

    val breathingTechniques = remember {
        listOf(
            BreathingTechnique("Respiración Cuadrada", "Sama Vritti — Balance de hemisferios cerebrales, estabilidad y claridad absoluta.", 4, 4, 4, 4),
            BreathingTechnique("Aliento Relajador (4-7-8)", "Pranayama Profundo — Desintegra la ansiedad mental e induce calma somática instantánea.", 4, 7, 8, 0),
            BreathingTechnique("Fuego Alquímico (Tummo)", "Aliento de Poder — Despierta tu fuerza vital Kundalini, infundiendo calor consciente y vigor.", 3, 12, 2, 0),
            BreathingTechnique("Sintonía Solar (5-5)", "Coherencia Cardíaca — Armoniza la respiración con el pulso magnético cósmico y la paz profunda.", 5, 0, 5, 0)
        )
    }
    var currentTechName by remember { mutableStateOf("Respiración Cuadrada") }
    val currentTech = remember(currentTechName) {
        breathingTechniques.find { it.name == currentTechName } ?: breathingTechniques[0]
    }

    // Breathing Animation State
    var breathPhase by remember { mutableStateOf("Santuario") }
    var breathTimer by remember { mutableStateOf(0) }

    LaunchedEffect(isMeditationPlaying, currentTechName) {
        if (isMeditationPlaying) {
            try {
                while (true) {
                    if (currentTech.inhale > 0) {
                        breathPhase = "Inhala"
                        for (i in currentTech.inhale downTo 1) { breathTimer = i; delay(1000) }
                    }
                    if (currentTech.holdFull > 0) {
                        breathPhase = "Retén Lleno"
                        for (i in currentTech.holdFull downTo 1) { breathTimer = i; delay(1000) }
                    }
                    if (currentTech.exhale > 0) {
                        breathPhase = "Exhala"
                        for (i in currentTech.exhale downTo 1) { breathTimer = i; delay(1000) }
                    }
                    if (currentTech.holdEmpty > 0) {
                        breathPhase = "Retén Vacío"
                        for (i in currentTech.holdEmpty downTo 1) { breathTimer = i; delay(1000) }
                    }
                }
            } catch (e: Exception) {
                // Handle cancellation
            }
        } else {
            breathPhase = "Santuario"
            breathTimer = 0
        }
    }

    val targetRadiusScale = when (breathPhase) {
        "Inhala" -> 1.15f
        "Retén Lleno" -> 1.15f
        "Exhala" -> 0.45f
        "Retén Vacío" -> 0.35f
        else -> 0.75f
    }

    val radiusScale by animateFloatAsState(
        targetValue = targetRadiusScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "breathRadius"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightCosmos)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Title Summary
        item {
            Text(
                text = "Templo Interior - ∞ Zona de Meditación",
                color = MysticGold,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Entra en resonancia con el todo y silencia la mente",
                color = OffWhite.copy(alpha = 0.8f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 2. Breathing Oracle Sphere Canvas Guide
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
                        text = "Oráculo de la Respiración Sagrada",
                        color = MysticGoldLight,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Selecciona un patrón respiratorio gnóstico:",
                        color = OffWhite.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Row of techniques as small chips, easily toggled
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        breathingTechniques.forEach { tech ->
                            val isSelected = currentTechName == tech.name
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(enabled = !isMeditationPlaying) {
                                        currentTechName = tech.name
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MossGreen.copy(0.7f) else CosmicSurfaceLight
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = tech.name.substringBefore(" (").replace("Respiración ", ""),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else OffWhite.copy(0.8f),
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp).fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentTech.description,
                        color = MysticGoldLight,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Beautiful mandala vector concentric breathing lines
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(180.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = size / 2f
                            val baseRadius = 70.dp.toPx()
                            val animatedRadius = baseRadius * radiusScale

                            // Draw flower of life outer lines
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(MossGreen.copy(alpha = 0.2f), Color.Transparent)
                                ),
                                radius = baseRadius * 1.2f
                            )

                            // Inner animated gold energy sphere
                            drawCircle(
                                color = MysticGold,
                                radius = animatedRadius,
                                style = Stroke(width = 3.dp.toPx())
                            )

                            // Outer supporting halos
                            drawCircle(
                                color = MossGreenLight.copy(alpha = 0.5f),
                                radius = animatedRadius * 0.8f,
                                style = Stroke(width = 1.dp.toPx())
                            )
                        }

                        // Text indicators within the breathing core
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isMeditationPlaying) breathPhase else "Santuario",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (isMeditationPlaying) {
                                Text(
                                    text = "$breathTimer s",
                                    color = MysticGold,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            isMeditationPlaying = !isMeditationPlaying
                            if (isMeditationPlaying) {
                                viewModel.logMeditation(selectedPractice, 300, "Cuencos,Lluvia", "0.5,0.4")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMeditationPlaying) AstralRose else MossGreen
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("breathing_start_button")
                    ) {
                        val label = if (isMeditationPlaying) "Detener Sesión Sagrada" else "Iniciar Ciclo Respiratorio Prana"
                        Text(label, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. AI Guided Meditation Generator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Creador de Meditaciones Guiadas (IA)",
                        color = MysticGold,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "¿Qué emoción necesitas canalizar hoy? Synapse creará un ritual meditativo específico para ti.",
                        color = OffWhite.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = emotionalFocusInput,
                        onValueChange = { emotionalFocusInput = it },
                        placeholder = { Text("Ej: Necesito soltar la ansiedad laboral...", color = OffWhite.copy(0.4f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MysticGold,
                            unfocusedBorderColor = CosmicSurfaceLight,
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("ai_meditation_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (emotionalFocusInput.isNotEmpty()) {
                                viewModel.sendChatMessage("Créame una meditación estructurada paso a paso y guiada basada en esta emoción: $emotionalFocusInput. Describe el ambiente místico, el conteo y las frases de transmutación.")
                                onNavigate("synapse_chat")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Canalizar Meditación Guiada", color = PureBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 4. 12-Channel Spiritual Sound Mixing Deck
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mezclador Trascendente 3D",
                            color = MysticGold,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = MysticGoldLight)
                    }
                    Text(
                        text = "Combina hasta 12 frecuencias vibratorias puras en tiempo real.",
                        color = OffWhite.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    soundMixerState.forEachIndexed { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = item.name,
                                color = OffWhite,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1.5f)
                            )
                            Slider(
                                value = item.volume,
                                onValueChange = { soundMixerState[index] = item.copy(volume = it) },
                                colors = SliderDefaults.colors(
                                    thumbColor = MysticGoldLight,
                                    activeTrackColor = MossGreen,
                                    inactiveTrackColor = CosmicSurfaceLight
                                ),
                                modifier = Modifier.weight(2.5f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${(item.volume * 100).toInt()}%",
                                color = MysticGold,
                                fontSize = 11.sp,
                                modifier = Modifier.width(35.dp)
                            )
                        }
                    }
                }
            }
        }

        // 5. Library of 100+ Base Meditative Practices Preview
        item {
            Text(
                text = "Biblioteca de Sabiduría Meditativa",
                color = MysticGoldLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        val practices = listOf(
            "Transmutación del Espejo de Sombra" to "Disuelve miedos recurrentes y abraza tu fuerza no integrada.",
            "Despertar Espiritual Gnostic" to "Desconecta de la ilusión del entorno exterior y sintoniza tu chispa divina.",
            "Alineación Cósmica Merkabah" to "Sincroniza tus centros de energía vital a través de geometría sagrada.",
            "Ciclo Hermético del Kybalión" to "Resonancia bajo los principios de mentalismo y polaridad.",
            "El Retorno a Isis" to "Sanación femenina interna y restauración del alma instintiva."
        )

        practices.forEach { (name, desc) ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPractice = name },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedPractice == name) MossGreen.copy(0.3f) else CosmicSurface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            if (selectedPractice == name) {
                                Icon(Icons.Default.SelfImprovement, contentDescription = "Seleccionada", tint = MysticGold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(desc, color = OffWhite.copy(0.7f), fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

data class SoundChannel(
    val name: String,
    val volume: Float
)
