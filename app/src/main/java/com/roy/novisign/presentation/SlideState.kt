package com.roy.novisign.presentation

import com.roy.novisign.domain.model.MediaSlide

/**
 * Represents the different states of loading and displaying slides.
 *
 * This sealed class is used to model the various stages of the slide presentation lifecycle,
 * from idle to loading, success (with slide data), or error.
 */
sealed class SlideState {
    data object Idle : SlideState()
    data object Loading : SlideState()
    data class Success(val data: List<MediaSlide>) : SlideState()
    data class Error(val exception: String) : SlideState()
}