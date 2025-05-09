package com.ud.example.parcial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text

@Composable
fun NumeroGrid(selectedNums: Set<Int>, onNumeroClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        items((1..50).toList()) { numero ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(50.dp)
                    .background(
                        if (selectedNums.contains(numero)) Color.Red else Color.LightGray
                    )
                    .clickable { onNumeroClick(numero) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$numero", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
