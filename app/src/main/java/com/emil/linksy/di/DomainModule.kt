package com.emil.linksy.di

import com.emil.domain.usecase.AllUserDataUseCase
import com.emil.domain.usecase.ChangePasswordUseCase
import com.emil.domain.usecase.ConfirmCodeUseCase
import com.emil.domain.usecase.ConfirmPasswordRecoveryUseCase
import com.emil.domain.usecase.CreateMomentUseCase
import com.emil.domain.usecase.DeleteAvatarUseCase
import com.emil.domain.usecase.DeleteMomentUseCase
import com.emil.domain.usecase.DeletePostUseCase
import com.emil.domain.usecase.GetUserMomentsUseCase
import com.emil.domain.usecase.GetUserPostsUseCase
import com.emil.domain.usecase.GetUsersByLinkUseCase
import com.emil.domain.usecase.GetUsersByUsernameUseCase
import com.emil.domain.usecase.LoginUseCase
import com.emil.domain.usecase.PublishPostUseCase
import com.emil.domain.usecase.RefreshTokenUseCase
import com.emil.domain.usecase.RegisterUseCase
import com.emil.domain.usecase.RequestPasswordRecoveryUseCase
import com.emil.domain.usecase.ResendCodeUseCase
import com.emil.domain.usecase.UpdateBirthdayUseCase
import com.emil.domain.usecase.UpdateLinkUseCase
import com.emil.domain.usecase.UpdateUsernameUseCase
import com.emil.domain.usecase.UploadAvatarUseCase
import com.emil.domain.usecase.UserProfileDataUseCase
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
    factory<RequestPasswordRecoveryUseCase>{
        RequestPasswordRecoveryUseCase(authRepository = get())
    }
    factory<ConfirmPasswordRecoveryUseCase>{
        ConfirmPasswordRecoveryUseCase(authRepository = get())
    }
    factory<UserProfileDataUseCase>{
       UserProfileDataUseCase(userRepository = get())
    }
    factory<AllUserDataUseCase>{
       AllUserDataUseCase(userRepository = get())
    }
    factory<RefreshTokenUseCase>{
       RefreshTokenUseCase(tokenRepository = get())
    }
    factory<UploadAvatarUseCase>{
        UploadAvatarUseCase(userRepository = get())
    }
    factory<UpdateBirthdayUseCase>{
        UpdateBirthdayUseCase(userRepository = get())
    }
    factory<UpdateUsernameUseCase>{
        UpdateUsernameUseCase(userRepository = get())
    }
    factory<UpdateLinkUseCase>{
        UpdateLinkUseCase(userRepository = get())
    }
    factory<DeleteAvatarUseCase>{
        DeleteAvatarUseCase(userRepository = get())
    }
    factory<ChangePasswordUseCase>{
        ChangePasswordUseCase(userRepository = get())
    }
    factory<PublishPostUseCase>{
       PublishPostUseCase(postRepository = get())
    }
    factory<GetUserPostsUseCase>{
       GetUserPostsUseCase(postRepository = get())
    }
    factory<DeletePostUseCase>{
        DeletePostUseCase(postRepository = get())
    }
    factory<CreateMomentUseCase>{
       CreateMomentUseCase(momentRepository = get())
    }
    factory<GetUserMomentsUseCase>{
       GetUserMomentsUseCase(momentRepository = get())
    }
    factory<DeleteMomentUseCase>{
        DeleteMomentUseCase(momentRepository = get())
    }
    factory<GetUsersByUsernameUseCase>{
        GetUsersByUsernameUseCase(peopleRepository = get())
    }
    factory<GetUsersByLinkUseCase>{
        GetUsersByLinkUseCase(peopleRepository = get())
    }

}
