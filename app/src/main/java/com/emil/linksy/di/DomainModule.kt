package com.emil.linksy.di

import com.emil.domain.usecase.AllUserDataUseCase
import com.emil.domain.usecase.ConfirmCodeUseCase
import com.emil.domain.usecase.ConfirmPasswordChangeUseCase
import com.emil.domain.usecase.LoginUseCase
import com.emil.domain.usecase.RefreshTokenUseCase
import com.emil.domain.usecase.RegisterUseCase
import com.emil.domain.usecase.RequestPasswordChangeUseCase
import com.emil.domain.usecase.ResendCodeUseCase
import com.emil.domain.usecase.UserDataUseCase
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
    factory<LoginUseCase>{
       LoginUseCase(authRepository = get())
    }
    factory<RequestPasswordChangeUseCase>{
        RequestPasswordChangeUseCase(authRepository = get())
    }
    factory<ConfirmPasswordChangeUseCase>{
        ConfirmPasswordChangeUseCase(authRepository = get())
    }
    factory<UserDataUseCase>{
       UserDataUseCase(userRepository = get())
    }
    factory<AllUserDataUseCase>{
       AllUserDataUseCase(userRepository = get())
    }
    factory<RefreshTokenUseCase>{
       RefreshTokenUseCase(tokenRepository = get())
    }
}
