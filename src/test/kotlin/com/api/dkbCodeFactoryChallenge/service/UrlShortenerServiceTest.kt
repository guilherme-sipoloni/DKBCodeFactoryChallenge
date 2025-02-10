package com.api.service

import com.api.dkbCodeFactoryChallenge.exception.DuplicateShortCodeException
import com.api.dkbCodeFactoryChallenge.exception.NotFoundException
import com.api.dkbCodeFactoryChallenge.model.UrlShortedMapping
import com.api.dkbCodeFactoryChallenge.model.request.ShortenerRequest
import com.api.dkbCodeFactoryChallenge.repository.UrlShortedMappingRepository
import com.api.dkbCodeFactoryChallenge.service.UrlShortenerService
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import java.time.LocalDateTime

class UrlShortenerServiceTest {

    private val urlMappingRepository = mockk<UrlShortedMappingRepository>()
    private val urlShortenerService = spyk(UrlShortenerService(urlMappingRepository))

    @Test
    fun `should generate a short url and save short code in mapping table`() {
        val originalUrl = "https://test.com"
        every { urlMappingRepository.save(any()) } returns
                UrlShortedMapping(
                    shortCode = "abc123",
                    originalUrl = originalUrl,
                    createdAt = LocalDateTime.now()
                )

        urlShortenerService.shortenUrl(ShortenerRequest(url = originalUrl))

        verify(exactly = 1) { urlMappingRepository.save(any()) }
    }

    @Test
    fun `should return the original URL from a valid short code`() {
        val shortCode = "abc123"
        val originalUrl = "https://test.com"
        every { urlMappingRepository.findByShortCode(shortCode) } returns
                UrlShortedMapping(
                    shortCode = shortCode,
                    originalUrl = originalUrl,
                    createdAt = LocalDateTime.now()
                )

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

    @Test
    fun `should retry when duplicate short code happen`() {
        val originalUrl = "https://example.com"
        val shortCode1 = "abc123"
        val shortCode2 = "xyz789"

        every { urlMappingRepository.save(UrlShortedMapping(shortCode = shortCode1, originalUrl = originalUrl)) } throws DataIntegrityViolationException("Duplicate short code")
        every { urlMappingRepository.save(UrlShortedMapping(shortCode = shortCode2, originalUrl = originalUrl)) } returns UrlShortedMapping(shortCode = shortCode2, originalUrl = originalUrl)

        every { urlShortenerService.generateShortCode() } returnsMany listOf(shortCode1, shortCode2)

        val shortUrl = urlShortenerService.shortenUrl(ShortenerRequest(originalUrl))

        assertEquals("http://localhost:8080/api/$shortCode2", shortUrl)
        verify(exactly = 2) { urlMappingRepository.save(any()) }
    }

    @Test
    fun `should throw DuplicateShortCodeException after max retries`() {
        val originalUrl = "https://example.com"
        val shortCode = "abc123"

        every { urlMappingRepository.save(UrlShortedMapping(shortCode = shortCode, originalUrl = originalUrl)) } throws DataIntegrityViolationException("Duplicate short code")
        every { urlShortenerService.generateShortCode() } returns shortCode

        val exception = assertThrows(DuplicateShortCodeException::class.java) {
            urlShortenerService.shortenUrl(ShortenerRequest(originalUrl))
        }
        assertEquals("Failed to generate a unique short code after multiple attempts", exception.message)
        verify(exactly = 3) { urlMappingRepository.save(any()) }
    }
}