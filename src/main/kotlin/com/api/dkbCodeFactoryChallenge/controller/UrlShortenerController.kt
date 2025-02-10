package com.api.dkbCodeFactoryChallenge.controller

import com.api.dkbCodeFactoryChallenge.model.request.ShortenerRequest
import com.api.dkbCodeFactoryChallenge.model.request.ShortenerResponse
import com.api.dkbCodeFactoryChallenge.service.UrlShortenerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView
import java.net.URI

@RestController
@RequestMapping("/api")
class UrlShortenerController(private val urlShortenerService: UrlShortenerService) {

    @PostMapping("/shorten")
    fun shortenUrl(@Valid @RequestBody request: ShortenerRequest): ResponseEntity<ShortenerResponse> {
        val shortUrl = urlShortenerService.shortenUrl(request.url)
        return ResponseEntity.ok(ShortenerResponse(shortUrl))
    }

    @GetMapping("/{shortCode}")
    fun redirect(@PathVariable shortCode: String): RedirectView {
        val originalUrl = urlShortenerService.getOriginalUrl(shortCode)
        return RedirectView(originalUrl)
    }
}

