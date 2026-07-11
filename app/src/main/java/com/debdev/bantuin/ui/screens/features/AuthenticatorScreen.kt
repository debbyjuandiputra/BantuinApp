package com.debdev.bantuin.ui.screens.features

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debdev.bantuin.util.TotpGenerator
import kotlinx.coroutines.delay

@Composable
fun AuthenticatorScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var secretKey by remember { mutableStateOf("") }
    var digits by remember { mutableStateOf(6) }
    var period by remember { mutableStateOf(30) }
    var digitsMenuExpanded by remember { mutableStateOf(false) }
    var periodMenuExpanded by remember { mutableStateOf(false) }
    var currentCode by remember { mutableStateOf("------") }
    var secondsLeft by remember { mutableStateOf(30) }

    LaunchedEffect(secretKey, digits, period) {
        while (true) {
            if (secretKey.isNotBlank()) {
                currentCode = TotpGenerator.generate(secretKey, digits, period)
            } else {
                currentCode = "-".repeat(digits)
            }
            secondsLeft = TotpGenerator.secondsRemaining(period)
            delay(1000)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Generate Kode Authenticator") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") } },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        Column(modifier = Modifier.padding(20.dp)) {
            OutlinedTextField(
                value = secretKey,
                onValueChange = { secretKey = it },
                label = { Text("Masukkan Kunci (Secret Key)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(onClick = { digitsMenuExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("$digits Digit")
                    }
                    DropdownMenu(expanded = digitsMenuExpanded, onDismissRequest = { digitsMenuExpanded = false }) {
                        listOf(6, 8).forEach { d ->
                            DropdownMenuItem(onClick = { digits = d; digitsMenuExpanded = false }) { Text("$d Digit") }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(onClick = { periodMenuExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("${period}s")
                    }
                    DropdownMenu(expanded = periodMenuExpanded, onDismissRequest = { periodMenuExpanded = false }) {
                        listOf(30, 60).forEach { p ->
                            DropdownMenuItem(onClick = { period = p; periodMenuExpanded = false }) { Text("${p}s") }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Card(elevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        currentCode.chunked(currentCode.length / 2 + currentCode.length % 2).joinToString(" "),
                        fontSize = 36.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Berganti dalam ${secondsLeft}s", fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("OTP", currentCode))
                        Toast.makeText(context, "Kode disalin", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Salin Kode")
                    }
                }
            }

            Text(
                "Mengikuti standar Google Authenticator (TOTP, HMAC-SHA1). Kunci bisa berupa Base32 standar; karakter tidak valid otomatis diabaikan.",
                fontSize = 11.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}
