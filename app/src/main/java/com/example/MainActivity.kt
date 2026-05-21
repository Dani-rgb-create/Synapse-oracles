package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.CircleShape
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.EsotericViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NucleoEsenciaTheme {
                val navController = rememberNavController()
                val esotericViewModel: EsotericViewModel = viewModel()
                
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "home"

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val profileState by esotericViewModel.userProfile.collectAsState()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = CosmicSurface,
                            drawerContentColor = Color.White,
                            modifier = Modifier.width(300.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(NightCosmos)
                                    .padding(16.dp)
                            ) {
                                // Header Banner
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CosmicSurface, RoundedCornerShape(12.dp))
                                        .border(1.dp, MysticGold.copy(0.2f), RoundedCornerShape(12.dp))
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "SYNAPSE STUDIO",
                                        color = MysticGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp
                                    )
                                    Text(
                                        text = "NÚCLEO ESENCIA",
                                        color = OffWhite,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Sintonizado: ${profileState?.name ?: "Buscador"}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Arquetipo: ${profileState?.mainArchetype ?: "Iniciado"}",
                                        color = MysticGoldLight,
                                        fontSize = 11.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "PASADIZOS DE LUZ",
                                    color = OffWhite.copy(0.5f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                                )

                                val drawerItems = listOf(
                                    Triple("home", "Santuario (Inicio)", Icons.Default.Home),
                                    Triple("synapse_chat", "Santuario Synapse", Icons.Default.ChatBubble),
                                    Triple("meditate", "El Templo (Meditar)", Icons.Default.SelfImprovement),
                                    Triple("tarot", "Tarot Sagrado", Icons.Default.AutoAwesome),
                                    Triple("self_knowledge", "Autoconocimiento", Icons.Default.Psychology),
                                    Triple("journey", "Viaje del Héroe", Icons.Default.Timeline),
                                    Triple("cloud_sync", "Alineación (Local)", Icons.Default.Save),
                                    Triple("archetypes", "Arquetipos del Alma", Icons.Default.Face)
                                )

                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    items(drawerItems.size) { index ->
                                        val (route, label, icon) = drawerItems[index]
                                        val isSelected = currentRoute == route
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) MossGreen.copy(0.3f) else Color.Transparent)
                                                .clickable {
                                                    scope.launch { drawerState.close() }
                                                    navController.navigate(route) {
                                                        popUpTo("home") { saveState = true }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = label,
                                                tint = if (isSelected) MysticGold else OffWhite.copy(0.8f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = label,
                                                color = if (isSelected) MysticGold else Color.White,
                                                fontSize = 13.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "V1.0.0 • Offline Alquímico",
                                        color = OffWhite.copy(0.4f),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            // Beautiful Floating Bottom Navigation Panel with Gold Accents
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .background(CosmicSurface)
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BottomNavItem(
                                    label = "Santuario",
                                    icon = Icons.Default.Home,
                                    isSelected = currentRoute == "home",
                                    modifier = Modifier.testTag("nav_home_button")
                                ) {
                                    navController.navigate("home") {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                
                                BottomNavItem(
                                    label = "Synapse",
                                    icon = Icons.Default.ChatBubble,
                                    isSelected = currentRoute == "synapse_chat",
                                    modifier = Modifier.testTag("nav_synapse_button")
                                ) {
                                    navController.navigate("synapse_chat") {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }

                                BottomNavItem(
                                    label = "El Templo",
                                    icon = Icons.Default.SelfImprovement,
                                    isSelected = currentRoute == "meditate",
                                    modifier = Modifier.testTag("nav_meditate_button")
                                ) {
                                    navController.navigate("meditate") {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }

                                BottomNavItem(
                                    label = "El Viaje",
                                    icon = Icons.Default.Timeline,
                                    isSelected = currentRoute == "journey",
                                    modifier = Modifier.testTag("nav_journey_button")
                                ) {
                                    navController.navigate("journey") {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            NavHost(
                                navController = navController,
                                startDestination = "home",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(NightCosmos)
                                    .padding(bottom = innerPadding.calculateBottomPadding()) // Adapt to bottom nave heights
                            ) {
                                composable("home") {
                                    HomeScreen(
                                        viewModel = esotericViewModel,
                                        onOpenDrawer = {
                                            scope.launch { drawerState.open() }
                                        },
                                        onNavigate = { target ->
                                            navController.navigate(target)
                                        }
                                    )
                                }
                                composable("synapse_chat") {
                                    SynapseChatScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("meditate") {
                                    MeditationScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("tarot") {
                                    TarotScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("self_knowledge") {
                                    SelfKnowledgeScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("astrology") {
                                    AstrologyScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("numerology") {
                                    NumerologyScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("divine_connection") {
                                    DivineConnectionScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("wisdome") {
                                    EternalWisdomScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("journey") {
                                    HeroJourneyScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("cloud_sync") {
                                    CloudSyncScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                                composable("archetypes") {
                                    ArchetypesScreen(viewModel = esotericViewModel) { target ->
                                        navController.navigate(target)
                                    }
                                }
                            }

                            // Interactive glowing Synapse STUDIO logo orb floating on top of all screens
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = innerPadding.calculateBottomPadding() + 16.dp, end = 16.dp)
                            ) {
                                SynapseStudioFloatingOrb(currentRoute = currentRoute) {
                                    if (currentRoute != "synapse_chat") {
                                        navController.navigate("synapse_chat") {
                                            popUpTo("home") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
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
}

@Composable
fun RowScope.BottomNavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val activeColor = MysticGold
    val inactiveColor = OffWhite.copy(alpha = 0.5f)

    Column(
        modifier = modifier
            .weight(1f)
            .clickable { onClick() }
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = if (isSelected) activeColor else inactiveColor,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SynapseStudioFloatingOrb(
    currentRoute: String,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SynapseOrbTransition")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraAlpha"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cosmicRotation"
    )

    val rippleScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auraRipple"
    )
    
    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleAlpha"
    )

    val isActiveRoute = currentRoute == "synapse_chat"

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(72.dp)
            .testTag("synapse_studio_floating_logo")
    ) {
        // Outer emanating ripple rings
        Box(
            modifier = Modifier
                .size(42.dp)
                .scale(rippleScale)
                .alpha(rippleAlpha)
                .border(1.5.dp, if (isActiveRoute) AstralRose else MysticGold, CircleShape)
        )

        // Mid glow aura
        Box(
            modifier = Modifier
                .size(52.dp)
                .scale(scale)
                .alpha(auraAlpha)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            (if (isActiveRoute) AstralRose else MysticGold).copy(0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Core interactive button
        Box(
            modifier = Modifier
                .size(50.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = if (isActiveRoute) {
                            listOf(CosmicSurfaceLight, PureBlack)
                        } else {
                            listOf(NightCosmos, CosmicSurface)
                        }
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            MysticGold,
                            MossGreen,
                            AstralBlue,
                            MysticGold
                        )
                    ),
                    shape = CircleShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            // Internal spinning geometry (Infinity symbol representing the synapse connection)
            Icon(
                imageVector = Icons.Default.AllInclusive,
                contentDescription = "Oráculo Synapse Studio",
                tint = if (isActiveRoute) MysticGold else MysticGoldLight,
                modifier = Modifier
                    .size(26.dp)
                    .rotate(rotation)
            )

            // Absolute tiny "Alive" status LED in upper-right quadrant representing active conscious flow
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .size(6.dp)
                    .background(
                        color = if (isActiveRoute) AstralRose else Color(0xFF00FFCC),
                        shape = CircleShape
                    )
            )
        }
    }
}
