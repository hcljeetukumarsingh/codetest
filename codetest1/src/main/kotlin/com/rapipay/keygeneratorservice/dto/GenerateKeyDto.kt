package com.rapipay.keygeneratorservice.dto

import javax.validation.constraints.NotNull

data class GenerateKeyDto (
    @NotNull
    var identifierName : String,
    @NotNull
    var identifierValue :String,
    @NotNull
    var generatedBy : String,
)
