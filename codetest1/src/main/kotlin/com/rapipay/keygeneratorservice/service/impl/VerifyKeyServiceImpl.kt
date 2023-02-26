package com.rapipay.keygeneratorservice.service.impl

import com.rapipay.keygeneratorservice.dto.VerifyKeyDto
import com.rapipay.keygeneratorservice.entity.GenerateKey
import com.rapipay.keygeneratorservice.exception.NotFoundException
import com.rapipay.keygeneratorservice.exception.IAmUsedException
import com.rapipay.keygeneratorservice.exception.InvalidStateException
import com.rapipay.keygeneratorservice.exception.TooManyRequestException
import com.rapipay.keygeneratorservice.repository.GenerateKeyRepository
import com.rapipay.keygeneratorservice.service.VerifyKeyService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.util.Loggers
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class VerifyKeyServiceImpl(
    var generateKeyRepository: GenerateKeyRepository
) : VerifyKeyService {

    @Value("\${max.verify.attempts}")
    var maxVerifyAttempts: Int = 0

    @Value("\${otp.validation.time.in.seconds}")
    var otpValidationTime: Int = 0

    val log = Loggers.getLogger(VerifyKeyServiceImpl::class.java)
    override fun verify(verifyKeyDto: VerifyKeyDto, isOtp: Boolean): Mono<Boolean> {
        return generateKeyRepository.findTop1ByIdentifierValueAndGeneratedByOrderByCreatedAtDesc(
            verifyKeyDto.identifierValue,
            verifyKeyDto.generatedBy
        ).flatMap { entity ->
            checkOTPValidationTime(entity,verifyKeyDto,isOtp)
        }.flatMap { entity ->
            verifyKey(entity,verifyKeyDto,isOtp)
        }.switchIfEmpty {
            throw  NotFoundException("NYE-KEYGEN-004")
        }
    }

    fun checkOTPValidationTime(generateKey: GenerateKey,verifyKeyDto: VerifyKeyDto,isOtp: Boolean): Mono<GenerateKey>{
        var localDateTime = generateKey.createdAt
        var zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli()
        if ((System.currentTimeMillis() - zonedDateTime) / 1000 >  otpValidationTime && (generateKey.isOtp == true))
            throw InvalidStateException("NYE-KEYGEN-007",HttpStatus.BAD_REQUEST)
        return Mono.just(generateKey)
    }

    fun verifyKey(generateKey: GenerateKey, verifyKeyDto: VerifyKeyDto, isOtp: Boolean): Mono<Boolean> {
        if (generateKey.isVerified == true)
            throw  IAmUsedException("NYE-KEYGEN-005")

        if (generateKey.noOfVerifyAttempts!! >= maxVerifyAttempts)
            throw InvalidStateException("NYE-KEYGEN-002")

        return if (generateKey.key == verifyKeyDto.key && generateKey.isOtp == isOtp) {
            generateKey.isVerified = true
            generateKey.updatedAt = LocalDateTime.now()
            generateKeyRepository.save(generateKey).flatMap {
                Mono.just(true)
            }
        } else {
            generateKey.noOfVerifyAttempts = generateKey.noOfVerifyAttempts?.plus(1)
            generateKey.updatedAt = LocalDateTime.now()
            generateKeyRepository.save(generateKey).flatMap {
                throw TooManyRequestException("NYE-KEYGEN-001", HttpStatus.BAD_REQUEST)
            }

        }
    }
}