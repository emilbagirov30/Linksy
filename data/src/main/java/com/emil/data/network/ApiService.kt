package com.emil.data.network

import com.emil.data.model.AllUserDataDto
import com.emil.data.model.ChannelResponseDto
import com.emil.data.model.ChatResponseDto
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
import com.emil.domain.model.ChatResponse
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

    @POST("api/posts/create")
    @Multipart
    suspend fun createPost (@Header("Authorization") token:String,
                            @Part("text") text: String?,
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
    suspend fun getOutsiderUserPosts (@Path("id") id:Long): Response<List<PostResponseDto>>
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
    @POST("api/message/send/{id}")
    @Multipart
    suspend fun sendMessage (@Header("Authorization") token:String, @Path("id") id:Long,
                            @Part("text") text: String?,
                            @Part image: MultipartBody.Part?,
                            @Part video: MultipartBody.Part?,
                            @Part audio: MultipartBody.Part?,
                            @Part voice: MultipartBody.Part?):Response<Unit>


    @GET("api/messages/user_messages")
    suspend fun getUserMessages(@Header("Authorization") token:String): Response<List<MessageResponseDto>>
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
}
