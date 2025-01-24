package com.emil.linksy.di

import com.emil.domain.usecase.RefreshTokenUseCase
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
        ConfirmCodeViewModel(confirmUseCase = get(), resendCodeUseCase = get())
    }
    viewModel <LoginViewModel> {
      LoginViewModel(loginUseCase = get(), tokenManager = get())
    }
    viewModel <RecoveryPasswordViewModel> {
       RecoveryPasswordViewModel(requestPasswordRecoveryUseCase = get(), confirmPasswordRecoveryUseCase = get ())
    }
    viewModel <UserViewModel> {
      UserViewModel(userProfileDataUseCase = get (), getEveryoneOffTheBlacklistUseCase = get(), getMessageModeUseCase = get(), setMessageModeUseCase = get())
    }
    viewModel <PasswordChangeViewModel> {
        PasswordChangeViewModel(changePasswordUseCase = get (), tokenManager = get())
    }
    viewModel <PostViewModel> {
        PostViewModel(publishPostUseCase = get (), getUserPostsUseCase = get(), getOutsiderUserPostsUseCase = get(), deletePostUseCase = get(),
            getCommentsUseCase = get(), addLikeUseCase = get(), addCommentUseCase = get(), deleteLikeUseCase = get(), deleteCommentUseCase = get())
    }

    viewModel <MomentViewModel> {
        MomentViewModel(createMomentUseCase = get(), getUserMomentsUseCase = get(), getOutsiderUserMomentsUseCase = get(), deleteMomentUseCase = get())
    }
    viewModel <ProfileManagementViewModel> {
       ProfileManagementViewModel(allUserDataUseCase = get (), uploadAvatarUseCase = get(),
           updateBirthdayUseCase = get(), updateUsernameUseCase = get (), updateLinkUseCase = get (), deleteAvatarUseCase = get())
    }
    viewModel <PeopleViewModel> {
        PeopleViewModel(findUsersByUsernameUseCase = get(), findUsersByLinkUseCase = get(), getUserPageDataUseCase = get(),
            subscribeUseCase = get(), unsubscribeUseCase = get(),
            getUserSubscribersUseCase = get(), getUserSubscriptionsUseCase = get(),
            getOutsiderUserSubscribersUseCase = get(), getOutsiderUserSubscriptionsUseCase = get(), addToBlackListUseCase = get(), removeFromBlackListUseCase = get())
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
               subscribeToChatStatusUseCase = get()
           )
       }

    viewModel <ChatViewModel> {
      ChatViewModel(getUserChatsUseCase = get(), getUserChatsFromLocalDb = get(), insertChatInLocalDbUseCase = get(), getChatIdUseCase = get(), createGroupUseCase = get(), getGroupMembersUseCase = get(),
          subscribeToUserChatsUseCase = get(), connectToWebSocketUseCase = get(), getGroupDataUseCase = get(), editGroupUseCase = get())
    }

    viewModel <ChannelViewModel>{
       ChannelViewModel(createChannelUseCase = get(), getChannelsUseCase = get(), getChannelPageDataUseCase = get(),
           get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get())
    }



    viewModel <FeedViewModel> {
       FeedViewModel(getAllSubscriptionsPostsUseCase = get(), getAllChannelsPostsUseCase = get())
    }






    single { TokenService() }
}