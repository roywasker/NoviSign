package com.roy.novisign.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roy.novisign.domain.usecase.FetchPlaylistUseCase
import com.roy.novisign.utils.ApiConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SlideShowViewModel @Inject constructor(
    private val fetchPlaylistUseCase: FetchPlaylistUseCase
) : ViewModel() {

    private val _slides = MutableStateFlow<SlideState>(SlideState.Idle)
    val slides: StateFlow<SlideState> = _slides.asStateFlow()

    init {
        fetchPlaylist()
    }

    private fun fetchPlaylist() {
        viewModelScope.launch {
            _slides.value = SlideState.Loading
            fetchPlaylistUseCase.invoke(
                ApiConstants.SCREEN_KEY,
                onNetworkFailure = { networkFailure ->
                    _slides.value = SlideState.Error(networkFailure.networkError.message.toString())
                },
                onFailure = { error ->
                    _slides.value = SlideState.Error(error.generalError.message.toString())
                },
                onSuccess = { list ->
                    _slides.value = SlideState.Success(list)
                }
            )
        }
    }
}