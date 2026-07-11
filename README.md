# Bantuin — Alat Bantu Siswa & Mahasiswa

Aplikasi Android (Kotlin + Jetpack Compose) dengan fitur-fitur bantu tugas untuk siswa & mahasiswa.

- **Developer ID:** DebDev
- **Developer:** Debby Juandi Putra
- **Package name:** `com.debdev.bantuin`
- **Email:** bantuin.help@gmail.com

---

## ⚠️ Langkah WAJIB sebelum project bisa di-build

### 1. Generate Gradle Wrapper
Karena project ini dibuat di luar Android Studio, file biner `gradle/wrapper/gradle-wrapper.jar`, `gradlew`, dan `gradlew.bat` **belum ada**. Cara termudah:

1. Buka folder project ini di **Android Studio** (File → Open).
2. Android Studio otomatis mendeteksi & membuatkan file wrapper saat sinkronisasi pertama.
3. Atau via terminal (kalau sudah punya Gradle lokal): `gradle wrapper --gradle-version 8.4`

Setelah wrapper ter-generate, barulah workflow GitHub Actions (`.github/workflows/build.yml`) bisa jalan otomatis setiap push ke branch `main`.

### 2. Setup Firebase (untuk sistem login/register)
1. Buat project baru di [Firebase Console](https://console.firebase.google.com).
2. Tambahkan Android App dengan package name **`com.debdev.bantuin`**.
3. Download `google-services.json`, taruh di folder `app/google-services.json`.
4. Aktifkan **Firestore Database** (mode production, lalu atur rules sesuai kebutuhan keamanan).
5. Buat collection `users` (akan terisi otomatis begitu ada yang register).

Tanpa langkah ini, fitur login/register tidak akan berfungsi karena project belum terhubung ke Firebase manapun.

---

## ✅ Fitur yang sudah AKTIF & berfungsi
| # | Fitur | Kategori | Catatan |
|---|-------|----------|---------|
| 1 | Konversi Dokumen | Dokumen | UI lengkap (pilih file & jenis konversi). Mesin konversi asli (PDF↔DOCX, XLSX↔CSV) masih placeholder — lihat catatan di bawah |
| 2 | Aplikasi Premium | Lainnya | CapCut Pro & Canva Pro, masing-masing dengan pilihan 7 hari / 1 bulan, mengarah ke link Lynk.id kamu |
| 3 | Cek Panjang Karakter | Dokumen/Lainnya | Input teks langsung berfungsi penuh. Input via file: `.txt` terbaca penuh; docx/pdf/dll masih placeholder |
| 12 | UUID Generator | Programming | Generate UUID v4 + salin ke clipboard |
| 13 | Generate Kode Authenticator | Programming | TOTP standar Google Authenticator (HMAC-SHA1), bisa pilih 6/8 digit & periode 30/60 detik |

## 🔒 Fitur yang sengaja di-nonaktifkan dulu (abu-abu, tidak bisa diklik)
Scan Dokumen, Base64 Encode/Decode, URL Encode/Decode, To Do List, Alarm & Pengingat, Kalkulator Ilmiah, Kalkulator Statistik, Hapus Latar Belakang — sesuai instruksi, semua ini ditampilkan tapi belum aktif.

---

## 📌 Catatan teknis penting

**Konversi Dokumen** — konversi PDF↔DOCX dan XLSX↔CSV yang sesungguhnya butuh library tambahan yang cukup berat untuk Android (mis. Apache POI untuk docx/xlsx, PDFBox-Android untuk pdf), atau alternatifnya memakai API cloud converter. UI sudah siap; tinggal isi logic di `ConvertDocumentScreen.kt` bagian `// TODO`.

**Cek Panjang Karakter (via file)** — ekstraksi teks dari `.docx`/`.pdf` butuh library serupa. Untuk sekarang, hanya `.txt` yang benar-benar dibaca isinya.

**Autentikasi** — memakai Firestore + hashing SHA-256 (bukan bcrypt, karena bcrypt butuh library tambahan). Untuk keamanan lebih tinggi ke depannya, pertimbangkan migrasi ke Firebase Authentication custom token atau backend sendiri dengan bcrypt/argon2.

**Data yang tersimpan opsional** (nama lengkap, nama panggilan, asal instansi, status, tanggal lahir, NIS/NIM, bidang studi) — semuanya diisi lewat menu profil **setelah** login, sesuai permintaan. Foto profil sengaja belum ditambahkan.

**Mode Terang/Gelap** — default selalu **terang** setiap install baru, tersimpan lokal di device (bukan di akun), bisa diganti dari ikon matahari/bulan di dalam drawer (hamburger menu).

---

## 🎨 Struktur Project
```
Bantuin/
├── app/
│   ├── src/main/
│   │   ├── java/com/debdev/bantuin/
│   │   │   ├── auth/AuthManager.kt          → Login, register, sesi, profil
│   │   │   ├── model/Models.kt              → UserData, daftar 13 fitur
│   │   │   ├── util/TotpGenerator.kt        → Logic TOTP authenticator
│   │   │   ├── ui/theme/                    → Warna, tema terang/gelap
│   │   │   ├── ui/components/               → TopBar, Drawer, Grid fitur, Tab kategori
│   │   │   ├── ui/screens/                  → Login, Register, Dashboard, Profil, Kebijakan
│   │   │   └── ui/screens/features/         → Layar tiap fitur (convert, premium, dst)
│   │   ├── res/
│   │   │   ├── drawable/                    → ic_back, ic_instagram, ic_sun, ic_moon, ic_copy (vector)
│   │   │   └── mipmap-*/                    → Ikon aplikasi (dari logo yang kamu upload)
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── .github/workflows/build.yml              → Auto-build APK via GitHub Actions
└── build.gradle
```

---

## Rekening Donasi (ditampilkan di dalam app)
- SeaBank: 901678752217 — a.n. Debby Juandi Putra
- BSI: 7306306598 — a.n. Debby Juandi Putra

## Kontak (ditampilkan di dalam app)
- Email Aplikasi → bantuin.help@gmail.com
- Kontak Developer → https://debbyjuandiputra.github.io/sosmed/
- Instagram → https://www.instagram.com/bantuin_app

---

## Langkah selanjutnya yang disarankan
1. Buka di Android Studio → sinkronisasi → generate wrapper otomatis.
2. Hubungkan Firebase (lihat langkah 2 di atas).
3. Coba jalankan di emulator/device untuk test alur register → login → dashboard.
4. Baru lanjut aktifkan satu-satu fitur yang masih abu-abu sesuai prioritas kamu.
