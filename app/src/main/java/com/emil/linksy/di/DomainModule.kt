package com.emil.linksy.di

import com.emil.domain.usecase.AcceptUserToChannelUseCase
import com.emil.domain.usecase.AddChannelPostCommentUseCase
import com.emil.domain.usecase.AddCommentUseCase
import com.emil.domain.usecase.AddLikeUseCase
import com.emil.domain.usecase.AddMembersUseCase
import com.emil.domain.usecase.AddScoreUseCase
import com.emil.domain.usecase.AddToBlackListUseCase
import com.emil.domain.usecase.AllUserDataUseCase
import com.emil.domain.usecase.ChangePasswordUseCase
import com.emil.domain.usecase.CheckIsGroupUseCase
import com.emil.domain.usecase.ClearAllMessagesUseCase
import com.emil.domain.usecase.ClearChatsUseCase
import com.emil.domain.usecase.ConfirmCodeUseCase
import com.emil.domain.usecase.ConfirmPasswordRecoveryUseCase
import com.emil.domain.usecase.ConnectToWebSocketUseCase
import com.emil.domain.usecase.CreateChannelPostUseCase
import com.emil.domain.usecase.CreateChannelUseCase
import com.emil.domain.usecase.CreateGroupUseCase
import com.emil.domain.usecase.CreateMomentUseCase
import com.emil.domain.usecase.DeleteAvatarUseCase
import com.emil.domain.usecase.DeleteChannelCommentUseCase
import com.emil.domain.usecase.DeleteChannelPostUseCase
import com.emil.domain.usecase.DeleteChatUseCase
import com.emil.domain.usecase.DeleteCommentUseCase
import com.emil.domain.usecase.DeleteLikeUseCase
import com.emil.domain.usecase.DeleteMessageFromLocalDbUseCase
import com.emil.domain.usecase.DeleteMessageUseCase
import com.emil.domain.usecase.DeleteMomentUseCase
import com.emil.domain.usecase.DeletePostUseCase
import com.emil.domain.usecase.DeleteRequestUseCase
import com.emil.domain.usecase.DeleteScoreUseCase
import com.emil.domain.usecase.DisconnectFromWebSocketUseCase
import com.emil.domain.usecase.EditGroupUseCase
import com.emil.domain.usecase.EditMessageUseCase
import com.emil.domain.usecase.FindChannelByLinkUseCase
import com.emil.domain.usecase.FindChannelByNameUseCase
import com.emil.domain.usecase.GetUserMomentsUseCase
import com.emil.domain.usecase.GetUserPostsUseCase
import com.emil.domain.usecase.FindUsersByLinkUseCase
import com.emil.domain.usecase.FindUsersByUsernameUseCase
import com.emil.domain.usecase.GetAllChannelsPostsUseCase
import com.emil.domain.usecase.GetAllSubscriptionsPostsUseCase
import com.emil.domain.usecase.GetAllUnseenMomentsUseCase
import com.emil.domain.usecase.GetChannelManagementDataUseCase
import com.emil.domain.usecase.GetChannelMembersUseCase
import com.emil.domain.usecase.GetChannelPageDataUseCase
import com.emil.domain.usecase.GetChannelPostCommentsUseCase
import com.emil.domain.usecase.GetChannelPostsUseCase
import com.emil.domain.usecase.GetChannelSubscriptionsRequestUseCse
import com.emil.domain.usecase.GetChannelsUseCase
import com.emil.domain.usecase.GetChatIdUseCase
import com.emil.domain.usecase.GetCommentsUseCase
import com.emil.domain.usecase.GetEveryoneOffTheBlacklistUseCase
import com.emil.domain.usecase.GetGroupDataUseCase
import com.emil.domain.usecase.GetGroupMembersUseCase
import com.emil.domain.usecase.GetGroupSendersUseCase
import com.emil.domain.usecase.GetMessageModeUseCase
import com.emil.domain.usecase.GetOutsiderUserMomentsUseCase
import com.emil.domain.usecase.GetOutsiderUserPostsUseCase
import com.emil.domain.usecase.GetOutsiderUserSubscribersUseCase
import com.emil.domain.usecase.GetOutsiderUserSubscriptionsUseCase
import com.emil.domain.usecase.GetPostAppreciatedUseCase
import com.emil.domain.usecase.GetPostLikesUseCase
import com.emil.domain.usecase.GetRecommendationsUseCase
import com.emil.domain.usecase.GetUserChatsFromLocalDb
import com.emil.domain.usecase.GetUserChatsUseCase
import com.emil.domain.usecase.GetUserMessagesByChat
import com.emil.domain.usecase.GetUserMessagesByChatFromLocalDb
import com.emil.domain.usecase.GetUserMessagesUseCase
import com.emil.domain.usecase.GetUserPageDataUseCase
import com.emil.domain.usecase.GetUserSubscribersUseCase
import com.emil.domain.usecase.GetUserSubscriptionsUseCase
import com.emil.domain.usecase.GetUserUnseenMomentsUseCase
import com.emil.domain.usecase.InsertChatInLocalDbUseCase
import com.emil.domain.usecase.InsertMessageInLocalDbUseCase
import com.emil.domain.usecase.LeaveTheGroupUseCase
import com.emil.domain.usecase.LoginUseCase
import com.emil.domain.usecase.PublishPostUseCase
import com.emil.domain.usecase.RefreshTokenUseCase
import com.emil.domain.usecase.RegisterUseCase
import com.emil.domain.usecase.RejectSubscriptionRequestUseCase
import com.emil.domain.usecase.RemoveFromBlackListUseCase
import com.emil.domain.usecase.RequestPasswordRecoveryUseCase
import com.emil.domain.usecase.ResendCodeUseCase
import com.emil.domain.usecase.SendMessageUseCase
import com.emil.domain.usecase.SendReportUseCase
import com.emil.domain.usecase.SendStatusUseCase
import com.emil.domain.usecase.SetMessageModeUseCase
import com.emil.domain.usecase.SubmitRequestUseCase
import com.emil.domain.usecase.SubscribeChannelUseCase
import com.emil.domain.usecase.SubscribeToChatStatusUseCase
import com.emil.domain.usecase.SubscribeToEditMessagesUseCase
import com.emil.domain.usecase.SubscribeToMessagesDeleteUseCase
import com.emil.domain.usecase.SubscribeToUserChatViewedUseCase
import com.emil.domain.usecase.SubscribeToUserChatsCountUseCase
import com.emil.domain.usecase.SubscribeToUserChatsUseCase
import com.emil.domain.usecase.SubscribeToUserMessagesUseCase
import com.emil.domain.usecase.SubscribeUseCase
import com.emil.domain.usecase.UnsubscribeChannelUseCase
import com.emil.domain.usecase.UnsubscribeUseCase
import com.emil.domain.usecase.UpdateBirthdayUseCase
import com.emil.domain.usecase.UpdateLinkUseCase
import com.emil.domain.usecase.UpdateUsernameUseCase
import com.emil.domain.usecase.UploadAvatarUseCase
import com.emil.domain.usecase.UserProfileDataUseCase
import com.emil.domain.usecase.ViewMomentUseCase
import com.emil.domain.usecase.ViewedUseCase
import com.emil.domain.usecase.VoteUseCase
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
    factory<GetUserMessagesByChatFromLocalDb>{
        GetUserMessagesByChatFromLocalDb(messageRepository = get())
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

    factory<CreateChannelPostUseCase>{
        CreateChannelPostUseCase(channelRepository = get())
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
        SubmitRequestUseCase(channelRepository = get())
    }


    factory<SubscribeChannelUseCase>{
        SubscribeChannelUseCase(channelRepository = get())
    }
    factory<UnsubscribeChannelUseCase>{
        UnsubscribeChannelUseCase(channelRepository = get())}
    factory<VoteUseCase>{
      VoteUseCase(channelRepository = get())}

        factory<FindChannelByLinkUseCase>{
       FindChannelByLinkUseCase(channelRepository = get())
        }
    factory<FindChannelByNameUseCase>{
       FindChannelByNameUseCase(channelRepository = get())
    }



    factory<AddLikeUseCase>{
        AddLikeUseCase(postRepository = get())
    }

    factory<DeleteLikeUseCase>{
        DeleteLikeUseCase(postRepository = get())
    }


    factory<GetCommentsUseCase>{
        GetCommentsUseCase(postRepository = get())
    }

    factory<AddCommentUseCase>{
        AddCommentUseCase(postRepository = get())
    }



    factory<AddToBlackListUseCase>{
        AddToBlackListUseCase(peopleRepository = get())
    }


    factory<RemoveFromBlackListUseCase>{
        RemoveFromBlackListUseCase(peopleRepository = get())
    }

    factory<GetEveryoneOffTheBlacklistUseCase>{
        GetEveryoneOffTheBlacklistUseCase(userRepository = get())
    }
    factory<GetMessageModeUseCase>{
        GetMessageModeUseCase(userRepository = get())
    }

    factory<SetMessageModeUseCase>{
        SetMessageModeUseCase(userRepository = get())
    }


    factory<ConnectToWebSocketUseCase>{
        ConnectToWebSocketUseCase(repository = get())
    }
    factory<DisconnectFromWebSocketUseCase>{
        DisconnectFromWebSocketUseCase(repository = get())
    }
    factory<SubscribeToUserMessagesUseCase>{
        SubscribeToUserMessagesUseCase(repository = get())
    }
    factory<GetUserMessagesByChat>{
        GetUserMessagesByChat(messageRepository = get())
    }
    factory<SubscribeToUserChatsUseCase>{
        SubscribeToUserChatsUseCase(repository = get())
    }
    factory<ViewedUseCase>{
        ViewedUseCase(messageRepository = get())
    }
    factory<SubscribeToUserChatViewedUseCase>{
        SubscribeToUserChatViewedUseCase(repository = get())
    }
    factory<SubscribeToUserChatsCountUseCase>{
        SubscribeToUserChatsCountUseCase(repository = get())
    }
    factory<EditGroupUseCase>{
        EditGroupUseCase(chatRepository = get())
    }
    factory<GetGroupDataUseCase>{
        GetGroupDataUseCase(chatRepository = get())
    }
    factory<GetChannelManagementDataUseCase>{
        GetChannelManagementDataUseCase(channelRepository = get())
    }
    factory<AddScoreUseCase>{
        AddScoreUseCase(channelRepository = get())
    }
    factory<DeleteScoreUseCase>{
        DeleteScoreUseCase(channelRepository = get())
    }
    factory<AddChannelPostCommentUseCase>{
        AddChannelPostCommentUseCase(channelRepository = get())
    }
    factory<GetChannelPostCommentsUseCase>{
        GetChannelPostCommentsUseCase(channelRepository = get())
    }

    factory<SubscribeToMessagesDeleteUseCase>{
       SubscribeToMessagesDeleteUseCase(repository = get())
    }

    factory<DeleteMessageUseCase>{
       DeleteMessageUseCase(messageRepository = get())
    }
    factory<SubscribeToEditMessagesUseCase>{
       SubscribeToEditMessagesUseCase(repository = get())
    }
    factory<EditMessageUseCase>{
        EditMessageUseCase(messageRepository = get())
    }

    factory<GetAllSubscriptionsPostsUseCase>{
        GetAllSubscriptionsPostsUseCase(feedRepository = get())
    }
    factory<GetAllChannelsPostsUseCase>{
        GetAllChannelsPostsUseCase(feedRepository = get())
    }

    factory<DeleteCommentUseCase>{
        DeleteCommentUseCase(postRepository = get())
    }
    factory<DeleteChannelCommentUseCase>{
        DeleteChannelCommentUseCase(channelRepository = get())
    }

    factory<SendStatusUseCase>{
        SendStatusUseCase(repository = get())
    }

    factory<SubscribeToChatStatusUseCase>{
        SubscribeToChatStatusUseCase(repository = get())
    }


    factory<DeleteMessageFromLocalDbUseCase>{
       DeleteMessageFromLocalDbUseCase(messageRepository = get())
    }


    factory<ClearAllMessagesUseCase>{
       ClearAllMessagesUseCase(messageRepository = get())
    }

    factory<DeleteChatUseCase>{
       DeleteChatUseCase(chatRepository = get())
    }

    factory<ClearChatsUseCase>{
       ClearChatsUseCase(chatRepository = get())
    }


    factory<AddMembersUseCase>{
        AddMembersUseCase(chatRepository = get())
    }
    factory<LeaveTheGroupUseCase>{
       LeaveTheGroupUseCase(chatRepository = get())
    }


    factory<GetPostAppreciatedUseCase>{
       GetPostAppreciatedUseCase(channelRepository = get())
    }
    factory<GetPostLikesUseCase>{
        GetPostLikesUseCase(postRepository = get())
    }

    factory<GetRecommendationsUseCase>{
       GetRecommendationsUseCase(feedRepository = get())
    }

    factory<ViewMomentUseCase>{
       ViewMomentUseCase(momentRepository = get())
    }
    factory<GetUserUnseenMomentsUseCase>{
        GetUserUnseenMomentsUseCase(momentRepository = get())
    }
    factory<GetAllUnseenMomentsUseCase>{
       GetAllUnseenMomentsUseCase(feedRepository = get())
    }
    factory<SendReportUseCase>{
        SendReportUseCase(peopleRepository = get())
    }
    factory<GetGroupSendersUseCase>{
        GetGroupSendersUseCase(chatRepository = get())
    }

}














