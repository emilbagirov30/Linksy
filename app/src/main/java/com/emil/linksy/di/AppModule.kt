package com.emil.linksy.di

import com.emil.domain.usecase.user.RefreshTokenUseCase
import com.emil.linksy.app.service.TokenService
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.presentation.viewmodel.ProfileManagementViewModel
import com.emil.linksy.presentation.viewmodel.ConfirmCodeViewModel
import com.emil.linksy.presentation.viewmodel.FeedViewModel
import com.emil.linksy.presentation.viewmodel.LoginViewModel
import com.emil.linksy.presentation.viewmodel.MessageViewModel
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.presentation.viewmodel.PasswordChangeViewModel
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.presentation.viewmodel.RecoveryPasswordViewModel
import com.emil.linksy.presentation.viewmodel.RegistrationViewModel
import com.emil.linksy.presentation.viewmodel.UserViewModel
import com.emil.linksy.util.TokenManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { TokenManager(androidContext()) }
    single { RefreshTokenUseCase(get()) }

    viewModel <RegistrationViewModel> {
        RegistrationViewModel(registerUseCase = get())
    }
    viewModel <ConfirmCodeViewModel> {
        ConfirmCodeViewModel(
            confirmUseCase = get(),
            resendCodeUseCase = get())
    }
    viewModel <LoginViewModel> {
      LoginViewModel(loginUseCase = get(), tokenManager = get())
    }
    viewModel <RecoveryPasswordViewModel> {
       RecoveryPasswordViewModel(requestPasswordRecoveryUseCase = get(), confirmPasswordRecoveryUseCase = get ())
    }
    viewModel <UserViewModel> {
      UserViewModel(userProfileDataUseCase = get (),
          getEveryoneOffTheBlacklistUseCase = get(),
          getMessageModeUseCase = get(),
          setMessageModeUseCase = get())
    }
    viewModel <PasswordChangeViewModel> {
        PasswordChangeViewModel(
            changePasswordUseCase = get (),
            tokenManager = get())
    }
    viewModel <PostViewModel> {
        PostViewModel(publishPostUseCase = get (), getUserPostsUseCase = get(), getOutsiderUserPostsUseCase = get(),
            deletePostUseCase = get(), getCommentsUseCase = get(), addLikeUseCase = get(), addCommentUseCase = get(), deleteLikeUseCase = get(),
            deleteCommentUseCase = get(), getPostLikesUseCase = get())
    }

    viewModel <MomentViewModel> {
        MomentViewModel(createMomentUseCase = get(), getUserMomentsUseCase = get(),
            getOutsiderUserMomentsUseCase = get(), deleteMomentUseCase = get(),
            viewMomentUseCase = get(), getUserUnseenMomentsUseCase = get())
    }
    viewModel <ProfileManagementViewModel> {
       ProfileManagementViewModel(getAllUserDataUseCase = get (),
           uploadAvatarUseCase = get(), updateBirthdayUseCase = get(), updateUsernameUseCase = get (), updateLinkUseCase = get (),
           deleteAvatarUseCase = get())
    }
    viewModel <PeopleViewModel> {
        PeopleViewModel(findUsersByUsernameUseCase = get(), findUsersByLinkUseCase = get(), getUserPageDataUseCase = get(),
            subscribeUseCase = get(), unsubscribeUseCase = get(),
            getUserSubscribersUseCase = get(), getUserSubscriptionsUseCase = get(),
            getOutsiderUserSubscribersUseCase = get(), getOutsiderUserSubscriptionsUseCase = get(), addToBlackListUseCase = get(), removeFromBlackListUseCase = get(), sendReportUseCase = get())
    }


       viewModel<MessageViewModel> {
           MessageViewModel(
               sendMessageUseCase = get(),
               getUserMessagesUseCase = get(),
               insertMessageInLocalDbUseCase = get(),
               getUserMessagesByChatFromLocalDb = get(),
               connectToWebSocketUseCase = get(),
               subscribeToUserMessagesUseCase = get(),
               disconnectFromWebSocketUseCase = get (),
               getUserMessagesByChat = get(),
               viewedUseCase = get(),
               subscribeToUserChatViewedUseCase = get(),
               deleteMessageUseCase = get(),
               subscribeToDeleteMessageUseCase = get(),
               editMessageUseCase = get(),
               subscribeToEditMessagesUseCase = get(),
               sendStatusUseCase = get(),
               subscribeToChatStatusUseCase = get(),
               deleteMessageFromLocalDbUseCase = get(),
               clearAllMessagesUseCase = get()
           )
       }

    viewModel <ChatViewModel> {
      ChatViewModel(getUserChatsUseCase = get(), getUserChatsFromLocalDb = get(),
          insertChatInLocalDbUseCase = get(), getChatIdUseCase = get(), createGroupUseCase = get(), getGroupMembersUseCase = get(),
          subscribeToUserChatsUseCase = get(), connectToWebSocketUseCase = get(), getGroupDataUseCase = get(), editGroupUseCase = get(),
          deleteChatUseCase = get(), clearChatsUseCase = get(), addMembersUseCase = get(), leaveTheGroupUseCase = get(), getGroupSendersUseCase = get())
    }

    viewModel <ChannelViewModel>{
       ChannelViewModel(createChannelUseCase = get(),
           getChannelsUseCase = get(),
           getChannelPageDataUseCase = get(),
           getChannelManagementDataUseCase = get(),
           getChannelPostsUseCase = get(),
           getChannelMembersUseCase = get(),
           getPostAppreciatedUseCase = get(),
           getChannelPostCommentsUseCase = get(),
           getChannelSubscriptionsRequestUseCse = get(),
           addScoreUseCase = get(),
           acceptUserToChannelUseCase = get(),
           addChannelPostCommentUseCase = get(),
           deleteRequestUseCase = get(),
           submitRequestUseCase = get(),
           deleteScoreUseCase = get(),
           voteUseCase = get(),
           createChannelPostUseCase = get(),
           deleteChannelPostUseCase = get(),
           findChannelByLinkUseCase = get(),
           findChannelByNameUseCase = get(),
           subscribeChannelUseCase = get(),
           unsubscribeChannelUseCase = get(),
           deleteChannelCommentUseCase = get(),
           rejectSubscriptionRequestUseCase = get()
         )
    }



    viewModel <FeedViewModel> {
       FeedViewModel(getAllSubscriptionsPostsUseCase = get(), getAllChannelsPostsUseCase = get(),
           getRecommendationsUseCase = get(), getAllUnseenMomentsUseCase = get())
    }

    single { TokenService() }
}