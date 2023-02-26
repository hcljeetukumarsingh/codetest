package com.rapipay.keygeneratorservice.exception

import org.springframework.http.HttpStatus

open class AppException(
    val errorKey : String,
    val httpStatus: HttpStatus,
) : RuntimeException()
