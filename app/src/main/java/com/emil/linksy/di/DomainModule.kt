package com.emil.linksy.di

import com.emil.domain.usecase.ConfirmCodeUseCase
import com.emil.domain.usecase.RegisterUseCase
import org.koin.dsl.module


val domainModule = module {

    factory<RegisterUseCase>{
        RegisterUseCase(authRepository = get())
    }
    factory<ConfirmCodeUseCase>{
       ConfirmCodeUseCase(authRepository = get())
    }
}
