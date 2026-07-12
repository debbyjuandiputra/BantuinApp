package com.debdev.bantuin.ui.screens.features

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debdev.bantuin.util.ExpressionEvaluator

private val buttonRows = listOf(
    listOf("sin(", "cos(", "tan(", "AC"),
    listOf("log(", "ln(", "sqrt(", "⌫"),
    listOf("(", ")", "^", "/"),
    listOf("7", "8", "9", "*"),
    listOf("4", "5", "6", "-"),
    listOf("1", "2", "3", "+"),
    listOf("0", ".", "!", "="),
    listOf("pi", "e", "%", "abs(")
)

@Composable
fun ScientificCalculatorScreen(onBack: () -> Unit) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    fun handlePress(key: String) {
        when (key) {
            "AC" -> { expression = ""; result = ""; error = "" }
            "⌫" -> { if (expression.isNotEmpty()) expression = expression.dropLast(1) }
            "=" -> {
                try {
                    val value = ExpressionEvaluator.evaluate(expression)
                    result = if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()
                    error = ""
                } catch (e: Exception) {
                    error = e.message ?: "Ekspresi tidak valid"
                    result = ""
                }
            }
            else -> { expression += key }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Kalkulator Ilmiah") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") } },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                expression.ifEmpty { "0" },
                fontSize = 22.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )
            Text(
                if (error.isNotEmpty()) error else result,
                fontSize = 30.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = if (error.isNotEmpty()) MaterialTheme.colors.error else MaterialTheme.colors.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }

        Divider()

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(buttonRows.flatten()) { key ->
                CalcButton(key) { handlePress(key) }
            }
        }
    }
}

@Composable
private fun CalcButton(label: String, onClick: () -> Unit) {
    val isOperator = label in listOf("/", "*", "-", "+", "=", "^", "%")
    Button(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1.3f),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isOperator) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            contentColor = if (isOperator) androidx.compose.ui.graphics.Color.White else MaterialTheme.colors.onSurface
        ),
        elevation = ButtonDefaults.elevation(1.dp)
    ) {
        Text(label, fontSize = 15.sp)
    }
}
