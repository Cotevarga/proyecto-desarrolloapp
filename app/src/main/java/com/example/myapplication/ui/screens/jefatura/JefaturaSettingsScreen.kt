package com.example.myapplication.ui.screens.jefatura

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@Composable
fun JefaturaSettingsScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    // Estados de configuración de Jefatura UTP
    var notificarNuevasSolicitudes by remember { mutableStateOf(true) }
    var permitirEdicionDocente by remember { mutableStateOf(false) }
    var autoAprobarGuiasSimples by remember { mutableStateOf(false) }
    var visibilidadMuroAlumnos by remember { mutableStateOf(true) }

    var showChangePasswordDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar para Jefatura
            UtpTopHeaderBar(
                userRole = "JEFATURA",
                showBack = true,
                onBack = onBack,
                onAccountSettings = {},
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(12.dp)) }

                // Tarjeta de Perfil Institucional UTP
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(52.dp),
                                shape = CircleShape,
                                color = UtpCelestePrimary.copy(alpha = 0.15f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = UtpCelestePrimary,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Prof. Carolina M.",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UtpTextPrimary
                                )
                                Text(
                                    text = "Jefatura Técnica Pedagógica (UTP)",
                                    fontSize = 12.sp,
                                    color = UtpTextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = UtpCelestePrimary.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "Escuela Escritora Marcela Paz",
                                        color = UtpCelestePrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // SECCIÓN 1: GESTIÓN DE SOLICITUDES Y ALERTAS UTP
                item { SettingsSectionHeader("GESTIÓN DE SOLICITUDES Y ALERTAS UTP") }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column {
                            SettingsSwitchTile(
                                icon = Icons.Default.NotificationsActive,
                                title = "Notificaciones de Solicitudes",
                                subtitle = "Recibir aviso cuando un docente envíe un nuevo contenido",
                                checked = notificarNuevasSolicitudes,
                                onCheckedChange = { notificarNuevasSolicitudes = it }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp), color = Color.LightGray.copy(alpha = 0.3f))
                            SettingsSwitchTile(
                                icon = Icons.Default.AutoFixHigh,
                                title = "Auto-aprobación de Material Complementario",
                                subtitle = "Aprobar automáticamente recursos sin evaluación",
                                checked = autoAprobarGuiasSimples,
                                onCheckedChange = { autoAprobarGuiasSimples = it }
                            )
                        }
                    }
                }

                // SECCIÓN 2: CONTROL DE PERMISOS Y PUBLICACIONES
                item { SettingsSectionHeader("PERMISOS Y POLÍTICAS DEL MURO") }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column {
                            SettingsSwitchTile(
                                icon = Icons.Default.EditNote,
                                title = "Permitir Re-edición a Docentes",
                                subtitle = "Permite a los profesores editar contenidos ya autorizados",
                                checked = permitirEdicionDocente,
                                onCheckedChange = { permitirEdicionDocente = it }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp), color = Color.LightGray.copy(alpha = 0.3f))
                            SettingsSwitchTile(
                                icon = Icons.Default.Visibility,
                                title = "Publicación Inmediata en Muro Alumnos",
                                subtitle = "Hacer visibles las publicaciones autorizadas automáticamente",
                                checked = visibilidadMuroAlumnos,
                                onCheckedChange = { visibilidadMuroAlumnos = it }
                            )
                        }
                    }
                }

                // SECCIÓN 3: SEGURIDAD Y ACCESO
                item { SettingsSectionHeader("SEGURIDAD DE CUENTA JEFATURA") }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column {
                            SettingsActionTile(
                                icon = Icons.Default.Lock,
                                title = "Cambiar Contraseña",
                                subtitle = "Actualiza tu clave de administración UTP",
                                onClick = { showChangePasswordDialog = true }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp), color = Color.LightGray.copy(alpha = 0.3f))
                            SettingsActionTile(
                                icon = Icons.Default.VerifiedUser,
                                title = "Firma Digital de Autorizaciones",
                                subtitle = "Activa para adjuntar firma a certificados y reportes",
                                showChevron = true,
                                onClick = {}
                            )
                        }
                    }
                }

                // Botón Cerrar Sesión
                item {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpBadgeRedBg)
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = null,
                            tint = UtpBadgeRedText,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cerrar Sesión de Jefatura",
                            color = UtpBadgeRedText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        // Diálogo Cambiar Contraseña
        if (showChangePasswordDialog) {
            AlertDialog(
                onDismissRequest = { showChangePasswordDialog = false },
                title = { Text("Actualizar Clave Jefatura") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Ingresa tu contraseña actual y confirma la nueva clave.", fontSize = 12.sp, color = UtpTextSecondary)
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Contraseña Actual") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Nueva Contraseña UTP") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showChangePasswordDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpCelestePrimary)
                    ) {
                        Text("Guardar Cambios")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showChangePasswordDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = UtpTextSecondary,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
fun SettingsActionTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    showChevron: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(icon, contentDescription = null, tint = UtpCelestePrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Text(subtitle, fontSize = 11.sp, color = UtpTextSecondary)
            }
        }
        if (showChevron) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = UtpTextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun SettingsSwitchTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(icon, contentDescription = null, tint = UtpCelestePrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
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
                checkedTrackColor = UtpCelestePrimary
            )
        )
    }
}