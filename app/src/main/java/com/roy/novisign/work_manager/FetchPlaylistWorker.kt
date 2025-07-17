package com.roy.novisign.work_manager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.roy.novisign.domain.usecase.FetchPlaylistUseCase
import com.roy.novisign.utils.ApiConstants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * A [CoroutineWorker] responsible for fetching the playlist from the backend.
 *
 * This worker uses the [FetchPlaylistUseCase] to perform the network request.
 * It handles success, network failures, and general failures by logging appropriate messages.
 * In case of an exception during the process, it will mark the work for retry.
 *
 * @param context The application context.
 * @param params Parameters to setup the worker, like input data.
 * @param fetchPlaylistUseCase The use case responsible for the playlist fetching logic.
 */
@HiltWorker
class FetchPlaylistWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val fetchPlaylistUseCase: FetchPlaylistUseCase
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            fetchPlaylistUseCase.invoke(
                ApiConstants.SCREEN_KEY,
                onNetworkFailure = {
                    Log.e("FetchPlaylistWorker", "Network error: ${it.networkError.message}")
                },
                onFailure = {
                    Log.e("FetchPlaylistWorker", "General error: ${it.generalError.message}")
                },
                onSuccess = {}
            )
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}