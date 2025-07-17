package com.roy.novisign.utils

import com.roy.novisign.data.remote.NetResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import kotlinx.io.IOException

suspend inline fun <reified T> HttpClient.networkRequestBlock(
    apiCall: HttpRequestBuilder
): NetResult<T> {
    return try {
        val response = request {
            takeFrom(apiCall)
        }
        NetResult.Success(response.body())
    }catch (e: IOException){
        NetResult.NetworkFailure(e)

    } catch (e: Exception) {
        NetResult.Failure(e)
    }
}