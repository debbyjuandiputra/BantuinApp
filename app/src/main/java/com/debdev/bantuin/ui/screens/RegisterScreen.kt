package com.debdev.bantuin.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debdev.bantuin.auth.AuthManager
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    authManager: AuthManager,
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit,
    onOpenPolicy: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var agreed by remember { mutableStateOf(false) }
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
        Text("Buat Akun", fontSize = 28.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, modifier = Modifier.padding(bottom = 24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Buat Username Kamu") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Buat Password Kamu (min 8 karakter)") },
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)
        ) {
            Checkbox(checked = agreed, onCheckedChange = { agreed = it })
            Text("Saya menyetujui ", fontSize = 12.sp)
            Text(
                "Kebijakan & Syarat Penggunaan",
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clickable { onOpenPolicy() }
            )
        }

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colors.error, fontSize = 12.sp, modifier = Modifier.padding(bottom = 12.dp))
        }

        Button(
            onClick = {
                error = ""
                loading = true
                scope.launch {
                    val result = authManager.register(username.trim(), password, email.trim(), agreed)
                    loading = false
                    result.onSuccess { onRegisterSuccess() }
                        .onFailure { error = it.message ?: "Registrasi gagal" }
                }
            },
            enabled = !loading && username.isNotBlank() && password.isNotBlank() && email.isNotBlank() && agreed,
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(top = 8.dp)
        ) {
            if (loading) CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White)
            else Text("Daftar")
        }

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Text("Sudah punya akun? ", fontSize = 13.sp)
            TextButton(onClick = onGoToLogin) { Text("Masuk") }
        }
    }
}

