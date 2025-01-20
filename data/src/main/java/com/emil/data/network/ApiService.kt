package com.emil.data.network

import com.emil.data.model.AllUserDataDto
import com.emil.data.model.ChannelPageDataResponseDto
import com.emil.data.model.ChannelPostResponseDto
import com.emil.data.model.ChannelResponseDto
import com.emil.data.model.ChatResponseDto
import com.emil.data.model.CommentBody
import com.emil.data.model.CommentResponseDto
import com.emil.data.model.GroupResponseDto
import com.emil.data.model.MessageResponseDto
import com.emil.data.model.MomentResponseDto
import com.emil.data.model.PasswordChangeBody
import com.emil.data.model.PasswordRecoveryBody
import com.emil.data.model.PostBody
import com.emil.data.model.PostResponseDto
import com.emil.data.model.RegistrationBody
import com.emil.data.model.TokenDto
import com.emil.data.model.UserLoginBody
import com.emil.data.model.UserPageDataResponseDto
import com.emil.data.model.UserProfileDataDto
import com.emil.data.model.UserResponseDto
import com.emil.domain.model.ChannelType
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.MessageMode
import com.emil.domain.model.MessageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/users/register")
    suspend fun registerUser(@Body request: RegistrationBody): Response<Unit>

    @POST("api/users/confirm")
    suspend fun confirmCode(@Query("email") email:String, @Query("code") code: String): Response<Unit>
    @POST("api/users/resend_code")
    suspend fun resendCode(@Query("email") emailParam:String): Response<Unit>
    @POST("api/users/login")
    suspend fun login(@Body request:UserLoginBody): Response<TokenDto>
    @POST("api/users/request_password_change")
    suspend fun requestPasswordChange(@Query("email") emailParam:String): Response<Unit>

    @POST("api/users/confirm_password_change")
    suspend fun confirmPasswordChange(@Body request:PasswordRecoveryBody): Response<Unit>
    @GET ("api/users/profile_data")
    suspend fun getUserProfileData (@Header("Authorization") token:String): Response<UserProfileDataDto>
    @DELETE ("api/users/delete_avatar")
    suspend fun deleteAvatar (@Header("Authorization") token:String): Response<Unit>
    @GET ("api/users/all_data")
    suspend fun getAllUserData (@Header("Authorization") token:String): Response<AllUserDataDto>

    @POST ("api/upload/avatar")
    @Multipart
    suspend fun uploadAvatar (@Header("Authorization") token:String,@Part image: MultipartBody.Part): Response<Unit>

    @PATCH("api/users/refresh_token")
    suspend fun refreshToken (@Query("refreshToken") token:String):Response<TokenDto>
    @PATCH("api/users/update_birthday")
    suspend fun updateBirthday (@Header("Authorization") token:String,@Query("birthday") birthday:String):Response<Unit>
    @PATCH("api/users/update_username")
    suspend fun updateUsername (@Header("Authorization") token:String,@Query("username") username:String):Response<Unit>
    @PATCH("api/users/update_link")
    suspend fun updateLink (@Header("Authorization") token:String, @Query("link") link:String):Response<Unit>
    @PATCH("api/users/change_password")
    suspend fun changePassword (@Header("Authorization") token:String, @Body passwordChangeBody: PasswordChangeBody):Response<Unit>

    @POST("api/posts/cu")
    @Multipart
    suspend fun createOrUpdatePost (@Header("Authorization") token:String,
                            @Part("id") postId: Long?,
                            @Part("text") text: String?,
                                    @Part("imageUrl") imageUrl: String?,
                                    @Part("videoUrl") videoUrl: String?,
                                    @Part("audioUrl") audioUrl: String?,
                                    @Part("voiceUrl") voiceUrl: String?,
                            @Part image: MultipartBody.Part?,
                            @Part video: MultipartBody.Part?,
                            @Part audio: MultipartBody.Part?,
                            @Part voice: MultipartBody.Part?):Response<Unit>



    @POST("api/moments/create")
    @Multipart
    suspend fun createMoment (@Header("Authorization") token:String,
                            @Part image: MultipartBody.Part?,
                            @Part video: MultipartBody.Part?,
                            @Part audio: MultipartBody.Part?, @Part("text") text: String?):Response<Unit>







    @GET ("api/posts/user_posts")
    suspend fun getUserPosts (@Header("Authorization") token:String): Response<List<PostResponseDto>>
    @DELETE ("api/posts/delete_post")
    suspend fun deletePost (@Header("Authorization") token:String,@Query("postId")postId:Long): Response<Unit>
    @GET ("api/moments/user_moments")
    suspend fun getUserMoments (@Header("Authorization") token:String): Response<List<MomentResponseDto>>
    @DELETE ("api/moments/delete_moment")
    suspend fun deleteMoment (@Header("Authorization") token:String,@Query("momentId")momentId:Long): Response<Unit>

    @GET ("api/people/find/username")
    suspend fun findUserByUsername (@Header("Authorization") token:String,@Query("startsWith")startsWith:String): Response<List<UserResponseDto>>

    @GET ("api/people/find/link")
    suspend fun findUserByLink (@Header("Authorization") token:String,@Query("startsWith")startsWith:String): Response<List<UserResponseDto>>

    @GET("api/people/{id}")
    suspend fun getUserPageData(@Header("Authorization") token:String,@Path("id") id:Long): Response<UserPageDataResponseDto>
    @GET ("api/people/user_posts/{id}")
    suspend fun getOutsiderUserPosts (@Header("Authorization") token: String,@Path("id") id:Long): Response<List<PostResponseDto>>
    @GET ("api/people/user_moments/{id}")
    suspend fun getOutsiderUserMoments (@Path("id") id:Long): Response<List<MomentResponseDto>>




    @PUT("api/people/subscribe/{id}")
    suspend fun subscribe (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>
    @DELETE("api/people/unsubscribe/{id}")
    suspend fun unsubscribe (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>

    @GET("api/people/user_subscribers")
    suspend fun getUserSubscribers(@Header("Authorization") token:String): Response<List<UserResponseDto>>
    @GET("api/people/user_subscriptions")
    suspend fun getUserSubscriptions(@Header("Authorization") token:String): Response<List<UserResponseDto>>

    @GET("api/people/outsider/user_subscribers/{id}")
    suspend fun getOutsiderUserSubscribers(@Path("id") id:Long): Response<List<UserResponseDto>>
    @GET("api/people/outsider/user_subscriptions/{id}")
    suspend fun getOutsiderUserSubscriptions(@Path("id") id:Long): Response<List<UserResponseDto>>

    @POST("api/message/send")
    @Multipart
    suspend fun sendMessage (@Header("Authorization") token:String,
                             @Part("recipientId") recipientId:Long?,
                             @Part("chatId") chatId:Long?,
                            @Part("text") text: String?,
                            @Part image: MultipartBody.Part?,
                            @Part video: MultipartBody.Part?,
                            @Part audio: MultipartBody.Part?,
                            @Part voice: MultipartBody.Part?):Response<Unit>


    @GET("api/messages/user_messages")
    suspend fun getUserMessages(@Header("Authorization") token:String): Response<MutableList<MessageResponseDto>>
    @GET("api/chats/user_chats")
    suspend fun getUserChats(@Header("Authorization") token:String): Response<List<ChatResponseDto>>
    @GET("api/chats/users_chat_id")
    suspend fun getChatId(@Header("Authorization") token:String,@Query("id") id:Long): Response<Long>


    @POST("api/chats/create/group")
    @Multipart
    suspend fun createGroup (@Header("Authorization") token:String,
                             @Part("ids") participant: String?,
                             @Part("name") text: String?,
                             @Part image: MultipartBody.Part?,
                             ):Response<Unit>



    @GET("api/chats/group/members/{id}")
    suspend fun getGroupMembers(@Header("Authorization") token:String,@Path("id")groupId:Long): Response<List<UserResponseDto>>



    @POST("api/channels/create")
    @Multipart
    suspend fun createChannel (@Header("Authorization") token:String, @Part("name") name: String?,
                             @Part("link") link: String?, @Part("description") description: String, @Part("type") type: String,
                             @Part image: MultipartBody.Part?
    ):Response<Unit>

    @GET("api/channels/user_channels")
    suspend fun getChannels (@Header("Authorization") token:String):Response<List<ChannelResponseDto>>


    @GET("api/channels/{id}")
    suspend fun getChannelPageData(@Header("Authorization") token:String,@Path("id") id:Long): Response<ChannelPageDataResponseDto>


    @POST("api/channels/create_post")
    @Multipart
    suspend fun createChannelPost(@Header("Authorization") token:String, @Part("id") channelId:Long, @Part("text") text:String?,
                           @Part image: MultipartBody.Part?, @Part video: MultipartBody.Part?, @Part audio: MultipartBody.Part?,
                                  @Part("title") pollTitle:String?, @Part("options") options:List<String>):Response<Unit>


        @POST("api/channels/submit")
        suspend fun submitRequest(@Header("Authorization") token: String, @Query("id") channelId: Long): Response<Unit>

        @DELETE("api/channels/delete_request")
        suspend fun deleteRequest(@Header("Authorization") token: String, @Query("id") channelId: Long): Response<Unit>

        @GET("api/channels/subscriptions_request")
        suspend fun getChannelSubscriptionRequests(@Header("Authorization") token: String, @Query("id") channelId: Long): Response<List<UserResponseDto>>

        @POST("api/channels/requests/accept")
        suspend fun acceptUserToChannel(@Header("Authorization") token: String, @Query("channelId") channelId: Long, @Query("candidateId") candidateId: Long): Response<Unit>

        @POST("api/channels/requests/reject")
        suspend fun rejectSubscriptionRequest(@Header("Authorization") token: String, @Query("channelId") channelId: Long, @Query("candidateId") candidateId: Long): Response<Unit>

        @GET("api/channels/posts/{id}")
        suspend fun getChannelsPost(@Header("Authorization") token: String, @Path("id") channelId: Long): Response<List<ChannelPostResponseDto>>

        @GET("api/channels/members/{id}")
        suspend fun getChannelMembers(@Header("Authorization") token: String, @Path("id") channelId: Long): Response<List<UserResponseDto>>

        @DELETE("api/channels/delete_post")
        suspend fun deleteChannelPost(@Header("Authorization") token: String, @Query("channelId") channelId: Long): Response<Unit>

    @PUT("api/channels/subscribe/{id}")
    suspend fun subscribeChannel (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>
    @DELETE("api/channels/unsubscribe/{id}")
    suspend fun unsubscribeChannel (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>

    @POST("api/channels/poll/option/vote/{id}")
    suspend fun vote (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>

    @GET ("api/channels/find/name")
    suspend fun findChannelByName (@Query("prefix")prefix:String): Response<List<ChannelResponseDto>>

    @GET ("api/channels/find/link")
    suspend fun findChannelByLink (@Query("prefix")prefix:String): Response<List<ChannelResponseDto>>


    @POST("api/posts/like/add/{id}")
    suspend fun addLike(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @DELETE("api/posts/like/delete/{id}")
    suspend fun deleteLike(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @POST("api/posts/add/comment")
    suspend fun addComment(@Header("Authorization") token: String, @Body commentRequest: CommentBody): Response<Unit>

    @GET("api/posts/{id}/comments")
    suspend fun getPostComments(@Path("id") id: Long): Response<List<CommentResponseDto>>


    @POST("api/people/blacklist/add/{id}")
    suspend fun addToBlackList(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @DELETE("api/people/blacklist/remove/{id}")
    suspend fun removeBlackList(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @GET("api/people/blacklist/all")
    suspend fun getBlackList(@Header("Authorization") token: String): Response<List<UserResponseDto>>

    @GET("api/users/message_mode")
    suspend fun getMessageMode(@Header("Authorization") token: String): Response<MessageMode>

    @PUT("api/users/message_mode")
    suspend fun setMessageMode(@Header("Authorization") token: String,@Body messageMode: MessageMode): Response<Unit>

    @GET("api/messages/by/chat/{chatId}")
    suspend fun getUserMessagesByChat(@Header("Authorization") token:String, @Path("chatId") chatId: Long): Response<MutableList<MessageResponseDto>>

    @PUT("api/messages/viewed/chat/{chatId}")
    suspend fun viewed(@Header("Authorization") token: String,@Path("chatId") chatId: Long): Response<Unit>


    @GET("api/chats/group/data/{id}")
    suspend fun getGroupData(@Header("Authorization") token: String,@Path("id") chatId: Long): Response<GroupResponseDto>




    @PUT("api/chats/edit/group")
    @Multipart
    suspend fun editGroup (@Header("Authorization") token:String,
                           @Part("id") groupId: Long,
                             @Part("name") text: String?,
                             @Part("oldAvatarUrl") oldAvatarUrl: String?,
                             @Part image: MultipartBody.Part?,
    ):Response<Unit>

}
