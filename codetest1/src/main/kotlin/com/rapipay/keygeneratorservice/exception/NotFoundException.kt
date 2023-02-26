package com.rapipay.keygeneratorservice.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    errorKey: String,
    httpStatus: HttpStatus = HttpStatus.NOT_FOUND
): AppException(errorKey,httpStatus)