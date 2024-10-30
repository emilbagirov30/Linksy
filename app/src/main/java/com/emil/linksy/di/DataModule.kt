package com.emil.linksy.di

import com.emil.data.repository.AuthRepositoryImpl
import com.emil.domain.repository.AuthRepository
import org.koin.dsl.module

val dataModule = module {

    single<AuthRepository>{
        AuthRepositoryImpl()
    }

}