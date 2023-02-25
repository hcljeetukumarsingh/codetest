package com.rapipay.keygeneratorservice.repository

import com.rapipay.keygeneratorservice.entity.GenerateKey
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
interface GenerateKeyRepository :
    ReactiveMongoRepository<GenerateKey, String> {

    fun findTop1ByIdentifierValueAndGeneratedByOrderByCreatedAtDesc(
        identifierValue: String,
        generatedBy: String
    ): Mono<GenerateKey>

    fun findAllByIdentifierValueAndGeneratedByAndCreatedAtBetween(
        identifierValue: String,
        generatedBy: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Flux<GenerateKey>
}