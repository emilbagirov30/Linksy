package com.emil.data.repository

import android.annotation.SuppressLint
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.EditMessageResponse
import com.emil.domain.model.MessageResponse
import com.emil.domain.model.Status
import com.emil.domain.model.StatusResponse
import com.emil.domain.repository.WebSocketRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

class WebSocketRepositoryImpl() : WebSocketRepository {
    private val BASE_URL = "wss://linksy-mes.ru:9614/ws/websocket"
    private val stompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, BASE_URL)
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val jsonAdapterMessage = moshi.adapter(MessageResponse::class.java)
    private val jsonAdapterChats = moshi.adapter(ChatResponse::class.java)
    private val jsonAdapterMessageId = moshi.adapter(Long::class.java)
    private val jsonAdapterEditMessage = moshi.adapter(EditMessageResponse::class.java)
    private val jsonAdapterStatus = moshi.adapter(StatusResponse::class.java)
    override fun connect() {
        if (!stompClient.isConnected)
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

    override fun subscribeToDeletedMessages(token: String, chatId: Long): Flow<Long> = callbackFlow {
        val topic = stompClient.topic("/user/$token/queue/messages/deleted/$chatId/")
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

    override fun subscribeToEditMessages(token: String, chatId: Long): Flow<EditMessageResponse> = callbackFlow {
        val topic = stompClient.topic("/user/$token/queue/messages/edited/${chatId}/")
            .subscribe({ stompMessage ->
                val message = stompMessage.payload
                val messageResponse = jsonAdapterEditMessage.fromJson(message)
                messageResponse?.let { trySend(it).isSuccess }
            }, { error ->

            })

        awaitClose {
            topic.dispose()
        }

    }

    @SuppressLint("CheckResult")
    override fun sendStatus(status: Status) {
        val destination = "/app/chat/status"
        val jsonAdapterStatus = moshi.adapter(Status::class.java)
        val payload = jsonAdapterStatus.toJson(status)
        stompClient.send(destination, payload)
            .subscribe({

            }, { _ ->

            })
    }

    override fun subscribeToStatusMessages(token: String, chatId: Long): Flow<StatusResponse> = callbackFlow {
        val topic = stompClient.topic("/user/$token/queue/messages/status/${chatId}/")
            .subscribe({ stompMessage ->
                val message = stompMessage.payload
                val messageResponse = jsonAdapterStatus.fromJson(message)
                messageResponse?.let { trySend(it).isSuccess }
            }, { error ->

            })

        awaitClose {
            topic.dispose()
        }

    }
}