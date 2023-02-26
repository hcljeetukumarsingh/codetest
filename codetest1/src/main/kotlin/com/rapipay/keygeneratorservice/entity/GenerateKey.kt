package com.rapipay.keygeneratorservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document("generated-keys")
data class GenerateKey(
    @Id
    var id : String? = null,

    @Field(value = "identifier_name")
    var identifierName: String,

    @Field(value = "identifier_value")
    @Indexed(name = "identifier_value")
    var identifierValue: String,

    @Field(value = "generated_by")
    @Indexed(name= "generated_by")
    var generatedBy: String,

    @Field(value = "key")
    @Indexed(name = "key")
    var key: String? = "",

    @Field(value = "is_otp")
    var isOtp: Boolean? = false,

    @Field(value = "is_verified")
    var isVerified: Boolean? = false,

    @Field(value = "total_verify_attempts")
    var noOfVerifyAttempts: Int? = 0,

    @Field(value = "created_at")
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @Field(value = "updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now()

){
}
