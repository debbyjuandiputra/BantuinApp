package com.debdev.bantuin.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debdev.bantuin.R

private const val SEABANK_NUMBER = "901678752217"
private const val SEABANK_NAME = "Debby Juandi Putra"
private const val BSI_NUMBER = "7306306598"
private const val BSI_NAME = "Debby Juandi Putra"
private const val DEVELOPER_CONTACT_URL = "https://debbyjuandiputra.github.io/sosmed/"
private const val INSTAGRAM_URL = "https://www.instagram.com/bantuin_app?igsh=bmkzNG5zem5vMmt6"
private const val CONTACT_EMAIL = "bantuin.help@gmail.com"

@Composable
fun AppDrawerContent(
    isDarkMode: Boolean,
    onToggleMode: () -> Unit,
    onOpenPolicy: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val context = LocalContext.current
    var showDonationDialog by remember { mutableStateOf(false) }
    var showContactDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 24.dp)
    ) {
        Text(
            "Bantuin",
            fontSize = 24.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem(Icons.Default.Favorite, "Donasi") {
            showDonationDialog = true
        }
        DrawerItem(Icons.Default.Policy, "Kebijakan") {
            onCloseDrawer()
            onOpenPolicy()
        }
        DrawerItem(Icons.Default.ContactMail, "Kontak") {
            showContactDialog = true
        }

        Spacer(modifier = Modifier.weight(1f))

        // Baris bawah: Instagram (kiri) berdampingan dengan toggle mode (kanan)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM_URL))
                context.startActivity(intent)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_instagram),
                    contentDescription = "Instagram",
                    modifier = Modifier.size(28.dp)
                )
            }

            IconButton(onClick = onToggleMode) {
                Image(
                    painter = painterResource(id = if (isDarkMode) R.drawable.ic_moon else R.drawable.ic_sun),
                    contentDescription = "Ganti Mode",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }

    if (showDonationDialog) {
        DonationDialog(context = context, onDismiss = { showDonationDialog = false })
    }
    if (showContactDialog) {
        ContactDialog(context = context, onDismiss = { showContactDialog = false })
    }
}

@Composable
private fun DrawerItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colors.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontSize = 15.sp)
    }
}

@Composable
private fun DonationDialog(context: Context, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dukung Pengembang") },
        text = {
            Column {
                Text("Jika aplikasi ini bermanfaat, kamu bisa mendukung pengembangannya melalui:", fontSize = 13.sp, modifier = Modifier.padding(bottom = 16.dp))

                RekeningRow(context, bank = "SeaBank", number = SEABANK_NUMBER, name = SEABANK_NAME)
                Spacer(modifier = Modifier.height(12.dp))
                RekeningRow(context, bank = "BSI", number = BSI_NUMBER, name = BSI_NAME)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Tutup") }
        }
    )
}

@Composable
private fun RekeningRow(context: Context, bank: String, number: String, name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(bank, fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(number, fontSize = 13.sp)
            Text(name, fontSize = 11.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
        }
        IconButton(onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Nomor Rekening", number))
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_copy),
                contentDescription = "Salin nomor rekening $bank",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ContactDialog(context: Context, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hubungi Kami") },
        text = {
            Column {
                ContactOption("Email Aplikasi") {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$CONTACT_EMAIL"))
                    context.startActivity(intent)
                    onDismiss()
                }
                Spacer(modifier = Modifier.height(8.dp))
                ContactOption("Kontak Developer") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEVELOPER_CONTACT_URL))
                    context.startActivity(intent)
                    onDismiss()
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
private fun ContactOption(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = MaterialTheme.colors.primary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(label, fontSize = 14.sp)
    }
}
