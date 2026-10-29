package com.example.sensus.common

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: AppError) : Result<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }
}

sealed class AppError(open val message: String) {
    data class Network(override val message: String = "Error de conexión. Verifica tu internet.") : AppError(message)
    data class Unauthorized(override val message: String = "Sesión inválida o expirada.") : AppError(message)
    data class Forbidden(override val message: String = "Acceso denegado.") : AppError(message)
    data class NotFound(override val message: String = "Elemento no encontrado.") : AppError(message)
    data class Unprocessable(override val message: String = "No fue posible procesar la solicitud.") : AppError(message)
    data class Validation(override val message: String = "Datos de entrada inválidos.") : AppError(message)
    data class Generic(override val message: String = "Ha ocurrido un error inesperado.") : AppError(message)
}
