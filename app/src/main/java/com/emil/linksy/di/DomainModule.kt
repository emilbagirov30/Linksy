package com.emil.linksy.di

import com.emil.domain.usecase.channel.AcceptUserToChannelUseCase
import com.emil.domain.usecase.channel.AddChannelPostCommentUseCase
import com.emil.domain.usecase.people.AddCommentUseCase
import com.emil.domain.usecase.people.AddLikeUseCase
import com.emil.domain.usecase.chat.AddMembersUseCase
import com.emil.domain.usecase.channel.AddScoreUseCase
import com.emil.domain.usecase.user.AddToBlackListUseCase
import com.emil.domain.usecase.settings.GetAllUserDataUseCase
import com.emil.domain.usecase.settings.ChangePasswordUseCase
import com.emil.domain.usecase.chat.CheckIsGroupUseCase
import com.emil.domain.usecase.room.ClearAllMessagesUseCase
import com.emil.domain.usecase.room.ClearChatsUseCase
import com.emil.domain.usecase.auth.ConfirmCodeUseCase
import com.emil.domain.usecase.auth.ConfirmPasswordRecoveryUseCase
import com.emil.domain.usecase.websocket.ConnectToWebSocketUseCase
import com.emil.domain.usecase.channel.CreateChannelPostUseCase
import com.emil.domain.usecase.channel.CreateChannelUseCase
import com.emil.domain.usecase.chat.CreateGroupUseCase
import com.emil.domain.usecase.user.CreateMomentUseCase
import com.emil.domain.usecase.settings.DeleteAvatarUseCase
import com.emil.domain.usecase.channel.DeleteChannelCommentUseCase
import com.emil.domain.usecase.channel.DeleteChannelPostUseCase
import com.emil.domain.usecase.chat.DeleteChatUseCase
import com.emil.domain.usecase.people.DeleteCommentUseCase
import com.emil.domain.usecase.people.DeleteLikeUseCase
import com.emil.domain.usecase.room.DeleteMessageFromLocalDbUseCase
import com.emil.domain.usecase.message.DeleteMessageUseCase
import com.emil.domain.usecase.user.DeleteMomentUseCase
import com.emil.domain.usecase.user.DeletePostUseCase
import com.emil.domain.usecase.channel.DeleteRequestUseCase
import com.emil.domain.usecase.channel.DeleteScoreUseCase
import com.emil.domain.usecase.websocket.DisconnectFromWebSocketUseCase
import com.emil.domain.usecase.chat.EditGroupUseCase
import com.emil.domain.usecase.message.EditMessageUseCase
import com.emil.domain.usecase.channel.FindChannelByLinkUseCase
import com.emil.domain.usecase.channel.FindChannelByNameUseCase
import com.emil.domain.usecase.user.GetUserMomentsUseCase
import com.emil.domain.usecase.user.GetUserPostsUseCase
import com.emil.domain.usecase.people.FindUsersByLinkUseCase
import com.emil.domain.usecase.people.FindUsersByUsernameUseCase
import com.emil.domain.usecase.feed.GetAllChannelsPostsUseCase
import com.emil.domain.usecase.feed.GetAllSubscriptionsPostsUseCase
import com.emil.domain.usecase.feed.GetAllUnseenMomentsUseCase
import com.emil.domain.usecase.channel.GetChannelManagementDataUseCase
import com.emil.domain.usecase.channel.GetChannelMembersUseCase
import com.emil.domain.usecase.channel.GetChannelPageDataUseCase
import com.emil.domain.usecase.channel.GetChannelPostCommentsUseCase
import com.emil.domain.usecase.channel.GetChannelPostsUseCase
import com.emil.domain.usecase.channel.GetChannelSubscriptionsRequestUseCse
import com.emil.domain.usecase.channel.GetChannelsUseCase
import com.emil.domain.usecase.chat.GetChatIdUseCase
import com.emil.domain.usecase.people.GetCommentsUseCase
import com.emil.domain.usecase.settings.GetEveryoneOffTheBlacklistUseCase
import com.emil.domain.usecase.chat.GetGroupDataUseCase
import com.emil.domain.usecase.chat.GetGroupMembersUseCase
import com.emil.domain.usecase.chat.GetGroupSendersUseCase
import com.emil.domain.usecase.settings.GetMessageModeUseCase
import com.emil.domain.usecase.people.GetOutsiderUserMomentsUseCase
import com.emil.domain.usecase.people.GetOutsiderUserPostsUseCase
import com.emil.domain.usecase.people.GetOutsiderUserSubscribersUseCase
import com.emil.domain.usecase.people.GetOutsiderUserSubscriptionsUseCase
import com.emil.domain.usecase.channel.GetPostAppreciatedUseCase
import com.emil.domain.usecase.people.GetPostLikesUseCase
import com.emil.domain.usecase.feed.GetRecommendationsUseCase
import com.emil.domain.usecase.room.GetUserChatsFromLocalDb
import com.emil.domain.usecase.user.GetUserChatsUseCase
import com.emil.domain.usecase.message.GetUserMessagesByChat
import com.emil.domain.usecase.room.GetUserMessagesByChatFromLocalDb
import com.emil.domain.usecase.message.GetUserMessagesUseCase
import com.emil.domain.usecase.people.GetUserPageDataUseCase
import com.emil.domain.usecase.people.GetUserSubscribersUseCase
import com.emil.domain.usecase.chat.GetUserSubscriptionsUseCase
import com.emil.domain.usecase.feed.GetUserUnseenMomentsUseCase
import com.emil.domain.usecase.room.InsertChatInLocalDbUseCase
import com.emil.domain.usecase.room.InsertMessageInLocalDbUseCase
import com.emil.domain.usecase.chat.LeaveTheGroupUseCase
import com.emil.domain.usecase.auth.LoginUseCase
import com.emil.domain.usecase.user.PublishPostUseCase
import com.emil.domain.usecase.user.RefreshTokenUseCase
import com.emil.domain.usecase.auth.RegisterUseCase
import com.emil.domain.usecase.channel.RejectSubscriptionRequestUseCase
import com.emil.domain.usecase.user.RemoveFromBlackListUseCase
import com.emil.domain.usecase.auth.RequestPasswordRecoveryUseCase
import com.emil.domain.usecase.auth.ResendCodeUseCase
import com.emil.domain.usecase.message.SendMessageUseCase
import com.emil.domain.usecase.user.SendReportUseCase
import com.emil.domain.usecase.message.SendStatusUseCase
import com.emil.domain.usecase.settings.SetMessageModeUseCase
import com.emil.domain.usecase.channel.SubmitRequestUseCase
import com.emil.domain.usecase.channel.SubscribeChannelUseCase
import com.emil.domain.usecase.websocket.SubscribeToChatStatusUseCase
import com.emil.domain.usecase.websocket.SubscribeToEditMessagesUseCase
import com.emil.domain.usecase.websocket.SubscribeToMessagesDeleteUseCase
import com.emil.domain.usecase.websocket.SubscribeToUserChatViewedUseCase
import com.emil.domain.usecase.websocket.SubscribeToUserChatsCountUseCase
import com.emil.domain.usecase.websocket.SubscribeToUserChatsUseCase
import com.emil.domain.usecase.websocket.SubscribeToUserMessagesUseCase
import com.emil.domain.usecase.people.SubscribeUseCase
import com.emil.domain.usecase.channel.UnsubscribeChannelUseCase
import com.emil.domain.usecase.people.UnsubscribeUseCase
import com.emil.domain.usecase.settings.UpdateBirthdayUseCase
import com.emil.domain.usecase.settings.UpdateLinkUseCase
import com.emil.domain.usecase.settings.UpdateUsernameUseCase
import com.emil.domain.usecase.settings.UploadAvatarUseCase
import com.emil.domain.usecase.people.UserProfileDataUseCase
import com.emil.domain.usecase.people.ViewMomentUseCase
import com.emil.domain.usecase.message.ViewedUseCase
import com.emil.domain.usecase.channel.VoteUseCase
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
    factory<GetAllUserDataUseCase>{
       GetAllUserDataUseCase(userRepository = get())
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
        GetChannelMembersUseCase(channelRepository = get())
    }

    factory<GetChannelPostsUseCase>{
       GetChannelPostsUseCase(channelRepository = get())
    }


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
        UnsubscribeChannelUseCase(channelRepository = get())
    }
    factory<VoteUseCase>{
      VoteUseCase(channelRepository = get())
    }

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














