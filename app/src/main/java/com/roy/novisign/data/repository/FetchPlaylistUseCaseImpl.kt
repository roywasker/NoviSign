package com.roy.novisign.data.repository

import android.content.Context
import android.util.Log
import com.roy.novisign.utils.ApiConstants
import com.roy.novisign.data.remote.NetResult
import com.roy.novisign.data.remote.NoviSignApi
import com.roy.novisign.domain.model.MediaSlide
import com.roy.novisign.domain.usecase.FetchPlaylistUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.time.Duration.Companion.seconds
import javax.inject.Inject
import androidx.core.net.toUri
import io.ktor.utils.io.jvm.javaio.copyTo
import java.io.File

class FetchPlaylistUseCaseImpl @Inject constructor(
    private val api: NoviSignApi,
    @ApplicationContext private val context: Context
) : FetchPlaylistUseCase {

    override suspend fun invoke(
        screenKey: String,
        onNetworkFailure: suspend (NetResult.NetworkFailure<Exception>) -> Unit,
        onFailure: suspend (NetResult.Failure<Exception>) -> Unit,
        onSuccess: suspend (List<MediaSlide>) -> Unit
    ) {
        val result = api.fetchPlaylist(screenKey)
        when (result) {
            is NetResult.Success -> {
                val slides = result.response.playlists
                    .flatMap { it.playlistItems }
                    .sortedBy { it.orderKey }
                    .map { item ->
                        val url = ApiConstants.BASE_URL +
                                ApiConstants.CREATIVE_ENDPOINT +
                                item.creativeKey
                        val localPath = getMediaFileFromCacheOrDownload(url)
                        val duration = item.duration.seconds
                        if (item.creativeKey.endsWith(".mp4")) {
                            MediaSlide.VideoSlide(localPath, duration)
                        } else {
                            MediaSlide.ImageSlide(localPath, duration)
                        }
                    }
                onSuccess(slides)
            }
            is NetResult.Failure<*> -> {
                onFailure.invoke(NetResult.Failure(result.generalError))
            }
            is NetResult.NetworkFailure<*> -> {
                onNetworkFailure.invoke(NetResult.NetworkFailure(result.networkError))
            }
        }
    }
    override suspend fun getMediaFileFromCacheOrDownload(url: String): String {
        val fileName = url.toUri().lastPathSegment ?: url.hashCode().toString()
        val file = File(context.cacheDir, fileName)
        if (!file.exists()) {
            val channel =  api.downloadMedia(url)
            when (channel) {
                is NetResult.Success -> {
                    channel.response.copyTo(file.outputStream())
                }
                is NetResult.Failure<*> -> {
                    Log.e("FetchPlaylist", "downloadMediaIfNeeded: ${channel.generalError.message}")
                }
                is NetResult.NetworkFailure<*> -> {
                    Log.e("FetchPlaylist", "downloadMediaIfNeeded: ${channel.networkError.message}")
                }
            }
        }
        return file.toURI().toString()
    }
}
