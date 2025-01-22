package com.emil.linksy.di

import androidx.room.Room
import com.emil.data.repository.AppDatabase
import com.emil.data.repository.AuthRepositoryImpl
import com.emil.data.repository.ChannelRepositoryImpl
import com.emil.data.repository.ChatDao
import com.emil.data.repository.ChatRepositoryImpl
import com.emil.data.repository.FeedRepositoryImpl
import com.emil.data.repository.MessageDao
import com.emil.data.repository.MessageRepositoryImpl
import com.emil.data.repository.MomentRepositoryImpl
import com.emil.data.repository.PeopleRepositoryImpl
import com.emil.data.repository.PostRepositoryImpl
import com.emil.data.repository.TokenRepositoryImpl
import com.emil.data.repository.UserRepositoryImpl
import com.emil.data.repository.WebSocketRepositoryImpl
import com.emil.domain.repository.AuthRepository
import com.emil.domain.repository.ChannelRepository
import com.emil.domain.repository.ChatRepository
import com.emil.domain.repository.FeedRepository
import com.emil.domain.repository.MessageRepository
import com.emil.domain.repository.MomentRepository
import com.emil.domain.repository.PeopleRepository
import com.emil.domain.repository.PostRepository
import com.emil.domain.repository.TokenRepository
import com.emil.domain.repository.UserRepository
import com.emil.domain.repository.WebSocketRepository
import com.emil.linksy.app.service.TokenService
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
    single<PeopleRepository>{
      PeopleRepositoryImpl()
    }

    single<MessageRepository>{
       MessageRepositoryImpl(messageDao = get())
    }
    single<ChatRepository>{
        ChatRepositoryImpl(chatDao = get())
    }
    single<ChannelRepository>{
     ChannelRepositoryImpl()
    }
    single<MessageDao> {
        get<AppDatabase>().messageDao()
    }
    single<ChatDao> {
        get<AppDatabase>().chatDao()
    }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "appLocalDb"
        ).build()
    }

    single<WebSocketRepository>{
       WebSocketRepositoryImpl()
    }

    single<FeedRepository>{
     FeedRepositoryImpl()
    }
}