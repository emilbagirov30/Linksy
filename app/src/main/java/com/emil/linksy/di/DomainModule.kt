package com.emil.linksy.di

import com.emil.domain.usecase.AcceptUserToChannelUseCase
import com.emil.domain.usecase.AllUserDataUseCase
import com.emil.domain.usecase.ChangePasswordUseCase
import com.emil.domain.usecase.CheckIsGroupUseCase
import com.emil.domain.usecase.ConfirmCodeUseCase
import com.emil.domain.usecase.ConfirmPasswordRecoveryUseCase
import com.emil.domain.usecase.CreateChannelUseCase
import com.emil.domain.usecase.CreateGroupUseCase
import com.emil.domain.usecase.CreateMomentUseCase
import com.emil.domain.usecase.DeleteAvatarUseCase
import com.emil.domain.usecase.DeleteChannelPostUseCase
import com.emil.domain.usecase.DeleteMomentUseCase
import com.emil.domain.usecase.DeletePostUseCase
import com.emil.domain.usecase.DeleteRequestUseCase
import com.emil.domain.usecase.GetUserMomentsUseCase
import com.emil.domain.usecase.GetUserPostsUseCase
import com.emil.domain.usecase.FindUsersByLinkUseCase
import com.emil.domain.usecase.FindUsersByUsernameUseCase
import com.emil.domain.usecase.GetChannelMembersUseCase
import com.emil.domain.usecase.GetChannelPageDataUseCase
import com.emil.domain.usecase.GetChannelPostsUseCase
import com.emil.domain.usecase.GetChannelSubscriptionsRequestUseCse
import com.emil.domain.usecase.GetChannelsUseCase
import com.emil.domain.usecase.GetChatIdUseCase
import com.emil.domain.usecase.GetGroupMembersUseCase
import com.emil.domain.usecase.GetOutsiderUserMomentsUseCase
import com.emil.domain.usecase.GetOutsiderUserPostsUseCase
import com.emil.domain.usecase.GetOutsiderUserSubscribersUseCase
import com.emil.domain.usecase.GetOutsiderUserSubscriptionsUseCase
import com.emil.domain.usecase.GetUserChatsFromLocalDb
import com.emil.domain.usecase.GetUserChatsUseCase
import com.emil.domain.usecase.GetUserMessagesFromLocalDb
import com.emil.domain.usecase.GetUserMessagesUseCase
import com.emil.domain.usecase.GetUserPageDataUseCase
import com.emil.domain.usecase.GetUserSubscribersUseCase
import com.emil.domain.usecase.GetUserSubscriptionsUseCase
import com.emil.domain.usecase.InsertChatInLocalDbUseCase
import com.emil.domain.usecase.InsertMessageInLocalDbUseCase
import com.emil.domain.usecase.LoginUseCase
import com.emil.domain.usecase.PublishPostUseCase
import com.emil.domain.usecase.RefreshTokenUseCase
import com.emil.domain.usecase.RegisterUseCase
import com.emil.domain.usecase.RejectSubscriptionRequestUseCase
import com.emil.domain.usecase.RequestPasswordRecoveryUseCase
import com.emil.domain.usecase.ResendCodeUseCase
import com.emil.domain.usecase.SendMessageUseCase
import com.emil.domain.usecase.SubmitRequestUseCase
import com.emil.domain.usecase.SubscribeUseCase
import com.emil.domain.usecase.UnsubscribeUseCase
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
    factory<FindUsersByUsernameUseCase>{
        FindUsersByUsernameUseCase(peopleRepository = get())
    }
    factory<FindUsersByLinkUseCase>{
        FindUsersByLinkUseCase(peopleRepository = get())
    }
    factory<GetOutsiderUserPostsUseCase>{
        GetOutsiderUserPostsUseCase(postRepository = get())
    }
    factory<GetOutsiderUserMomentsUseCase>{
        GetOutsiderUserMomentsUseCase(momentRepository = get())
    }
    factory<GetUserPageDataUseCase>{
        GetUserPageDataUseCase(peopleRepository = get())
    }

    factory<SubscribeUseCase>{
        SubscribeUseCase(peopleRepository = get())
    }
    factory<UnsubscribeUseCase>{
        UnsubscribeUseCase(peopleRepository = get())
    }
    factory<GetUserSubscribersUseCase>{
        GetUserSubscribersUseCase(peopleRepository = get())
    }
    factory<GetUserSubscriptionsUseCase>{
        GetUserSubscriptionsUseCase(peopleRepository = get())
    }
    factory<GetOutsiderUserSubscriptionsUseCase>{
        GetOutsiderUserSubscriptionsUseCase(peopleRepository = get())
    }
    factory<GetOutsiderUserSubscribersUseCase>{
        GetOutsiderUserSubscribersUseCase(peopleRepository = get())
    }

    factory<SendMessageUseCase>{
        SendMessageUseCase(messageRepository = get())
    }
    factory<GetUserMessagesUseCase>{
        GetUserMessagesUseCase(messageRepository = get())
    }
    factory<GetUserChatsUseCase>{
       GetUserChatsUseCase(chatRepository = get())
    }
    factory<GetUserMessagesFromLocalDb>{
        GetUserMessagesFromLocalDb(messageRepository = get())
    }
    factory<InsertMessageInLocalDbUseCase>{
        InsertMessageInLocalDbUseCase(messageRepository = get())
    }
    factory<GetUserChatsFromLocalDb>{
        GetUserChatsFromLocalDb(chatRepository = get())
    }
    factory<InsertChatInLocalDbUseCase>{
        InsertChatInLocalDbUseCase(chatRepository = get())
    }
    factory<CheckIsGroupUseCase>{
        CheckIsGroupUseCase(chatRepository = get())
    }
    factory<GetChatIdUseCase>{
        GetChatIdUseCase(chatRepository = get())
    }
    factory<CreateGroupUseCase>{
       CreateGroupUseCase(chatRepository = get())
    }
    factory<GetGroupMembersUseCase>{
        GetGroupMembersUseCase(chatRepository = get())
    }
    factory<CreateChannelUseCase>{
        CreateChannelUseCase(channelRepository = get())
    }
    factory<GetChannelsUseCase>{
       GetChannelsUseCase(channelRepository = get())
    }
    factory<GetChannelPageDataUseCase>{
       GetChannelPageDataUseCase(channelRepository = get())
    }

    factory<AcceptUserToChannelUseCase>{
        AcceptUserToChannelUseCase(channelRepository = get())
    }
    factory<CreateChannelUseCase> {
        CreateChannelUseCase(channelRepository = get())
    }
        factory<DeleteChannelPostUseCase>{
            DeleteChannelPostUseCase(channelRepository = get())
        }
    factory<DeleteRequestUseCase>{
        DeleteRequestUseCase(channelRepository = get())
    }

    factory<GetChannelMembersUseCase>{
        GetChannelMembersUseCase(channelRepository = get())}

    factory<GetChannelPostsUseCase>{
       GetChannelPostsUseCase(channelRepository = get())}


    factory<GetChannelSubscriptionsRequestUseCse>{
       GetChannelSubscriptionsRequestUseCse(channelRepository = get())
    }


    factory<RejectSubscriptionRequestUseCase>{
       RejectSubscriptionRequestUseCase(channelRepository = get())
    }

    factory<SubmitRequestUseCase>{
        SubmitRequestUseCase(channelRepository = get())}
}














