package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
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
import com.example.ui.theme.*
import com.example.viewmodel.EsotericViewModel

@Composable
fun EternalWisdomScreen(
    viewModel: EsotericViewModel,
    onNavigate: (String) -> Unit
) {
    var expandedPrincipleName by remember { mutableStateOf<String?>(null) }

    val principles = listOf(
        HermeticLaw(
            "1. El Mentalismo",
            "El Todo es Mente; el Universo es Mental.",
            "Toda tu realidad material se engendra en la fuente primordial de tu pensamiento consciente e inconsciente. Eres el arquitecto de tu propio cosmos."
        ),
        HermeticLaw(
            "2. La Correspondencia",
            "Como es arriba, es abajo; como es adentro, es afuera.",
            "Tu mundo espiritual interno se refleja fielmente en los sucesos exteriores de tu vida material. Modifica tu templo de sintonía interna para reordenar tu entorno solar."
        ),
        HermeticLaw(
            "3. La Vibración",
            "Nada está inmóvil; todo se mueve; todo vibra.",
            "Cada partícula de tu esencia vibra en frecuencias cósmicas. El sonido meditativo, los decretos de luz y la disolución de la sombra elevan instantáneamente tu estado alquímico."
        ),
        HermeticLaw(
            "4. La Polaridad",
            "Todo es doble; todo tiene dos polos; su par de opuestos.",
            "El amor y el temor son la misma emoción en escalas vibratorias contrarias. Puedes transmutar cualquier sufrimiento enfocando tu foco en el polo de luz."
        ),
        HermeticLaw(
            "5. El Ritmo",
            "Todo fluye y refluye; todo tiene sus períodos de avance y retroceso.",
            "La vida funciona como el movimiento de un péndulo. No catalogues el dolor como definitivo; es solo el retroceso que antecede al impulso evolutivo."
        ),
        HermeticLaw(
            "6. La Causa y el Efecto",
            "Toda causa tiene su efecto; todo efecto tiene su causa; el azar no existe.",
            "No existen las casualidades azarosas, sino sincronicidades regidas por las causas invisibles que siembras. Consecución cábala pura."
        ),
        HermeticLaw(
            "7. El Género",
            "El género existe por doquier; todo tiene sus principios masculino y femenino.",
            "La Gran Obra Alquímica consiste en consagrar y unificar la sinergia activa de tu hemisferio intuitivo sagrado con tu hemisferio racional ordenador."
        )
    )

    val egregores = listOf(
        EgregoreItem("Kether (Corona)", "Cábala", "La corona del Árbol de la Vida, la unidad primordial absoluta.", "Meditar con Kether disuelve divisiones del ego egoísta."),
        EgregoreItem("Malkuth (Reino)", "Cábala", "La base física del árbol, el arraigo con la Madre Tierra y la materia.", "Establece grounding y salud material."),
        EgregoreItem("Abraxas", "Práctica Gnóstica", "El arquetipo de la totalidad cósmica que aúna el bien y el mal trascendental.", "Ayuda a integrar la dualidad oculta."),
        EgregoreItem("Sophia (Sabiduría)", "Gnosticismo", "La chispa de inspiración femenina espiritual que reconecta al iniciado.", "Canaliza inspiración poética solar.")
    )

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
                text = "Sabiduría Eterna - Los Principios",
                color = MysticGold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            Text(
                text = "Los 7 Principios Herméticos del Kybalión",
                color = MysticGoldLight,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Expanded/collapse hermetic rows
        principles.forEach { law ->
            item {
                val isExpanded = expandedPrincipleName == law.name
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandedPrincipleName = if (isExpanded) null else law.name
                        },
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(law.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ExpandMore, contentDescription = null, tint = MysticGold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(law.subtitle, color = MysticGold, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                Text(law.desc, color = OffWhite.copy(0.8f), fontSize = 12.sp, lineHeight = 18.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.sendChatMessage("Explícame a profundidad el principio hermético de '${law.name}' ('${law.subtitle}') y sugiéreme un ejercicio mental práctico.")
                                        onNavigate("synapse_chat")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                                    modifier = Modifier.fillMaxWidth().testTag("law_ai_button")
                                ) {
                                    Text("Synapse Profundización", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Glossary Egregoras
        item {
            Text(
                text = "Glosario de Arquetipos Universales",
                color = MysticGoldLight,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        egregores.forEach { item ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(item.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "Tradición: ${item.source}",
                                color = MysticGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.desc, color = OffWhite.copy(0.8f), fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Consejo Meditativo: ${item.effect}",
                            color = MysticGoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                viewModel.sendChatMessage("Sugiéreme un ritual y una meditación completa para entrar en comunión e invocar el arquetipo de '${item.title}' de la tradición '${item.source}'.")
                                onNavigate("synapse_chat")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Meditar con este Arquetipo", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

data class HermeticLaw(
    val name: String,
    val subtitle: String,
    val desc: String
)

data class EgregoreItem(
    val title: String,
    val source: String,
    val desc: String,
    val effect: String
)
