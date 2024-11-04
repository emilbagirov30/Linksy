package com.emil.linksy.di

import com.emil.domain.usecase.ConfirmCodeUseCase
import com.emil.domain.usecase.RegisterUseCase
import com.emil.domain.usecase.ResendCodeUseCase
import org.koin.dsl.module


val domainModule = module {

    factory<RegisterUseCase>{
        RegisterUseCase(authRepository = get())
    }
    factory<ConfirmCodeUseCase>{
       ConfirmCodeUseCase(authRepository = get())
    }
    factory<ResendCodeUseCase>{
       ResendCodeUseCase(authRepository = get())
    }
}
