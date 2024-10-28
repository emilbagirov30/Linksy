package com.emil.presentation.di

import com.emil.domain.usecase.RegisterUseCase
import org.koin.dsl.module


val domainModule = module {
    factory<RegisterUseCase>{
RegisterUseCase(userRepository = get())
    }

}
