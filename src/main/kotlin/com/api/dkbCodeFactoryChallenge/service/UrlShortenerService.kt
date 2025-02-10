package com.api.dkbCodeFactoryChallenge.service

import com.api.dkbCodeFactoryChallenge.exception.DuplicateShortCodeException
import com.api.dkbCodeFactoryChallenge.exception.NotFoundException
import com.api.dkbCodeFactoryChallenge.model.UrlShortedMapping
import com.api.dkbCodeFactoryChallenge.model.request.ShortenerRequest
import com.api.dkbCodeFactoryChallenge.repository.UrlShortedMappingRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.net.URI

@Service
class UrlShortenerService(private val urlShortedMappingRepository: UrlShortedMappingRepository) {

    fun shortenUrl(shortenerRequest: ShortenerRequest): String {
        val originalUrlValidated = validateAndAddProtocol(shortenerRequest.url)
        var retries = 3

        do {
            val shortCode = generateShortCode()
            try {
                urlShortedMappingRepository.save(UrlShortedMapping(shortCode = shortCode, originalUrl = originalUrlValidated))
                return "http://localhost:8080/api/$shortCode"
            } catch (ex: DataIntegrityViolationException) {
                retries--
                if (retries == 0) {
                    throw DuplicateShortCodeException("Failed to generate a unique short code after multiple attempts")
                }
            }
        } while (true)
    }

    fun getOriginalUrl(shortCode: String): String {
        val urlMapping = urlShortedMappingRepository.findByShortCode(shortCode)
            ?: throw NotFoundException("Short URL not found")
        return urlMapping.originalUrl
    }

    fun generateShortCode(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..6)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun validateAndAddProtocol(url: String, defaultProtocol: String = "http"): String {
        return try {
            val uri = URI(url)
            if (uri.scheme == null) "$defaultProtocol://$url" else url
        } catch (e: Exception) {
            "$defaultProtocol://$url"
        }
    }
}