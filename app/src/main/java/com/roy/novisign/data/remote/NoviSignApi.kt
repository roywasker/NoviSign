package com.roy.novisign.data.remote

import com.roy.novisign.data.remote.Response.PlaylistResponse
import io.ktor.utils.io.ByteReadChannel

/**
 * Defines the contract for interacting with the NoviSign API.
 * This interface provides methods for fetching playlists and downloading media content.
 */
interface NoviSignApi {
    suspend fun fetchPlaylist(screenKey: String): NetResult<PlaylistResponse>

    suspend fun downloadMedia(url: String): NetResult<ByteReadChannel>
}