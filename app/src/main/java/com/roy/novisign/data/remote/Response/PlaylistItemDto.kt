package com.roy.novisign.data.remote.Response

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistItemDto(
    val creativeRefKey: String? = null,
    val duration: Int,
    val expireDate: String,
    val startDate: String,
    val collectStatistics: Boolean,
    val creativeLabel: String,
    val slidePriority: Int,
    val playlistKey: String,
    val creativeKey: String,
    val orderKey: Int,
    val eventTypesList: List<String>
)