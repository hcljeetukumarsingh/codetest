package com.rapipay.keygeneratorservice.util

import java.util.UUID

object SecretKeyGenerator {

    fun generateSecretKey(): String {
        return UUID.randomUUID().toString() + "-" + System.currentTimeMillis()
    }
}