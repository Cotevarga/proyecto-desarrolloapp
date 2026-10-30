package com.example.myapplication.ui.screens.student

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun StudentPracticeScreen(
    onComplete: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UtpWarmBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Text("Aprendemos a contar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Señala cada manzana. Di un número cada vez.", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { Toast.makeText(context, "🔊 Reproduciendo audio...", Toast.LENGTH_SHORT).show() },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurpleSoft)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = UtpPurplePrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Escuchar", color = UtpPurplePrimary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🍎 🍎 🍎", fontSize = 40.sp)
                    Text("1    2    3", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = UtpPurpleSoft,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("3", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                            Text("¡Hay 3! La última cuenta dice cuántas hay.", fontSize = 12.sp, color = UtpTextPrimary)
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
            ) {
                Text("Ir a la Prueba / Desafío", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        item { Spacer(modifier = Modifier.height(60.dp)) }
    }
}