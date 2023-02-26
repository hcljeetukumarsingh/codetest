package com.rapipay.keygeneratorservice.service.impl

import com.rapipay.keygeneratorservice.service.MessageSourceService
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class MessageSourceServiceImpl(

    private val messageSource: MessageSource
) : MessageSourceService {

    override fun getMessage(key: String, params: Array<String>): String {
        return messageSource.getMessage(key, params, Locale.ENGLISH)
    }
}