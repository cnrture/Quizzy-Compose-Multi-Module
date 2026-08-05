package com.canerture.network

import com.canerture.core.common.AuthorizationException
import com.canerture.core.common.BadRequestException
import com.canerture.core.common.NetworkException
import com.canerture.core.common.NotFoundException
import com.canerture.core.common.UnknownException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException

private const val DEFAULT_ERROR_MESSAGE = "An unknown error occurred, please try again later."

suspend fun <T : Any> safeApiCall(apiToBeCalled: suspend () -> T): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            Result.success(apiToBeCalled())
        } catch (e: HttpException) {
            val message = e.parseErrorMessage()
            when (e.code()) {
                400 -> Result.failure(BadRequestException(message))
                401 -> Result.failure(AuthorizationException(message))
                404 -> Result.failure(NotFoundException(message))
                else -> Result.failure(UnknownException(message))
            }
        } catch (_: IOException) {
            Result.failure(NetworkException())
        } catch (_: Exception) {
            ensureActive()
            Result.failure(UnknownException())
        }
    }
}

private fun HttpException.parseErrorMessage(): String = runCatching {
    Json.parseToJsonElement(response()?.errorBody()?.string().orEmpty())
        .jsonObject["message"]?.jsonPrimitive?.content
}.getOrNull().orEmpty().ifEmpty { DEFAULT_ERROR_MESSAGE }
