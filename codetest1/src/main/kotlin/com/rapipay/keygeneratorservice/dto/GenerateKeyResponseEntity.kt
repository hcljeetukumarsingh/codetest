package com.rapipay.keygeneratorservice.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

data class GenerateKeyResponseEntity<T>(
    @JsonProperty(value = "status")
    var responseCode: String,
    @JsonProperty(value = "message")
    var responseMessage: String,
    @JsonProperty(value = "result")
    var responseBody: T
)