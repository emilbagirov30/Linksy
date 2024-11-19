package com.emil.linksy.di

import com.emil.domain.usecase.RefreshTokenUseCase
import com.emil.linksy.app.service.TokenService
import com.emil.linksy.presentation.viewmodel.ProfileManagementViewModel
import com.emil.linksy.presentation.viewmodel.ConfirmCodeViewModel
import com.emil.linksy.presentation.viewmodel.LoginViewModel
import com.emil.linksy.presentation.viewmodel.RecoveryPasswordViewModel
import com.emil.linksy.presentation.viewmodel.RegistrationViewModel
import com.emil.linksy.presentation.viewmodel.UserProfileDataViewModel
import com.emil.linksy.util.TokenManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TokenManager(androidContext()) }
    single { RefreshTokenUseCase(get()) }

    viewModel <RegistrationViewModel> {
        RegistrationViewModel(registerUseCase = get())
    }
    viewModel <ConfirmCodeViewModel> {
        ConfirmCodeViewModel(confirmUseCase = get(), resendCodeUseCase = get())
    }
    viewModel <LoginViewModel> {
      LoginViewModel(loginUseCase = get(), context = androidContext())
    }
    viewModel <RecoveryPasswordViewModel> {
       RecoveryPasswordViewModel(requestPasswordChangeUseCase = get(), confirmPasswordChangeUseCase = get ())
    }
    viewModel <UserProfileDataViewModel> {
      UserProfileDataViewModel(userProfileDataUseCase = get ())
    }
    viewModel <ProfileManagementViewModel> {
       ProfileManagementViewModel(allUserDataUseCase = get (), uploadAvatarUseCase = get(), updateBirthdayUseCase = get())
    }
    single { TokenService() }
}