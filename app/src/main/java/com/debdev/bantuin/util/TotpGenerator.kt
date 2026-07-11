package com.debdev.bantuin.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Generator kode OTP standar Google Authenticator (RFC 6238 / TOTP).
 * Mendukung kunci Base32 (standar), dan secara lenient akan mengabaikan
 * karakter yang tidak valid di Base32 (mirip perilaku layanan seperti 2fa.live),
 * sehingga pengguna cukup bebas memasukkan kunci apa pun.
 */
object TotpGenerator {

    private const val BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"

    fun generate(
        secret: String,
        digits: Int = 6,
        periodSeconds: Int = 30,
        timeMillis: Long = System.currentTimeMillis()
    ): String {
        val keyBytes = base32Decode(secret)
        if (keyBytes.isEmpty()) return "-".repeat(digits)

        val counter = timeMillis / 1000L / periodSeconds
        val counterBytes = ByteArray(8)
        var value = counter
        for (i in 7 downTo 0) {
            counterBytes[i] = (value and 0xff).toByte()
            value = value shr 8
        }

        val mac = Mac.getInstance("HmacSHA1")
        mac.init(SecretKeySpec(keyBytes, "HmacSHA1"))
        val hash = mac.doFinal(counterBytes)

        val offset = (hash[hash.size - 1].toInt() and 0xf)
        val binary = ((hash[offset].toInt() and 0x7f) shl 24) or
                ((hash[offset + 1].toInt() and 0xff) shl 16) or
                ((hash[offset + 2].toInt() and 0xff) shl 8) or
                (hash[offset + 3].toInt() and 0xff)

        val otp = binary % Math.pow(10.0, digits.toDouble()).toInt()
        return otp.toString().padStart(digits, '0')
    }

    /** Detik tersisa sebelum kode berganti pada periode saat ini. */
    fun secondsRemaining(periodSeconds: Int = 30, timeMillis: Long = System.currentTimeMillis()): Int {
        val elapsed = (timeMillis / 1000L) % periodSeconds
        return (periodSeconds - elapsed).toInt()
    }

    private fun base32Decode(input: String): ByteArray {
        val clean = input.uppercase().filter { it in BASE32_CHARS }
        if (clean.isEmpty()) return ByteArray(0)

        var bits = 0
        var value = 0
        val output = ArrayList<Byte>()

        for (c in clean) {
            value = (value shl 5) or BASE32_CHARS.indexOf(c)
            bits += 5
            if (bits >= 8) {
                output.add(((value shr (bits - 8)) and 0xff).toByte())
                bits -= 8
            }
        }
        return output.toByteArray()
    }
}
