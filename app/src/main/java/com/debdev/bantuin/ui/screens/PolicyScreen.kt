package com.debdev.bantuin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val POLICY_TEXT = """
Syarat dan Ketentuan Penggunaan
Terakhir diperbarui: 11 Juli 2026

Dengan membuat akun atau menggunakan aplikasi ini, Anda dianggap telah membaca, memahami, dan menyetujui seluruh syarat dan ketentuan berikut.

1. Penggunaan Layanan
Aplikasi ini menyediakan berbagai alat bantu bagi siswa, mahasiswa, dan pengguna umum, seperti konversi file, pemindaian dokumen, kalkulator, generator UUID, generator kode autentikator, pengingat, serta fitur lainnya.

2. Akun Pengguna
Pengguna bertanggung jawab atas keamanan akun dan password yang dimiliki.
Pengguna wajib memberikan informasi yang benar dan tidak menggunakan identitas milik orang lain tanpa izin.

3. Kewajiban Pengguna
Pengguna dilarang:
- Menggunakan aplikasi untuk kegiatan yang melanggar hukum.
- Menyalahgunakan layanan sehingga mengganggu pengguna lain.
- Mencoba memperoleh akses tanpa izin ke sistem aplikasi.
- Menyebarkan malware, virus, atau kode berbahaya melalui layanan.

4. Hak Pengembang
Pengembang berhak:
- Memperbarui atau menghentikan fitur tertentu.
- Menangguhkan atau menghapus akun yang terbukti melanggar ketentuan.
- Melakukan pemeliharaan sistem sewaktu-waktu.

5. Hak Kekayaan Intelektual
Seluruh hak cipta, desain, logo, ikon, serta konten aplikasi merupakan milik pengembang atau pihak yang memberikan izin penggunaannya.
Pengguna tidak diperbolehkan menyalin, memodifikasi, atau mendistribusikan bagian aplikasi tanpa izin.

6. Batasan Tanggung Jawab
Aplikasi disediakan sebagaimana adanya.
Pengembang tidak bertanggung jawab atas kerugian yang timbul akibat penggunaan aplikasi, kehilangan data, gangguan layanan, maupun kesalahan yang berasal dari perangkat atau jaringan pengguna.

7. Perubahan Layanan
Fitur dalam aplikasi dapat ditambah, diubah, atau dihapus sewaktu-waktu tanpa pemberitahuan terlebih dahulu.

8. Penghentian Akun
Pengembang dapat menonaktifkan akun yang melanggar syarat penggunaan atau ketentuan hukum yang berlaku.

9. Perubahan Syarat dan Ketentuan
Syarat dan Ketentuan ini dapat diperbarui sewaktu-waktu. Versi terbaru akan tersedia di dalam aplikasi.

10. Hukum yang Berlaku
Syarat dan Ketentuan ini diatur sesuai dengan hukum yang berlaku di Republik Indonesia.
"""

@Composable
fun PolicyScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Kebijakan & Syarat Penggunaan") },
            navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali") }
            },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 1.dp
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(POLICY_TEXT, fontSize = 14.sp, lineHeight = 22.sp)
        }
    }
}
