package com.rapipay.keygeneratorservice.dto

import javax.validation.constraints.NotNull


data class VerifyKeyDto (
    @NotNull
    var identifierValue :String,
    @NotNull
    var generatedBy : String,
    @NotNull
    var key : String,
)


