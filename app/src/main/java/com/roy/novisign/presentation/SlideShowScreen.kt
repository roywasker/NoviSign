package com.roy.novisign.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.roy.novisign.domain.model.MediaSlide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.isActive

@Composable
fun SlideShowScreen(
    viewModel: SlideShowViewModel = hiltViewModel()
) {

    viewModel.slides.collectAsStateWithLifecycle().value.let {  slideState ->
        when(slideState){
            is SlideState.Error -> {
                Box(
                    modifier = Modifier.
                    fillMaxSize()
                        .padding(all = 8.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = slideState.exception,
                        fontSize = 20.sp,
                        maxLines = 2
                    )
                }
            }
            SlideState.Idle -> {}
            SlideState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SlideState.Success -> {
                if (slideState.data.isEmpty()){
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(all = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No slides available",
                            fontSize = 20.sp
                        )
                    }
                }else {
                    SlideShowContent(slideState.data)
                }
            }
        }
    }
}

@Composable
private fun SlideShowContent(slides: List<MediaSlide>) {

    var currentMediaIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }
    var controlsVisible by remember { mutableStateOf(true) }

    val currentSlide = slides[currentMediaIndex]
    val durationMs = currentSlide.duration.inWholeMilliseconds

    // Reset progress when slide changes
    LaunchedEffect(currentMediaIndex) {
        progress = 0f
    }

    // Progress loop only runs when playing
    LaunchedEffect(isPlaying, currentMediaIndex) {
        if (!isPlaying) return@LaunchedEffect

        val intervalMs = 100L
        while (isActive && isPlaying && progress < 1f) {
            delay(intervalMs)
            progress += intervalMs.toFloat() / durationMs
        }
        if (progress >= 1f) {
            currentMediaIndex = (currentMediaIndex + 1) % slides.size
        }
    }

    // Auto-hide controls after 3s
    LaunchedEffect(controlsVisible) {
        if (controlsVisible) {
            delay(3000)
            controlsVisible = false
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { controlsVisible = !controlsVisible } }
    ) {
        Crossfade(targetState = currentSlide) { slide ->
            when (slide) {
                is MediaSlide.ImageSlide -> {
                    AsyncImage(
                        model = slide.url,
                        contentDescription = "media image",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
                is MediaSlide.VideoSlide -> {
                    val context = LocalContext.current
                    val exoPlayer = remember {
                        SimpleExoPlayer.Builder(context).build().apply {
                            setMediaItem(MediaItem.fromUri(slide.url))
                            prepare()
                        }
                    }

                    LaunchedEffect(isPlaying) {
                        exoPlayer.playWhenReady = isPlaying
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            factory = { ctx ->
                                PlayerView(ctx).apply {
                                    player = exoPlayer; useController = false
                                }
                            },
                        )
                        DisposableEffect(exoPlayer) {
                            onDispose { exoPlayer.release() }
                        }
                    }
                }
            }
        }

        //playback controls
        AnimatedVisibility(
            visible = controlsVisible,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Slider(
                        value = progress.coerceIn(0f, 1f),
                        onValueChange = { progress = it; controlsVisible = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            currentMediaIndex = (currentMediaIndex - 1 + slides.size) % slides.size
                            progress = 0f
                            controlsVisible = true
                        }) {
                            Icon(
                                Icons.Filled.SkipPrevious,
                                contentDescription = "Previous",
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        IconButton(onClick = {
                            isPlaying = !isPlaying
                            controlsVisible = true
                        }) {
                            Icon(
                                if (isPlaying) Icons.Filled.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .padding(8.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        IconButton(onClick = {
                            currentMediaIndex = (currentMediaIndex + 1) % slides.size
                            progress = 0f
                            controlsVisible = true
                        }) {
                            Icon(
                                Icons.Filled.SkipNext,
                                contentDescription = "Next",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}