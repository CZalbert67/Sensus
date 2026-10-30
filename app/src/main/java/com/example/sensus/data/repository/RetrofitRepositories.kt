package com.example.sensus.data.repository

import com.example.sensus.common.AppError
import com.example.sensus.common.Result
import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.data.local.SessionType
import com.example.sensus.data.remote.api.AuthApiService
import com.example.sensus.data.remote.api.MerchantApiService
import com.example.sensus.data.remote.api.ProfileApiService
import com.example.sensus.data.remote.dto.DeleteAccountRequest
import com.example.sensus.data.remote.dto.HorarioDiaDto
import com.example.sensus.data.remote.dto.LoginRequest
import com.example.sensus.data.remote.dto.RegisterRequest
import com.example.sensus.data.remote.dto.RequestPasswordResetRequest
import com.example.sensus.data.remote.dto.ResetPasswordRequest
import com.example.sensus.data.remote.dto.UpdateMerchantProfileRequest
import com.example.sensus.data.remote.dto.UpdateProfileRequest
import com.example.sensus.domain.model.HorarioDia
import com.example.sensus.domain.model.MerchantProfile
import com.example.sensus.domain.model.UserProfile
import com.example.sensus.domain.repository.AuthRepository
import com.example.sensus.domain.repository.MerchantRepository
import com.example.sensus.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class RetrofitAuthRepository(
    private val authApi: AuthApiService,
    private val securePreferences: SecurePreferences
) : AuthRepository {

    override suspend fun login(correo: String, password: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.login(LoginRequest(correo = correo, password = password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                securePreferences.saveAuthToken(body.token)
                securePreferences.saveSessionType(SessionType.REGISTERED)
                val user = body.user
                Result.Success(
                    UserProfile(
                        id = user.id,
                        nombreCompleto = user.nombreCompleto,
                        correo = user.correo,
                        fotoPerfilUrl = user.fotoPerfilUrl,
                        roles = user.roles.ifEmpty { listOf("Consumidor") }
                    )
                )
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error inesperado al iniciar sesión."))
        }
    }

    override suspend fun register(nombreCompleto: String, correo: String, password: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.register(RegisterRequest(nombreCompleto = nombreCompleto, correo = correo, password = password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                securePreferences.saveAuthToken(body.token)
                securePreferences.saveSessionType(SessionType.REGISTERED)
                val user = body.user
                Result.Success(
                    UserProfile(
                        id = user.id,
                        nombreCompleto = user.nombreCompleto,
                        correo = user.correo,
                        fotoPerfilUrl = user.fotoPerfilUrl,
                        roles = user.roles.ifEmpty { listOf("Consumidor") }
                    )
                )
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error inesperado al registrar usuario."))
        }
    }

    override suspend fun requestPasswordReset(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.requestPasswordReset(RequestPasswordResetRequest(correo = email))
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error inesperado."))
        }
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.resetPassword(ResetPasswordRequest(token = token, nuevaPassword = newPassword))
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error inesperado."))
        }
    }

    override suspend fun createGuestSession(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.createGuestSession()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                securePreferences.saveGuestSession(body.token, body.maxRadiusMeters)
                Result.Success(body.token)
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error inesperado."))
        }
    }

    override suspend fun deleteAccount(password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.deleteAccount(DeleteAccountRequest(password = password))
            if (response.isSuccessful) {
                securePreferences.clearAll()
                Result.Success(Unit)
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error inesperado."))
        }
    }

    private fun <T> mapHttpError(response: Response<T>): Result<Nothing> {
        val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
        return when (response.code()) {
            400 -> Result.Failure(AppError.Validation(errorMsg ?: "Datos de entrada inválidos."))
            401 -> Result.Failure(AppError.Unauthorized(errorMsg ?: "No autorizado."))
            403 -> Result.Failure(AppError.Forbidden(errorMsg ?: "Acceso restringido."))
            404 -> Result.Failure(AppError.NotFound(errorMsg ?: "No encontrado."))
            429 -> Result.Failure(AppError.Unprocessable("Límite de peticiones alcanzado. Intenta de nuevo en unos minutos."))
            else -> Result.Failure(AppError.Generic("Error del servidor HTTP ${response.code()}."))
        }
    }
}

