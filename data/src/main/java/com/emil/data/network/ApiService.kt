package com.emil.data.network

import com.emil.data.model.AllUserDataDto
import com.emil.data.model.ChannelManagementResponseDto
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
import com.emil.data.model.PostAppreciatedResponseDto
import com.emil.data.model.PostResponseDto
import com.emil.data.model.RecommendationResponseDto
import com.emil.data.model.RegistrationBody
import com.emil.data.model.ReportBody
import com.emil.data.model.TokenDto
import com.emil.data.model.UnseenSubscriptionMomentResponseDto
import com.emil.data.model.UserLoginBody
import com.emil.data.model.UserPageDataResponseDto
import com.emil.data.model.UserProfileDataDto
import com.emil.data.model.UserResponseDto
import com.emil.domain.model.MessageMode
import com.emil.domain.model.UnseenSubscriptionMomentResponse
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
    @POST("user/api/users/register")
    suspend fun registerUser(@Body request: RegistrationBody): Response<Unit>

    @POST("user/api/users/confirm")
    suspend fun confirmCode(@Query("email") email:String, @Query("code") code: String): Response<Unit>
    @POST("user/api/users/resend_code")
    suspend fun resendCode(@Query("email") emailParam:String): Response<Unit>
    @POST("user/api/users/login")
    suspend fun login(@Body request:UserLoginBody): Response<TokenDto>
    @POST("user/api/users/request_password_change")
    suspend fun requestPasswordChange(@Query("email") emailParam:String): Response<Unit>

    @POST("user/api/users/confirm_password_change")
    suspend fun confirmPasswordChange(@Body request:PasswordRecoveryBody): Response<Unit>
    @GET ("user/api/users/profile_data")
    suspend fun getUserProfileData (@Header("Authorization") token:String): Response<UserProfileDataDto>
    @DELETE ("user/api/users/delete_avatar")
    suspend fun deleteAvatar (@Header("Authorization") token:String): Response<Unit>
    @GET ("user/api/users/all_data")
    suspend fun getAllUserData (@Header("Authorization") token:String): Response<AllUserDataDto>

    @POST ("cloud/api/upload/avatar")
    @Multipart
    suspend fun uploadAvatar (@Header("Authorization") token:String,@Part image: MultipartBody.Part): Response<Unit>

    @PATCH("user/api/users/refresh_token")
    suspend fun refreshToken (@Query("refreshToken") token:String):Response<TokenDto>
    @PATCH("user/api/users/update_birthday")
    suspend fun updateBirthday (@Header("Authorization") token:String,@Query("birthday") birthday:String):Response<Unit>
    @PATCH("user/api/users/update_username")
    suspend fun updateUsername (@Header("Authorization") token:String,@Query("username") username:String):Response<Unit>
    @PATCH("user/api/users/update_link")
    suspend fun updateLink (@Header("Authorization") token:String, @Query("link") link:String):Response<Unit>
    @PATCH("user/api/users/change_password")
    suspend fun changePassword (@Header("Authorization") token:String, @Body passwordChangeBody: PasswordChangeBody):Response<TokenDto>

    @POST("cloud/api/posts/cu")
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



    @POST("cloud/api/moments/create")
    @Multipart
    suspend fun createMoment (@Header("Authorization") token:String,
                            @Part image: MultipartBody.Part?,
                            @Part video: MultipartBody.Part?,
                            @Part audio: MultipartBody.Part?, @Part("text") text: String?):Response<Unit>







    @GET ("user/api/posts/user_posts")
    suspend fun getUserPosts (@Header("Authorization") token:String): Response<List<PostResponseDto>>
    @DELETE ("user/api/posts/delete_post")
    suspend fun deletePost (@Header("Authorization") token:String,@Query("postId")postId:Long): Response<Unit>
    @GET ("user/api/moments/user_moments")
    suspend fun getUserMoments (@Header("Authorization") token:String): Response<List<MomentResponseDto>>
    @DELETE ("user/api/moments/delete_moment")
    suspend fun deleteMoment (@Header("Authorization") token:String,@Query("momentId")momentId:Long): Response<Unit>

    @GET ("user/api/people/find/username")
    suspend fun findUserByUsername (@Header("Authorization") token:String,@Query("startsWith")startsWith:String): Response<List<UserResponseDto>>

    @GET ("user/api/people/find/link")
    suspend fun findUserByLink (@Header("Authorization") token:String,@Query("startsWith")startsWith:String): Response<List<UserResponseDto>>

    @GET("user/api/people/{id}")
    suspend fun getUserPageData(@Header("Authorization") token:String,@Path("id") id:Long): Response<UserPageDataResponseDto>
    @GET ("user/api/people/user_posts/{id}")
    suspend fun getOutsiderUserPosts (@Header("Authorization") token: String,@Path("id") id:Long): Response<List<PostResponseDto>>
    @GET ("user/api/people/user_moments/{id}")
    suspend fun getOutsiderUserMoments (@Path("id") id:Long): Response<List<MomentResponseDto>>




    @PUT("user/api/people/subscribe/{id}")
    suspend fun subscribe (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>
    @DELETE("user/api/people/unsubscribe/{id}")
    suspend fun unsubscribe (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>

    @GET("user/api/people/user_subscribers")
    suspend fun getUserSubscribers(@Header("Authorization") token:String): Response<List<UserResponseDto>>
    @GET("user/api/people/user_subscriptions")
    suspend fun getUserSubscriptions(@Header("Authorization") token:String): Response<List<UserResponseDto>>

    @GET("user/api/people/outsider/user_subscribers/{id}")
    suspend fun getOutsiderUserSubscribers(@Path("id") id:Long): Response<List<UserResponseDto>>
    @GET("user/api/people/outsider/user_subscriptions/{id}")
    suspend fun getOutsiderUserSubscriptions(@Path("id") id:Long): Response<List<UserResponseDto>>

    @POST("cloud/api/message/send")
    @Multipart
    suspend fun sendMessage (@Header("Authorization") token:String,
                             @Part("recipientId") recipientId:Long?,
                             @Part("chatId") chatId:Long?,
                            @Part("text") text: String?,
                            @Part image: MultipartBody.Part?,
                            @Part video: MultipartBody.Part?,
                            @Part audio: MultipartBody.Part?,
                            @Part voice: MultipartBody.Part?):Response<Unit>


    @GET("user/api/messages/user_messages")
    suspend fun getUserMessages(@Header("Authorization") token:String): Response<MutableList<MessageResponseDto>>
    @GET("user/api/chats/user_chats")
    suspend fun getUserChats(@Header("Authorization") token:String): Response<List<ChatResponseDto>>
    @GET("user/api/chats/users_chat_id")
    suspend fun getChatId(@Header("Authorization") token:String,@Query("id") id:Long): Response<Long>


    @POST("cloud/api/chats/create/group")
    @Multipart
    suspend fun createGroup (@Header("Authorization") token:String,
                             @Part("ids") participant: String?,
                             @Part("name") text: String?,
                             @Part image: MultipartBody.Part?,
                             ):Response<Unit>



    @GET("user/api/chats/group/members/{id}")
    suspend fun getGroupMembers(@Header("Authorization") token:String,@Path("id")groupId:Long): Response<List<UserResponseDto>>



    @POST("cloud/api/channels/cu")
    @Multipart
    suspend fun createOrUpdateChannel (@Header("Authorization") token:String, @Part("name") name: String?,@Part("channelId") channelId: Long?,
                             @Part("link") link: String?, @Part("description") description: String, @Part("type") type: String, @Part("oldAvatarUrl") oldAvatarUrl: String?,
                             @Part image: MultipartBody.Part?
    ):Response<Unit>

    @GET("user/api/channels/user_channels")
    suspend fun getChannels (@Header("Authorization") token:String):Response<List<ChannelResponseDto>>


    @GET("user/api/channels/{id}")
    suspend fun getChannelPageData(@Header("Authorization") token:String,@Path("id") id:Long): Response<ChannelPageDataResponseDto>


    @POST("cloud/api/channels/cu/post")
    @Multipart
    suspend fun createOrUpdateChannelPost(@Header("Authorization") token:String, @Part("channelId") channelId:Long, @Part("text") text:String?,
                                  @Part("postId") postId: Long?,
                                  @Part("imageUrl") imageUrl: String?,
                                  @Part("videoUrl") videoUrl: String?,
                                  @Part("audioUrl") audioUrl: String?,
                                  @Part image: MultipartBody.Part?, @Part video: MultipartBody.Part?, @Part audio: MultipartBody.Part?,
                                  @Part("title") pollTitle:String?, @Part("options") options:List<String>):Response<Unit>


        @POST("user/api/channels/submit")
        suspend fun submitRequest(@Header("Authorization") token: String, @Query("id") channelId: Long): Response<Unit>

        @DELETE("user/api/channels/delete_request")
        suspend fun deleteRequest(@Header("Authorization") token: String, @Query("id") channelId: Long): Response<Unit>

        @GET("user/api/channels/subscriptions_request")
        suspend fun getChannelSubscriptionRequests(@Header("Authorization") token: String, @Query("id") channelId: Long): Response<List<UserResponseDto>>

        @POST("user/api/channels/requests/accept")
        suspend fun acceptUserToChannel(@Header("Authorization") token: String, @Query("channelId") channelId: Long, @Query("candidateId") candidateId: Long): Response<Unit>

        @POST("user/api/channels/requests/reject")
        suspend fun rejectSubscriptionRequest(@Header("Authorization") token: String, @Query("channelId") channelId: Long, @Query("candidateId") candidateId: Long): Response<Unit>

        @GET("user/api/channels/posts/{id}")
        suspend fun getChannelsPost(@Header("Authorization") token: String, @Path("id") channelId: Long): Response<List<ChannelPostResponseDto>>

        @GET("user/api/channels/members/{id}")
        suspend fun getChannelMembers(@Header("Authorization") token: String, @Path("id") channelId: Long): Response<List<UserResponseDto>>

        @DELETE("user/api/channels/delete_post")
        suspend fun deleteChannelPost(@Header("Authorization") token: String, @Query("channelId") channelId: Long): Response<Unit>

    @PUT("user/api/channels/subscribe/{id}")
    suspend fun subscribeChannel (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>
    @DELETE("user/api/channels/unsubscribe/{id}")
    suspend fun unsubscribeChannel (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>

    @POST("user/api/channels/poll/option/vote/{id}")
    suspend fun vote (@Header("Authorization") token:String,@Path("id") id:Long): Response<Unit>

    @GET ("user/api/channels/find/name")
    suspend fun findChannelByName (@Query("prefix")prefix:String): Response<List<ChannelResponseDto>>

    @GET ("user/api/channels/find/link")
    suspend fun findChannelByLink (@Query("prefix")prefix:String): Response<List<ChannelResponseDto>>


    @POST("user/api/posts/like/add/{id}")
    suspend fun addLike(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @DELETE("user/api/posts/like/delete/{id}")
    suspend fun deleteLike(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @POST("user/api/posts/add/comment")
    suspend fun addComment(@Header("Authorization") token: String, @Body commentRequest: CommentBody): Response<Unit>

    @GET("user/api/posts/{id}/comments")
    suspend fun getPostComments(@Path("id") id: Long): Response<List<CommentResponseDto>>


    @POST("user/api/people/blacklist/add/{id}")
    suspend fun addToBlackList(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @DELETE("user/api/people/blacklist/remove/{id}")
    suspend fun removeBlackList(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @GET("user/api/people/blacklist/all")
    suspend fun getBlackList(@Header("Authorization") token: String): Response<List<UserResponseDto>>

    @GET("user/api/users/message_mode")
    suspend fun getMessageMode(@Header("Authorization") token: String): Response<MessageMode>

    @PUT("user/api/users/message_mode")
    suspend fun setMessageMode(@Header("Authorization") token: String,@Body messageMode: MessageMode): Response<Unit>

    @GET("user/api/messages/by/chat/{chatId}")
    suspend fun getUserMessagesByChat(@Header("Authorization") token:String, @Path("chatId") chatId: Long): Response<MutableList<MessageResponseDto>>

    @PUT("user/api/messages/viewed/chat/{chatId}")
    suspend fun viewed(@Header("Authorization") token: String,@Path("chatId") chatId: Long): Response<Unit>


    @GET("user/api/chats/group/data/{id}")
    suspend fun getGroupData(@Header("Authorization") token: String,@Path("id") chatId: Long): Response<GroupResponseDto>


    @GET("user/api/channels/management")
    suspend fun getChannelManagementData(@Header("Authorization") token: String,@Query("id") channelId: Long): Response<ChannelManagementResponseDto>

    @PUT("cloud/api/chats/edit/group")
    @Multipart
    suspend fun editGroup (@Header("Authorization") token:String,
                           @Part("id") groupId: Long,
                             @Part("name") text: String?,
                             @Part("oldAvatarUrl") oldAvatarUrl: String?,
                             @Part image: MultipartBody.Part?,
    ):Response<Unit>



    @POST("user/api/channels/post/add/score")
    suspend fun addScore(@Header("Authorization") token: String, @Query("id") id: Long, @Query("score") score: Int): Response<Unit>
    @DELETE("user/api/channels/post/delete/score")
    suspend fun deleteScore(@Header("Authorization") token: String, @Query("id") id: Long): Response<Unit>
    @POST("user/api/channels/post/add/comment")
    suspend fun addChannelPostComment(@Header("Authorization") token: String, @Body commentRequest: CommentBody): Response<Unit>
    @GET("user/api/channels/post/{id}/comments")
    suspend fun getChannelPostComments(@Path("id") id: Long): Response<List<CommentResponseDto>>

    @DELETE("user/api/messages/delete")
    suspend fun deleteMessage(@Header("Authorization") token:String, @Query("id") messageId: Long): Response<Unit>

    @PUT("user/api/messages/edit")
    suspend fun editMessage(@Header("Authorization") token:String, @Query("id") messageId: Long,@Query("text") text:String): Response<Unit>



    @GET ("user/api/feed/channels")
    suspend fun  getAllChannelsPost (@Header("Authorization") token:String): Response<List<ChannelPostResponseDto>>
    @GET ("user/api/feed/users")
    suspend fun  getAllSubscriptionsPosts (@Header("Authorization") token:String): Response<List<PostResponseDto>>


    @DELETE("user/api/posts/comment/delete")
    suspend fun deleteComment(@Header("Authorization") token:String, @Query("id") commentId: Long): Response<Unit>

    @DELETE("user/api/channels/post/comment/delete")
    suspend fun deleteChannelComment(@Header("Authorization") token:String, @Query("id") commentId: Long): Response<Unit>


    @DELETE("user/api/chats/delete")
    suspend fun deleteChat(@Header("Authorization") token:String, @Query("id") chatId: Long): Response<Unit>

    @PUT("user/api/chats/group/add/members")
    suspend fun addMembersToGroup(@Header("Authorization") token:String,  @Query("id")groupId: Long,@Query("newMembers") newMembers: List<Long>): Response<Unit>

    @DELETE("user/api/chats/group/leave")
    suspend fun leaveTheGroup(@Header("Authorization") token:String, @Query("id") groupId: Long): Response<Unit>

    @GET ("user/api/channels/post/{id}/appreciated")
    suspend fun  getPostAppreciated (@Header("Authorization") token:String,@Path("id") postId: Long): Response<List<PostAppreciatedResponseDto>>


    @GET ("user/api/posts/{id}/likes")
    suspend fun  getPostLikes (@Header("Authorization") token:String,@Path("id") postId: Long): Response<List<UserResponseDto>>

    @GET ("user/api/feed/recommendation")
    suspend fun  getRecommendation (@Header("Authorization") token:String): Response<List<RecommendationResponseDto>>


    @POST ("user/api/moments/view")
    suspend fun viewMoment (@Header("Authorization") token:String,@Query("momentId")momentId:Long): Response<Unit>


    @GET ("user/api/feed/moments")
    suspend fun  getAllUnseenMoments (@Header("Authorization") token:String): Response<List<UnseenSubscriptionMomentResponseDto>>


    @GET ("user/api/moments/unseen_moments")
    suspend fun  getUserUnseenMoments (@Header("Authorization") token:String,@Query("userId")userId:Long): Response<List<MomentResponseDto>>

    @POST ("user/api/people/add/report")
    suspend fun sendReport (@Header("Authorization") token:String,@Body reportBody: ReportBody): Response<Unit>
}
