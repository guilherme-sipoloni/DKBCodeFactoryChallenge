package com.api.dkbCodeFactoryChallenge.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ShortenerResponse(
    @JsonProperty(namespace = "short_url")
    val shortUrl: String
)