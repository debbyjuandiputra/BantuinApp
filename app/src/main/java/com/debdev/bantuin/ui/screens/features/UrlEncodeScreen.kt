package com.debdev.bantuin.ui.screens.features

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun UrlEncodeScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    fun encode() {
        error = ""
        output = try {
            URLEncoder.encode(input, "UTF-8")
        } catch (e: Exception) {
            error = "Gagal encode: ${e.message}"
            ""
        }
    }

    fun decode() {
        error = ""
        output = try {
            URLDecoder.decode(input, "UTF-8")
        } catch (e: Exception) {
            error = "Teks bukan hasil URL encode yang valid"
            ""
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("URL Encode & Decode") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") } },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Teks atau URL") },
                modifier = Modifier.fillMaxWidth().height(140.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { encode() }, modifier = Modifier.weight(1f)) { Text("Encode") }
                Button(onClick = { decode() }, modifier = Modifier.weight(1f)) { Text("Decode") }
            }

            IconButton(
                onClick = {
                    val temp = input
                    input = output
                    output = temp
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(Icons.Default.SwapVert, contentDescription = "Tukar input/output")
            }

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colors.error, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            }

            Text("Hasil:", fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))

            Card(elevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Text(
                    output.ifEmpty { "—" },
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }

            OutlinedButton(
                onClick = {
                    if (output.isNotEmpty()) {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Hasil", output))
                        Toast.makeText(context, "Disalin", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Salin Hasil")
            }
        }
    }
}
