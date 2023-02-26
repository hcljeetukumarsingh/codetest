package com.rapipay.keygeneratorservice.exception

import com.rapipay.keygeneratorservice.service.MessageSourceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class CustomExceptionHandler(
    private val messageSourceService: MessageSourceService
) {

    @ExceptionHandler
    fun handleInvalidState(appException: AppException): ResponseEntity<Any> {
        return ResponseEntity(
            ErrorResponse(
                listOf(
                    Error(
                        appException.errorKey,
                        messageSourceService.getMessage("${appException.errorKey}.MESSAGE"),
                        messageSourceService.getMessage("${appException.errorKey}.DESCRIPTION")
                    )
                )
            ),
            appException.httpStatus
        )
    }
}