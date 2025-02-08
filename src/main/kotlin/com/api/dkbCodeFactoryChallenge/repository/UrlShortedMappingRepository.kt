package com.api.dkbCodeFactoryChallenge.repository

import com.api.dkbCodeFactoryChallenge.model.UrlShortedMapping
import org.springframework.data.jpa.repository.JpaRepository

interface UrlMappingRepository : JpaRepository<UrlShortedMapping, Long> {
    fun findByShortCode(shortCode: String): UrlShortedMapping?
}