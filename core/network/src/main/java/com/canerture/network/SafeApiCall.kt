package com.canerture.network

import com.canerture.core.common.AuthorizationException
import com.canerture.core.common.BadRequestException
import com.canerture.core.common.NetworkException
import com.canerture.core.common.NotFoundException
import com.canerture.core.common.Resource
import com.canerture.core.common.UnknownException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException

suspend fun <T : Any> safeApiCall(apiToBeCalled: suspend () -> T): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            Resource.Success(apiToBeCalled())
        } catch (e: HttpException) {
            val message = Json.parseToJsonElement(
                e.response()?.errorBody()?.string().orEmpty()
            ).jsonObject["message"]?.jsonPrimitive?.content.orEmpty().ifEmpty {
                "An unknown error occurred, please try again later."
            }
            when (e.code()) {
                400 -> Resource.Error(BadRequestException(message))
                401 -> Resource.Error(AuthorizationException(message))
                404 -> Resource.Error(NotFoundException(message))
                else -> Resource.Error(UnknownException(message))
            }
        } catch (e: IOException) {
            Resource.Error(NetworkException())
        } catch (e: Exception) {
            Resource.Error(UnknownException())
        }
    }
}