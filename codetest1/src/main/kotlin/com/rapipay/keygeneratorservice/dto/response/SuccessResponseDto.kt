package com.rapipay.keygeneratorservice.dto.response

class SuccessResponseDto<T>(
    val message: String,
    val results: T
)
