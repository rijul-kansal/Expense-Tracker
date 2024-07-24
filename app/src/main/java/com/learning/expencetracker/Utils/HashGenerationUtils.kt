package com.learning.expencetracker.Utils

import android.util.Base64
import android.util.Base64.NO_WRAP
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HashGenerationUtils {
    fun generateHashFromSDK(
        hashData: String,
        salt: String?,
        merchantSecretKey: String? = null
    ): String? {
        return if (merchantSecretKey.isNullOrEmpty()) calculateHash("$hashData$salt")
        else calculateHmacSha1(hashData, merchantSecretKey)
    }
    fun generateV2HashFromSDK(hashString: String, salt: String?): String? {
        return calculateHmacSha256(hashString, salt)
    }
    private fun calculateHash(hashString: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        messageDigest.update(hashString.toByteArray())
        val mdbytes = messageDigest.digest()
        return getHexString(mdbytes)
    }
    private fun calculateHmacSha1(hashString: String, key: String): String? {
        return try {
            val type = "HmacSHA1"
            val secret = SecretKeySpec(key.toByteArray(), type)
            val mac: Mac = Mac.getInstance(type)
            mac.init(secret)
            val bytes: ByteArray = mac.doFinal(hashString.toByteArray())
            getHexString(bytes)
        } catch (e: Exception) {
            null
        }
    }
    private fun getHexString(data: ByteArray): String {
        // Create Hex String
        val hexString = StringBuilder()
        for (byte in data) {
            var hex = Integer.toHexString(0xFF and byte.toInt())
            while (hex.length < 2) hex = "0$hex"
            hexString.append(hex)
        }
        return hexString.toString()
    }
    private fun calculateHmacSha256(hashString: String, salt: String?): String? {
        return try {
            val type = "HmacSHA256"
            val secret = SecretKeySpec(salt?.toByteArray(), type)
            val mac: Mac = Mac.getInstance(type)
            mac.init(secret)
            val bytes: ByteArray = mac.doFinal(hashString.toByteArray())
            Base64.encodeToString(bytes, NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }
}