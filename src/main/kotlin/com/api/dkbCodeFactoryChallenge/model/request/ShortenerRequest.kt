package com.api.dkbCodeFactoryChallenge.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ShortenerRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+(\\/[^\\s]*)?\$", message = "Invalid URL")
    val url: String
)