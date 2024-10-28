package com.emil.presentation.di

import com.emil.data.repository.UserRepositoryImpl
import com.emil.domain.repository.UserRepository
import org.koin.dsl.module

val dataModule = module {

    single<UserRepository>{
                UserRepositoryImpl()
            }

}