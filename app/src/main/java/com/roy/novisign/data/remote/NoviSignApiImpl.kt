package com.roy.novisign.data.remote

import com.roy.novisign.data.remote.Response.PlaylistResponse
import com.roy.novisign.utils.ApiConstants
import com.roy.novisign.utils.networkRequestBlock
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.utils.io.ByteReadChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoviSignApiImpl @Inject constructor(
    private val client: HttpClient
) : NoviSignApi {
    override suspend fun fetchPlaylist(screenKey: String): NetResult<PlaylistResponse> {
        return try {
            client.networkRequestBlock(
                HttpRequestBuilder().apply {
                    method = HttpMethod.Get
                    url("${ApiConstants.BASE_URL}${ApiConstants.PLAYLIST_ENDPOINT}$screenKey")
                }
            )
        } catch (e: Exception) {
            NetResult.Failure(e)
        }
    }

    override suspend fun downloadMedia(url: String): NetResult<ByteReadChannel> {
        return try {
            client.networkRequestBlock(
                HttpRequestBuilder().apply {
                    method = HttpMethod.Get
                    url(url)
                }
            )
        } catch (e: Exception) {
            NetResult.Failure(e)
        }
    }
}