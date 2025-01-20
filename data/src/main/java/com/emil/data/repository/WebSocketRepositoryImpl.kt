package com.emil.data.repository

import com.emil.data.TemporaryKeyStore
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.MessageResponse
import com.emil.domain.repository.WebSocketRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

class WebSocketRepositoryImpl() : WebSocketRepository {

    private val stompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, TemporaryKeyStore.BASE_WS)
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val jsonAdapterMessage = moshi.adapter(MessageResponse::class.java)
    private val jsonAdapterChats = moshi.adapter(ChatResponse::class.java)
    private val jsonAdapterMessageId = moshi.adapter(Long::class.java)
    override fun connect() {
            stompClient.connect()

    }

    override fun disconnect() {
        stompClient.disconnect()
    }

    override fun subscribeToUserMessages(token: String,chatId:Long): Flow<MessageResponse> = callbackFlow {
            val topic = stompClient.topic("/user/$token/queue/messages/${chatId}/")
                .subscribe({ stompMessage ->
                    val message = stompMessage.payload
                    val messageResponse = jsonAdapterMessage.fromJson(message)
                    messageResponse?.let { trySend(it).isSuccess }
                }, { error ->

                })

            awaitClose {
                topic.dispose()
            }

    }

    override fun subscribeToUserChats(token: String): Flow<ChatResponse> = callbackFlow {
        val topic = stompClient.topic("/user/$token/queue/chats/").subscribe({ stompMessage ->
            val message = stompMessage.payload
            val messageResponse = jsonAdapterChats.fromJson(message)
            messageResponse?.let { trySend(it).isSuccess }
        }, { error ->

        })

        awaitClose {
            topic.dispose()
        }
    }

    override fun subscribeToUserChatsCount(token: String): Flow<ChatResponse>  = callbackFlow {
        val topic = stompClient.topic("/user/$token/queue/count/").subscribe({ stompMessage ->
            val message = stompMessage.payload
            val messageResponse = jsonAdapterChats.fromJson(message)
            messageResponse?.let { trySend(it).isSuccess }
        }, { error ->

        })

        awaitClose {
            topic.dispose()
        }
    }

    override fun subscribeToUserChatViewed(token: String, chatId: Long): Flow<Long> = callbackFlow {
            val topic = stompClient.topic("/user/$token/queue/messages/viewed/$chatId/")
                .subscribe({ stompMessage ->
                    val message = stompMessage.payload
                    val messageResponse = jsonAdapterMessageId.fromJson(message)
                    messageResponse?.let { trySend(it).isSuccess }
                }, { error ->

                })

            awaitClose {
                topic.dispose()
            }

    }
}