package com.example.myapplication.ui.screens.jefatura

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.AsignaturaUTP
import com.example.myapplication.data.model.EstadoContenido
import com.example.myapplication.data.repository.UtpRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*



@Composable
fun AdminSubjectsScreen(
    onBack: () -> Unit = {},
    onSelectSubject: (String) -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val unidades by UtpRepository.unidadesState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "JEFATURA",
                showBack = false,
                onBack = onBack,
                onAccountSettings = onAccountSettings,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(12.dp)) }

                item {
                    Column {
                        Text(
                            text = "Panel de Gestión por Materias",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Selecciona una asignatura para administrar las solicitudes pendientes.",
                            fontSize = 12.sp,
                            color = UtpTextSecondary
                        )
                    }
                }

                val asignaturas = AsignaturaUTP.values().toList()
                val filas = asignaturas.chunked(2)

                items(filas) { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (asig in fila) {
                            val cantidadPendientes = unidades.count {
                                it.asignatura == asig && it.estado == EstadoContenido.PENDIENTE
                            }

                            SubjectCardItem(
                                name = asig.displayName,
                                pendingCount = cantidadPendientes,
                                icon = getIconForAsignatura(asig),
                                modifier = Modifier.weight(1f),
                                onClick = { onSelectSubject(asig.displayName) }
                            )
                        }
                        if (fila.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun SubjectCardItem(
    name: String,
    pendingCount: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = UtpBadgeBlueBg,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = UtpCelestePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (pendingCount > 0) UtpBadgeRedBg else UtpBadgeBlueBg
                ) {
                    Text(
                        text = if (pendingCount == 1) "1 Pendiente" else "$pendingCount Pendientes",
                        fontSize = 10.sp,
                        color = if (pendingCount > 0) UtpBadgeRedText else UtpCelestePrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = UtpTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ver solicitudes",
                    fontSize = 11.sp,
                    color = UtpCelestePrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = UtpCelestePrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun getIconForAsignatura(asignatura: AsignaturaUTP): ImageVector {
    return when (asignatura) {
        AsignaturaUTP.MATEMATICAS -> Icons.Default.Percent
        AsignaturaUTP.LENGUAJE -> Icons.Default.Book
        AsignaturaUTP.HISTORIA -> Icons.Default.MenuBook
        AsignaturaUTP.CIENCIAS -> Icons.Default.Science
    }
}