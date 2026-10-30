package com.example.sensus.data.di

import android.content.Context
import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.data.remote.NetworkClient
import com.example.sensus.data.repository.RetrofitAuthRepository
import com.example.sensus.data.repository.RetrofitMerchantRepository
import com.example.sensus.data.repository.RetrofitProfileRepository
import com.example.sensus.domain.repository.AuthRepository
import com.example.sensus.domain.repository.MerchantRepository
import com.example.sensus.domain.repository.ProfileRepository
import com.example.sensus.domain.usecase.DeleteAccountUseCase
import com.example.sensus.domain.usecase.RequestPasswordResetUseCase
import com.example.sensus.domain.usecase.ResetPasswordUseCase
import com.example.sensus.domain.usecase.StartGuestSessionUseCase
import com.example.sensus.domain.usecase.UpdateMerchantProfileUseCase
import com.example.sensus.domain.usecase.UpdateProfileUseCase

class SensusContainer(
    context: Context,
    baseUrl: String = NetworkClient.DEFAULT_BASE_URL
) {
    val securePreferences: SecurePreferences by lazy {
        SecurePreferences(context.applicationContext)
    }

    val networkClient: NetworkClient by lazy {
        NetworkClient(securePreferences, baseUrl)
    }

    val authRepository: AuthRepository by lazy {
        RetrofitAuthRepository(networkClient.authApi, securePreferences)
    }

    val profileRepository: ProfileRepository by lazy {
        RetrofitProfileRepository(networkClient.profileApi)
    }

    val merchantRepository: MerchantRepository by lazy {
        RetrofitMerchantRepository(networkClient.merchantApi)
    }

    // Sprint 1 Use Cases
    val requestPasswordResetUseCase by lazy { RequestPasswordResetUseCase(authRepository) }
    val resetPasswordUseCase by lazy { ResetPasswordUseCase(authRepository) }
    val startGuestSessionUseCase by lazy { StartGuestSessionUseCase(authRepository, securePreferences) }
    val deleteAccountUseCase by lazy { DeleteAccountUseCase(authRepository, securePreferences) }
    val updateProfileUseCase by lazy { UpdateProfileUseCase(profileRepository) }
    val updateMerchantProfileUseCase by lazy { UpdateMerchantProfileUseCase(merchantRepository) }

    companion object {
        @Volatile
        private var INSTANCE: SensusContainer? = null

        fun getInstance(context: Context): SensusContainer {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SensusContainer(context).also { INSTANCE = it }
            }
        }
    }
}
