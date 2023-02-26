package com.rapipay.keygeneratorservice.exception

import org.springframework.http.HttpStatus

class TooEarlyException(
    errorKey: String,
    httpStatus: HttpStatus = HttpStatus.TOO_EARLY
): AppException(errorKey,httpStatus)