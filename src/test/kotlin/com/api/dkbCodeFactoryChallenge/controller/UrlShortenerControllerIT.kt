package com.api.dkbCodeFactoryChallenge.controller

import com.api.dkbCodeFactoryChallenge.exception.NotFoundException
import com.api.dkbCodeFactoryChallenge.service.UrlShortenerService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var urlShortenerService: UrlShortenerService

    @Test
    fun `should return a short URL for a valid request`() {
        val originalUrl = "https://test.com"
        val shortUrl = "http://localhost:8080/api/abc123"
        every { urlShortenerService.shortenUrl(originalUrl) } returns shortUrl

        mockMvc.perform(
            post("/api/shorten")
                .contentType("application/json")
                .content("{\"url\": \"$originalUrl\"}")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.shortUrl").value(shortUrl))
    }

    @Test
    fun `should return 400 Bad Request for an invalid URL`() {
        val invalidUrl = "not-a-valid-url"

        mockMvc.perform(
            post("/api/shorten")
                .contentType("application/json")
                .content("{\"url\": \"$invalidUrl\"}")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.url").value("Invalid URL"))
    }

    @Test
    fun `should redirect to the original URL for a valid short code`() {
        val shortCode = "abc123"
        val originalUrl = "https://test.com"
        every { urlShortenerService.getOriginalUrl(shortCode) } returns originalUrl

        mockMvc.perform(get("/api/$shortCode"))
            .andExpect(status().isFound)
            .andExpect(redirectedUrl(originalUrl))
    }

    @Test
    fun `should return 404 Not Found for an invalid short code`() {
        val invalidShortCode = "invalid"
        every { urlShortenerService.getOriginalUrl(invalidShortCode) } throws NotFoundException("Short URL not found")

        mockMvc.perform(get("/api/$invalidShortCode"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("Short URL not found"))
    }

    @Test
    fun `should return 400 Bad Request for an empty URL`() {
        mockMvc.perform(
            post("/api/shorten")
                .contentType("application/json")
                .content("{\"url\": \"\"}")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.url").value("Invalid URL"))
    }
}