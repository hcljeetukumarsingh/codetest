package com.rapipay.keygeneratorservice.service.impl

import com.mapk.kmapper.KMapper
import com.rapipay.keygeneratorservice.dto.GenerateKeyDto
import com.rapipay.keygeneratorservice.entity.GenerateKey
import com.rapipay.keygeneratorservice.exception.TooEarlyException
import com.rapipay.keygeneratorservice.exception.TooManyRequestException
import com.rapipay.keygeneratorservice.repository.GenerateKeyRepository
import com.rapipay.keygeneratorservice.service.GenerateKeyService
import com.rapipay.keygeneratorservice.util.OtpGenerator
import com.rapipay.keygeneratorservice.util.SecretKeyGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.util.Loggers
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime


@Service
@PropertySource("classpath:test.properties")
class GenerateKeyServiceImpl(
    private val generateKeyRepository: GenerateKeyRepository,
) : GenerateKeyService {

    @Value("\${min.regeneration.in.seconds}")
    var regenerationTime: Int = 0

    @Value("\${max.generation.attempts}")
    var maxGenerationAttempts: Int = 0

    @Value("\${test.user.mobile.1}")
    var testUserMobile1:String = ""

    @Value("\${test.user.email.1}")
    var testUserEmail1:String = ""

    @Value("\${test.user.mobile.2}")
    var testUserMobile2:String = ""

    @Value("\${test.user.email.2}")
    var testUserEmail2:String = ""

    @Value("\${test.fixed.otp}")
    var testFixOtp:String = ""

    val log = Loggers.getLogger(GenerateKeyServiceImpl::class.java)
    override fun generateOtp(generateKeyDto: GenerateKeyDto): Mono<GenerateKey> {
        return generateKeyRepository.findTop1ByIdentifierValueAndGeneratedByOrderByCreatedAtDesc(
            generateKeyDto.identifierValue,
            generateKeyDto.generatedBy
        )
            .flatMap { entity ->
                checkRegenerationTime(entity)
            }.flatMap { entity ->
                checkRegenerationAttempt(entity)
            }
            .flatMap {
                generateOtpKey(generateKeyDto)
            }
            .switchIfEmpty {
                generateOtpKey(generateKeyDto)
            }
            .flatMap { response ->
                Mono.just(response)
            }
    }

    fun checkRegenerationTime(generateKey: GenerateKey): Mono<GenerateKey> {
        var localDateTime = generateKey.createdAt
        var zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli()
        if ((System.currentTimeMillis() - zonedDateTime) / 1000 < regenerationTime && (generateKey.isOtp == true))
            return Mono.error(TooEarlyException("NYE-KEYGEN-003", HttpStatus.TOO_EARLY))
        return Mono.just(generateKey)
    }

    fun checkRegenerationAttempt(generateKey: GenerateKey): Mono<GenerateKey> {
        val flux = generateKeyRepository.findAllByIdentifierValueAndGeneratedByAndCreatedAtBetween(
            generateKey.identifierValue,
            generateKey.generatedBy,
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now()
        )
        return flux.count().flatMap {
            if (it >= maxGenerationAttempts)
                throw TooManyRequestException("NYE-KEYGEN-006")
            Mono.just(generateKey)
        }
    }

    fun generateOtpKey(generateKeyDto: GenerateKeyDto): Mono<GenerateKey> {
        val generateKeyEntity: GenerateKey = KMapper(::GenerateKey).map(generateKeyDto)
        val testUsers = arrayOf(testUserMobile1,testUserMobile2,testUserEmail1,testUserEmail2)
        if(testUsers.contains(generateKeyDto.identifierValue)) {
            generateKeyEntity.key = testFixOtp
        }
        else {
            generateKeyEntity.key = OtpGenerator.generate()
        }
        generateKeyEntity.isVerified = false
        generateKeyEntity.isOtp = true

        return generateKeyRepository.save(generateKeyEntity)
            .flatMap { response ->
                Mono.just(response)
            }
    }

    override fun generateSecretKey(generateKeyDto: GenerateKeyDto): Mono<GenerateKey> {

        val generateKeyEntity: GenerateKey = KMapper(::GenerateKey).map(generateKeyDto)
        generateKeyEntity.key = SecretKeyGenerator.generateSecretKey()
        generateKeyEntity.isOtp = false
        generateKeyEntity.isVerified = false

        return generateKeyRepository.save(generateKeyEntity)
            .flatMap { response ->
                Mono.just(response)
            }
    }
}




