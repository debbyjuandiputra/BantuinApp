package com.debdev.bantuin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debdev.bantuin.auth.AuthManager
import com.debdev.bantuin.model.UserData
import kotlinx.coroutines.launch

private val statusOptions = listOf("Siswa", "Mahasiswa", "Pekerja", "Umum")

@Composable
fun ProfileScreen(
    authManager: AuthManager,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    var profile by remember { mutableStateOf<UserData?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val result = authManager.getProfile()
        profile = result.getOrNull()
        loading = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Profil Saya") },
            navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") }
            },
            actions = {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            val p = profile
            LazyColumn(modifier = Modifier.padding(20.dp)) {
                item { ProfileField("Username", p?.username.orEmpty()) }
                item { ProfileField("Email", p?.email.orEmpty()) }
                item { ProfileField("Nama Lengkap", p?.namaLengkap.orEmpty()) }
                item { ProfileField("Nama Panggilan", p?.namaPanggilan.orEmpty()) }
                item { ProfileField("Asal Sekolah/Kampus", p?.asalInstansi.orEmpty()) }
                item { ProfileField("Status", p?.status.orEmpty()) }
                item { ProfileField("Tanggal Lahir", p?.tanggalLahir.orEmpty()) }
                item { ProfileField("NIS/NIM", p?.nisNim.orEmpty()) }
                item { ProfileField("Bidang/Program Studi", p?.bidangProdi.orEmpty()) }
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
        Text(
            if (value.isBlank()) "Belum diisi" else value,
            fontSize = 16.sp,
            color = if (value.isBlank()) MaterialTheme.colors.onSurface.copy(alpha = 0.4f) else MaterialTheme.colors.onSurface
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun EditProfileScreen(
    authManager: AuthManager,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    var loading by remember { mutableStateOf(true) }
    var saving by remember { mutableStateOf(false) }
    var namaLengkap by remember { mutableStateOf("") }
    var namaPanggilan by remember { mutableStateOf("") }
    var asalInstansi by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }
    var nisNim by remember { mutableStateOf("") }
    var bidangProdi by remember { mutableStateOf("") }
    var statusExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val result = authManager.getProfile()
        result.getOrNull()?.let {
            namaLengkap = it.namaLengkap
            namaPanggilan = it.namaPanggilan
            asalInstansi = it.asalInstansi
            status = it.status
            tanggalLahir = it.tanggalLahir
            nisNim = it.nisNim
            bidangProdi = it.bidangProdi
        }
        loading = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Edit Profil") },
            navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") }
            },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Column
        }

        LazyColumn(modifier = Modifier.padding(20.dp)) {
            item {
                Text(
                    "Semua data di bawah ini bersifat opsional.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = namaLengkap, onValueChange = { namaLengkap = it },
                    label = { Text("Nama Lengkap") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = namaPanggilan, onValueChange = { namaPanggilan = it },
                    label = { Text("Nama Panggilan") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = asalInstansi, onValueChange = { asalInstansi = it },
                    label = { Text("Asal Sekolah/Kampus") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = {
                            IconButton(onClick = { statusExpanded = true }) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                        statusOptions.forEach { opt ->
                            DropdownMenuItem(onClick = { status = opt; statusExpanded = false }) {
                                Text(opt)
                            }
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = tanggalLahir, onValueChange = { tanggalLahir = it },
                    label = { Text("Tanggal Lahir (YYYY-MM-DD)") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = nisNim, onValueChange = { nisNim = it },
                    label = { Text("NIS/NIM") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = bidangProdi, onValueChange = { bidangProdi = it },
                    label = { Text("Bidang/Program Studi") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                )
            }
            item {
                Button(
                    onClick = {
                        saving = true
                        scope.launch {
                            authManager.updateProfile(
                                UserData(
                                    namaLengkap = namaLengkap,
                                    namaPanggilan = namaPanggilan,
                                    asalInstansi = asalInstansi,
                                    status = status,
                                    tanggalLahir = tanggalLahir,
                                    nisNim = nisNim,
                                    bidangProdi = bidangProdi
                                )
                            )
                            saving = false
                            onSaved()
                        }
                    },
                    enabled = !saving,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    if (saving) CircularProgressIndicator(modifier = Modifier.size(22.dp), color = androidx.compose.ui.graphics.Color.White)
                    else Text("Simpan")
                }
            }
        }
    }
}
