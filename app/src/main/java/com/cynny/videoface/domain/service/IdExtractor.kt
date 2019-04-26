package com.cynny.videoface.domain.service


interface IdExtractor {
    fun extractId(url: String): String?
}