package com.debdev.bantuin.ui.screens.features

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow
import kotlin.math.sqrt

private data class StatResult(
    val count: Int,
    val sum: Double,
    val mean: Double,
    val median: Double,
    val mode: String,
    val min: Double,
    val max: Double,
    val range: Double,
    val variance: Double,
    val stdDev: Double
)

private fun computeStats(numbers: List<Double>): StatResult {
    val sorted = numbers.sorted()
    val n = numbers.size
    val sum = numbers.sum()
    val mean = sum / n
    val median = if (n % 2 == 0) (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0 else sorted[n / 2]

    val frequency = numbers.groupingBy { it }.eachCount()
    val maxFreq = frequency.values.maxOrNull() ?: 0
    val modes = frequency.filterValues { it == maxFreq && maxFreq > 1 }.keys
    val modeText = if (modes.isEmpty()) "Tidak ada" else modes.joinToString(", ") { formatNum(it) }

    val variance = numbers.sumOf { (it - mean).pow(2) } / n
    val stdDev = sqrt(variance)

    return StatResult(
        count = n,
        sum = sum,
        mean = mean,
        median = median,
        mode = modeText,
        min = sorted.first(),
        max = sorted.last(),
        range = sorted.last() - sorted.first(),
        variance = variance,
        stdDev = stdDev
    )
}

private fun formatNum(d: Double): String =
    if (d == d.toLong().toDouble()) d.toLong().toString() else "%.4f".format(d).trimEnd('0').trimEnd('.')

@Composable
fun StatisticalCalculatorScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var stats by remember { mutableStateOf<StatResult?>(null) }
    var error by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Kalkulator Statistik") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") } },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
            Text("Masukkan data angka, dipisah koma", fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("contoh: 7, 8, 9, 10, 8, 7") },
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    error = ""
                    stats = null
                    val numbers = input.split(",", "\n", ";")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .mapNotNull { it.toDoubleOrNull() }

                    if (numbers.isEmpty()) {
                        error = "Masukkan minimal satu angka yang valid"
                    } else {
                        stats = computeStats(numbers)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Hitung")
            }

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colors.error, fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp))
            }

            stats?.let { s ->
                Spacer(modifier = Modifier.height(20.dp))
                Card(elevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        StatRowItem("Jumlah Data (n)", s.count.toString())
                        StatRowItem("Total (Σ)", formatNum(s.sum))
                        StatRowItem("Rata-rata (Mean)", formatNum(s.mean))
                        StatRowItem("Median", formatNum(s.median))
                        StatRowItem("Modus", s.mode)
                        StatRowItem("Minimum", formatNum(s.min))
                        StatRowItem("Maksimum", formatNum(s.max))
                        StatRowItem("Rentang (Range)", formatNum(s.range))
                        StatRowItem("Varians (Populasi)", formatNum(s.variance))
                        StatRowItem("Standar Deviasi", formatNum(s.stdDev))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatRowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp)
        Text(value, fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = MaterialTheme.colors.primary)
    }
    Divider()
}
