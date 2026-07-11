package com.debdev.bantuin.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/** Data profil lengkap user. Field selain username/password/email bersifat opsional. */
data class UserData(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val namaLengkap: String = "",
    val namaPanggilan: String = "",
    val asalInstansi: String = "",
    val status: String = "",       // Siswa, Mahasiswa, Pekerja, Umum
    val tanggalLahir: String = "",
    val nisNim: String = "",
    val bidangProdi: String = "",
    val agreedToPolicy: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

enum class FeatureCategory(val label: String) {
    SEMUA("Semua"),
    DOKUMEN("Dokumen"),
    PERHITUNGAN("Perhitungan"),
    PROGRAMMING("Programming"),
    PENJADWALAN("Penjadwalan"),
    LAINNYA("Lainnya")
}

data class FeatureItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val categories: List<FeatureCategory>,
    val route: String,
    val enabled: Boolean = true
)

val featureList = listOf(
    FeatureItem(
        1, "Konversi Dokumen", Icons.Default.SwapHoriz,
        listOf(FeatureCategory.DOKUMEN), "convert_document"
    ),
    FeatureItem(
        2, "Aplikasi Premium", Icons.Default.WorkspacePremium,
        listOf(FeatureCategory.LAINNYA), "premium_app"
    ),
    FeatureItem(
        3, "Cek Panjang Karakter", Icons.Default.TextFields,
        listOf(FeatureCategory.LAINNYA, FeatureCategory.DOKUMEN), "char_counter"
    ),
    FeatureItem(
        4, "Scan Dokumen", Icons.Default.DocumentScanner,
        listOf(FeatureCategory.DOKUMEN), "scan_document", enabled = false
    ),
    FeatureItem(
        5, "Base64 Encode & Decode", Icons.Default.Code,
        listOf(FeatureCategory.PROGRAMMING), "base64", enabled = false
    ),
    FeatureItem(
        6, "URL Encode & Decode", Icons.Default.Link,
        listOf(FeatureCategory.PROGRAMMING), "url_encode", enabled = false
    ),
    FeatureItem(
        7, "To Do List Modern", Icons.Default.Checklist,
        listOf(FeatureCategory.PENJADWALAN), "todo_list", enabled = false
    ),
    FeatureItem(
        8, "Alarm dan Pengingat", Icons.Default.Alarm,
        listOf(FeatureCategory.PENJADWALAN), "alarm", enabled = false
    ),
    FeatureItem(
        9, "Kalkulator Ilmiah", Icons.Default.Calculate,
        listOf(FeatureCategory.PERHITUNGAN), "calc_scientific", enabled = false
    ),
    FeatureItem(
        10, "Kalkulator Statistik", Icons.Default.BarChart,
        listOf(FeatureCategory.PERHITUNGAN), "calc_statistic", enabled = false
    ),
    FeatureItem(
        11, "Hapus Latar Belakang", Icons.Default.Image,
        listOf(FeatureCategory.LAINNYA), "remove_bg", enabled = false
    ),
    FeatureItem(
        12, "UUID Generator", Icons.Default.VpnKey,
        listOf(FeatureCategory.PROGRAMMING), "uuid_generator"
    ),
    FeatureItem(
        13, "Generate Kode Authenticator", Icons.Default.Security,
        listOf(FeatureCategory.PROGRAMMING), "authenticator"
    )
)
