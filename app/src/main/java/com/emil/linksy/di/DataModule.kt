package com.emil.linksy.di

import com.emil.data.repository.AuthRepositoryImpl
import com.emil.data.repository.MomentRepositoryImpl
import com.emil.data.repository.PostRepositoryImpl
import com.emil.data.repository.TokenRepositoryImpl
import com.emil.data.repository.UserRepositoryImpl
import com.emil.domain.repository.AuthRepository
import com.emil.domain.repository.MomentRepository
import com.emil.domain.repository.PostRepository
import com.emil.domain.repository.TokenRepository
import com.emil.domain.repository.UserRepository
import org.koin.dsl.module

val dataModule = module {

    single<AuthRepository>{
        AuthRepositoryImpl()
    }
    single<UserRepository>{
      UserRepositoryImpl()
    }
    single<TokenRepository>{
       TokenRepositoryImpl ()
    }
    single<PostRepository>{
        PostRepositoryImpl ()
    }
    single<MomentRepository>{
      MomentRepositoryImpl ()
    }

}