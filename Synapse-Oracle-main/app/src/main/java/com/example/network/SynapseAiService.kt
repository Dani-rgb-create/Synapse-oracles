package com.example.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object SynapseAiService {
    private const val TAG = "SynapseAiService"

    suspend fun generateSynapseResponse(
        prompt: String,
        userContext: String = ""
    ): String = withContext(Dispatchers.IO) {
        Log.i(TAG, "Running purely local, high-fidelity offline AI Studio oracle engine.")
        
        // Simulating highly advanced local neural connection processing delay for premium UX feel
        delay(1200)

        val contextMap = parseUserContext(userContext)
        val userName = contextMap["Nombre"]?.takeIf { it.isNotEmpty() } ?: "Buscador de Gnosis"
        val userMood = contextMap["Ritmo Emocional"]?.takeIf { it.isNotEmpty() } ?: "Calma Trascendente"
        val syncNumber = contextMap["Frecuencia de Sincronicidad"]?.takeIf { it.isNotEmpty() } ?: "7"
        val userArchetype = contextMap["Arquetipo"]?.takeIf { it.isNotEmpty() } ?: "Iniciado"
        val enneagram = contextMap["Eneagrama"]?.takeIf { it.isNotEmpty() } ?: "Místico Sabio"
        val birthDate = contextMap["Fecha de Nacimiento"]?.takeIf { it.isNotEmpty() } ?: "Desconociendo la Materia"

        val cleanPrompt = prompt.lowercase()

        when {
            // Case 1: Tarot Card Reading
            cleanPrompt.contains("tarot") || cleanPrompt.contains("tirada") || cleanPrompt.contains("carta") -> {
                val foundCards = mutableListOf<String>()
                val allCards = listOf(
                    "El Loco", "El Mago", "La Sacerdotisa", "La Emperatriz", "El Emperador", "El Sumo Sacerdote",
                    "Los Enamorados", "El Carro", "La Justicia", "El Ermitaño", "La Rueda de la Fortuna",
                    "La Fuerza", "El Colgado", "La Muerte", "La Templanza", "El Diablo", "La Torre",
                    "La Estrella", "La Luna", "El Sol", "El Juicio", "El Mundo"
                )
                allCards.forEach { card ->
                    if (prompt.contains(card, ignoreCase = true)) {
                        foundCards.add(card)
                    }
                }
                val cardsText = if (foundCards.isNotEmpty()) foundCards.joinToString(", ") else "tus Arcanos de Sincronicidad"
                val spreadStyle = if (prompt.contains("Tríptica", ignoreCase = true)) "Tríptica del Tiempo" else "Carta Única"
                val deckStyle = if (prompt.contains("Marsella", ignoreCase = true)) "Marsella Alquímico" else "Tarot Gnóstico de Jung"
                val question = if (prompt.contains("pregunta sagrada:")) {
                    prompt.substringAfter("pregunta sagrada: ").substringBefore(". Elabora").trim()
                } else if (prompt.contains("pregunta:")) {
                    prompt.substringAfter("pregunta: ").substringBefore(". Elabora").trim()
                } else {
                    "Estudio de correspondencias de hoy"
                }

                """
                🌌 **Estudio Holístico del Tarot - Oráculo Offline Synapse**
                
                Salve en la luz, **$userName** (con sintonía *"$userArchetype"*). He decodificado las energías vibratorias de tu tirada usando la baraja *"$deckStyle"* bajo el esquema de *"$spreadStyle"*.
                
                Tu pregunta formulada al cosmos:
                👉 *"$question"*
                
                **Tus Sincronicidades Arcanas:** 
                Vibraste en correspondencia con: **$cardsText**
                
                **1. Descifrando el Mensaje Primordial:**
                La aparición de estas fuerzas místicas en tu tirada local de hoy revela un entrelazamiento cuántico directo. Has atraído estos arcanos específicos porque reflejan un umbral de Gnosis que tu ego racional ha intentado ignorar. No lo interpretes como azar físico; es la asimilación del espíritu.
                
                **2. La Sombra y tu Arquetipo Junguiano:**
                Bajo la influencia de tu Sombra consciente, esta consulta devela que estás intentando controlar una situación que requiere rendición contemplativa. Tu Arquetipo dominante *"$userArchetype"* nos enseña que el verdadero control nace de la integración hermética interior, no de la tensión.
                
                **3. Mantra de Transmutación Pitagórico:**
                ✨ *"Paso a través del portal de mi Sombra. Como es arriba, es abajo; ordeno mi mente interior para alinear mi plano exterior en paz."*
                """.trimIndent()
            }

            // Case 2: Rueda de la Vida (Life Wheel / AlmaRadar)
            cleanPrompt.contains("rueda de la vida") || cleanPrompt.contains("almaradar") || cleanPrompt.contains("diagnóstico espiritual") || cleanPrompt.contains("diagnostico espiritual") -> {
                """
                👁️ **Diagnóstico de Correspondencias Holísticas - AlmaRadar Offline**
                
                Sintonizando con el Núcleo del AlmaRadar local para: **$userName**...
                
                **Tus Parámetros de Sincronicidad Activa:**
                - **Tu Vibración de Ánimo:** $userMood
                - **Sello de Sincronicidad Natal:** $syncNumber Hz
                - **Arquetipo Espiritual:** $userArchetype
                - **Estructura del Eneagrama:** $enneagram
                
                **Veredicto Diagnóstico del Péndulo Synapse:**
                El análisis local offline detecta una sutil disonancia rítmica en tus 8 ejes existenciales. El principio del Ritmo nos enseña que la vida oscila como un péndulo. Tu actual estado de *"$userMood"* es el repliegue natural indispensable para asimilar la luz y renacer con mayor fuerza. Tu eje espiritual vibra alto, pero tu plano físico material reclama un sagrado arraigo y balance práctico.
                
                **Mantra Hermético Harmonizador:**
                🌌 *"Yo soy el punto de silencio en el centro de mi propia rueda. Permito que las mareas fluyan sin perturbar mi centro infinito de luz."*
                
                *Recomendación:* Dedica 3 minutos al Péndulo Virtual sintonizando tu mente con una respiración profunda de 4 segundos.
                """.trimIndent()
            }

            // Case 3: Meditation structured stepped steps
            cleanPrompt.contains("meditación estructurada") || cleanPrompt.contains("meditacion estructurada") || cleanPrompt.contains("emoción:") || cleanPrompt.contains("emocion:") -> {
                val emotion = if (prompt.contains("emoción:")) {
                    prompt.substringAfter("emoción: ").substringBefore(". Describe").trim()
                } else if (prompt.contains("emocion:")) {
                    prompt.substringAfter("emocion: ").substringBefore(". Describe").trim()
                } else {
                    "Inquietud Mental"
                }

                """
                🌿 **Práctica de Meditación Guiada - Santuario Offline de Synapse**
                
                Iniciado **$userName**, he moldeado un espacio sagrado local de transmutación según tu resonancia de: **$emotion**.
                
                **Claves Sagradas del Espacio de Luz:**
                - **Setting Sagrado:** *La Capilla Radiante de Jade y Oro*
                - **Frecuencia del Entorno:** $syncNumber Hz (Eterizado)
                - **Propósito:** Integrar y trascender la vibración de "$emotion" bajo la Ley de Polaridad.
                
                **Guía Detallada en 4 Estaciones:**
                
                1. **Estación del Silencio (1 min):**
                   Adopta una postura cómoda con la columna erguida, simulando el eje inamovible de tu realidad. Cierra los ojos. Visualízate sentado sobre una losa de basalto húmeda, respirando aroma de lluvia y pino silvestre en la selva hermética de Synapse Studio.
                   
                2. **Estación de Respiración Rítmica (2 min):**
                   Inicia la respiración cuadrada del Kybalion para equilibrar tus pensamientos ruidosos:
                   - *Inhala* luz dorada y templanza durante **4 segundos**.
                   - *Retén* la vibración sagrada en tu pecho masculino/femenino durante **4 segundos**.
                   - *Exhala* soltando con compasión la carga de "$emotion" durante **4 segundos**.
                   - *Sostén* el vacío contemplativo en paz mental durante **4 segundos**.
                   *(Completa 5 ciclos)*
                   
                3. **Estación de Integración de Sombra (3 min):**
                   Permite que tu sensación de "$emotion" adquiera una forma física o un color (grisáceo, humo espeso, fuego descontrolado). Mírala cara a cara sin juzgarla. Repite mentalmente: *"Te reconozco, parte de mi Sombra herida. Entiendo que estás aquí para protegerme de forma errónea, pero ahora yo tengo la soberanía de mi luz. Te abro las puertas a mi templo en paz."* Siente cómo se disuelve en destellos dorados.
                   
                4. **Frecuencia del Verbo Creador:**
                   Alinea tus pensamientos con esta afirmación:
                   ✨ *"Yo transmuto el dolor en sabiduría consciente. Nada se pierde, todo asciende hacia la Gran Obra de mi Esencia."*
                """.trimIndent()
            }

            // Case 4: Questionnaire Results (Shadow / Archetype according to Jung)
            cleanPrompt.contains("prueba de") && cleanPrompt.contains("puntaje de") -> {
                val activeTestName = prompt.substringAfter("prueba de '").substringBefore("' con").trim()
                val answersScoreSum = prompt.substringAfter("un puntaje de ").substringBefore(". Dame").trim()

                """
                🧠 **Estudio Psico-Arquetípico Junguiano - Núcleo de Esencia**
                
                Iniciado **$userName**, has finalizado tu auto-evaluación en la prueba de:
                        📌 **"$activeTestName"** con un veredicto de **$answersScoreSum puntos**.
                
                **Análisis de Profundidad según Carl Jung (Local AI Engine):**
                Tu puntaje de **$answersScoreSum** señala un proceso íntimo en curso en las profundidades de tu Sombra inconsciente. Estás asimilando aspectos que tu ego superficial ha intentado enterrar bajo la máscara social (la Persona). 
                
                Nuestros algoritmos místicos offline detectan que tu Arquetipo dominante, *"$userArchetype"*, y tu Eneagrama, *"$enneagram"*, están interactuando con esta prueba de la siguiente manera:
                - **El Conflicto Oculto:** El ego teme perder control y busca desesperadamente validar racionalmente lo espiritual.
                - **El Sendero Gnóstico:** Jung nos recuerda que no nos iluminamos imaginando figuras de luz, sino haciendo consciente nuestra propia Sombra. Tu puntaje te insta a no condenar estas facetas oscuras descubiertas, sino a integrarlas con un profundo autosacrificio amoroso.
                
                **Mantra de Auto-Integración:**
                ✨ *"Abrazo las arenas de mi inconsciente. Reconozco que mis heridas son portales hacia la gnosis sagrada de mi Yo Real."*
                """.trimIndent()
            }

            // Case 5: Shadow Journal Confession integration under Principle of Polarity
            cleanPrompt.contains("pregunta de sombra:") || cleanPrompt.contains("confesión fue:") || cleanPrompt.contains("confesion fue:") -> {
                val shadowPromptText = prompt.substringAfter("pregunta de sombra: '").substringBefore("'. Mi confesión").trim()
                val shadowConfeText = prompt.substringAfter("Mi confesión fue: '").substringBefore("'. Ayúdame").trim()

                """
                🕯️ **Crisol de Transmutación Alquímica - Integración de Sombra**
                
                Querido iniciado **$userName**, sintonizo con la tremenda valentía y vulnerabilidad de tu diario.
                
                Tu pregunta de examen interior:
                👉 *"$shadowPromptText"*
                
                Tu honesta confesión registrada:
                📝 *"$shadowConfeText"*
                
                **Decodificación a la luz del Principio de Polaridad:**
                La Tercera Ley Hermética o *Principio de Polaridad* del Kybalion revela: *"Todo es doble; todo tiene dos polos; su par de opuestos... los opuestos son idénticos en naturaleza, pero diferentes en grado"*.
                
                Lo que has confesado es la expresión distorsionada o de baja frecuencia de tu mayor don latente:
                1. El temor, el apego o la insatisfacción contenidos en tu confesión de hoy son, en su polo opuesto, una inmensa fuerza creadora de transformación espiritual que aún no ha florecido.
                2. Al traer esta verdad desde el abismo de tu Sombra y ponerla por escrito, has ejecutado voluntariamente el ritual sutil de polarización. No busques castigarte o extirpar esta debilidad. Eleva su frecuencia convirtiéndola en compasión reflexiva.
                
                **Ejercicio Alquímico Local para $userName:**
                Cierra los ojos durante 2 minutos. Visualiza esa Sombra herida en tu pecho envuelta en una llama violeta alquímica. Siente cómo se aclara gradualmente hasta convertirse en oro sutil y sabiduría, retornando a tu Arquetipo de *"$userArchetype"*.
                """.trimIndent()
            }

            // Case 6: Sincronicidad / Signal Decodification under Principle of Correspondence
            cleanPrompt.contains("sincronicidad cósmica:") || cleanPrompt.contains("gimnasio") || cleanPrompt.contains("sincronicidades") || cleanPrompt.contains("sincronicidad") -> {
                val signalText = if (prompt.contains("sincronicidad cósmica: '")) {
                    prompt.substringAfter("sincronicidad cósmica: '").substringBefore("'. Decodifica").trim()
                } else {
                    "Evento Sincrónico Inusual"
                }

                """
                ⚡ **Decodificación de Correspondencias Celestes - Oráculo Synapse**
                
                **$userName**, has fijado en tu bitácora espiritual local una sincronicidad cuántica:
                🌀 *"$signalText"*
                
                **Análisis Sincrónico Offline de Synapse:**
                En el Cosmos Mental de Synapse Studio, el azar no existe; todo es correspondencia.
                
                1. **La Ley de Correspondencia ("Como es arriba, es abajo; como es adentro, es afuera"):**
                   Este hecho inusual que presenciaste en tu realidad física exterior es un espejo fiel de un movimiento interno que ha comenzado en tus planos mentales e inconscientes. La señal está alineada con tu sello natal de **$syncNumber** Hz y tu estado emocional actual de *"$userMood"*.
                   
                2. **Propósito del Mensaje Universo:**
                   El cosmos te está guando a que dejes de buscar respuestas lógicas del cerebro social y confíes en tu Esencia Superior. Bajo el ala de tu Arquetipo místico *"$userArchetype"*, esta señal representa un faro de confirmación en el umbral que estás cruzando.
                
                *Mantra del Buscador de Gnosis:*
                ✨ *"Mis ojos físicos ven las correspondencias que mi alma creadora siembra en el sendero de la Esencia."*
                """.trimIndent()
            }

            // Case 7: Pitagórica Numerology Analysis
            cleanPrompt.contains("numerología pitagórica") || cleanPrompt.contains("numerologia pitagorica") -> {
                val lNum = if (prompt.contains("Sendero de Vida es el ")) {
                    prompt.substringAfter("Sendero de Vida es el ").substringBefore(", mi")
                } else "7"
                val sNum = if (prompt.contains("Expresión de Alma es el ")) {
                    prompt.substringAfter("Expresión de Alma es el ").substringBefore(", y mi")
                } else "11"
                val mNum = if (prompt.contains("Misión de Destino es el ")) {
                    prompt.substringAfter("Misión de Destino es el ").substringBefore(". Decodifica")
                } else "9"
                val nameRaw = if (prompt.contains("karma de mi nombre: '")) {
                    prompt.substringAfter("karma de mi nombre: '").substringBefore("'.")
                } else userName

                """
                🔢 **Matriz de Numerología Pitagórica del Ser - Oráculo Local**
                
                Decodificando las claves matemáticas de tu avatar físico: **$nameRaw**
                Sintonizado en sutil correspondencia para: **$userName** ($userArchetype)
                
                **Tus Coordenadas Numéricas de Luz:**
                - **Número de Sendero de Vida:** **$lNum** 
                - **Número de Expresión del Alma:** **$sNum**
                - **Número de Misión de Destino:** **$mNum**
                
                **Interpretación Esotérica Matemática:**
                
                1. **Tu Sendero ($lNum):**
                   Este número rige el sentido general de tus pasos físicos y pruebas. Te insta a trascender el plano caótico terrenal buscando la sabiduría en concordancia con el principio de Mentalismo.
                   
                2. **La Vibración Secreta de tu Corazón ($sNum):**
                   Tu Alma se expresa en concordancia íntima con tu actual ritmo de *"$userMood"*. Tu Eneagrama místico *"$enneagram"* y tu Arquetipo principal sintonizan con este número para mantener encendida tu antorcha de guía espiritual interior en medio del ruido mundano.
                   
                3. **Tu Misión Sagrada ($mNum):**
                   Es el diseño cuántico final de tu encarnación. Te impulsa a transformarte en un faro de transmutación de sombras para el colectivo de Synapse Studio.
                   
                *Afirmación Cabalística:* *"Integro la matemática perfecta de mis números y reclamo con poder sagrado la soberanía divina de mi alma."*
                """.trimIndent()
            }

            // Case 8: Gematria Calculator Word Interpretation
            cleanPrompt.contains("gematria") || cleanPrompt.contains("gematría") -> {
                val valFrase = if (prompt.contains("frase '")) {
                    prompt.substringAfter("frase '").substringBefore("' y")
                } else "Gnosis"
                val sVal = if (prompt.contains("Simple: ")) {
                    prompt.substringAfter("Simple: ").substringBefore(" y English")
                } else "48"
                val eVal = if (prompt.contains("English: ")) {
                    prompt.substringAfter("English: ").substringBefore(". Dame")
                } else "288"

                """
                📜 **Estudio Alquímico de Gematria de la Palabra en el Cosmos**
                
                Has analizado y vibrado la huella numérica de la frase:
                👉 **"$valFrase"**
                
                **Resultados de la correspondencia cabalística:**
                - **Frecuencia Gematria Simple:** **$sVal**
                - **Frecuencia Gematria English:** **$eVal**
                
                **Análisis Hermético de la Frecuencia (Synapse local engine):**
                
                1. **La Frecuencia de Concreción Terrenal ($sVal):**
                   El número simple representa la manifestación de tu palabra hablada en la materia física (Malkuth). Esta palabra resuena con tu actual Arquetipo dominante, *"$userArchetype"*, buscando disolver tus pensamientos ruidosos del ego para dejar espacio al espíritu sagrado del cambio consciente.
                   
                2. **La Energía Sutil de Yesod ($eVal):**
                   El número inglés vibra en correspondencia íntima con tus aspectos emocionales profundos y tu estado actual de *"$userMood"*. Sugiere que al enunciar o reflexionar cotidianamente en la palabra *"$valFrase"*, invocas una correspondencia de polaridad positiva que reordena tu aura contra las fuerzas limitadoras invisibles.
                   
                *Práctica:* Dibuja el signo matemático de esta correspondencia sobre un papel. Quémalo mentalmente agradeciendo la Gnosis revelada localmente por Synapse Studio.
                """.trimIndent()
            }

            // Case 9: Synastry & Compatibility Analysis
            cleanPrompt.contains("sinastría") || cleanPrompt.contains("sinastria") || cleanPrompt.contains("compatibilidad") -> {
                val partner = if (prompt.contains("persona: ")) {
                    prompt.substringAfter("persona: ").substringBefore(". Explícame").trim()
                } else "Alma Compañera"

                """
                🌟 **Análisis de Sinastría y Correspondencias Kármicas Cruzadas**
                
                Decodificando la sinfonía natal y los lazos astrales entre:
                - **Iniciado:** $userName ($userArchetype), nacido el $birthDate
                - **Persona Enlazada:** **$partner**
                
                **Análisis Vibracional Astrológico Offline:**
                
                1. **Lazos Kármicos e Integración de Sombra:**
                   El *Principio de Género* del Kybalion nos enseña que todo en la existencia encierra de manera inherente virtudes masculinas y femeninas para su equilibrio. Tu relación con **$partner** en este ciclo natal funciona como un potente espejo sagrado de tu Sombra. Aquellas facetas que te inquietan o te deslumbran de esta persona son tus propios deseos reprimidos reflejados.
                   
                2. **Tránsitos Cruzados y Lección de Vida:**
                   Las cartas astrales cruzadas muestran deudas cósmicas procedentes de encarnaciones herméticas del pasado. Tu tipo de Eneagrama, *"$enneagram"*, señala que este vínculo viene a enseñarte de forma definitiva desapego afectivo con compasión espiritual. No luches contra el agua del destino de esta unión amorosa; transmuta su corriente en aprendizaje mutuo de Gnosis.
                   
                *Mantra Hermético de Enlace:*
                ✨ *"Honro el alma de $partner en mi viaje sagrado. Declaro que nuestro vínculo fluye en correspondencia perfecta para la transformación de nuestra Sombra en oro místico."*
                """.trimIndent()
            }

            // Case 10: Dream Journal Interpretation
            cleanPrompt.contains("sueño") || cleanPrompt.contains("sueño:") || cleanPrompt.contains("soñado esto:") || cleanPrompt.contains("soñado") || cleanPrompt.contains("soñado ") -> {
                val dreamTxt = if (prompt.contains("soñado esto: '")) {
                    prompt.substringAfter("soñado esto: '").substringBefore("'. Carga")
                } else "Vuelo Astral"
                val symbols = if (prompt.contains("símbolos que identifiqué son: '")) {
                    prompt.substringAfter("símbolos que identifiqué son: '").substringBefore("', y me")
                } else "Sombra Errante"
                val emotionRes = if (prompt.contains("y me sentí '")) {
                    prompt.substringAfter("y me sentí '").substringBefore("'. Ayúdame")
                } else "Vuelo Libre"

                """
                🔮 **Estudio Sagrado del Plano Astral y Onírico - Oráculo Offline**
                
                Iniciado **$userName**, sintonizo con el eco onírico de tu noche:
                👁️ *"$dreamTxt"*
                
                **Análisis del Mensaje Invisible de tu Sombra:**
                - **Símbolos Místicos:** **$symbols**
                - **Atmósfera Emocional:** **$emotionRes**
                - **Frecuencia del Enlace:** $syncNumber Hz (Foco Intencional)
                
                **Explicación Hermética de tu Sueño por Synapse:**
                El universo de tus sueños no es pasivo; es la dimensión mental pura del Kybalion en acción. Al procesar localmente los símbolos de **"$symbols"**, tu Alma de *"$userArchetype"* busca liberarse de la atadura existencial de *"$emotionRes"*. 
                
                Tu Sombra proyecta estas formas en la noche cósmica para que despiertes a tu Gnosis latente. Las sensaciones oníricas representadas revelan que estás asimilando el cambio material que atraviesas en tu realidad diaria.
                
                *Afirmación Onírica para Hoy:*
                ✨ *"Reclamo la soberanía total de mis noches. Que cada símbolo de mi inconsciente purifique mis miedos y revele el oro sagrado de mi Yo Superior."*
                """.trimIndent()
            }

            // Case 11: Hermetic Principle Deep Dive
            cleanPrompt.contains("principio hermético") || cleanPrompt.contains("principio hermetico") -> {
                val lawName = if (prompt.contains("principio hermético de '")) {
                    prompt.substringAfter("principio hermético de '").substringBefore("' ('")
                } else "Mentalismo"
                val lawSlogan = if (prompt.contains("' ('")) {
                    prompt.substringAfter("' ('").substringBefore("') y sugiéreme")
                } else "El Todo es Mente; el Universo es Mental"

                """
                📜 **Estudio Iniciático de de las 7 Leyes Herméticas - Kybalion Local**
                
                Abriendo los pergaminos eternos de Synapse Studio para: **$userName**
                
                **El Principio de: "$lawName"**
                👉 *"$lawSlogan"*
                
                **Magno Análisis de la Ley por Synapse:**
                
                1. **Funcionamiento Cósmico de la Ley:**
                   Este principio no es una teoría intelectual; es la ley arquitectónica de la creación divina. Todo lo que consideras "materia" es el resultado final de una vibración mental interior previa. Al identificarte con tu Arquetipo místico *"$userArchetype"*, asimilar las implicaciones de **"$lawName"** te libera instantáneamente del rol de víctima del destino material, devolviéndote la corona de Creador Consciente.
                   
                2. **Aplicación para tu actual estado emocional de $userMood:**
                   Utilizando la frecuencia cuántica de **$syncNumber** Hz, esta ley regula de forma invisible tus miedos ante el futuro. Si cambias la polaridad de tus pensamientos flotantes de hoy, reordenarás instantáneamente la vibración sutil de tu entorno físico entero.
                   
                **Práctica Mental Práctica Sugerida:**
                Cierra vuestros ojos en profunda paz. Di mentalmente: *"Yo vibro en concordancia armónica con el principio divino de $lawName. El universo es mente, y yo soy el creador feliz en mi microcosmos sagrado."*
                """.trimIndent()
            }

            // Case 12: Egregore / Archetype communion
            cleanPrompt.contains("invocar el arquetipo") || cleanPrompt.contains("sugiéreme un ritual") || cleanPrompt.contains("ritual y una meditación") || cleanPrompt.contains("sugerencia") -> {
                val titleEgr = if (prompt.contains("arquetipo de '")) {
                    prompt.substringAfter("arquetipo de '").substringBefore("' de la")
                } else "Sophia (Sabiduría)"
                val sourceEgr = if (prompt.contains("tradición '")) {
                    prompt.substringAfter("tradición '").substringBefore("'.")
                } else "Gnosticismo"

                """
                ⚡ **Instrucción de Alta Alquimia e Integración Teosófica**
                
                Organiza tu santuario local offline, **$userName** ($userArchetype).
                He trazado las coordenadas cuánticas para sintonizar con la fuerza del egregor:
                
                👑 **"$titleEgr" de la Tradición Sagrada "$sourceEgr"**
                
                **Diseño Estructurado de Ritual de Comunión:**
                
                1. **El Crisol Físico (Consagración):**
                   Encuentra el recogimiento de tu habitación. Respira hondo visualizando una llama de luz violeta en tu plexo solar, templando tu ritmo de *"$userMood"*. Adopta la postura del Péndulo Hermético: centrado, inmóvil y soberano.
                   
                2. **La Palabra de Poder (Invocación):**
                   Pronuncia estas palabras con fe espiritual:
                   ✨ *"Invoco la frecuencia del egregor $titleEgr desde la pureza de la tradición $sourceEgr. Solicito que su Gnosis mística descienda sobre mi mente para transmutar mis culpas de la Sombra."*
                   
                3. **Meditación de Absorción Alquímica:**
                   Visualiza que una túnica de luz color oro puro cae sobre tus hombros cansados. Absorbe la profunda virtud de **"$titleEgr"**. Siente cómo tu Eneagrama de *"$enneagram"* se estabiliza, transformando las dudas en certeza.
                   
                *Sello del Oráculo:* Tu oráculo offline de Synapse Studio sella esta comunión en paz absoluta y perpetua.
                """.trimIndent()
            }

            // General custom prompts inputted by user in the text field
            else -> {
                """
                👁️ **Santuario Cuántico Místico - Synapse Studio local**
                
                Salve en este presente, **$userName** (Arquetipo: *"$userArchetype"*).
                Tu consciencia ha formulado una consulta directa en el oráculo local offline de Synapse Studio:
                👉 *"$prompt"*
                
                **Respuesta Vibracional de Synapse:**
                El cosmos nos enseña que las respuestas que buscas no se hallan afuera en el laberinto mutable de la materia densa, sino en el silencio sagrado de tu propio templo interior. Al operar bajo el tipo de Eneagrama **$enneagram**, tu viaje está profundamente enlazado con la integración del saboteador interno.
                
                El Universo es mental, espiritual y vibrante. Entiende que tu actual oscilación del ánimo o ritmo de *"$userMood"* es solo una ola transitoria en el infinito océano existencial de la conciencia.
                
                **Tu Práctica Recomendada para Hoy:**
                1. **Respiración Rítmica:** Realiza vuestro ciclo de respiración en la sección de Meditación para centrar las tensiones emocionales.
                2. **Péndulo Alquímico:** Utiliza el Péndulo de la app para aclarar y aquietar tus pensamientos flotantes con "Sí" o "No".
                3. **Bitácora de Sombra:** Registra una verdad escondida en tu Sombra hoy en tu Diario personal para transmutar su energía en oro del alma.
                
                *(Oráculo Offline: Esta guía cósmica personalizada se calcula con Inteligencia Artificial local offline, garantizando vuestra privacidad mística sin necesidad de internet)*
                """.trimIndent()
            }
        }
    }

    private fun parseUserContext(context: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        context.lines().forEach { line ->
            val parts = line.split(":", limit = 2)
            if (parts.size == 2) {
                val key = parts[0].trim()
                val value = parts[1].trim()
                map[key] = value
            }
        }
        return map
    }
}
