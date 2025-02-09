package com.api.dkbCodeFactoryChallenge.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ShortenerRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*\$", message = "Invalid URL")
    val url: String
)