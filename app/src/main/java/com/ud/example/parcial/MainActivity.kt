package com.ud.example.parcial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ud.example.parcial.ui.theme.ParcialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParcialTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current as Activity
    var listaRifas by remember { mutableStateOf(mutableListOf<Rifa>()) }
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var fecha by remember { mutableStateOf(TextFieldValue("")) }
    var numeroGanador by remember { mutableStateOf<Int?>(null) }
    var rifasGanadoras by remember { mutableStateOf(listOf<String>()) }
    var rifaSeleccionada by remember { mutableStateOf<Rifa?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mostrarListaRifas by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedNumbers = result.data
                ?.getIntegerArrayListExtra("selectedNumbers")
                ?.toList() ?: emptyList()
            val nuevaRifa = Rifa(nombre.text, fecha.text, selectedNumbers)
            listaRifas.add(nuevaRifa)
            nombre = TextFieldValue("")
            fecha = TextFieldValue("")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gestor de Rifas") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    if (nombre.text.isNotBlank() && fecha.text.isNotBlank()) {
                        val intent = Intent(context, RifaActivity::class.java)
                        launcher.launch(intent)
                    }
                }) {
                    Text("Guardar Rifa")
                }
                Button(onClick = {
                    listaRifas.find { it.nombre == nombre.text }?.let {
                        fecha = TextFieldValue(it.fecha)
                    }
                }) {
                    Text("Buscar Rifa")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                numeroGanador = (1..50).random()
                rifasGanadoras = listaRifas
                    .filter { it.numerosSeleccionados.contains(numeroGanador) }
                    .map { it.nombre }
            }) {
                Text("Número Ganador")
            }

            numeroGanador?.let { ganador ->
                Text("Número ganador: $ganador", style = MaterialTheme.typography.titleMedium)
                if (rifasGanadoras.isNotEmpty()) {
                    Text("Rifas ganadoras:")
                    rifasGanadoras.forEach { Text("• $it") }
                } else {
                    Text("No hubo rifas ganadoras.")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                mostrarListaRifas = !mostrarListaRifas
            }) {
                Text(if (mostrarListaRifas) "Ocultar Rifas" else "Mostrar Rifas")
            }

            if (mostrarListaRifas) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Rifas Registradas:", style = MaterialTheme.typography.titleMedium)

                LazyColumn {
                    items(listaRifas.size) { index ->
                        val rifa = listaRifas[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Nombre: ${rifa.nombre}")
                                Text("Fecha: ${rifa.fecha}")
                                Text("Cantidad de Números: ${rifa.numerosSeleccionados.size}")
                                Spacer(Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(onClick = {
                                        rifaSeleccionada = rifa
                                        mostrarDialogo = true
                                    }) {
                                        Text("Ver Detalles Rifa")
                                    }
                                    Button(onClick = {
                                        listaRifas.remove(rifa)
                                    }) {
                                        Text("Eliminar Rifa")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (mostrarDialogo && rifaSeleccionada != null) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogo = false },
                    title = { Text("Detalles de la Rifa") },
                    text = {
                        Column {
                            Text("Nombre: ${rifaSeleccionada!!.nombre}")
                            Text("Fecha: ${rifaSeleccionada!!.fecha}")
                            Text(
                                "Números Seleccionados: " +
                                        rifaSeleccionada!!.numerosSeleccionados
                                            .sorted()
                                            .joinToString()
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { mostrarDialogo = false }) {
                            Text("Cerrar")
                        }
                    }
                )
            }
        }
    }
}
