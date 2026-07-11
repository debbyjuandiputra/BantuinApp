package com.debdev.bantuin.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.debdev.bantuin.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class AuthManager(context: Context) {

    private val firestore: FirebaseFirestore = Firebase.firestore

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "bantuin_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_UID = "uid"
        private const val KEY_USERNAME = "username"
    }

    // ---------- REGISTER (hanya wajib: username, password, email, persetujuan) ----------
    suspend fun register(
        username: String,
        password: String,
        email: String,
        agreedToPolicy: Boolean
    ): Result<Unit> {
        return try {
            if (username.length < 4) throw Exception("Username minimal 4 karakter")
            if (password.length < 8) throw Exception("Password minimal 8 karakter")
            if (!isValidEmail(email)) throw Exception("Format email tidak valid")
            if (!agreedToPolicy) throw Exception("Kamu harus menyetujui kebijakan & syarat penggunaan")

            val existing = firestore.collection("users")
                .whereEqualTo("username", username)
                .get().await()
            if (!existing.isEmpty) throw Exception("Username sudah digunakan")

            val uid = UUID.randomUUID().toString()
            val data = hashMapOf(
                "uid" to uid,
                "username" to username,
                "password_hash" to hashPassword(password),
                "email" to email,
                "nama_lengkap" to "",
                "nama_panggilan" to "",
                "asal_instansi" to "",
                "status" to "",
                "tanggal_lahir" to "",
                "nis_nim" to "",
                "bidang_prodi" to "",
                "agreed_to_policy" to true,
                "created_at" to System.currentTimeMillis(),
                "updated_at" to System.currentTimeMillis()
            )
            firestore.collection("users").document(uid).set(data).await()
            saveSession(uid, username)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- LOGIN ----------
    suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            if (username.isBlank() || password.isBlank()) throw Exception("Username dan password wajib diisi")

            val query = firestore.collection("users")
                .whereEqualTo("username", username)
                .get().await()

            if (query.isEmpty) throw Exception("Username atau password salah")

            val doc = query.documents[0]
            val storedHash = doc.getString("password_hash") ?: ""
            val uid = doc.getString("uid") ?: ""

            if (storedHash != hashPassword(password)) throw Exception("Username atau password salah")

            saveSession(uid, username)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isLoggedIn(): Boolean = prefs.getString(KEY_UID, null) != null

    fun getCurrentUid(): String? = prefs.getString(KEY_UID, null)

    fun getCurrentUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun logout() {
        prefs.edit().clear().apply()
    }

    // ---------- PROFIL ----------
    suspend fun getProfile(): Result<UserData> {
        return try {
            val uid = getCurrentUid() ?: throw Exception("Belum login")
            val doc = firestore.collection("users").document(uid).get().await()

            Result.success(
                UserData(
                    uid = uid,
                    username = doc.getString("username") ?: "",
                    email = doc.getString("email") ?: "",
                    namaLengkap = doc.getString("nama_lengkap") ?: "",
                    namaPanggilan = doc.getString("nama_panggilan") ?: "",
                    asalInstansi = doc.getString("asal_instansi") ?: "",
                    status = doc.getString("status") ?: "",
                    tanggalLahir = doc.getString("tanggal_lahir") ?: "",
                    nisNim = doc.getString("nis_nim") ?: "",
                    bidangProdi = doc.getString("bidang_prodi") ?: "",
                    agreedToPolicy = doc.getBoolean("agreed_to_policy") ?: false
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(data: UserData): Result<Unit> {
        return try {
            val uid = getCurrentUid() ?: throw Exception("Belum login")
            val update = mapOf(
                "nama_lengkap" to data.namaLengkap,
                "nama_panggilan" to data.namaPanggilan,
                "asal_instansi" to data.asalInstansi,
                "status" to data.status,
                "tanggal_lahir" to data.tanggalLahir,
                "nis_nim" to data.nisNim,
                "bidang_prodi" to data.bidangProdi,
                "updated_at" to System.currentTimeMillis()
            )
            firestore.collection("users").document(uid).update(update).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- HELPER ----------
    private fun saveSession(uid: String, username: String) {
        prefs.edit()
            .putString(KEY_UID, uid)
            .putString(KEY_USERNAME, username)
            .apply()
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
