package com.rapipay.keygeneratorservice.service

interface MessageSourceService {
    fun getMessage(key: String, params: Array<String> = emptyArray()): String
}