class RetrofitProfileRepository(
    private val profileApi: ProfileApiService
) : ProfileRepository {

    override suspend fun getProfile(): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = profileApi.getProfile()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.Success(
                    UserProfile(
                        id = dto.id,
                        nombreCompleto = dto.nombreCompleto,
                        correo = dto.correo,
                        fotoPerfilUrl = dto.fotoPerfilUrl,
                        roles = dto.roles.ifEmpty { listOf("Consumidor") }
                    )
                )
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error al obtener perfil."))
        }
    }

    override suspend fun updateProfile(nombreCompleto: String, fotoPerfilUrl: String?): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = profileApi.updateProfile(
                UpdateProfileRequest(
                    nombreCompleto = nombreCompleto,
                    fotoPerfilUrl = fotoPerfilUrl
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.Success(
                    UserProfile(
                        id = dto.id,
                        nombreCompleto = dto.nombreCompleto,
                        correo = dto.correo,
                        fotoPerfilUrl = dto.fotoPerfilUrl,
                        roles = dto.roles.ifEmpty { listOf("Consumidor") }
                    )
                )
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error al actualizar perfil."))
        }
    }

    override suspend fun requestPhotoSignature(): Result<Pair<String, Long>> = withContext(Dispatchers.IO) {
        try {
            val response = profileApi.generatePhotoSignature()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.Success(Pair(dto.signature, dto.timestamp))
            } else {
                mapHttpError(response)
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error al generar firma de foto."))
        }
    }

    private fun <T> mapHttpError(response: Response<T>): Result<Nothing> {
        val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
        return when (response.code()) {
            400 -> Result.Failure(AppError.Validation(errorMsg ?: "Datos inválidos."))
            401 -> Result.Failure(AppError.Unauthorized(errorMsg ?: "Sesión requerida."))
            403 -> Result.Failure(AppError.Forbidden(errorMsg ?: "Acceso no autorizado."))
            404 -> Result.Failure(AppError.NotFound(errorMsg ?: "Perfil no encontrado."))
            else -> Result.Failure(AppError.Generic("Error HTTP ${response.code()}"))
        }
    }
}

class RetrofitMerchantRepository(
    private val merchantApi: MerchantApiService
) : MerchantRepository {

    override suspend fun updateMerchantProfile(
        nombreComercial: String?,
        direccion: String,
        codigoPostal: String,
        horarios: List<HorarioDia>
    ): Result<MerchantProfile> = withContext(Dispatchers.IO) {
        try {
            val horariosDto = horarios.map { h ->
                HorarioDiaDto(
                    dia = mapDiaToDayOfWeekInt(h.dia),
                    horaApertura = h.horaApertura,
                    horaCierre = h.horaCierre,
                    estaCerrado = h.estaCerrado
                )
            }

            val request = UpdateMerchantProfileRequest(
                nombreComercial = nombreComercial,
                direccion = direccion,
                codigoPostal = codigoPostal,
                horarios = horariosDto
            )

            val response = merchantApi.updateMerchantProfile(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val mappedHorarios = dto.horarios?.map { hd ->
                    HorarioDia(
                        dia = mapDayOfWeekIntToDia(hd.dia),
                        horaApertura = hd.horaApertura,
                        horaCierre = hd.horaCierre,
                        estaCerrado = hd.estaCerrado
                    )
                } ?: horarios

                Result.Success(
                    MerchantProfile(
                        id = dto.id,
                        usuarioId = dto.usuarioId,
                        nombreComercial = dto.nombreComercial,
                        direccion = dto.direccion,
                        codigoPostal = dto.codigoPostal,
                        latitud = dto.latitud,
                        longitud = dto.longitud,
                        horarios = mappedHorarios
                    )
                )
            } else {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                when (response.code()) {
                    400 -> Result.Failure(AppError.Validation(errorMsg ?: "Datos comerciales inválidos."))
                    401 -> Result.Failure(AppError.Unauthorized(errorMsg ?: "Sesión requerida."))
                    403 -> Result.Failure(AppError.Forbidden(errorMsg ?: "Solo comerciantes pueden actualizar datos de local."))
                    404 -> Result.Failure(AppError.NotFound(errorMsg ?: "Comercio no encontrado."))
                    422 -> Result.Failure(AppError.Unprocessable(errorMsg ?: "No fue posible geocodificar la dirección."))
                    else -> Result.Failure(AppError.Generic("Error HTTP ${response.code()}."))
                }
            }
        } catch (e: IOException) {
            Result.Failure(AppError.Network("No se pudo conectar con el servidor: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.Failure(AppError.Generic(e.localizedMessage ?: "Error al actualizar comercio."))
        }
    }

    private fun mapDiaToDayOfWeekInt(dia: String): Int {
        return when (dia.lowercase().trim()) {
            "domingo" -> 0
            "lunes" -> 1
            "martes" -> 2
            "miércoles", "miercoles" -> 3
            "jueves" -> 4
            "viernes" -> 5
            "sábado", "sabado" -> 6
            else -> 1
        }
    }

    private fun mapDayOfWeekIntToDia(diaInt: Int): String {
        return when (diaInt) {
            0 -> "Domingo"
            1 -> "Lunes"
            2 -> "Martes"
            3 -> "Miércoles"
            4 -> "Jueves"
            5 -> "Viernes"
            6 -> "Sábado"
            else -> "Lunes"
        }
    }
}
