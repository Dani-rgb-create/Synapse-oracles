package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun ArchetypesScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val diaryEntries by viewModel.diaryEntries.collectAsStateWithLifecycle()
    val dreamEntries by viewModel.dreamEntries.collectAsStateWithLifecycle()
    val testResults by viewModel.testResults.collectAsStateWithLifecycle()
    
    val isAnalyzing by viewModel.isAnalyzingArchetypes.collectAsStateWithLifecycle()
    val aiResult by viewModel.archetypeAnalysisResult.collectAsStateWithLifecycle()

    var activeExpandedArchetype by remember { mutableStateOf<String?>(null) }

    // Derive scores for visual tracking based on users actual historical records
    val heroScore = remember(diaryEntries, testResults) {
        val base = 40
        val diaryBonus = (diaryEntries.filter { it.emotion == "Enérgico" || it.emotion == "Renacido" }.size * 10)
        val testBonus = testResults.filter { it.score > 70 }.size * 15
        (base + diaryBonus + testBonus).coerceIn(10, 100)
    }

    val shadowScore = remember(diaryEntries, dreamEntries) {
        val base = 30
        val sadnessBonus = (diaryEntries.filter { it.emotion == "Melancólico" }.size * 12)
        val dreamBonus = (dreamEntries.filter { it.emotionalCharge == "Pesadilla" || it.emotionalCharge == "Intensa" }.size * 15)
        (base + sadnessBonus + dreamBonus).coerceIn(10, 100)
    }

    val animaScore = remember(diaryEntries, dreamEntries) {
        val base = 50
        val contemplativeBonus = (diaryEntries.filter { it.emotion == "Contemplativo" }.size * 10)
        val symbolsBonus = (dreamEntries.filter { it.symbolFound.isNotEmpty() }.size * 8)
        (base + contemplativeBonus + symbolsBonus).coerceIn(10, 100)
    }

    val magicianScore = remember(testResults, profile) {
        val base = 35
        val profileBonus = if (profile?.mainArchetype == "El Alquimista" || profile?.mainArchetype == "El Sabio Gnostico") 25 else 0
        val scoreBonus = testResults.filter { it.testName.contains("Eneagrama") }.size * 15
        (base + profileBonus + scoreBonus).coerceIn(10, 100)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightCosmos)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Core Module Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate("home") },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("archetype_back_button")
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = MysticGold)
                }
                Text(
                    text = "Espejo Junguiano - Arquetipos",
                    color = MysticGold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Informational intro banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "El Mapa de la Psique Divina",
                        color = MysticGoldLight,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Carl Jung descubrió que heredamos imágenes primordiales que estructuran nuestra experiencia psíquica. Al rastrear e integrar estos arquetipos, transmutamos el plomo de nuestro ego inconsciente en el oro puro del sí-mismo integrado.",
                        color = OffWhite,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Live stats based on actual data
        item {
            Text(
                text = "Medidores de Sintonía Arquetípica",
                color = MysticGoldLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ArchetypeBar(
                        name = "El Héroe / Creador (Ego)",
                        score = heroScore,
                        color = AstralBlue,
                        description = "Impulso consciente de iluminar lo desconocido combatiendo dragones mentales."
                    )
                    ArchetypeBar(
                        name = "La Sombra (Inconsciente)",
                        score = shadowScore,
                        color = AstralRose,
                        description = "Cualidades y miedos rechazados que se esconden en tus sueños y patrones ciegos."
                    )
                    ArchetypeBar(
                        name = "Anima / Animus (Dualidad Sagrada)",
                        score = animaScore,
                        color = MysticGold,
                        description = "Unión mística de tus energías solares activas y lunares receptivas interiores."
                    )
                    ArchetypeBar(
                        name = "El Mago / El Alquimista",
                        score = magicianScore,
                        color = MossGreenLight,
                        description = "Vibración enfocada en descifrar e integrar el conocimiento sagrado cósmico."
                    )
                }
            }
        }

        // AI analysis initiator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MossGreen.copy(0.4f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Gnosis de Integración con Synapse",
                        color = MysticGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Deja que Synapse procese tus diarios, oscilaciones emocionales, memorias oníricas y resultados de cuestionarios para trazar tu diagnóstico arquetípico unificado.",
                        color = OffWhite,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (isAnalyzing) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = MysticGold, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Consolidando registros celestiales...", color = MysticGoldLight, fontSize = 12.sp)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.runArchetypeAnalysisWithAi() },
                            colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("run_ai_archetype_button")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PureBlack)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Canalizar Diagnóstico con Synapse", color = PureBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Dynamic AI Results block
        if (aiResult.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = MysticGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Lectura Holográfica de Synapse", color = MysticGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = aiResult,
                            color = OffWhite,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        // Offline detailed guide and exercises
        item {
            Text(
                text = "Guía del Templo Gnóstico (Jung)",
                color = MysticGoldLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        listOf(
            Triple(
                "El Héroe (Nuestra Luz de Búsqueda)",
                "Representa el deseo egoico sano de progresar, resolver tests y conquistar conocimiento divino. Es el conquistador del templo interior.",
                "Realiza el test de 'Tipos de Jung' en el Gimnasio del Alma. Mantén un diario de progreso de tus actividades físicas y meditaciones solares. Di en voz alta: *'Tengo el poder de disolver la parálisis interna'*"
            ),
            Triple(
                "La Sombra (Nuestros Demonios Ocultos)",
                "Guarda todos los complejos reprimidos, celos, miedos ciegos y la autocensura. El Trabajo de Sombra es indispensable para evitar el autoengaño.",
                "Entra a 'Trabajo de Sombra' en el Gimnasio, escribe una respuesta honesta al prompt del día sin rodeos. Identifica a tu saboteador y reconócelo como un guardián herido de tu niñez."
            ),
            Triple(
                "Anima y Animus (La Disonancia Unida)",
                "Representan el contrapeso de género psíquico: el Anima (el aspecto intuitivo y receptivo femenino en lo masculino) y el Animus (el aspecto asertivo en lo femenino).",
                "Usa el mezclador de sintonías en El Templo. Combina un sonido receptivo (como 'Agua Cósmica') y uno proactivo (como 'Fuego del Crisol') en tu meditación espiritual."
            ),
            Triple(
                "El Mago / El Alquimista (Maestro del Destino)",
                "Representa el poder de transmutar, manifestar realidades subjetivas y guiar el rumbo vital mediante la mente. Vibra con el Mentalismo.",
                "Escribe tres sincronicidades presenciadas en tu vida en la sección 'Sincronicidades' del Gimnasio. Realiza una tirada de Tarot y lee la explicación de Synapse para descifrar las leyes ocultas detrás de tu destino físico."
            )
        ).forEach { (title, explain, ritual) ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activeExpandedArchetype = if (activeExpandedArchetype == title) null else title
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = if (activeExpandedArchetype == title) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = MysticGold
                            )
                        }
                        
                        AnimatedVisibility(
                            visible = activeExpandedArchetype == title,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                Text(explain, color = OffWhite.copy(0.8f), fontSize = 12.sp, lineHeight = 18.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MossGreen.copy(0.3f))
                                        .padding(10.dp)
                                ) {
                                    Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = MysticGold, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Ritual de Integración Sagrado:", color = MysticGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(ritual, color = OffWhite, fontSize = 11.sp, lineHeight = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.ArchetypeBar(
    name: String,
    score: Int,
    color: Color,
    description: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("$score%", color = MysticGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { score.toFloat() / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = CosmicSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(description, color = OffWhite.copy(alpha = 0.6f), fontSize = 10.sp, lineHeight = 14.sp)
    }
}
