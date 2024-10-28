package com.emil.presentation.di

import com.emil.presentation.viewmodel.RegistrationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel <RegistrationViewModel> {
        RegistrationViewModel(registerUseCase = get())
    }
}