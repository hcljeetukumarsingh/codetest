package com.rapipay.keygeneratorservice.controller

import com.rapipay.keygeneratorservice.constants.AppConstants.OTP_GENERATED
import com.rapipay.keygeneratorservice.constants.AppConstants.OTP_VERIFIED
import com.rapipay.keygeneratorservice.dto.GenerateKeyDto
import com.rapipay.keygeneratorservice.dto.VerifyKeyDto
import com.rapipay.keygeneratorservice.dto.response.SuccessResponseDto
import com.rapipay.keygeneratorservice.entity.GenerateKey
import com.rapipay.keygeneratorservice.service.GenerateKeyService
import com.rapipay.keygeneratorservice.service.VerifyKeyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.util.Loggers
import javax.validation.Valid

@RestController
@RequestMapping("/key_generator/api/v1")
class OtpController(
    val generateKeyService: GenerateKeyService,
    val verifyKeyService: VerifyKeyService
) {

    val logger = Loggers.getLogger(OtpController::class.java)
    @PostMapping("/generate_otp")
    fun generateKey(@Valid @RequestBody generateKeyDto: GenerateKeyDto): Mono<ResponseEntity<SuccessResponseDto<GenerateKey>>> {
        logger.info("Sending OTP $generateKeyDto")
        return generateKeyService.generateOtp(generateKeyDto)
            .flatMap {
                Mono.just(
                    ResponseEntity(
                        SuccessResponseDto(
                            OTP_GENERATED,
                            it
                        ),
                        HttpStatus.OK
                    )
                )
            }
    }

    @PostMapping("/verify_otp")
    fun verifyKey(@Valid @RequestBody verifyKeyDto: VerifyKeyDto): Mono<ResponseEntity<SuccessResponseDto<Any>>> {
        logger.info("Verifying OTP $verifyKeyDto")
        return verifyKeyService.verify(verifyKeyDto, true)
            .flatMap {
                Mono.just(
                    ResponseEntity(
                        SuccessResponseDto(
                            OTP_VERIFIED,
                            it
                        ),
                        HttpStatus.OK
                    )
                )
            }
    }
}