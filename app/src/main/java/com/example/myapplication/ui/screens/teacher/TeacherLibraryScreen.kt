package com.example.myapplication.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.DbStatusBadge
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherLibraryScreen(
    onBack: () -> Unit,
    onSelectObjective: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        Text("Biblioteca autorizada", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("1° Básico • Contenidos revisados por UTP", fontSize = 12.sp, color = UtpTextSecondary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UtpWarmBackground)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar en la biblioteca...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = UtpCardBackground,
                            unfocusedContainerColor = UtpCardBackground
                        )
                    )
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text("Todas las asignaturas") }
                        )
                        FilterChip(
                            selected = false,
                            onClick = {},
                            label = { Text("Autorizados: 4") }
                        )
                    }
                }

                item {
                    LibraryCourseCard(
                        category = "Lenguaje y Comunicación",
                        title = "Jugamos con los sonidos",
                        objective = "Objetivo: Reconocer sonidos iniciales en palabras.",
                        badgeText = "Autorizado por UTP"
                    )
                }

                item {
                    LibraryCourseCard(
                        category = "Matemática",
                        title = "Contamos del 1 al 10",
                        objective = "Objetivo: Contar y representar cantidades del 1 al 10.",
                        badgeText = "Autorizado por UTP"
                    )
                }

                item {
                    LibraryCourseCard(
                        category = "Ciencias Naturales",
                        title = "Conocemos los seres vivos",
                        objective = "Objetivo: Reconocer características de los seres vivos.",
                        badgeText = "Autorizado por UTP"
                    )
                }

                item {
                    LibraryCourseCard(
                        category = "Historia, Geografía y Ciencias Sociales",
                        title = "Mi familia y mi comunidad",
                        objective = "Objetivo: Reconocer integrantes y roles de la comunidad.",
                        badgeText = "Autorizado por UTP"
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onSelectObjective,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                    ) {
                        Text("Seleccionar OA de Matemática", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }

        DbStatusBadge(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp),
            statusText = "En proceso de conexión a BD",
            isVisible = true
        )
    }
}

@Composable
fun LibraryCourseCard(
    category: String,
    title: String,
    objective: String,
    badgeText: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Book, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(category, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                }
                Surface(shape = RoundedCornerShape(8.dp), color = UtpBadgeGreenBg) {
                    Text(badgeText, color = UtpBadgeGreenText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)

            Spacer(modifier = Modifier.height(4.dp))

            Text(objective, fontSize = 12.sp, color = UtpTextSecondary)

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = UtpTextSecondary)
            }
        }
    }
}
