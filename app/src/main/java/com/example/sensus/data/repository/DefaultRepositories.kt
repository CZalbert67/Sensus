package com.example.sensus.data.repository

import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.data.remote.NetworkClient
import com.example.sensus.domain.repository.AuthRepository
import com.example.sensus.domain.repository.MerchantRepository
import com.example.sensus.domain.repository.ProfileRepository

/**
 * Repositorios de producción conectados directamente con el Backend de .NET 10.
 * Eliminados todos los datos simulados/en memoria.
 */
class DefaultAuthRepository(
    securePreferences: SecurePreferences,
    networkClient: NetworkClient = NetworkClient(securePreferences)
) : AuthRepository by RetrofitAuthRepository(networkClient.authApi, securePreferences)

class DefaultProfileRepository(
    securePreferences: SecurePreferences,
    networkClient: NetworkClient = NetworkClient(securePreferences)
) : ProfileRepository by RetrofitProfileRepository(networkClient.profileApi)

class DefaultMerchantRepository(
    networkClient: NetworkClient
) : MerchantRepository by RetrofitMerchantRepository(networkClient.merchantApi)
