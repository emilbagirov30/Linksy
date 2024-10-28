package com.emil.linksy.di

import com.emil.domain.usecase.RegisterUseCase
import org.koin.dsl.module


val domainModule = module {
    factory<RegisterUseCase>{
        RegisterUseCase(userRepository = get())
    }

}
