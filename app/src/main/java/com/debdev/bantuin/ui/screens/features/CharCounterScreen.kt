package com.debdev.bantuin.ui.screens.features

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun CharCounterScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var inputMode by remember { mutableStateOf("text") } // "text" atau "file"
    var text by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var fileNote by remember { mutableStateOf("") }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            fileName = uri.lastPathSegment
            val name = fileName?.lowercase() ?: ""
            if (name.endsWith(".txt")) {
                try {
                    val stream = context.contentResolver.openInputStream(uri)
                    val reader = BufferedReader(InputStreamReader(stream))
                    text = reader.readText()
                    reader.close()
                    fileNote = ""
                } catch (e: Exception) {
                    fileNote = "Gagal membaca file: ${e.message}"
                }
            } else {
                text = ""
                fileNote = "Ekstraksi teks dari format ini (docx/pdf/dll) akan tersedia pada update mendatang. Untuk saat ini gunakan file .txt atau salin-tempel teksnya."
            }
        }
    }

    val charCount = text.length
    val sentenceCount = if (text.isBlank()) 0 else text.split(Regex("[.!?]+")).count { it.trim().isNotEmpty() }
    val paragraphCount = if (text.isBlank()) 0 else text.split(Regex("\n\\s*\n")).count { it.trim().isNotEmpty() }
    val wordCount = if (text.isBlank()) 0 else text.trim().split(Regex("\\s+")).size
    val matchCount = if (searchQuery.isBlank()) 0 else Regex(Regex.escape(searchQuery), RegexOption.IGNORE_CASE).findAll(text).count()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Cek Panjang Karakter") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") } },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth()) {
                FilterChipLike("Via Teks", inputMode == "text") { inputMode = "text" }
                Spacer(modifier = Modifier.width(10.dp))
                FilterChipLike("Via File", inputMode == "file") { inputMode = "file" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (inputMode == "text") {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Masukkan atau tempel teks") },
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                )
            } else {
                OutlinedButton(onClick = { filePicker.launch("*/*") }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pilih File (txt, docx, pdf, dll)")
                }
                fileName?.let { Text("File: $it", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp)) }
                if (fileNote.isNotEmpty()) {
                    Text(fileNote, fontSize = 11.sp, color = MaterialTheme.colors.error, modifier = Modifier.padding(top = 6.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    StatRow("Jumlah Karakter", charCount.toString())
                    StatRow("Jumlah Kata", wordCount.toString())
                    StatRow("Jumlah Kalimat", sentenceCount.toString())
                    StatRow("Jumlah Paragraf", paragraphCount.toString())
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari teks/kalimat") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (searchQuery.isNotBlank()) {
                Text("Ditemukan $matchCount kecocokan", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp)
        Text(value, fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = MaterialTheme.colors.primary)
    }
}

@Composable
private fun FilterChipLike(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        contentColor = if (selected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colors.onSurface,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colors.primary),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(label, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
    }
}
