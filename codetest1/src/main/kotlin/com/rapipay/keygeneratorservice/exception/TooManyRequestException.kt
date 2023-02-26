package com.rapipay.keygeneratorservice.exception

import org.springframework.http.HttpStatus

class TooManyRequestException(
    errorKey: String,
    httpStatus: HttpStatus = HttpStatus.TOO_MANY_REQUESTS
): AppException(errorKey,httpStatus)