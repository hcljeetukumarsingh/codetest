package com.rapipay.keygeneratorservice.util
import com.rapipay.keygeneratorservice.constants.AppConstants
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.BASE_VALUE
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.CRYPTOGRAPHIC_ALGORITHM
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.DIGIT_POWER
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.HASH_16
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.HASH_24
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.HASH_8
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.ONE
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.OTP_DIGITS
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.STRING_SIZE_16
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.THOUSAND_SECONDS
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.THREE
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.TIME_GAP
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.TWO
import com.rapipay.keygeneratorservice.constants.GenerateOtpConstants.ZERO
import java.lang.reflect.UndeclaredThrowableException
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

object OtpGenerator {

    /*HMAC is a specific type of message authentication code (MAC)
    involving a cryptographic hash function and a secret cryptographic key.*/
    private fun hmac_sha(
        crypto: String, keyBytes: ByteArray,
        text: ByteArray
    ): ByteArray {
        try {
            val hmac: Mac = Mac.getInstance(crypto)
            val macKey = SecretKeySpec(keyBytes, "RAW")
            hmac.init(macKey)
            return hmac.doFinal(text)
        } catch (gse: GeneralSecurityException) {
            throw UndeclaredThrowableException(gse)
        }
    }

//    This function converts hexString to byte array
    private fun hexStr2Bytes(hex: String): ByteArray {
        val bArray = BigInteger("10$hex", STRING_SIZE_16).toByteArray()
        val ret = ByteArray(bArray.size - ONE)
        for (i in ret.indices) ret[i] = bArray[i + ONE]
        return ret
    }

    /*This function generate totp using key , time, total digits in otp
    and crytographic algorithm used*/
    fun generateTOTP(
        key: String,                     // Secret key
        time: String,                    // Time in steps form
        returnDigits: String?,           // Number of digits in otp
        crypto: String = CRYPTOGRAPHIC_ALGORITHM   // Cryptographic algorithm for generating otp
    ): String? {
        var time = time
        val codeDigits = Integer.decode(returnDigits).toInt()
        var result: String? = null
        while (time.length < STRING_SIZE_16) time = "0$time"
        val msg = hexStr2Bytes(time)
        val k = hexStr2Bytes(key)
        val hash = hmac_sha(crypto, k, msg)
        val offset: Int = (hash[hash.size - ONE] and (0xf).toByte()).toInt()

        val binary: Int = ((hash[offset] and (0x7f)).toInt() shl HASH_24) or
                ((hash[offset + ONE] and (0x7f)).toInt() shl HASH_16) or
                ((hash[offset + TWO] and (0x7f).toByte()).toInt() shl HASH_8) or
                (hash[offset + THREE] and (0x7f).toByte()).toInt()

        val base = BASE_VALUE
        var exponent = codeDigits
        var digitPower = DIGIT_POWER

        while (exponent != ZERO) {
            digitPower *= base
            --exponent
        }
        val otp = binary % digitPower
        result = otp.toString()
        while (result!!.length < codeDigits) {
            result = "0$result"
        }
        return result
    }

    fun generate(): String {

        val seed32 = SecretKeyGenerator.generateSecretKey().replace("-","")
        val initialTime: Long = ZERO.toLong()
        val timeGap: Long = TIME_GAP.toLong()
        val unixTime = System.currentTimeMillis() / THOUSAND_SECONDS.toLong()
        longArrayOf( unixTime)
        var steps: String
        val T = (unixTime- initialTime) / timeGap
        steps = java.lang.Long.toHexString(T).uppercase(Locale.getDefault())
        while (steps.length < STRING_SIZE_16) steps = "0$steps"
        var otpDigit = OTP_DIGITS
        var cryptoGraphicAlgorithm = CRYPTOGRAPHIC_ALGORITHM

        return generateTOTP(
            seed32, steps, otpDigit,
            cryptoGraphicAlgorithm
        ).toString()
    }

}