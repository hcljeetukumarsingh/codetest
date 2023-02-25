package com.rapipay.keygeneratorservice.service

import com.rapipay.keygeneratorservice.dto.VerifyKeyDto
import reactor.core.publisher.Mono

interface VerifyKeyService {
    fun verify(verifyKeyDto: VerifyKeyDto, isOtp: Boolean): Mono<Boolean>
}