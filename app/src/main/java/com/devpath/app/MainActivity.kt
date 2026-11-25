package com.devpath.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DevPathScreen { Dashboard, Catalog, Playground, AiTutor, Exam }

data class LessonItem(
    val id: String,
    val title: String,
    val language: String,
    val badgeColor: Color,
    val durationMin: Int,
    val xp: Int,
    val level: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevPathTheme {
                DevPathMainContainer()
            }
        }
    }
}

@Composable
fun DevPathTheme(content: @Composable () -> Unit) {
    val lightColors = lightColorScheme(
        primary = Color(0xFF1E40AF),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFDBEAFE),
        onPrimaryContainer = Color(0xFF1E3A8A),
        secondary = Color(0xFFD97706),
        background = Color(0xFFF8F7F4),
        onBackground = Color(0xFF14161A),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF14161A)
    )
    MaterialTheme(colorScheme = lightColors, content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevPathMainContainer() {
    var currentScreen by remember { mutableStateOf(DevPathScreen.Dashboard) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("DevPath", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("De zero a developpeur certifie", style = MaterialTheme.typography.labelSmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = currentScreen == DevPathScreen.Dashboard,
                    onClick = { currentScreen = DevPathScreen.Dashboard },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
                    label = { Text("Accueil") }
                )
                NavigationBarItem(
                    selected = currentScreen == DevPathScreen.Catalog,
                    onClick = { currentScreen = DevPathScreen.Catalog },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Parcours") },
                    label = { Text("Parcours") }
                )
                NavigationBarItem(
                    selected = currentScreen == DevPathScreen.Playground,
                    onClick = { currentScreen = DevPathScreen.Playground },
                    icon = { Icon(Icons.Default.Code, contentDescription = "Editeur") },
                    label = { Text("Editeur") }
                )
                NavigationBarItem(
                    selected = currentScreen == DevPathScreen.AiTutor,
                    onClick = { currentScreen = DevPathScreen.AiTutor },
                    icon = { Icon(Icons.Default.Face, contentDescription = "Tuteur IA") },
                    label = { Text("IA") }
                )
                NavigationBarItem(
                    selected = currentScreen == DevPathScreen.Exam,
                    onClick = { currentScreen = DevPathScreen.Exam },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Certificat") },
                    label = { Text("Examen") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentScreen) {
                DevPathScreen.Dashboard -> DashboardScreen { currentScreen = DevPathScreen.Playground }
                DevPathScreen.Catalog -> CatalogScreen()
                DevPathScreen.Playground -> PlaygroundScreen()
                DevPathScreen.AiTutor -> AiTutorScreen()
                DevPathScreen.Exam -> ExamScreen()
            }
        }
    }
}

@Composable
fun DashboardScreen(onOpenPlayground: () -> Unit) {
    val lessons = remember {
        listOf(
            LessonItem("py-01", "Variables & Types en Python", "Python", Color(0xFF3776AB), 7, 50, "Niveau 1"),
            LessonItem("js-01", "Portee et Closures JS", "JavaScript", Color(0xFFD6BA32), 8, 60, "Niveau 1"),
            LessonItem("sql-01", "Requetes & Joins SQL", "SQL", Color(0xFF0D9488), 9, 75, "Niveau 1"),
            LessonItem("kt-01", "Null-Safety & Flow en Kotlin", "Kotlin", Color(0xFF7F52FF), 10, 90, "Niveau 2")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Serie active : 7 jours", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("XP cumules : 1 240  |  Maitrise FSRS : 78%", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { 0.78f },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item {
            Text("Prochaines Lecons Recommandees", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        items(lessons) { lesson ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().clickable { onOpenPlayground() }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = lesson.badgeColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${lesson.language} · ${lesson.level}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = lesson.badgeColor
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(text = "${lesson.durationMin} min · +${lesson.xp} XP", style = MaterialTheme.typography.bodySmall)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Demarrer", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun CatalogScreen() {
    val languages = listOf(
        "Python" to Color(0xFF3776AB), "JavaScript" to Color(0xFFD6BA32), "SQL" to Color(0xFF0D9488),
        "Kotlin" to Color(0xFF7F52FF), "TypeScript" to Color(0xFF3178C6), "Java" to Color(0xFFE53E3E),
        "C++" to Color(0xFF00599C), "Go" to Color(0xFF00ADD8), "Rust" to Color(0xFFCE422B)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Catalogue des Parcours (45 Langages)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(languages) { (lang, color) ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(color = color, shape = RoundedCornerShape(8.dp), modifier = Modifier.size(40.dp)) {}
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(lang, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text("Parcours complet de 0 a Hero", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaygroundScreen() {
    val defaultCode = "def valider_devpath(xp):
    if xp >= 1000:
        return "Certifie"
    return "En apprentissage"

print(valider_devpath(1240))"
    var code by remember { mutableStateOf(defaultCode) }
    var output by remember { mutableStateOf("Executez votre code pour voir la sortie...") }
    val symbols = listOf("{", "}", "(", ")", "[", "]", ";", "=", ":", """)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Editeur Mobile Integre", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            modifier = Modifier.fillMaxWidth().weight(1f),
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, fontFamily = FontFamily.Monospace)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(symbols) { sym ->
                Button(
                    onClick = { code += sym },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(sym, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { output = "=== EXECUTION OK (Mode Degrade Local) ===\nCertifie\nProcess returned status 0 (0.04s)" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Run")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Executer le Code")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(100.dp).padding(4.dp)
        ) {
            Text(
                text = output,
                color = Color(0xFF10B981),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun AiTutorScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tuteur IA Socratique", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tuteur :", fontWeight = FontWeight.Bold)
                Text("Quelle est la difference selon vous entre une variable definie avec 'val' et 'var' en Kotlin ?")
            }
        }
    }
}

@Composable
fun ExamScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Certification Finale", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Icon(Icons.Default.CheckCircle, contentDescription = "Certifie", tint = Color(0xFF10B981), modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Felicitations !", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Vous avez valide le module Fondations & Algorithmique avec un score de 92%.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {}) {
            Text("Telecharger le Certificat (PDF)")
        }
    }
}
