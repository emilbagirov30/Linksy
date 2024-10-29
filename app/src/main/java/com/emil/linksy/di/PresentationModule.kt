package com.emil.linksy.di

import com.emil.linksy.presentation.viewmodel.ConfirmViewModel
import com.emil.linksy.presentation.viewmodel.RegistrationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel <RegistrationViewModel> {
        RegistrationViewModel(registerUseCase = get())
    }
    viewModel <ConfirmViewModel> {
        ConfirmViewModel(confirmUseCase = get())
    }
}