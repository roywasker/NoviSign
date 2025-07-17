package com.roy.novisign.data.remote.Response

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDto(
    val channelTime: Int,
    val playlistItems: List<PlaylistItemDto>,
    val playlistKey: String
)