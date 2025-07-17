package com.roy.novisign.data.remote.Response

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistResponse(
    val screenKey: String,
    val company: String,
    val breakpointInterval: Int,
    val playlists: List<PlaylistDto>,
    val modified: Long
)