package com.debdev.bantuin.ui.screens.features

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

private enum class ConvertMode(val label: String) {
    PDF_TO_DOCX("PDF → DOCX"),
    DOCX_TO_PDF("DOCX → PDF"),
    XLSX_TO_CSV("XLSX → CSV"),
    CSV_TO_XLSX("CSV → XLSX")
}

@Composable
fun ConvertDocumentScreen(onBack: () -> Unit) {
    var selectedMode by remember { mutableStateOf(ConvertMode.PDF_TO_DOCX) }
    var pickedFileName by remember { mutableStateOf<String?>(null) }
    var pickedUri by remember { mutableStateOf<Uri?>(null) }
    var status by remember { mutableStateOf("") }
    var modeMenuExpanded by remember { mutableStateOf(false) }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pickedUri = uri
        pickedFileName = uri?.lastPathSegment
        status = ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Konversi Dokumen") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") } },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Text("Pilih jenis konversi:", fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))

            Box {
                OutlinedButton(onClick = { modeMenuExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(selectedMode.label)
                }
                DropdownMenu(expanded = modeMenuExpanded, onDismissRequest = { modeMenuExpanded = false }) {
                    ConvertMode.values().forEach { mode ->
                        DropdownMenuItem(onClick = { selectedMode = mode; modeMenuExpanded = false; pickedFileName = null }) {
                            Text(mode.label)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    val mimeType = when (selectedMode) {
                        ConvertMode.PDF_TO_DOCX -> "application/pdf"
                        ConvertMode.DOCX_TO_PDF -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        ConvertMode.XLSX_TO_CSV -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        ConvertMode.CSV_TO_XLSX -> "text/comma-separated-values"
                    }
                    filePicker.launch(mimeType)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pilih File")
            }

            pickedFileName?.let {
                Text("File dipilih: $it", fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // TODO: Integrasikan library konversi nyata (mis. Apache POI untuk docx/xlsx,
                    // PDFBox-Android untuk pdf) atau API konversi cloud di sini.
                    status = if (pickedUri == null) "Pilih file terlebih dahulu"
                    else "Konversi ${selectedMode.label} sedang diproses... (fitur backend konversi menyusul)"
                },
                enabled = pickedUri != null,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Konversi Sekarang")
            }

            if (status.isNotEmpty()) {
                Text(status, fontSize = 12.sp, modifier = Modifier.padding(top = 16.dp), color = MaterialTheme.colors.primary)
            }

            Divider(modifier = Modifier.padding(vertical = 24.dp))
            Text(
                "Catatan: konversi PDF↔DOCX dan XLSX↔CSV pada versi ini masih tahap awal. " +
                "Mesin konversi penuh akan ditambahkan pada update berikutnya.",
                fontSize = 11.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
