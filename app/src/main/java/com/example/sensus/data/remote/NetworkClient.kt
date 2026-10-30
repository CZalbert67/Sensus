package com.example.sensus.data.remote

import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.data.remote.api.AuthApiService
import com.example.sensus.data.remote.api.MerchantApiService
import com.example.sensus.data.remote.api.ProfileApiService
import com.example.sensus.data.remote.interceptor.AuthInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

class NetworkClient(
    private val securePreferences: SecurePreferences,
    baseUrl: String = DEFAULT_BASE_URL
) {

    companion object {
        // En dispositivo físico con `adb reverse tcp:5235 tcp:5235` apunta directamente al backend de la PC
        const val DEFAULT_BASE_URL = "http://localhost:5235/"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        encodeDefaults = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor(securePreferences))
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val authApi: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
    val profileApi: ProfileApiService by lazy { retrofit.create(ProfileApiService::class.java) }
    val merchantApi: MerchantApiService by lazy { retrofit.create(MerchantApiService::class.java) }
}
