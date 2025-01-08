package com.canerture.network

import com.canerture.core.common.AuthorizationException
import com.canerture.core.common.BadRequestException
import com.canerture.core.common.NetworkException
import com.canerture.core.common.NotFoundException
import com.canerture.core.common.Resource
import com.canerture.core.common.UnknownException
import com.canerture.network.model.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> BaseResponse<T>): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response: BaseResponse<T> = apiToBeCalled()
            if (response.data == null) {
                Resource.Error(UnknownException("An unknown error occurred, please try again later."))
            } else {
                Resource.Success(response.data)
            }
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
            Resource.Error(NetworkException("Please check your internet connection."))
        } catch (e: Exception) {
            Resource.Error(UnknownException("An unknown error occurred, please try again later."))
        }
    }
}

fun <T> Resource<T>.toUnit(): Resource<Unit> {
    return when (this) {
        is Resource.Success -> Resource.Success(Unit)
        is Resource.Error -> Resource.Error(exception)
    }
}