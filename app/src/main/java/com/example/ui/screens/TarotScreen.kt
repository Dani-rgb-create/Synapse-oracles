package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.EsotericViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TarotScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedSpread by remember { mutableStateOf("Carta Única") }
    var selectedDeckStyle by remember { mutableStateOf("Tarot Gnóstico de Jung") }
    var customQuestionInput by remember { mutableStateOf("") }

    // Selection game state
    val maxCardsToSelect = when (selectedSpread) {
        "Carta Única" -> 1
        "Tirada de Sí o No" -> 2
        "Tríptica del Tiempo" -> 3
        "Cruz Alquímica" -> 4
        else -> 1
    }
    val selectedIndices = remember { mutableStateListOf<Int>() }
    var isRevealed by remember { mutableStateOf(false) }
    var isShuffling by remember { mutableStateOf(false) }

    // Complete major arcana deck of 22 cards
    val fullTarotDeck = remember {
        listOf(
            TarotCard("El Loco (0)", "⚡", "El salto de fe indómito, el inicio sin límites, el caos creador gnostique.", "No temas arriesgarte; el universo sostiene tus pasos inocentes."),
            TarotCard("El Mago (I)", "⚗️", "La maestría de los elementos, la voluntad hermética alquimizando el plomo.", "Tienes todos los recursos celestiales para materializar tu visión."),
            TarotCard("La Sacerdotisa (II)", "🔑", "El plano del inconsciente, las verdades veladas del templo de Isis, el silencio.", "Mira hacia adentro. La respuesta está en tu receptividad meditativa."),
            TarotCard("La Emperatriz (III)", "🌿", "La gestación armónica de ideas, fecundidad creadora y esplendor sensual.", "Momento de florecer, encauzar la creatividad y confiar en la abundancia cósmica."),
            TarotCard("El Emperador (IV)", "🏰", "La estructura, el orden trascendente del plano físico y la soberanía mental.", "Establece límites y fundamentos sólidos; lidera con sabiduría y entereza."),
            TarotCard("El Sumo Sacerdote (V)", "📜", "La gnosis compartida, el puente divino y la revelación de leyes sagradas.", "Busca el saber profundo; un maestro sutil o una analogía espiritual te guiarán."),
            TarotCard("Los Enamorados (VI)", "💖", "La encrucijada mística del alma, el balance de opuestos internos.", "Sintoniza tu corazón en tus elecciones. Integra sombra y luz con compasión."),
            TarotCard("El Carro (VII)", "🛡️", "El triunfo de la voluntad disciplinada canalizando la dualidad instintiva.", "Avanza firme en tu sendero. Mantén el foco para cruzar con éxito el umbral."),
            TarotCard("La Justicia (VIII)", "⚖️", "El equilibrio kármico inapelable, honestidad interna y honestidad objetiva.", "Toda acción desencadena su reacción cósmica equivalente. Actúa con total lucidez."),
            TarotCard("El Ermitaño (IX)", "🕯️", "La lámpara del autodescubrimiento solitario, introspección sagrada.", "Apaga el bullicio mundano temporalmente; tu viaje interior necesita recogimiento."),
            TarotCard("La Rueda de la Fortuna (X)", "🔄", "Los ciclos eternos inmutables de correspondencia y flujo universal.", "Comprende el vaivén del cosmos; las mareas bajas pronto verterán luz dorada."),
            TarotCard("La Fuerza (XI)", "🦁", "El dominio de las pasiones indómitas mediante el influjo suave de l'espíritu.", "La autodisciplina y el perdón místico desarmarán cualquier amenaza."),
            TarotCard("El Colgado (XII)", "⚓", "El sacrificio sagrado, la iluminación alcanzada al revertir la perspectiva común.", "Suelta la necesidad de control. Contempla el enigma con quietud contemplativa."),
            TarotCard("La Muerte (XIII)", "💀", "La gran transmutación mística, disolución del ego para el florecimiento espiritual.", "Suelta con benevolencia lo marchito; la oruga debe perecer para que nazca la mariposa."),
            TarotCard("La Templanza (XIV)", "🧪", "La alquimia espiritual templada, la integración del flujo sutil del ser.", "Une tus fuerzas antagónicas. Encuentra bienestar en el balance rítmico."),
            TarotCard("El Diablo (XV)", "🔥", "El guardián de tus apetitos terrenos, dependencias y fijaciones inconscientes.", "Confronta tus ataduras sin juzgarte; tu Sombra oculta el fuego del cambio sagrado."),
            TarotCard("La Torre (XVI)", "⚡", "La destrucción súbita de las estructuras rígidas del orgullo y la falsa seguridad.", "El relámpago que vulnera tu ilusión es un favor sagrado; ahora puedes ver las estrellas."),
            TarotCard("La Estrella (XVII)", "⭐", "El néctar celestial purificador de la esperanza sagrada y la revelación cósmica.", "Sintoniza con las leyes eternas; la constelación de tu destino te bendice."),
            TarotCard("La Luna (XVIII)", "🌙", "La noche oscura del alma, ensueños proféticos, ilusiones del abismo.", "Confronta tus miedos lunares; la sombra es el sustrato de tu iluminación primordial."),
            TarotCard("El Sol (XIX)", "☀️", "La coronación resplandeciente del oro espiritual, la lucidez y el gozo supremo.", "Disfruta de la radiancia. El calor de la consciencia expande tus raíces sagradas."),
            TarotCard("El Juicio (XX)", "🎺", "El despertar de la tumba existencial, la emancipación kármica del iniciado.", "Siente la llamada sagrada hacia tu renacer espiritual. Tus cadenas se desvanecen."),
            TarotCard("El Mundo (XXI)", "🌌", "La consumación celestial del mandala existencial. Eres uno con la Gran Obra.", "El ciclo cósmico termina en perfecta alineación. Eres la sinfonía entera.")
        ).shuffled() // Shuffle pool each session for absolute fresh synchronicity
    }

    // Helper to clear and re-shuffle
    fun resetReading() {
        selectedIndices.clear()
        isRevealed = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightCosmos)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Brand Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("SYNAPSE", color = MysticGold, fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("STUDIO", color = OffWhite, fontSize = 24.sp, fontWeight = FontWeight.Light, letterSpacing = 3.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "El Espejo Sagrado de Sincronicidad y Gnosis",
                    color = MysticGoldLight,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 2. Spread and Deck Config
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Configurar Canal Astral", color = MysticGold, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Estilo de la Baraja", color = OffWhite.copy(0.7f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val decks = listOf("Tarot Gnóstico de Jung", "Marsella alquímico")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        decks.forEach { deck ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(enabled = !isRevealed && !isShuffling) { selectedDeckStyle = deck },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedDeckStyle == deck) MossGreen.copy(0.8f) else CosmicSurfaceLight
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = deck,
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Esquema de la Tirada", color = OffWhite.copy(0.7f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val spreadsRow1 = listOf("Carta Única", "Tirada de Sí o No")
                    val spreadsRow2 = listOf("Tríptica del Tiempo", "Cruz Alquímica")
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            spreadsRow1.forEach { spread ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable(enabled = !isRevealed && !isShuffling) {
                                            selectedSpread = spread
                                            resetReading()
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedSpread == spread) MossGreen.copy(0.81f) else CosmicSurfaceLight
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = spread,
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            spreadsRow2.forEach { spread ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable(enabled = !isRevealed && !isShuffling) {
                                            selectedSpread = spread
                                            resetReading()
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedSpread == spread) MossGreen.copy(0.81f) else CosmicSurfaceLight
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = spread,
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. User Custom Question Input
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Plantea tu pregunta sagrada al Cosmos:", color = MysticGold, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = customQuestionInput,
                        onValueChange = { customQuestionInput = it },
                        readOnly = isRevealed,
                        placeholder = { Text("Ej: ¿Hacia dónde deba guiar mi sombra para integrarla hoy?", color = OffWhite.copy(0.4f), fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedBorderColor = MysticGold,
                            unfocusedBorderColor = CosmicSurfaceLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tarot_question_input")
                    )
                }
            }
        }

        // 4. Interactive 3D Card Selection Area
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PureBlack),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MossGreen.copy(0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isRevealed) "Tu Destino Revelado" else "Elige $maxCardsToSelect arcanos de la baraja:",
                            color = MysticGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isRevealed || selectedIndices.isNotEmpty()) {
                            IconButton(onClick = { resetReading() }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Reiniciar", tint = MysticGold)
                            }
                        }
                    }

                    if (!isRevealed) {
                        Text(
                            text = "Seleccionados: ${selectedIndices.size} de $maxCardsToSelect",
                            color = if (selectedIndices.size == maxCardsToSelect) MysticGoldLight else OffWhite.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isRevealed) {
                        // Interactive Face-Down Grid containing the complete 22 Major Arcana cards
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        ) {
                            val density = LocalDensity.current.density
                            // Grid of Tarot card backs for pick action
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                itemsIndexed(fullTarotDeck) { index, card ->
                                    val isSelected = selectedIndices.contains(index)
                                    // Animated selection hover scales & offsets
                                    val scale by animateFloatAsState(if (isSelected) 1.1f else 1.0f)
                                    val elevation by animateDpAsState(if (isSelected) 8.dp else 2.dp)
                                    val borderAlpha by animateFloatAsState(if (isSelected) 1.0f else 0.3f)

                                    // Custom 3D Card Back representation 
                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(0.6f)
                                            .graphicsLayer {
                                                scaleX = scale
                                                scaleY = scale
                                                cameraDistance = 12f * density
                                            }
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(CosmicSurface)
                                            .border(
                                                BorderStroke(
                                                    width = if (isSelected) 2.dp else 1.dp,
                                                    color = if (isSelected) MysticGold else MysticGoldLight.copy(alpha = borderAlpha)
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                if (isSelected) {
                                                    selectedIndices.remove(index)
                                                } else {
                                                    if (selectedIndices.size < maxCardsToSelect) {
                                                        selectedIndices.add(index)
                                                    }
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Mystical geometric pattern back
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(2.dp)
                                        ) {
                                            Text("👁️", fontSize = 16.sp)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "S • S",
                                                color = MysticGold.copy(0.7f),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("✨", fontSize = 10.sp, color = MysticGoldLight)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Revelado Layout displaying chosen cards with beautiful 3D rotating animation
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            selectedIndices.forEachIndexed { i, index ->
                                val card = fullTarotDeck[index]

                                // 3D Flip Rotation State Machine
                                var flipped by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    delay(100L * i) // staggered flip delay
                                    flipped = true
                                }
                                val rotationY by animateFloatAsState(
                                    targetValue = if (flipped) 180f else 0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 6.dp)
                                        .graphicsLayer {
                                            this.rotationY = rotationY
                                            cameraDistance = 12f * density
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Render appropriate face depending on 3D turn angle
                                    if (rotationY <= 90f) {
                                        // Back
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(0.6f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(CosmicSurface)
                                                .border(2.dp, MysticGold, RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("✨", fontSize = 24.sp, color = MysticGold)
                                        }
                                    } else {
                                        // Front (Mirror flipped so text reads normally!)
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(0.6f)
                                                .graphicsLayer {
                                                    this.rotationY = 180f
                                                }
                                                .border(
                                                    BorderStroke(1.5.dp, MysticGold),
                                                    RoundedCornerShape(12.dp)
                                                ),
                                            colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = when (selectedSpread) {
                                                        "Tríptica del Tiempo" -> {
                                                            when (i) {
                                                                0 -> "ANTE"
                                                                1 -> "PRES"
                                                                else -> "POST"
                                                            }
                                                        }
                                                        "Tirada de Sí o No" -> {
                                                            when (i) {
                                                                0 -> "SÍ"
                                                                else -> "NO"
                                                            }
                                                        }
                                                        "Cruz Alquímica" -> {
                                                            when (i) {
                                                                0 -> "INIC"
                                                                1 -> "REPR"
                                                                2 -> "ALI"
                                                                else -> "FIN"
                                                            }
                                                        }
                                                        else -> "ARCANO"
                                                    },
                                                    color = MysticGold,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center
                                                )

                                                Text(
                                                    text = card.emoji,
                                                    fontSize = 24.sp,
                                                    textAlign = TextAlign.Center
                                                )

                                                Text(
                                                    text = card.title.substringBefore(" ("),
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center,
                                                    lineHeight = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Activation Button trigger
                    if (selectedIndices.size == maxCardsToSelect && !isRevealed) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isShuffling = true
                                    delay(1000) // Cosmic resonance delay
                                    isShuffling = false
                                    isRevealed = true

                                    val drawn = selectedIndices.map { fullTarotDeck[it] }
                                    // Save Tarot reading into memory database
                                    viewModel.saveTarotReading(
                                        spread = selectedSpread,
                                        prompt = customQuestionInput.ifEmpty { "Consulta Gnostica en Synapse Studio" },
                                        cards = drawn.map { it.title },
                                        interpretation = drawn.joinToString(" || ") { "${it.title}: ${it.meaning}" },
                                        style = selectedDeckStyle
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("tarot_shuffle_button"),
                            enabled = !isShuffling
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PureBlack)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isShuffling) "Canalizando Eter..." else "Develar Arcanos de Synapse",
                                color = PureBlack,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 5. Layout drawing the selected Cards details
        if (isRevealed) {
            item {
                Text(
                    text = "Consonancias Esotéricas de Synapse:",
                    color = MysticGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            selectedIndices.forEachIndexed { i, index ->
                val card = fullTarotDeck[index]
                item {
                    val labelPosition = when (selectedSpread) {
                        "Tríptica del Tiempo" -> {
                            when (i) {
                                0 -> "PASADO: Origen / Arraigo Celestial"
                                1 -> "PRESENTE: Desafío / Umbral de Gnosis"
                                else -> "FUTURO: Destino / Síntesis de Sombra"
                            }
                        }
                        "Tirada de Sí o No" -> {
                            when (i) {
                                0 -> "SÍ / SENDERO SOLAR: Acciones, luz y posibilidades proactivas"
                                else -> "NO / SENDERO LUNAR: Advertencias, freno y repliegue"
                            }
                        }
                        "Cruz Alquímica" -> {
                            when (i) {
                                0 -> "EL CONSULTANTE: Estado vibracional y ego actual"
                                1 -> "EL OBSTÁCULO: Impulso reprimido o sombra a resolver"
                                2 -> "EL ALIADO SUTIL: Luz consciente y guía alquímica"
                                else -> "LA REVELACIÓN: Desenlace del Sendero de Transmutación"
                            }
                        }
                        else -> "REVELACIÓN ACCESIBLE: El Origen de la Consulta"
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MysticGold.copy(0.5f), RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column {
                            // Holographic Header Brush
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(MossGreen, CosmicSurface)
                                        )
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = labelPosition,
                                    color = MysticGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = card.emoji,
                                        fontSize = 24.sp,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = card.title,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = card.desc,
                                    color = OffWhite.copy(0.7f),
                                    fontSize = 12.sp
                                )
                                Divider(modifier = Modifier.padding(vertical = 10.dp), color = MossGreen)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = MysticGold, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = card.meaning,
                                        color = MysticGoldLight,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Deep Synapse AI Interpretation Trigger
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MossGreen.copy(0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¿Deseas descifrar el hilo de sabiduría completo?",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                val drawn = selectedIndices.map { fullTarotDeck[it] }
                                val cardsPrompt = drawn.joinToString(" y ") { "${it.title}: ${it.desc}" }
                                val finalQuestion = customQuestionInput.ifEmpty { "un análisis general de sincronicidad en este ciclo natal" }
                                viewModel.sendChatMessage("He realizado la tirada de Tarot '${selectedSpread}' de mi '$selectedDeckStyle'. Saqué las siguientes cartas: $cardsPrompt. Mi pregunta sagrada: $finalQuestion. Elabora tu interpretación profunda como la inteligencia cuántica mística de Synapse STUDIO conectándola con mi sombra y arquetipo.")
                                onNavigate("synapse_chat")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MysticGold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("tarot_ai_interpret_button")
                        ) {
                            Text("Interpretar con Synapse STUDIO AI", color = PureBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

data class TarotCard(
    val title: String,
    val emoji: String,
    val desc: String,
    val meaning: String
)
