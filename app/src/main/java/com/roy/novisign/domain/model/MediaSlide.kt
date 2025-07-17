package com.roy.novisign.domain.model

import kotlin.time.Duration

/**
 * A slide to display in the slideshow; either an image or a video.
 */
sealed class MediaSlide {
    abstract val url: String
    abstract val duration: Duration

    data class ImageSlide(
        override val url: String,
        override val duration: Duration
    ) : MediaSlide()

    data class VideoSlide(
        override val url: String,
        override val duration: Duration
    ) : MediaSlide()
}