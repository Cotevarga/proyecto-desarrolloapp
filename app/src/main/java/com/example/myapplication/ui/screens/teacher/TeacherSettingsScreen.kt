package com.example.myapplication.ui.screens.teacher

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.repository.TeacherRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@Composable
fun TeacherSettingsScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    val context = LocalContext.current
    val docente = remember { repository.getDocenteById("doc_1") }

    var notifPermisos by remember { mutableStateOf(true) }
    var notifRecordatorios by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                userName = docente.nombre,
                showBack = true,
                onBack = onBack,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Text(
                        text = "Configuración de Perfil",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = UtpTextPrimary
                    )
                }

                // TARJETA DE INFORMACIÓN DEL DOCENTE
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(24.dp),
                                    color = UtpPurpleSoft,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = null,
                                            tint = UtpPurplePrimary,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = docente.nombre,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UtpTextPrimary
                                    )
                                    Text(
                                        text = "Asignatura: ${docente.materiaPrincipal}",
                                        fontSize = 12.sp,
                                        color = UtpTextSecondary
                                    )
                                    Text(
                                        text = "Cursos: 1° Año A • 1° Año B",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = UtpPurplePrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // OPCIONES DE NOTIFICACIONES
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Preferencias de Notificaciones",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = UtpTextPrimary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            SettingSwitchRow(
                                icon = Icons.Default.NotificationsActive,
                                title = "Respuestas de UTP",
                                subtitle = "Notificar cuando se apruebe o rechace un permiso",
                                checked = notifPermisos,
                                onCheckedChange = { notifPermisos = it }
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))

                            SettingSwitchRow(
                                icon = Icons.Default.Event,
                                title = "Recordatorios de Evaluaciones",
                                subtitle = "Avisar 24h antes de pruebas agendadas",
                                checked = notifRecordatorios,
                                onCheckedChange = { notifRecordatorios = it }
                            )
                        }
                    }
                }

                // SEGURIDAD Y ACCIONES
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Seguridad y Cuenta",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = UtpTextPrimary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            TextButton(
                                onClick = {
                                    Toast.makeText(context, "Se envió un correo para restablecer contraseña", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = UtpPurplePrimary)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Cambiar Contraseña", fontSize = 13.sp, color = UtpTextPrimary, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFF1F5F9))

                            TextButton(
                                onClick = onLogout,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFDC2626))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Cerrar Sesión", fontSize = 13.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(20.dp))
            Column {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Text(subtitle, fontSize = 11.sp, color = UtpTextSecondary)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = UtpPurplePrimary
            )
        )
    }
}