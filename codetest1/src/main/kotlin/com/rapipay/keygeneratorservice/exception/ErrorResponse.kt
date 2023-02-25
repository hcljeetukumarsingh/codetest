package com.rapipay.keygeneratorservice.exception

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    val errors: List<Error>
)

data class Error(
    val code: String,
    val message: String,
    val description: String
)