package com.debdev.bantuin.ui.screens.features

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable

private data class PremiumApp(
    val name: String,
    val sevenDayUrl: String,
    val oneMonthUrl: String
)

private val premiumApps = listOf(
    PremiumApp("CapCut Pro", "https://lynk.id/yorpedia/d5nw5ox55qmy", "https://lynk.id/yorpedia/wexoe4wl1xeo"),
    PremiumApp("Canva Pro", "https://lynk.id/yorpedia/pg05gpwx1lol", "https://lynk.id/yorpedia/9dqpvkqvzmdk")
)

@Composable
fun PremiumScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var expandedApp by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Aplikasi Premium") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") } },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        Column(modifier = Modifier.padding(20.dp)) {
            premiumApps.forEach { app ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp), elevation = 2.dp) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedApp = if (expandedApp == app.name) null else app.name }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(app.name, fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }

                        if (expandedApp == app.name) {
                            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                DurationOption("7 Hari") {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(app.sevenDayUrl)))
                                }
                                DurationOption("1 Bulan") {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(app.oneMonthUrl)))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            Text(
                "Kamu akan diarahkan ke halaman pembelian eksternal untuk menyelesaikan transaksi.",
                fontSize = 11.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun DurationOption(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp)
        Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(18.dp))
    }
    Divider()
}
