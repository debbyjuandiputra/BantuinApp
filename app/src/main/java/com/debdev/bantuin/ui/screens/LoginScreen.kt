package com.debdev.bantuin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debdev.bantuin.auth.AuthManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authManager: AuthManager,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bantuin", fontSize = 40.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = MaterialTheme.colors.primary)
        Text("Alat Bantu Siswa & Mahasiswa", fontSize = 13.sp, modifier = Modifier.padding(bottom = 40.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colors.error, fontSize = 12.sp, modifier = Modifier.padding(bottom = 12.dp))
        }

        Button(
            onClick = {
                error = ""
                loading = true
                scope.launch {
                    val result = authManager.login(username.trim(), password)
                    loading = false
                    result.onSuccess { onLoginSuccess() }
                        .onFailure { error = it.message ?: "Login gagal" }
                }
            },
            enabled = !loading && username.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(top = 8.dp)
        ) {
            if (loading) CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color_White)
            else Text("Masuk")
        }

        Row(modifier = Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Belum punya akun? ", fontSize = 13.sp)
            TextButton(onClick = onGoToRegister) { Text("Daftar") }
        }
    }
}

private val Color_White = androidx.compose.ui.graphics.Color.White
