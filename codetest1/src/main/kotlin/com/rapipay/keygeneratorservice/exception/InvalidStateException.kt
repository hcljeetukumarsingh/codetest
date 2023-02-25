package com.rapipay.keygeneratorservice.exception

import org.springframework.http.HttpStatus

class InvalidStateException(
    errorKey : String,
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
): AppException(errorKey, httpStatus)
