package com.api.dkbCodeFactoryChallenge.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "url_shorted_mapping")
data class UrlShortedMapping(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "short_code", unique = true, nullable = false)
    val shortCode: String,

    @Column(name = "original_url", nullable = false)
    val originalUrl: String
)