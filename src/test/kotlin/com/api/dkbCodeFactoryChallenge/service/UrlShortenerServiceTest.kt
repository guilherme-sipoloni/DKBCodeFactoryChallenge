package com.api.service

import com.api.dkbCodeFactoryChallenge.exception.NotFoundException
import com.api.dkbCodeFactoryChallenge.model.UrlShortedMapping
import com.api.dkbCodeFactoryChallenge.repository.UrlShortedMappingRepository
import com.api.dkbCodeFactoryChallenge.service.UrlShortenerService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.web.bind.MethodArgumentNotValidException

class UrlShortenerServiceTest {

    private val urlMappingRepository = mockk<UrlShortedMappingRepository>()
    private val urlShortenerService = UrlShortenerService(urlMappingRepository)

    @Test
    fun `should generate a short url and save short code in mapping table`() {
        val originalUrl = "https://test.com"
        every { urlMappingRepository.save(any()) } returns UrlShortedMapping(shortCode = "abc123", originalUrl = originalUrl)

        urlShortenerService.shortenUrl(originalUrl)

        verify(exactly = 1) { urlMappingRepository.save(any()) }
    }

    @Test
    fun `should return the original URL from a valid short code`() {
        val shortCode = "abc123"
        val originalUrl = "https://test.com"
        every { urlMappingRepository.findByShortCode(shortCode) } returns UrlShortedMapping(shortCode = shortCode, originalUrl = originalUrl)

        val result = urlShortenerService.getOriginalUrl(shortCode)

        assertEquals(originalUrl, result)
        verify(exactly = 1) { urlMappingRepository.findByShortCode(shortCode) }
    }

    @Test
    fun `should throw NotFoundException for an invalid short code`() {
        val shortCode = "invalid"
        every { urlMappingRepository.findByShortCode(shortCode) } returns null

        val exception = assertThrows(NotFoundException::class.java) {
            urlShortenerService.getOriginalUrl(shortCode)
        }
        assertEquals("Short URL not found", exception.message)
        verify(exactly = 1) { urlMappingRepository.findByShortCode(shortCode) }
    }
}