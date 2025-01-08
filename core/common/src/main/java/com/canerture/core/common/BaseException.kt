package com.canerture.core.common

open class BaseException(message: String) : Exception(message)

class BadRequestException(message: String = "") :
    BaseException(message.ifEmpty { "The request could not be understood by the server due to malformed syntax." })

class AuthorizationException(message: String = "") :
    BaseException(message.ifEmpty { "You are not authorized to access this resource." })

class NotFoundException(message: String = "") :
    BaseException(message.ifEmpty { "The requested resource could not be found." })

class UnknownException(message: String = "") :
    BaseException(message.ifEmpty { "An unknown error occurred, please try again later." })

class NetworkException(message: String = "") : BaseException(
    message.ifEmpty { "Please check your internet connection." }
)