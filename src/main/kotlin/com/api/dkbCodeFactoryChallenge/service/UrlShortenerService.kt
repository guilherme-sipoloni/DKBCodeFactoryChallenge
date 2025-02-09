package com.api.dkbCodeFactoryChallenge.service

import com.api.dkbCodeFactoryChallenge.exception.NotFoundException
import com.api.dkbCodeFactoryChallenge.model.UrlShortedMapping
import com.api.dkbCodeFactoryChallenge.repository.UrlShortedMappingRepository
import org.springframework.stereotype.Service

@Service
class UrlShortenerService(private val urlShortedMappingRepository: UrlShortedMappingRepository) {

    fun shortenUrl(originalUrl: String): String {
        val shortCode = generateShortCode(originalUrl)

        urlShortedMappingRepository.save(UrlShortedMapping(shortCode = shortCode, originalUrl = originalUrl))

        return "http://localhost:8080/$shortCode"
    }

    fun getOriginalUrl(shortCode: String): String {
        val urlMapping = urlShortedMappingRepository.findByShortCode(shortCode)
            ?: throw NotFoundException("Short URL not found")
        return urlMapping.originalUrl
    }

    private fun generateShortCode(originalUrl: String): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..6)
            .map { allowedChars.random() }
            .joinToString("")
    }
}