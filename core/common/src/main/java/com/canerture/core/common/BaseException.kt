package com.canerture.core.common

open class BaseException(message: String) : Exception(message)

class BadRequestException(message: String) : BaseException(message)
class AuthorizationException(message: String) : BaseException(message)
class NotFoundException(message: String) : BaseException(message)
class UnknownException(message: String) : BaseException(message)
class NetworkException(message: String) : BaseException(message)