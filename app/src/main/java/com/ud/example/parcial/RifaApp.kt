package com.ud.example.parcial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class RifaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeleccionarNumerosScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionarNumerosScreen() {
    val context = LocalContext.current as Activity
    var selectedNums by remember { mutableStateOf(mutableSetOf<Int>()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Seleccionar Números") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val resultIntent = Intent().apply {
                    putIntegerArrayListExtra(
                        "selectedNumbers",
                        ArrayList(selectedNums)
                    )
                }
                context.setResult(Activity.RESULT_OK, resultIntent)
                context.finish()
            }) {
                Text("Guardar")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            NumeroGrid(
                selectedNums = selectedNums,
                onNumeroClick = { numero ->
                    if (!selectedNums.contains(numero)) {
                        selectedNums = selectedNums.toMutableSet().apply { add(numero) }
                    }
                }
            )
        }
    }
}
