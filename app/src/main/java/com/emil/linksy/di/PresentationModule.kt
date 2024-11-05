package com.emil.linksy.di

import com.emil.linksy.presentation.viewmodel.ConfirmCodeViewModel
import com.emil.linksy.presentation.viewmodel.LoginViewModel
import com.emil.linksy.presentation.viewmodel.RecoveryPasswordViewModel
import com.emil.linksy.presentation.viewmodel.RegistrationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel <RegistrationViewModel> {
        RegistrationViewModel(registerUseCase = get())
    }
    viewModel <ConfirmCodeViewModel> {
        ConfirmCodeViewModel(confirmUseCase = get(), resendCodeUseCase = get())
    }
    viewModel <LoginViewModel> {
      LoginViewModel(loginUseCase = get())
    }
    viewModel <RecoveryPasswordViewModel> {
       RecoveryPasswordViewModel(requestPasswordChangeUseCase = get(), confirmPasswordChangeUseCase = get ())
    }
}