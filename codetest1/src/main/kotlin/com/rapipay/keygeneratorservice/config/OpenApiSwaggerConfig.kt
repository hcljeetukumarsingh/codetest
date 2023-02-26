package com.rapipay.keygeneratorservice.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "KeyGenerator APIs",
        version = "v1",
        description = "This App provides REST APIs for Key Generation",
        contact = Contact(name = "Rapipay", url = "https://rapipay.com", email = "rapiay@gmail.com")
    )
)
class OpenApiSwaggerConfig {

}