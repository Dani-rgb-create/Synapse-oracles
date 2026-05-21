package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun SelfKnowledgeScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    var activeTab by remember { mutableStateOf("Tests") }

    // State of active Test
    var activeTestName by remember { mutableStateOf<String?>(null) }
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var answersScoreSum by remember { mutableStateOf(0) }
    var testFinished by remember { mutableStateOf(false) }

    val testRec = remember(activeTestName, answersScoreSum) {
        getTestDiagnosis(activeTestName, answersScoreSum)
    }

    // State of Shadow Journal entry
    var activeShadowPrompt by remember { mutableStateOf("¿De qué manera buscas secretamente validación actuando como la Mártir?") }
    var shadowJournalInput by remember { mutableStateOf("") }
    var shadowSubmitFinished by remember { mutableStateOf(false) }

    // State of Sincronicidad Signals
    var signalDescription by remember { mutableStateOf("") }
    var signalTriggerFinished by remember { mutableStateOf(false) }

    val testQuestions = mapOf(
        "Tipos de Jung" to listOf(
            "¿Sientes que tus corazonadas y revelaciones repentinas definen el rumbo de tu vida por encima de la lógica convencional?" to 20,
            "¿Sueles ensimismarte y pasar largos periodos a solas sintiendo que tu mundo interior es más real que el plano físico exterior?" to 20,
            "¿Al tomar decisiones, priorizas el impacto humano/espiritual sutil en lugar de un análisis objetivo de pros y contras?" to 20,
            "¿Te sientes cómodo navegando en la ambigüedad moral o mística, percibiendo que todo es parte de una dualidad complementaria?" to 20,
            "¿Percibes que detrás del flujo diario de sucesos físicos opera una corriente invisible de sincronías y arquetipos?" to 20
        ),
        "Eneagrama Esotérico" to listOf(
            "¿Te persigue un estándar moral interno tan alto que te culpas recurrentemente por tus faltas o imperfecciones humanas?" to 20,
            "¿Dedicas gran cantidad de tu energía a adquirir sabiduría mística offline temiendo quedar indefenso o desinformado ante el caos?" to 20,
            "¿Te consideras un buscador inquieto que huye de los compromisos rígidos para no apagar el fuego de su libertad espiritual?" to 20,
            "¿Sueles responsabilizarte por el bienestar ajeno a costa de negar o silenciar tus propias heridas y carencias?" to 20,
            "¿Has sentido un profundo anhelo de pertenecer a una verdad superior, sintiéndote a menudo un extranjero en la sociedad terrenal?" to 20
        ),
        "Fisuras de Sombra y Liberación" to listOf(
            "¿Sientes que boicoteas tus mayores éxitos espirituales o terrenales justo antes de que se manifiesten por miedo a brillar demasiado?" to 20,
            "¿Tiendes a actuar con el arquetipo de la Mártir, sacrificando tu paz mental por complacer a otros con la esperanza oculta de recibir reconocimiento?" to 20,
            "¿Te descubres a menudo proyectando irritación o frustración hacia las personas exitosas o egocéntricas como si fuesen tu espejo incómodo?" to 20,
            "¿Sientes vergüenza o culpabilidad cuando experimentas desánimo, tristeza o rabia, intentando ocultar esas 'vibraciones bajas'?" to 20,
            "¿Te cuesta recibir amor o regalos sin sentir una deuda inmediata o el impulso de compensar al otro de inmediato?" to 20
        ),
        "Animal de Poder" to listOf(
            "¿Te identificas con el vuelo elevado del Águila, prefiriendo observar el panorama general y la verdad celestial antes de actuar en tierra?" to 20,
            "¿Sientes la afinidad nocturna y astuta del Búho, amando descifrar verdades ocultas y misterios silenciosos en la quietud?" to 20,
            "¿Resuenas con la constante transmutación de la Serpiente, soltando el pasado mudando de piel ante cada crisis existencial?" to 20,
            "¿Te guía la garra protectora y territorial del Jaguar, defendiendo tu espacio sagrado con coraje instintivo y fuerza pasional?" to 20,
            "¿Sientes un llamado chamánico primordial que te empuja a sintonizarte con la naturaleza terrestre más que con los dogmas de la razón pura?" to 20
        )
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
                text = "Gimnasio del Alma - Autoconocimiento",
                color = MysticGold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Segmented Control Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Tests", "Trabajo de Sombra", "Sincronicidades").forEach { tab ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeTab = tab },
                        colors = CardDefaults.cardColors(
                            containerColor = if (activeTab == tab) MossGreen else CosmicSurface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = tab,
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Content switching sections
        when (activeTab) {
            "Tests" -> {
                if (activeTestName == null) {
                    item {
                        Text("Pruebas Esotéricas Disponibles", color = MysticGoldLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    testQuestions.keys.forEach { testName ->
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activeTestName = testName
                                        currentQuestionIdx = 0
                                        answersScoreSum = 0
                                        testFinished = false
                                    },
                                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(testName, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        Text("Explora tus arquetipos e inconsciente con Synapse.", color = OffWhite.copy(0.7f), fontSize = 12.sp)
                                    }
                                    Icon(Icons.Default.Visibility, contentDescription = null, tint = MysticGold)
                                }
                            }
                        }
                    }

                    // Dynamic AI Test generator card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MossGreen.copy(0.4f)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Generador de Cuestionarios IA", color = MysticGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Deja que la sabiduría de Synapse diseñe un test completamente personalizado sobre tus miedos o vidas pasadas.", color = OffWhite, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.sendChatMessage("Créame un cuestionario gnóstico e interactivo de 3 preguntas para descifrar si vibro más en la energía de la Hermitaña o de la Alquimista.")
                                        onNavigate("synapse_chat")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Generar Cuestionario Gnóstico", color = PureBlack)
                                }
                            }
                        }
                    }
                } else {
                    // Quiz in progress
                    val qList = testQuestions[activeTestName] ?: emptyList()
                    if (!testFinished) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "$activeTestName - Pregunta ${currentQuestionIdx + 1}/${qList.size}",
                                        color = MysticGold,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = qList[currentQuestionIdx].first,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        lineHeight = 22.sp
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))

                                    Button(
                                        onClick = {
                                            val currentScore = answersScoreSum + qList[currentQuestionIdx].second
                                            answersScoreSum = currentScore
                                            if (currentQuestionIdx + 1 < qList.size) {
                                                currentQuestionIdx++
                                            } else {
                                                testFinished = true
                                                val diag = getTestDiagnosis(activeTestName, currentScore)
                                                viewModel.saveTestResult(
                                                    testName = activeTestName!!,
                                                    score = currentScore,
                                                    categoryBreakdown = diag,
                                                    recommendation = "Continúa con la integración de Sombra e incrementa tu meditación."
                                                )
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                                        modifier = Modifier.fillMaxWidth().testTag("quiz_yes_button")
                                    ) {
                                        Text("Sí, completamente", color = Color.White)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = {
                                            if (currentQuestionIdx + 1 < qList.size) {
                                                currentQuestionIdx++
                                            } else {
                                                testFinished = true
                                                val diag = getTestDiagnosis(activeTestName, answersScoreSum)
                                                viewModel.saveTestResult(
                                                    testName = activeTestName!!,
                                                    score = answersScoreSum,
                                                    categoryBreakdown = diag,
                                                    recommendation = "Continúa con la integración de Sombra."
                                                )
                                            }
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OffWhite),
                                        modifier = Modifier.fillMaxWidth().testTag("quiz_no_button")
                                    ) {
                                        Text("En absoluto / No")
                                    }
                                }
                            }
                        }
                    } else {
                        // Test Results Dashboard
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("¡Prueba Completada!", color = MysticGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("Sintonía Espiritual: $answersScoreSum%", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(testRec, color = MysticGoldLight, fontSize = 13.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Hemos guardado el resultado en ‘El Viaje’ espiritual offline. Tu arquetipo hermético se ha actualizado en tiempo real en la base de datos.", color = OffWhite.copy(0.8f), fontSize = 11.sp, textAlign = TextAlign.Center)

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Button(
                                            onClick = {
                                                viewModel.sendChatMessage("He terminado la prueba de '$activeTestName' con un puntaje de $answersScoreSum% resultando en: '$testRec'. Dame un análisis de mi Sombra y mi Arquetipo según Jung.")
                                                onNavigate("synapse_chat")
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = MysticGold)
                                        ) {
                                            Text("Synapse Explicación", color = PureBlack)
                                        }
                                        OutlinedButton(
                                            onClick = { activeTestName = null }
                                        ) {
                                            Text("Volver", color = OffWhite)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "Trabajo de Sombra" -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Arquetipos de Sombra Identificados", color = MysticGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("• La Víctima (No integrada, buscando validación externa)\n• El Saboteador (Miedo inconsciente al brillo propio)\n• El Mártir (Negando necesidades personales por deber social)", color = OffWhite.copy(0.7f), fontSize = 12.sp, lineHeight = 20.sp)
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Entrada del Diario de Sombra", color = MysticGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(activeShadowPrompt, color = OffWhite, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = shadowJournalInput,
                                onValueChange = { shadowJournalInput = it },
                                placeholder = { Text("Escribe con total honestidad, nadie te juzga...", color = OffWhite.copy(0.4f)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = OffWhite,
                                    unfocusedTextColor = OffWhite,
                                    focusedBorderColor = MysticGold
                                ),
                                modifier = Modifier.fillMaxWidth().height(120.dp).testTag("shadow_input_field")
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    if (shadowJournalInput.isNotEmpty()) {
                                        viewModel.logShadowWork(
                                            prompt = activeShadowPrompt,
                                            response = shadowJournalInput,
                                            archetypes = "Mártir, Víctima",
                                            integrated = true
                                        )
                                        shadowSubmitFinished = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Consolidar e Integrar Sombra", color = Color.White)
                            }
                        }
                    }
                }

                if (shadowSubmitFinished) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MossGreen.copy(0.3f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("¡Tu Sombra ha sido abrazada!", color = MysticGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.sendChatMessage("He respondido la pregunta de sombra: '$activeShadowPrompt'. Mi confesión fue: '$shadowJournalInput'. Ayúdame a integrarlo con el principio de Polaridad.")
                                        onNavigate("synapse_chat")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MysticGold)
                                ) {
                                    Text("Analizar con Synapse en Chat", color = PureBlack)
                                }
                            }
                        }
                    }
                }
            }

            "Sincronicidades" -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Registrar Señal o Repetición Numérica", color = MysticGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("¿Has visto repetitivamente el 11:11, 333, un búho, una pluma o algún coincidente extraño?", color = OffWhite.copy(0.7f), fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = signalDescription,
                                onValueChange = { signalDescription = it },
                                placeholder = { Text("Ej: He visto el 11:11 en el reloj de mi cocina 3 veces seguidas...", color = OffWhite.copy(0.4f)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = OffWhite,
                                    unfocusedTextColor = OffWhite,
                                    focusedBorderColor = MysticGold
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("signal_input_field")
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    if (signalDescription.isNotEmpty()) {
                                        viewModel.logUniverseSignal(
                                            desc = signalDescription,
                                            count = 3,
                                            context = "Registrado desde la calle",
                                            interpreterResponse = "Sincronicidad temporal activa"
                                        )
                                        signalTriggerFinished = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Vincular Sincronicidad", color = PureBlack, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (signalTriggerFinished) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MossGreen.copy(0.3f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Señal Vinculada Correctamente", color = Color.White, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.sendChatMessage("He registrado una sincronicidad cósmica: '$signalDescription'. Decodifica cábalamente el significado simbólico profundo de esto.")
                                        onNavigate("synapse_chat")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MysticGold)
                                ) {
                                    Text("Decodificar Significado con Synapse", color = PureBlack)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getTestDiagnosis(testName: String?, score: Int): String {
    return when (testName) {
        "Tipos de Jung" -> {
            when {
                score >= 80 -> "Esencia Altamente Intuitiva-Contemplativa. Tu mente habita en el éter; canaliza tu Gnosis para no perder arraigo terrenal."
                score >= 40 -> "Esencia Hermética Equilibrada. Integras el mundo empírico con el místico. Un puente firme para la sabiduría."
                else -> "Esencia Pragmática y Solar. Tu atención está volcada al plano exterior tangible."
            }
        }
        "Eneagrama Esotérico" -> {
            when {
                score >= 80 -> "Buscador de la Verdad Recóndita (Tríada Mental e Intelectual dominante). Gran capacidad de análisis sagrado."
                score >= 40 -> "Sostén de la Polaridad Activa (Tríada Emocional dominante). Tu motor es el servicio consciente y puro."
                else -> "Anclaje Terrenal Crítico (Tríada Instintiva dominante). Fuerte magnetismo pero requieres mayor desapego de tu ego."
            }
        }
        "Fisuras de Sombra y Liberación" -> {
            when {
                score >= 80 -> "Sombra Visible a Transmutar. Existen patrones activos de auto-boicot y culpa. Estás listo para liberarlos y brillar."
                score >= 40 -> "Sombra Intermedia. Consciencia parcial de tus heridas sagradas. El diario de sombra te guiará."
                else -> "Sombra Integrada o Latente. Posees una conexión integrada laboral con tu inconsciente."
            }
        }
        else -> {
            when {
                score >= 80 -> "Fuerza Totémica Cósmica del Aire y el Misterio (Águila y Búho protectores dominantes)."
                score >= 40 -> "Fuerza Totémica Terrestre de Transmutación y Garra (Serpiente y Jaguar protectores activos)."
                else -> "Fuerza Chamánica en Germinación. Tu alianza totémica con la Madre Tierra requiere mayor consagración."
            }
        }
    }
}
