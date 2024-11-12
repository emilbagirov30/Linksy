package com.emil.linksy.di

import com.emil.data.repository.AuthRepositoryImpl
import com.emil.data.repository.UserRepositoryImpl
import com.emil.domain.repository.AuthRepository
import com.emil.domain.repository.UserRepository
import org.koin.dsl.module

val dataModule = module {

    single<AuthRepository>{
        AuthRepositoryImpl()
    }
    single<UserRepository>{
      UserRepositoryImpl()
    }

}