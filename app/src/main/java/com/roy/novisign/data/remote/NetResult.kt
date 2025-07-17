package com.roy.novisign.data.remote

/**
 * A sealed class representing the result of a network operation.
 * It can be one of three states:
 * - [Success]: Indicates a successful operation with a response of type [T].
 * - [NetworkFailure]: Indicates a failure due to a network error, represented by an [Exception].
 * - [Failure]: Indicates a general failure, represented by an [Exception].
 *
 * @param T The type of the successful response.
 */
sealed class NetResult<T> {
    data class Success<T>(val response:T): NetResult<T>()
    data class NetworkFailure<T>(val networkError:Exception): NetResult<T>()
    data class Failure<T>(val generalError:Exception): NetResult<T>()
}