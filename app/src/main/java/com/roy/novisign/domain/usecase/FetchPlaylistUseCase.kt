package com.roy.novisign.domain.usecase

import com.roy.novisign.data.remote.NetResult
import com.roy.novisign.domain.model.MediaSlide

/**
 * Fetches the current playlist for a given screen key.
 * Returns a list of slides (images/videos) in their display order.
 */
interface FetchPlaylistUseCase {
    suspend operator fun invoke(
        screenKey: String,
        onNetworkFailure: suspend (NetResult.NetworkFailure<Exception>) -> Unit,
        onFailure: suspend (NetResult.Failure<Exception>) -> Unit,
        onSuccess: suspend (List<MediaSlide>) -> Unit
        )

    suspend fun getMediaFileFromCacheOrDownload(url: String): String
}