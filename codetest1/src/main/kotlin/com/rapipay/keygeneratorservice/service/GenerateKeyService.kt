package com.rapipay.keygeneratorservice.service

import com.rapipay.keygeneratorservice.dto.GenerateKeyDto
import com.rapipay.keygeneratorservice.entity.GenerateKey
import reactor.core.publisher.Mono

interface GenerateKeyService {
    fun generateOtp( generateKeyDto : GenerateKeyDto): Mono<GenerateKey>
    fun generateSecretKey(generateKeyDto: GenerateKeyDto) : Mono<GenerateKey>
}