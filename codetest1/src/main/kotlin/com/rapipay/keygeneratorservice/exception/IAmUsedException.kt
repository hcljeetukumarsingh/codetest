package com.rapipay.keygeneratorservice.exception

import org.springframework.http.HttpStatus

class IAmUsedException(
    errorKey: String,
    httpStatus: HttpStatus = HttpStatus.IM_USED
): AppException(errorKey,httpStatus)