package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.DbStatusBadge
import com.example.myapplication.ui.theme.*

data class SubjectGridItem(
    val name: String,
    val units: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSubjectsScreen(
    onBack: () -> Unit,
    onSelectSubject: (String) -> Unit
) {
    val subjects = listOf(
        SubjectGridItem("Matemáticas", "3 Unidades", Icons.Default.Percent),
        SubjectGridItem("Inglés", "2 Unidades", Icons.Default.Language),
        SubjectGridItem("Lenguaje", "3 Unidades", Icons.Default.Book),
        SubjectGridItem("Historia", "4 Unidades", Icons.Default.MenuBook)
    )

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = {
                    Column {
                        Text("Jefatura", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Panel de control académico", fontSize = 12.sp, color = UtpTextSecondary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Buscar") }
                    IconButton(onClick = {}) { Icon(Icons.Default.Person, contentDescription = "Perfil") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UtpWarmBackground)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Asignaturas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SubjectCardItem(subjects[0], Modifier.weight(1f), onSelectSubject)
                            SubjectCardItem(subjects[1], Modifier.weight(1f), onSelectSubject)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SubjectCardItem(subjects[2], Modifier.weight(1f), onSelectSubject)
                            SubjectCardItem(subjects[3], Modifier.weight(1f), onSelectSubject)
                        }
                    }
                }

                // Section: Autorizaciones pendientes
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Autorizaciones pendientes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                        Text(
                            text = "12 pendientes",
                            fontSize = 12.sp,
                            color = UtpPurplePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        PendingAuthorizationCard(
                            title = "Guía ejercicios • Lenguaje",
                            subtitle = "Revisión de ejercicios y retroalimentación final",
                            author = "Profesor Carlos",
                            date = "Hoy"
                        )
                        PendingAuthorizationCard(
                            title = "Prueba 2 • Matemáticas",
                            subtitle = "Corrección de respuestas",
                            author = "Profesora Alejandra",
                            date = "Ayer"
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }

        DbStatusBadge(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            statusText = "En proceso de conexión a BD",
            isVisible = true
        )
    }
}

@Composable
fun SubjectCardItem(
    item: SubjectGridItem,
    modifier: Modifier = Modifier,
    onSelectSubject: (String) -> Unit
) {
    Card(
        modifier = modifier.clickable { onSelectSubject(item.name) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = UtpBadgeBlueBg,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(item.icon, contentDescription = null, tint = UtpBadgeBlueText)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = UtpBadgeBlueBg
                ) {
                    Text(
                        text = item.units,
                        fontSize = 10.sp,
                        color = UtpBadgeBlueText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = item.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = UtpTextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Editar >",
                    fontSize = 12.sp,
                    color = UtpTextSecondary
                )
            }
        }
    }
}

@Composable
fun PendingAuthorizationCard(
    title: String,
    subtitle: String,
    author: String,
    date: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = date, fontSize = 11.sp, color = UtpTextSecondary)
                Text(text = author, fontSize = 11.sp, color = UtpBadgeRedText, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text(text = subtitle, fontSize = 12.sp, color = UtpTextSecondary)
        }
    }
}
