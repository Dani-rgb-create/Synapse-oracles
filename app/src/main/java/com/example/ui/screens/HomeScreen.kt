package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.viewmodel.EsotericViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: EsotericViewModel,
    onOpenDrawer: (() -> Unit)? = null,
    onNavigate: (String) -> Unit
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val moods by viewModel.dailyMoods.collectAsStateWithLifecycle()
    val diaries by viewModel.diaryEntries.collectAsStateWithLifecycle()
    val dreams by viewModel.dreamEntries.collectAsStateWithLifecycle()
    val meditations by viewModel.meditationLogs.collectAsStateWithLifecycle()
    val tarotReadings by viewModel.tarotHistories.collectAsStateWithLifecycle()
    val shadowLogs by viewModel.shadowLogs.collectAsStateWithLifecycle()

    val suggestions = remember(profile, diaries, dreams, meditations, tarotReadings, shadowLogs) {
        val list = mutableListOf<Triple<String, String, String>>() // (Title, Description, targetRoute)
        
        val p = profile
        val currentMood = p?.currentMood ?: "Contemplativo"
        
        // 1. Emotional state triggers
        when (currentMood) {
            "Melancólico" -> {
                list.add(Triple(
                    "Transmutación de la Melancolía",
                    "Tu vibración actual es Melancólica. Te sugiero refugiarte en El Templo y guiar tu mente con la atmósfera de música sagrada para retornar al equilibrio.",
                    "meditate"
                ))
            }
            "Enérgico" -> {
                list.add(Triple(
                    "Fuerza Ígnea Activa",
                    "Te encuentras cargado de vitalidad solar. Participa en un test Junguiano en el Gimnasio del Alma para canalizar tu energía hacia tu Arquetipo dominante.",
                    "self_knowledge"
                ))
            }
            "Contemplativo" -> {
                list.add(Triple(
                    "La Revelación del Silencio",
                    "La quietud es propicia para escuchar el éter. Realiza una tirada en el Espejo Tarot para recibir las correspondencias conscientes de tu Yo Superior.",
                    "tarot"
                ))
            }
            "Enigmas" -> {
                list.add(Triple(
                    "Aclara tus Dudas Rápidamente",
                    "Preguntas profundas orbitan tu mente terrenal. Consulta el Péndulo Sagrado de la app en la sección de Conexión para descifrar tu destino sutil con un Sí o un No.",
                    "divine_connection"
                ))
            }
            else -> {
                list.add(Triple(
                    "Estudio de las Sombras",
                    "Toda vibración es sagrada. Realiza una sesión en el Diario de Sombras de Autoconocimiento para reconciliarte con tu inconsciente herido.",
                    "self_knowledge"
                ))
            }
        }
        
        // 2. Activity footprints (Grow with user)
        if (diaries.isEmpty()) {
            list.add(Triple(
                "La Bitácora del Diario está Vacía",
                "No has registrado reflexiones en tu terminal. Confiesa tu Sombra o redacta qué sientes hoy para que la app resguarde tu memoria offline.",
                "self_knowledge"
            ))
        } else if (diaries.size in 1..2) {
            list.add(Triple(
                "Senda Gnóstica Iniciada",
                "Llevas ${diaries.size} reflexiones. Completa una entrada más para afianzar el hábito del autoexamen diario.",
                "self_knowledge"
            ))
        }

        if (meditations.isEmpty()) {
            list.add(Triple(
                "Enraizamiento Sagrado",
                "Aún no has meditado en El Templo. Dedica 2 minutos a la respiración guiada del Kybalion para recargar tu EP (Energía Esencial).",
                "meditate"
            ))
        } else {
            val totalSecs = meditations.sumOf { it.durationSeconds }
            if (totalSecs < 180) {
                list.add(Triple(
                    "Cultiva la Perseverancia",
                    "Has acumulado $totalSecs segundos de meditación consciente en El Templo. Sigue templando tu mente para desbloquear más energía.",
                    "meditate"
                ))
            }
        }

        if (dreams.isEmpty()) {
            list.add(Triple(
                "El Oniromanteion espera",
                "¿Has soñado hoy? Anota tu última visión nocturna en el registro de sueños para capturar correspondencias y símbolos del plano astral.",
                "divine_connection"
            ))
        }

        if (tarotReadings.isEmpty()) {
            list.add(Triple(
                "Llamado del Destino",
                "El mazo sagrado offline está intacto. Abre el Tarot y extrae una guía cósmica inmediata sobre tu pregunta terrenal.",
                "tarot"
            ))
        }

        // 3. Fallbacks or extensions
        if (list.size < 3) {
            list.add(Triple(
                "Axioma del Kybalión",
                "Estudia el Principio de Correspondencia en Sabiduría Eterna: 'Como es arriba, es abajo; como es adentro, es afuera'.",
                "wisdome"
            ))
            list.add(Triple(
                "Cálculo Cósmico Pitagórico",
                "Obtén la vibración numérica de tu nombre o fecha de nacimiento en la sección de Cábala.",
                "numerology"
            ))
        }
        
        list
    }

    var showMoodSelector by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightCosmos)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Welcoming Header with Mystical Background
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("welcome_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(MossGreen.copy(alpha = 0.5f), CosmicSurface)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onOpenDrawer?.invoke() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("welcome_hamburger_menu")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Abrir Menú Principal",
                                        tint = MysticGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "SYNAPSE STUDIO",
                                    color = MysticGoldLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(MossGreen.copy(0.2f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("GNOSIS PORTAL", color = MysticGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Hola, ${profile?.name ?: "Iniciado"}",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Arquetipo activo: ${profile?.mainArchetype ?: "Buscador de la Gnosis"}",
                            color = MysticGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Puntos de energía",
                                tint = AstralRose,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Energía Esencial: ${profile?.energyPoints ?: 100} EP",
                                color = OffWhite,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { showMoodSelector = !showMoodSelector },
                                colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(46.dp)
                                    .testTag("change_mood_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mood,
                                    contentDescription = null,
                                    tint = PureBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Registrar Sintonía", color = PureBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }

                            // Distinct golden physical capsule for offline local database/vault key
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 1.5.dp,
                                        brush = Brush.horizontalGradient(listOf(MysticGold, MossGreen)),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(MossGreen.copy(0.12f))
                                    .clickable { onNavigate("cloud_sync") }
                                    .testTag("welcome_settings_sync")
                                    .padding(horizontal = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VpnKey,
                                        contentDescription = "Cofre Sagrado",
                                        tint = MysticGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Cofre Local",
                                        color = MysticGoldLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Interactive Mood Selection Dialog Overlay (Within list)
        if (showMoodSelector) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "¿Cómo vibra tu alma en este ciclo solar?",
                            color = MysticGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        val emotions = listOf(
                            "Contemplativo" to Icons.Default.SelfImprovement,
                            "Enérgico" to Icons.Default.Thunderstorm,
                            "Melancólico" to Icons.Default.Cloud,
                            "Renacido" to Icons.Default.WbSunny,
                            "Enigmas" to Icons.Default.Lock
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            emotions.forEach { (emotion, icon) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.updateUserMood(emotion, 8, 7)
                                            showMoodSelector = false
                                        }
                                        .padding(8.dp)
                                ) {
                                    Icon(icon, contentDescription = emotion, tint = MysticGoldLight, modifier = Modifier.size(28.dp))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(emotion, fontSize = 10.sp, color = OffWhite)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Sincronicidad del Día
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(MossGreen)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${profile?.syncNumber ?: 7}",
                            color = MysticGold,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Sincronicidad Sagrada del Día",
                            color = MysticGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tu clave oculta de hoy es el ${profile?.syncNumber ?: 7}, representando el portal misterioso de la introspección hermética.",
                            color = OffWhite,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Guía de la Consciencia Superior (Dynamics computed offline suggestions)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Brush.horizontalGradient(listOf(MysticGold, MossGreen)), RoundedCornerShape(20.dp))
                    .testTag("higher_consciousness_suggestions"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MysticGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Sugerencias de la Consciencia Superior",
                                color = MysticGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tu alma gemela cósmica te sugiere estos pasos:",
                                color = OffWhite.copy(0.6f),
                                fontSize = 11.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    suggestions.take(3).forEach { (title, text, route) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onNavigate(route) }
                                .border(1.dp, MysticGold.copy(0.15f), RoundedCornerShape(14.dp)),
                            colors = CardDefaults.cardColors(containerColor = CosmicSurface.copy(0.9f)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = title,
                                        color = MysticGoldLight,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Cruzando Portal",
                                        tint = MysticGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = text,
                                    color = Color.White.copy(0.85f),
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Interactive Rueda de la Vida (Wheel of Life / AlmaRadar)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Rueda de la Vida - AlmaRadar",
                        color = MysticGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sintoniza los 8 ejes de tu energía existencial. Synapse analizará tu equilibrio cósmico.",
                        color = OffWhite,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val axes = listOf(
                        "Espiritualidad" to Icons.Default.SelfImprovement,
                        "Abundancia" to Icons.Default.Paid,
                        "Salud" to Icons.Default.HealthAndSafety,
                        "Amor" to Icons.Default.Favorite,
                        "Propósito" to Icons.Default.CompassCalibration,
                        "Creatividad" to Icons.Default.Brush,
                        "Comunidad" to Icons.Default.Groups,
                        "Aventura" to Icons.Default.Explore
                    )

                    axes.forEach { (name, icon) ->
                        var value by remember { mutableStateOf(0.6f) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(icon, contentDescription = name, tint = MysticGoldLight, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(name, color = OffWhite, fontSize = 13.sp, modifier = Modifier.width(110.dp))
                            Slider(
                                value = value,
                                onValueChange = { value = it },
                                colors = SliderDefaults.colors(
                                    thumbColor = MysticGold,
                                    activeTrackColor = MossGreen,
                                    inactiveTrackColor = CosmicSurfaceLight
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            viewModel.sendChatMessage("He sintonizado mi Rueda de la Vida en el AlmaRadar. Por favor, dame un breve diagnóstico espiritual y un mantra hermético específico.")
                            onNavigate("synapse_chat")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Analizar Equilibrio con Synapse", color = Color.White)
                    }
                }
            }
        }

        // 5. Quick Access Spiritual Hub Portal Buttons
        item {
            Text(
                text = "Portales Esotéricos",
                color = MysticGoldLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "Templo Interior",
                        subtitle = "Respirar y Paisajes 3D",
                        icon = Icons.Default.Audiotrack,
                        color = MossGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("meditate") }
                    )
                    PortalCard(
                        title = "Espejo Tarot",
                        subtitle = "Revelación Holográfica",
                        icon = Icons.Default.AutoAwesome,
                        color = CosmicSurfaceLight,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("tarot") }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "Gimnasio Alma",
                        subtitle = "Test & Diario de Sombra",
                        icon = Icons.Default.Psychology,
                        color = CosmicSurfaceLight,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("self_knowledge") }
                    )
                    PortalCard(
                        title = "Carta Natal 3D",
                        subtitle = "Astros e Interpretación",
                        icon = Icons.Default.Explore,
                        color = MossGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("astrology") }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "Cábala y Gematria",
                        subtitle = "Calculadora Pitagórica",
                        icon = Icons.Default.Functions,
                        color = MossGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("numerology") }
                    )
                    PortalCard(
                        title = "Divina Conexión",
                        subtitle = "Péndulo y OniroManteion",
                        icon = Icons.Default.Waves,
                        color = CosmicSurfaceLight,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("divine_connection") }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "Sabiduría Eterna",
                        subtitle = "Principios Herméticos",
                        icon = Icons.Default.MenuBook,
                        color = CosmicSurfaceLight,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("wisdome") }
                    )
                    PortalCard(
                        title = "Viaje del Héroe",
                        subtitle = "Línea del Camino",
                        icon = Icons.Default.Timeline,
                        color = MossGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("journey") }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "Espejo Arquetipos",
                        subtitle = "Seguimiento e IA",
                        icon = Icons.Default.Psychology,
                        color = MossGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("archetypes") }
                    )
                    PortalCard(
                        title = "Cofre Celestial",
                        subtitle = "Alineación y Resguardo",
                        icon = Icons.Default.Inventory2,
                        color = CosmicSurfaceLight,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("cloud_sync") }
                    )
                }
            }
        }
    }
}

@Composable
fun PortalCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = MysticGold, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = OffWhite.copy(alpha = 0.7f), fontSize = 10.sp)
        }
    }
}
