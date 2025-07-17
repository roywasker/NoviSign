package com.roy.novisign.di

import com.roy.novisign.data.remote.NoviSignApi
import com.roy.novisign.data.remote.NoviSignApiImpl
import com.roy.novisign.data.repository.FetchPlaylistUseCaseImpl
import com.roy.novisign.domain.usecase.FetchPlaylistUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides application-level dependencies.
 * This module is installed in the [SingletonComponent], meaning that
 * the provided dependencies will have a singleton scope throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) { json() }
    }

    @Provides
    @Singleton
    fun bindNoviSignApi(apiImpl: NoviSignApiImpl): NoviSignApi = apiImpl

    @Provides
    @Singleton
    fun bindFetchPlaylistUseCase(impl: FetchPlaylistUseCaseImpl): FetchPlaylistUseCase = impl
}